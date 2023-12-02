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

// TODO: add threads
public class HtmlParser {

    private static final Path INVALID_LINKS = Path.of(System.getProperty("user.dir") + "/" + "invalid_links.txt");
    private static final Pattern QUERY_PARAMETER_PATTERN = Pattern.compile("\\?(.*)");
    private static final Pattern MEDIA_PATTERN = Pattern.compile("\\.(gif|mp4|mp3|js|jpeg|jpg|pdf|png|bmp|webp|svgz)$");
    private static final int RECURSION_STEP = 1;

    private final UrlValidator urlValidator;

    public HtmlParser() {
        this.urlValidator = new UrlValidator();
        // TODO: get rid of logic with files, make clean up
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

    // TODO: add logging (search some libraries) and log the parsing process
    public Set<String> parse(Set<String> currentLinks, int searchDeep, Set<String> resultedLinks) {
        try {
            if (searchDeep == 0) {
                return resultedLinks;
            }

            // TODO: search lib for time counting
//            startTime
            Set<String> parsedLinks = new HashSet<>();
            for (String link : currentLinks) {
                Document document = Jsoup.connect(link).get();
                Elements elements = document.select("a"); // TODO: add "a" to constant

                parsedLinks.addAll(parseLinks(elements));
            }
//            endTime

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
            String link = element.attr("abs:href"); // TODO: add "abs:href" to constant
            System.out.println(link);

            // TODO: get rid of links with internalization
            // https://stackoverflow.com/questions/7223788/are-there-constants-for-language-codes-in-java-or-in-a-java-library
//            Set<String> isoLanguages = Set.of(Locale.getISOLanguages());
            if (hasValidProtocol(link) && !hasQueryParameters(link) && !isMedia(link)) {
                // TODO: implement methods
//            String str1 = deleteSharp(link);
//            String str2 = deleteSlash(link);

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
