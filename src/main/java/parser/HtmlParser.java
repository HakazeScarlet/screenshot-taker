package parser;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class HtmlParser {

    private static final Path INVALID_LINKS = Path.of(System.getProperty("user.dir") + "/" + "invalid_links.txt");
    private static final String[] SCHEMES = {"http", "https"};

    private final UrlValidator urlValidator;

    public HtmlParser() {
        this.urlValidator = new UrlValidator(SCHEMES);
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

    public void parse(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a");
            Set<String> linksElement = new HashSet<>();

            for (Element link : links) {
                String linkAttribute = link.attr("abs:href");

                if (urlValidator.isValid(linkAttribute)) {
                    linksElement.add(linkAttribute);
                } else {
                    Files.writeString(INVALID_LINKS, linkAttribute + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                }
            }
            System.out.println();
        } catch (IOException e) {
            throw new URLRequestException("Incorrect URL request, or the connection time is out", e);
        }
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
