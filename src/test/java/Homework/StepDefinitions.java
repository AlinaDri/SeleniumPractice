package Homework;

import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class StepDefinitions {

    @Given("user is in ss.com landing page")
    public void user_is_in_ss_com_landing_page() {
        // Setup ChromeDriver automatically using WebDriverManager
        // Clear cache and force download of compatible version
        WebDriverManager.chromedriver().clearDriverCache().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.ss.com/en/");
        String title = driver.getTitle();
        assertThat(driver.getCurrentUrl()).isEqualTo("https://www.ss.com/en/");
        assertThat(title).isEqualTo("Advertisements - SS.COM");
        //check if all categories are displayed
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
        driver.quit();
    }

    @When("User finds an interesting ad")
    public void user_finds_an_interesting_ad() {
        
    }

    @When("ads it to Favorites")
    public void ads_it_to_favorites() {
        
    }

    @Then("ad is successfully added to Favorites")
    public void ad_is_successfully_added_to_favorites() {
     
    }
    
}
