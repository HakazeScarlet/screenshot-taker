import parser.HtmlParser;

public class MainApplication {

    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParser();
        htmlParser.parse("https://github.com/");
    }
}