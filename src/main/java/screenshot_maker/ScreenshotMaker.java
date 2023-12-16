package screenshot_maker;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class ScreenshotMaker {

    private static final Path DIRECTORY_PATH = Path.of( File.separator + "screenshots");

    public void make(Set<String> resultedLinks) {

        for(String link : resultedLinks) {

            WebDriver driver = new ChromeDriver();

            driver.get(link);

            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File(DIRECTORY_PATH + File.separator + link + ".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            driver.quit();
        }
    }
}
