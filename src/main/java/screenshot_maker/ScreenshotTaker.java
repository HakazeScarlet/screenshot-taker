package screenshot_maker;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.Set;

// TODO: add logger
public class ScreenshotTaker {

    // TODO: change void type to List<File>
    public void take(Set<String> links) {

        WebDriver chrome = new ChromeDriver();

        int counter = 0;
        for (String link : links) {
            chrome.get(link);

            TakesScreenshot taker = (TakesScreenshot) chrome;
            File screenshot = taker.getScreenshotAs(OutputType.FILE); // TODO: add file to List

            // TODO: remove this before S3
            try {
                String pathname = File.separator + "screenshots" + File.separator + counter + ".png";
                String directory = new File("./").getCanonicalPath() + pathname;

                counter++;

                FileUtils.copyFile(screenshot, new File(directory));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        chrome.quit();
    }
}
