import storage.AmazonS3Service;

public class MainApplication {

    public static void main(String[] args) {
//        Set<String> resultedLinks = new HtmlParser().parse(Set.of("https://hakazescarlet.github.io/"), 3, new HashSet<>());
//
//        ScreenshotTaker taker = new ScreenshotTaker();
//        Set<String> resultedLinks = new HtmlParser().parse(Set.of("https://rainymood.com/"), 1, new HashSet<>());
//        List<File> screenshots = taker.take(resultedLinks);

        AmazonS3Service amazonS3Service = new AmazonS3Service();
//        amazonS3Service.save(screenshots);
        amazonS3Service.deleteAll();
//        amazonS3Service.getListObject();

//        htmlParser.parse(Set.of("https://cccstore.ru/"), 3, new HashSet<>());
//        Set<String> resultedLinks = new HtmlParser().parse(Set.of("https://github.com/"), 2, new HashSet<>());
//        Set<String> resultedLinks = new HtmlParser().parse(Set.of("https://airsoft-rus.ru/"), 2, new HashSet<>());
//        htmlParser.parse(Set.of("https://yandex.ru/pogoda/?lat=53.507852&lon=49.420415&utm_campaign=informer&utm_content=main_informer&utm_medium=web&utm_source=home"), 3, new HashSet<>());
//        htmlParser.parse(Set.of("https://ollivere.co/"), 2, new HashSet<>());
    }
}