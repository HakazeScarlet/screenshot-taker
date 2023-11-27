import parser.HtmlParser;

public class MainApplication {

    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParser();
//        htmlParser.parse("https://github.com/");
//        htmlParser.parse("https://cccstore.ru/");
//        htmlParser.parse("https://github.com/signup?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home");
        htmlParser.parse("https://airsoft-rus.ru/");
        htmlParser.parse("https://en.wikipedia.org/wiki/Cat");
    }
}