import parser.HtmlParser;

import java.util.HashSet;
import java.util.Set;

public class MainApplication {

    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParser();
//        htmlParser.parse("https://github.com/");
//        htmlParser.parse("https://cccstore.ru/");
//        htmlParser.parse("https://airsoft-rus.ru/", 3);
//        htmlParser.parse(Set.of("https://airsoft-rus.ru/"), 3, new HashSet<>());

//        Set<String> res = htmlParser.parse(Set.of("https://hakazescarlet.github.io/"), 3, new HashSet<>());
//        Set<String> res = htmlParser.parse(Set.of("https://ollivere.co/"), 2, new HashSet<>());
        Set<String> res = htmlParser.parse(Set.of("https://rainymood.com/"), 3, new HashSet<>());
        System.out.println();
    }
}