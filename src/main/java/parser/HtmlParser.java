package parser;

import org.apache.commons.validator.routines.UrlValidator;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// TODO: add threads
public class HtmlParser {

    private static final Pattern QUERY_PARAMETER_PATTERN = Pattern.compile("\\?(.*)");
    private static final Pattern MEDIA_PATTERN = Pattern.compile("\\.(gif|mp4|mp3|js|jpeg|jpg|pdf|png|bmp|webp|svgz)$");
    private static final Pattern LINK_ANCHOR = Pattern.compile("/#|/$|#$");
    private static final String LINK_TAG = "a";
    private static final String LINK_ATTRIBUTE = "abs:href";
    private static final int RECURSION_STEP = 1;
    private static final Set<String> ISO_LANGUAGES = Set.of(Locale.getISOLanguages());
    private static final Set<String> CODES = ISO_LANGUAGES.stream()
        .map(code -> "(" + code + ")")
        .collect(Collectors.toSet());
    private static final String JOINED_ISO_LANGUAGES = "/(" + String.join("|", CODES) + ")/";

    private final UrlValidator urlValidator;

    public HtmlParser() {
        this.urlValidator = new UrlValidator();
    }

    // TODO: add logging (search some libraries) and log the parsing process
    public Set<String> parse(Set<String> resultedLinks, int searchDeep, Set<String> currentLinks) {
        try {
            if (searchDeep == 0) {
                return resultedLinks;
            }

            Instant startInterval = new Instant();

            Set<String> parsedLinks = new HashSet<>();
            for (String link : currentLinks) {
                Document document = Jsoup.connect(link).get();
                Elements elements = document.select(LINK_TAG);

                parsedLinks.addAll(parseLinks(elements));
            }

            Instant endInterval = new Instant();
            Interval iterationTime = new Interval(startInterval, endInterval);

            resultedLinks.addAll(currentLinks);
            resultedLinks.addAll(parsedLinks);

            // TODO:  logger.info(searchDeep, endTime - startTime)

            return parse(parsedLinks, searchDeep - RECURSION_STEP, resultedLinks);
        } catch (Exception e) {
            // TODO: add logger for link & searchDeep
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
                !hasLinkAnchor(link) &&
                !isMedia(link) &&
                !hasInternalizationStatic(link)) {

                links.add(link);
                System.out.println(links);
            }
        }
        return links;
    }

    private boolean hasValidProtocol(String linkAttribute) {
        return urlValidator.isValid(linkAttribute);
    }

    private boolean hasLinkAnchor(String linkAttribute) {
        return LINK_ANCHOR.matcher(linkAttribute).find();
    }

    private boolean isMedia(String linkAttribute) {
        return MEDIA_PATTERN.matcher(linkAttribute).find();
    }

    private boolean hasQueryParameters(String linkAttribute) {
        return QUERY_PARAMETER_PATTERN.matcher(linkAttribute).find();
    }

    public static boolean hasInternalizationStatic(String link) {
        Pattern pattern = Pattern.compile(JOINED_ISO_LANGUAGES);
        Matcher matcher = pattern.matcher(link);
        return matcher.find();
    }

    private static final class URLRequestException extends RuntimeException {

        public URLRequestException(String message, Exception e) {
            super(message, e);
        }
    }
}
