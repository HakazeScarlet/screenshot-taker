import controller.UrlController;
import io.javalin.Javalin;
import parser.HtmlParser;

public class MainApplication {

    private static final int JAVALIN_PORT = 7071;

    public static void main(String[] args) {
        UrlController urlController = new UrlController(new HtmlParser());

        Javalin application = Javalin.create().start(JAVALIN_PORT);
        application.post("/links/{deep}", urlController.getLinks());
    }
}