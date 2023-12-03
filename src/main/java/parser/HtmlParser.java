package parser;

import org.apache.commons.validator.routines.UrlValidator;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

// TODO: add threads
public class HtmlParser {

    private static final Pattern QUERY_PARAMETER_PATTERN = Pattern.compile("\\?(.*)");
    private static final Pattern MEDIA_PATTERN = Pattern.compile("\\.(gif|mp4|mp3|js|jpeg|jpg|pdf|png|bmp|webp|svgz)$");
//    private static final Pattern ISO_LANGUAGE = Pattern.compile(Set.of(Locale.getISOLanguages()).toString());
    private static final String LINK_TAG = "a";
    private static final String LINK_ATTRIBUTE = "abs:href";
    private static final int RECURSION_STEP = 1;

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
            throw new URLRequestException("Incorrect URL request, or the connection time is out", e);
        }
    }

    // TODO: rewrite using stream API
    private Set<String> parseLinks(Elements elements) {
        Set<String> links = new HashSet<>();
        for (Element element : elements) {
            String link = element.attr(LINK_ATTRIBUTE);
            // TODO: get rid of links with internalization
            if (hasValidProtocol(link) && !hasQueryParameters(link) && !isMedia(link)) {
                // TODO: implement methods
//            String str1 = deleteSharp(link);
//            String str2 = deleteSlash(link);

                links.add(link);
                System.out.println(links);
            }
        }
        return links;
    }

    private boolean hasValidProtocol(String linkAttribute) {
        return urlValidator.isValid(linkAttribute);
    }

    private boolean isMedia(String linkAttribute) {
        return MEDIA_PATTERN.matcher(linkAttribute).find();
    }

    private boolean hasQueryParameters(String linkAttribute) {
        return QUERY_PARAMETER_PATTERN.matcher(linkAttribute).find();
    }

//    private boolean hasInternalization(String linkAttribute) {
//        return ISO_LANGUAGE.matcher(linkAttribute).find();
//    }

    private static final class URLRequestException extends RuntimeException {

        public URLRequestException(String message, Exception e) {
            super(message, e);
        }
    }
}
