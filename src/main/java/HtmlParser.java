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

public class HtmlParser {

    private static final Path FILE_PATH = Path.of(System.getProperty("user.dir") + "/" + "links.txt");

    public HtmlParser() {
        try {
            if (!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
            } else {
                Files.delete(FILE_PATH);
                Files.createFile(FILE_PATH);
            }
        } catch (IOException e) {
            throw new FileCreatingException("Unable to create new file", e);
        }
    }

    public void parse(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a");

            for (Element link : links) {
                String linkAttribute = link.attr("abs:href");
                HashSet<String> linksElement = new HashSet<>();
                linksElement.add(linkAttribute);
                Files.writeString(FILE_PATH, linksElement + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
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
