import parser.HtmlParser;

import java.util.HashSet;
import java.util.Set;

public class MainApplication {

    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParser();
//        htmlParser.parse(new HashSet<>(), 3, Set.of("https://cccstore.ru/"));
//        htmlParser.parse(new HashSet<>(), 3, Set.of("https://github.com/"));
//        htmlParser.parse(new HashSet<>(), 3, Set.of("https://airsoft-rus.ru/"));
        htmlParser.parse(new HashSet<>(), 3, Set.of("https://yandex.ru/pogoda/?lat=53.507852&lon=49.420415&utm_campaign=informer&utm_content=main_informer&utm_medium=web&utm_source=home"));
//        Set<String> res = htmlParser.parse(new HashSet<>(), 3, Set.of("https://hakazescarlet.github.io/"));
//        Set<String> res = htmlParser.parse(new HashSet<>(), 2, Set.of("https://ollivere.co/"));
//        Set<String> res = htmlParser.parse(new HashSet<>(), 3, Set.of("https://rainymood.com/"));
    }
}