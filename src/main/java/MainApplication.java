import parser.HtmlParser;
import screenshot_taker.ScreenshotTaker;

import java.util.HashSet;
import java.util.Set;

public class MainApplication {

    public static void main(String[] args) {
        Set<String> resultedLinks = new HtmlParser().parse(Set.of("https://hakazescarlet.github.io/"), 3, new HashSet<>());

        ScreenshotTaker taker = new ScreenshotTaker();
//        List<File> screenshots = taker.take(resultedLinks);

//        AmazonS3Service amazonS3Service = new AmazonS3Service();
//        amazonS3Service.save(screenshots);

//        htmlParser.parse(Set.of("https://cccstore.ru/"), 3, new HashSet<>());
//        htmlParser.parse(Set.of("https://github.com/"), 3, new HashSet<>());
//        htmlParser.parse(Set.of("https://airsoft-rus.ru/"), 3, new HashSet<>());
//        htmlParser.parse(Set.of("https://yandex.ru/pogoda/?lat=53.507852&lon=49.420415&utm_campaign=informer&utm_content=main_informer&utm_medium=web&utm_source=home"), 3, new HashSet<>());
//        htmlParser.parse(Set.of("https://ollivere.co/"), 2, new HashSet<>());
//        htmlParser.parse(Set.of("https://rainymood.com/"), 3, new HashSet<>());
    }
}