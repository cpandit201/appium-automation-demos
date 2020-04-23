import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AmazonSearch {

    public static void main (String args[]) throws Exception {

        //Initialize Appium with below DesiredCapablities
        /**
         * {
            "app": "/Users/cpandit/gitpropjects/serverless-java/AmazonMobileAppAutomation/Amazon_Shoppin_Search_Save_v20.8.0.100_apkpure.com.apk",
            "platformName": "Android",
            "platformVersion": "9",
            "automationName": "Appium",
            "deviceName": "OnePlus 7 Pro"
         }
         */

        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "/Users/cpandit/gitpropjects/serverless-java/AmazonMobileAppAutomation/Amazon_Shoppin_Search_Save_v20.8.0.100_apkpure.com.apk");//"Amazon_Shoppin_Search_Save_v20.8.0.100_apkpure.com.apk");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", "9");
            capabilities.setCapability("automationName", "Appium");
            capabilities.setCapability("deviceName", "OnePlus 7 Pro");

            //Expected Product Name
            String strSearchText = "Canon 1500D DSLR Camera";
            String strExpectedProductText = "Canon EOS 1500D 24.1 Digital SLR Camera (Black) with EF S18-55 is II Lens, 16GB";
            String strAreaPincode = "401107";

            WebDriver driver = new AppiumDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);

            //Verify element exists -> id = com.amazon.mShop.android.shopping:id/signin_to_yourAccount
            WebDriverWait wait = new WebDriverWait(driver, 30);
            By bySkipLoginButton = By.id("com.amazon.mShop.android.shopping:id/skip_sign_in_button");
            By byAmazonLogo = By.id("com.amazon.mShop.android.shopping:id/signin_to_yourAccount");
            wait.until(ExpectedConditions.visibilityOfElementLocated(byAmazonLogo));
            wait.until(ExpectedConditions.visibilityOfElementLocated(bySkipLoginButton));

            //Click Skip Sign in button - id	com.amazon.mShop.android.shopping:id/skip_sign_in_button
            driver.findElement(bySkipLoginButton).click();

            //Click Search Icon on top left - id	com.amazon.mShop.android.shopping:id/rs_search_src_text
            By bySearchBox = By.id("com.amazon.mShop.android.shopping:id/rs_search_src_text");
            wait.until(ExpectedConditions.visibilityOfElementLocated(bySearchBox)).click();

            //Wait for keyboard animations to popup
            TimeUnit.SECONDS.sleep(1);

            //Type Canon Camera - id	com.amazon.mShop.android.shopping:id/rs_search_src_text
            driver.findElement(bySearchBox).sendKeys(strSearchText);

            //Wait for auto Suggestion list view - id	com.amazon.mShop.android.shopping:id/iss_search_suggestions_list_view
            By byAutoSuggestionListView = By.xpath("//android.widget.ListView[@resource-id='com.amazon.mShop.android.shopping:id/iss_search_suggestions_list_view']//android.widget.LinearLayout"); //By.id("com.amazon.mShop.android.shopping:id/iss_search_suggestions_list_view/LinerLayout");
            //wait.until(ExpectedConditions.visibilityOfElementLocated(byAutoSuggestionListView));
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(byAutoSuggestionListView, 0));

            List <WebElement> autosuggestions = driver.findElements(byAutoSuggestionListView);
            System.out.println("Total suggestions : "+autosuggestions.size());

            //Click first elementId	0a549a78-9320-4eb5-adc6-2c5c13907627
            WebElement firstSuggestion = autosuggestions.size() > 0 ? autosuggestions.get(0) : null;
            if (firstSuggestion == null)
            {
                System.out.println("No autosuggestion results found..");
                System.exit(0);
            }

            //Click arrow for first item
            firstSuggestion.click();

            //Wait for view and find the element which has text of - <android.view.View> - text - "Canon EOS 1500D 24.1 Digital SLR Camera (Black) with EF S18-55 is II Lens, 16GB"
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//android.view.View"), 0));
            List <WebElement> productsElements = driver.findElements(By.xpath("//android.view.View"));
            System.out.println("Found total products : "+productsElements);

            boolean found = false;
            for (WebElement productElement : productsElements) {
                String elementText = productElement.getAttribute("text");
                if (elementText != null && elementText.contains(strExpectedProductText)) {
                    System.out.println("Found product : "+strExpectedProductText);
                    productElement.click();
                    found = true;
                    break;
                }
            }

            if (found == false) {
                System.out.println("No products found with that name..");
                System.exit(0);
            }

            //Enter a pincode - id	com.amazon.mShop.android.shopping:id/loc_ux_gps_enter_pincode
            By byPincodeButton = By.id("com.amazon.mShop.android.shopping:id/loc_ux_gps_enter_pincode");
            wait.until(ExpectedConditions.visibilityOfElementLocated(byPincodeButton)).click();

            //Clear and enter - id	com.amazon.mShop.android.shopping:id/loc_ux_pin_code_text_pt1 -
            By byPinCodeTextbox = By.id("com.amazon.mShop.android.shopping:id/loc_ux_pin_code_text_pt1");
            wait.until(ExpectedConditions.visibilityOfElementLocated(byPinCodeTextbox)).clear();
            driver.findElement(byPinCodeTextbox).sendKeys(strAreaPincode);

            //Click id	com.amazon.mShop.android.shopping:id/loc_ux_update_pin_code
            By byUpdatePincodeButton = By.id("com.amazon.mShop.android.shopping:id/loc_ux_update_pin_code");
            wait.until(ExpectedConditions.visibilityOfElementLocated(byUpdatePincodeButton)).click();

//            //wait for product page to load - id	bylineInfo
//            By canonLineItem = By.id("bylineInfo_feature_div");
//            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(canonLineItem,0));
//
//            //Reveal Wishlist Button
//            Actions action = new Actions(driver);
//            action.clickAndHold(driver.findElement(canonLineItem))
//                    .moveByOffset(0, -200)
//                    .build()
//                    .perform();

            //Click - id	wishlistButtonStack
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wishlistButtonStack"))).click();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



    }
}
