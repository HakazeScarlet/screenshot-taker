package parser;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlParser {

    private static final Path INVALID_LINKS = Path.of(System.getProperty("user.dir") + "/" + "invalid_links.txt");
    private static final Pattern QUERY_PARAMETER_PATTERN = Pattern.compile("\\?(.*)");
    private static final Pattern MEDIA_PATTERN = Pattern.compile("\\.(gif|mp4|mp3|js|jpeg|jpg|pdf|png|bmp|webp|svgz)$");
    private static final int RECURSION_STEP = 1;

    private final UrlValidator urlValidator;

    public HtmlParser() {
        this.urlValidator = new UrlValidator();
        try {
            if (!Files.exists(INVALID_LINKS)) {
                Files.createFile(INVALID_LINKS);
            } else {
                Files.delete(INVALID_LINKS);
                Files.createFile(INVALID_LINKS);
            }
        } catch (IOException e) {
            throw new FileCreatingException("Unable to create new file", e);
        }
    }

    public Set<String> parse1(Set<String> links, int searchDeep, Set<String> visitedLinks) {
        if (searchDeep == 0) {
            return links;
        }

        for (String link : links) {
            Document document = Jsoup.connect(link).get();
            Elements elements = document.select("a");

            Set<String> parsedLinks = parseLinks(elements);
            visitedLinks.addAll(parsedLinks);
            Set<String> result = parse1(parsedLinks, searchDeep - RECURSION_STEP, visitedLinks);
        }

//        try {
//
//            return parse(url, searchDeep - RECURSION_STEP);
//
//        } catch (IOException e) {
//            throw new URLRequestException("Incorrect URL request, or the connection time is out", e);
//        }
    }

    private Set<String> parseLinks(Elements elements) {
        Set<String> links = new HashSet<>();
        for (Element element : elements) {
            String link = element.attr("abs:href");
            if (hasValidProtocol(link) && !hasQueryParameters(link) && !isMedia(link)) {
                links.add(link);
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

    private static final class URLRequestException extends RuntimeException {

        public URLRequestException(String message, Exception e) {
            super(message, e);
        }
    }

    private static final class FileCreatingException extends RuntimeException {
        public FileCreatingException(String message, Exception e) {
            super(message, e);
        }
    }
}
