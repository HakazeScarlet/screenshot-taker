package screenshot_taker;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// TODO: add logger
public class ScreenshotTaker {

    public List<File> take(Set<String> links) {

        WebDriver chrome = new ChromeDriver();
        List<File> screenshots = new ArrayList<>();

        for (String link : links) {
            chrome.get(link);

            TakesScreenshot taker = (TakesScreenshot) chrome;
            File screenshot = taker.getScreenshotAs(OutputType.FILE);

            screenshots.add(screenshot);
        }

        chrome.quit();
        return screenshots;
    }
}
