import parser.HtmlParser;

import java.util.HashSet;
import java.util.Set;

public class MainApplication {

    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParser();
//        htmlParser.parse("https://github.com/");
//        htmlParser.parse("https://cccstore.ru/");
//        htmlParser.parse(3, "https://airsoft-rus.ru/");
//        htmlParser.parse(new HashSet<>(), 3, Set.of("https://airsoft-rus.ru/"));

//        Set<String> res = htmlParser.parse(new HashSet<>(), 3, Set.of("https://hakazescarlet.github.io/"));
//        Set<String> res = htmlParser.parse(new HashSet<>(), 2, Set.of("https://ollivere.co/"));
        Set<String> res = htmlParser.parse(new HashSet<>(), 3, Set.of("https://rainymood.com/"));
    }
}