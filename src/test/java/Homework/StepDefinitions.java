package Homework;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class StepDefinitions {

    private WebDriver driver;
    private WebDriverWait wait;
    private String adText; 

    @Before
    public void setup() {
        WebDriverManager.chromedriver().clearDriverCache().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10); // Use seconds as int instead of Duration
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Given("user is in ss.com landing page")
    public void user_is_in_ss_com_landing_page() {
        driver.get("https://www.ss.com/en/");
        String title = driver.getTitle();
        assertThat(driver.getCurrentUrl()).isEqualTo("https://www.ss.com/en/");
        assertThat(title).isEqualTo("Advertisements - SS.COM");
        assertThat(driver.findElement(By.xpath("//*[@title='Job and business']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Transport']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Real estate']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Сonstruction']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Electronics']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Clothes, footwear']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='For home']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Production, work']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='For children']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Animals']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Agriculture']")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.xpath("//*[@title='Rest, hobbies']")).isDisplayed()).isTrue();
        WebElement cookieButton = driver.findElement(By.xpath("//button[text()='Accept and continue']"));
        cookieButton.click();
    }

    @When("user navigates to category {string}")
    public void user_navigates_to_category(String category) {
        driver.findElement(By.xpath("//*[@title='" + category + "']")).click();
        assertThat(driver.getCurrentUrl()).contains(category.toLowerCase());
    }

    @When("user finds an interesting ad")
    public void user_finds_an_interesting_ad() {
        driver.findElement(By.xpath("//*[@title='Cars, Advertisements']")).click();
        assertThat(driver.getCurrentUrl()).contains("cars");
        driver.findElement(By.xpath("//*[@title='Audi, Advertisements']")).click();
        assertThat(driver.getCurrentUrl()).contains("audi");
        WebElement selectElement = driver.findElement(By.xpath("//span[text()='Model:']//select[@class='filter_sel']"));
        Select select = new Select(selectElement);
        select.selectByVisibleText("80");
        assertThat(driver.getCurrentUrl()).contains("80");
        WebElement firstAdLink = driver.findElement(By.xpath("//table//tr[td//a[contains(@href, '/msg/')]][1]/td[3]//a"));
        adText = firstAdLink.getText(); // Remove String declaration
        firstAdLink.click();
        assertThat(driver.findElement(By.xpath("//*[@id='msg_div_msg']")).getText().replaceAll("[\\r\\n]", " ")).contains(adText);
    }

    @When("ads it to Favorites")
    public void user_adds_ad_to_favorites() {
        WebElement favoriteLink = driver.findElement(By.id("add-to-favorites-lnk"));
        Actions actions = new Actions(driver);
        actions.moveToElement(favoriteLink).click().perform();
        WebElement alertAccept = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='alert_ok']")));
        alertAccept.click();
        WebElement favoritesCounter = driver.findElement(By.id("mnu_fav_id"));
        actions.moveToElement(favoritesCounter);
        assertThat(favoritesCounter.getText()).isEqualTo(" (1)");
    }

    @Then("ad is successfully added to Favorites")
    public void ad_is_successfully_added_to_favorites() {
        WebElement favoritesTab = driver.findElement(By.xpath("//*[@title='Favorites']"));
        favoritesTab.click();
        assertThat(driver.getCurrentUrl()).contains("favorites");
        WebElement favoriteRow = driver.findElement(By.xpath("//tr[contains(@id, 'tr_')]"));
        WebElement adTextInFavorites = favoriteRow.findElement(By.xpath(".//td[@class='msg2']//div[@class='d1']//a[@class='am']"));
        assertThat(adTextInFavorites.isDisplayed()).isTrue();
        String favoritesAdText = adTextInFavorites.getText();
        assertThat(favoritesAdText.trim()).contains(adText.trim());
    }

}
