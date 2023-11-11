import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlParser {

    public void parse(String url) {
        try {
                Document document = Jsoup.connect(url).get();
                Elements links = document.select("a");

                for (Element link : links) {
                    System.out.println("Link: " + link.attr("abs:href"));
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
}
