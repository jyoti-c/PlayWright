import com.microsoft.playwright.*;

import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GoogleDriveInvalidLogin {
  public static void main(String[] args) {
    try (Playwright playwright = Playwright.create()) {
      List<BrowserType> browserTypes = Arrays.asList(
          playwright.chromium(),
          playwright.firefox()
      );
      for (BrowserType browserType : browserTypes) {
        try (Browser browser = browserType.launch(new LaunchOptions().setHeadless(false))) {
          BrowserContext context = browser.newContext(new NewContextOptions()
              .setLocale("en-IN"));
          Page page = context.newPage();
          page.route("**", route -> {
            System.out.println(route.request().url());
            route.resume();
          });
          page.navigate("https://drive.google.com/");
          Page newpage = context.waitForPage(()->page.click("xpath=//div[@class='drive__hero-buttons']//a[contains(text(),'Go to Drive')]"));
          newpage.waitForLoadState();
          newpage.type("#identifierId","invalis");
          newpage.waitForResponse("**",()->  newpage.press("#identifierId","Enter"));
          newpage.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot-" + browserType.name() + ".png")));
          Object dimensions = page.evaluate("() => {\n" +
              "  return {\n" +
              "      width: document.documentElement.clientWidth,\n" +
              "      height: document.documentElement.clientHeight,\n" +
              "      deviceScaleFactor: window.devicePixelRatio\n" +
              "  }\n" +
              "}");
          System.out.println(dimensions);
        }
      }


    }
  }
}