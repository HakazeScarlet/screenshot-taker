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

    private static final Path FILE_PATH = Path.of(System.getProperty("user.dir") + "/" + "links.txt");
    private static final Path INVALID_FILE_PATH = Path.of(System.getProperty("user.dir") + "/" + "invalid_links.txt");
    private final String[] schemes = {"http", "https"};

    public HtmlParser() {
        try {
            if (!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
            } else if (!Files.exists(INVALID_FILE_PATH)) {
                Files.createFile(INVALID_FILE_PATH);
            } else {
                Files.delete(FILE_PATH);
                Files.createFile(FILE_PATH);
                Files.delete(INVALID_FILE_PATH);
                Files.createFile(INVALID_FILE_PATH);
            }
        } catch (IOException e) {
            throw new FileCreatingException("Unable to create new file", e);
        }
    }

    public void parse(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a");
            UrlValidator urlValidator = new UrlValidator(schemes);
            Set<String> linksElement = new HashSet<>();

            for (Element link : links) {
                String linkAttribute = link.attr("abs:href");

                if (urlValidator.isValid(linkAttribute)) {
                    linksElement.add(linkAttribute);
                    Files.writeString(FILE_PATH, linksElement + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                } else {
                    Files.writeString(INVALID_FILE_PATH, linksElement + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                }
            }
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
