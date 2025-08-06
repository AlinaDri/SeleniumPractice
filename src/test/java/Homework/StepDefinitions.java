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
import org.openqa.selenium.support.ui.WebDriverWait;

import Homework.utils.SeleniumUtils;

public class StepDefinitions {

    private WebDriver driver;
    private WebDriverWait wait;
    private String adText;
    private SeleniumUtils seleniumUtils;
    private String searchTerm;
    private String searchCity;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().clearDriverCache().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        seleniumUtils = new SeleniumUtils(driver, wait);
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
        seleniumUtils.acceptCookieConsent();
    }

    @When("user navigates to category {string}")
    public void user_navigates_to_category(String category) {
        seleniumUtils.navigateToCategory(category);
    }

    @When("user finds an interesting ad")
    public void user_finds_an_interesting_ad() {
        seleniumUtils.clickByTitleAndVerifyUrl("Cars, Advertisements", "cars");
        seleniumUtils.clickByTitleAndVerifyUrl("Audi, Advertisements", "audi");
        seleniumUtils.selectFromDropdown(By.xpath("//span[text()='Model:']//select[@class='filter_sel']"), "80");
        assertThat(driver.getCurrentUrl()).contains("80");
        WebElement firstAdLink = driver
                .findElement(By.xpath("//table//tr[td//a[contains(@href, '/msg/')]][1]/td[3]//a"));
        adText = firstAdLink.getText(); // Remove String declaration
        firstAdLink.click();
        assertThat(driver.findElement(By.xpath("//*[@id='msg_div_msg']")).getText().replaceAll("[\\r\\n]", " "))
                .contains(adText);
    }

    @When("ads it to Favorites")
    public void user_adds_ad_to_favorites() {
        seleniumUtils.addSingleAdToFavorites();
        seleniumUtils.verifyFavoritesCount(1);
    }

    @Then("ad is successfully added to Favorites")
    public void ad_is_successfully_added_to_favorites() {
        seleniumUtils.waitAndClick(By.xpath("//*[@title='Favorites']"));
        assertThat(driver.getCurrentUrl()).contains("favorites");
        WebElement favoriteRow = driver.findElement(By.xpath("//tr[contains(@id, 'tr_')]"));
        WebElement adTextInFavorites = favoriteRow
                .findElement(By.xpath(".//td[@class='msg2']//div[@class='d1']//a[@class='am']"));
        assertThat(adTextInFavorites.isDisplayed()).isTrue();
        String favoritesAdText = adTextInFavorites.getText();
        assertThat(favoritesAdText.trim()).contains(adText.trim());
    }

    @When("user finds three interesting ads")
    public void user_finds_three_interesting_ads() {
        seleniumUtils.clickByTitleAndVerifyUrl("Flats, Advertisements", "flats");
        seleniumUtils.clickByTitleAndVerifyUrl("Riga, Advertisements", "riga");
        seleniumUtils.clickByTitleAndVerifyUrl("Teika, Advertisements", "teika");
        seleniumUtils.selectFromDropdown(By.xpath("//select[option[text()='Sell']]"), "Sell");
        assertThat(driver.getCurrentUrl()).contains("sell");
        seleniumUtils.selectFromDropdown(By.xpath("//select[option[text()='3']]"), "3");
        seleniumUtils.checkFifthColumnInTableRows(3);
    }

    @When("ads them to Favorites")
    public void ads_them_to_favorites() {
        seleniumUtils.addMultipleAdsToFavorites(3);
        seleniumUtils.verifyFavoritesCount(3);
    }

    @Then("all ads are successfully added to Favorites")
    public void all_ads_are_successfully_added_to_favorites() {
        seleniumUtils.waitAndClick(By.xpath("//*[@title='Favorites']"));
        assertThat(driver.getCurrentUrl()).contains("favorites");
        java.util.List<WebElement> favoriteRows = driver.findElements(By.xpath("//tr[contains(@id, 'tr_')]"));
        assertThat(favoriteRows.size()).isEqualTo(3);
    }

    @When("user performs advanced search:")
    public void user_performs_advanced_search(io.cucumber.datatable.DataTable dataTable) {
        java.util.Map<String, String> searchData = dataTable.asMap(String.class, String.class);
        
        // Store search parameters for later verification
        searchTerm = searchData.get("Searched word or phrase:").replace("\"", "");
        searchCity = searchData.get("City, area");
        
        seleniumUtils.performAdvancedSearch(dataTable);
    }

    @When("user finds necessary ad")
    public void user_finds_necessary_ad() {
        seleniumUtils.waitForElement(By.xpath("//table//tr[td//a[contains(@href, '/msg/')]]"));
        
        WebElement firstAd = driver.findElement(By.xpath("//table//tr[td//a[contains(@href, '/msg/')]][1]"));
        
        // Use stored search parameters for verification
        WebElement categoryElement = firstAd.findElement(By.xpath(".//div[@class='ads_cat_names']"));
        String categoryText = categoryElement.getText();
        assertThat(categoryText.toLowerCase()).contains(searchTerm.toLowerCase());
        
        WebElement regionElement = firstAd.findElement(By.xpath(".//div[@class='ads_region']"));
        String regionText = regionElement.getText();
        assertThat(regionText.toLowerCase()).contains(searchCity.toLowerCase());
        
        // Rest of the implementation...
        WebElement adLink = firstAd.findElement(By.xpath(".//a[contains(@href, '/msg/')]"));
        adText = adLink.getText();
        adLink.click();
        
        seleniumUtils.waitForElement(By.xpath("//*[@id='msg_div_msg']"));
        assertThat(driver.getCurrentUrl()).contains("/msg/");
    }

    @When("user clicks {string}")
    public void user_clicks(String string) {
        WebElement element = driver.findElement(By.xpath("//*[contains(@title, '" + string + "') or text()='" + string + "']"));
        element.click();
    }

}
