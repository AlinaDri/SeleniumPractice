package Homework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import static org.assertj.core.api.Assertions.assertThat;

public class SeleniumUtils {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public SeleniumUtils(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    public void navigateToCategory(String category) {
        driver.findElement(By.linkText(category)).click();
        assertThat(driver.getCurrentUrl()).contains(category.toLowerCase().replace(" ", "-"));
    }
    
    public void clickByTitleAndVerifyUrl(String title, String expectedUrlContent) {
        driver.findElement(By.xpath("//*[@title='" + title + "']")).click();
        assertThat(driver.getCurrentUrl()).contains(expectedUrlContent.toLowerCase());
    }
    
    public void selectFromDropdown(By dropdownLocator, String optionText) {
        WebElement selectElement = driver.findElement(dropdownLocator);
        Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }
    
    public void waitAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }
    
    public void acceptCookieConsent() {
        WebElement cookieButton = driver.findElement(By.xpath("//button[text()='Accept and continue']"));
        cookieButton.click();
    }
    
    public void checkFifthColumnInTableRows(int minimumValue) {
        java.util.List<WebElement> tableRows = driver.findElements(By.xpath("//table//tr[count(td) >= 8]"));
        
        int invalidAds = 0;
        
        for (WebElement row : tableRows) {
            java.util.List<WebElement> cells = row.findElements(By.tagName("td"));
            WebElement fifthCell = cells.get(4); // 5th td element (index 4)
            String cellText = fifthCell.getText().trim();
            
            try {
                String numberStr = cellText.replaceAll("[^0-9]", "");
                if (!numberStr.isEmpty()) {
                    int number = Integer.parseInt(numberStr);
                    if (number < minimumValue) {
                        invalidAds++;
                    }
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric values
            }
        }
        
        // Assert that all 5th elements contain numbers >= minimumValue
        assertThat(invalidAds).withFailMessage("Found " + invalidAds + " rows with 5th column numbers less than " + minimumValue).isEqualTo(0);
    }

    public void addSingleAdToFavorites() {
        waitAndClick(By.id("add-to-favorites-lnk"));
        WebElement alertAccept = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='alert_ok']")));
        alertAccept.click();
    }

    public void addMultipleAdsToFavorites(int numberOfAds) {
        for (int i = 1; i <= numberOfAds; i++) {
            WebElement adLink = driver.findElement(By.xpath("//table//tr[td//a[contains(@href, '/msg/')]][" + i + "]/td[3]//a"));
            String listPageUrl = driver.getCurrentUrl();
            adLink.click();
            wait.until(ExpectedConditions.urlContains("/msg/"));
            addSingleAdToFavorites();
            if (i < numberOfAds) {
                driver.get(listPageUrl);
                wait.until(ExpectedConditions.urlToBe(listPageUrl));
            }
        }
    }

    public void verifyFavoritesCount(int expectedCount) {
        WebElement favoritesCounter = driver.findElement(By.id("mnu_fav_id"));
        assertThat(favoritesCounter.getText()).isEqualTo(" (" + expectedCount + ")");
    }
}
