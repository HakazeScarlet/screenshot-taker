package parser;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import util.InternalizationUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

// TODO: add threads
public class HtmlParser {

    private static final Pattern QUERY_PARAMETER_PATTERN = Pattern.compile("\\?(.*)");
    private static final Pattern MEDIA_PATTERN = Pattern.compile("\\.(gif|mp4|mp3|js|jpeg|jpg|pdf|png|bmp|webp|svgz)$");
    private static final String LINK_TAG = "a";
    private static final String LINK_ATTRIBUTE = "abs:href";
    private static final int RECURSION_STEP = 1;
    private static final int RECURSION_STOP = 0;
    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(HtmlParser.class);

    private final UrlValidator urlValidator;
    private final StopWatch stopWatch = new StopWatch();

    public HtmlParser() {
        this.urlValidator = new UrlValidator();
    }

    public Set<String> parse(Set<String> resultedLinks, int searchDeep, Set<String> currentLinks) {
        try {
            if (searchDeep == RECURSION_STOP) {
                return resultedLinks;
            }

            stopWatch.start();
            Set<String> parsedLinks = new HashSet<>();
            for (String link : currentLinks) {
                Document document = Jsoup.connect(link).get();
                Elements elements = document.select(LINK_TAG);

                parsedLinks.addAll(parseLinks(elements));
            }

            resultedLinks.addAll(currentLinks);
            resultedLinks.addAll(parsedLinks);
            stopWatch.stop();

            logger.info("Time of parsing: {}. Deep of search is: {}. Links amount: {}", stopWatch.getTime(TimeUnit.SECONDS), searchDeep, resultedLinks.size());

            stopWatch.reset();

            return parse(parsedLinks, searchDeep - RECURSION_STEP, resultedLinks);
        } catch (Exception e) {
            // TODO: add logger for link & searchDeep
//            logger.info("Uncorrected link: {}. Deep of search is: {}", );
            throw new URLRequestException("Incorrect URL request, or the lost connection", e);
        }
    }

    // TODO: rewrite using stream API
    private Set<String> parseLinks(Elements elements) {
        Set<String> links = new HashSet<>();
        for (Element element : elements) {
            String link = element.attr(LINK_ATTRIBUTE);
            if (hasValidProtocol(link) &&
                !hasQueryParameters(link) &&
                !isMedia(link) &&
                !InternalizationUtil.hasInternalizationStatic(link)
            ) {
                String normalized = removeEndSlash(chooseLinkBeforeFirstAnchor(link));
                links.add(normalized);
            }
        }
        return links;
    }

    private boolean hasValidProtocol(String link) {
        return urlValidator.isValid(link);
    }

    private String removeEndSlash(String link) {
        return link.replaceAll("/$", "");
    }

    private String chooseLinkBeforeFirstAnchor(String link) {
        int endIndex = link.indexOf("#");
        return endIndex == -1
            ? link
            : link.substring(0, endIndex);
    }

    private boolean isMedia(String link) {
        return MEDIA_PATTERN.matcher(link).find();
    }

    private boolean hasQueryParameters(String link) {
        return QUERY_PARAMETER_PATTERN.matcher(link).find();
    }

    private static final class URLRequestException extends RuntimeException {

        public URLRequestException(String message, Exception e) {
            super(message, e);
        }
    }
}
