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
}
