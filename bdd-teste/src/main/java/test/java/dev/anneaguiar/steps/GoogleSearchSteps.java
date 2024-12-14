package dev.anneaguiar.steps;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;

public class GoogleSearchSteps {
    WebDriver driver;

    @Given("I open the Google homepage")
    public void openGoogleHomepage() {
        // Configuração do ChromeDriver a partir do Chrome for Testing
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
        
        // Configurar ChromeOptions para o Chrome for Testing
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*"); // Evitar bloqueios de origem cruzada

        driver = new ChromeDriver(options);
        driver.get("https://www.google.com.br");
    }

    @When("I search for {string}")
    public void searchForTerm(String term) {
        driver.findElement(By.name("q")).sendKeys(term);
        driver.findElement(By.name("btnK")).submit();
    }

    @Then("I should see results containing {string}")
    public void verifyResults(String term) {
        assert driver.getPageSource().contains(term);
        driver.quit();
    }
}
