package org.example.sprintrestapi.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Alert;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class ProdutoUITest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFluxoCompletoEIntegracao() {
        driver.get("http://localhost:8080");

        WebElement inputNome = wait.until(ExpectedConditions.elementToBeClickable(By.id("nome")));
        inputNome.sendKeys("Produto Integrado");

        driver.findElement(By.id("quantidade")).sendKeys("5");
        driver.findElement(By.id("preco")).sendKeys("150.00");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement tabela = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tabelaCorpo")));
        wait.until(ExpectedConditions.textToBePresentInElement(tabela, "Produto Integrado"));

        assertTrue(driver.getPageSource().contains("Produto Integrado"));

        WebElement botaoExcluir = wait.until(ExpectedConditions.elementToBeClickable(By.className("btn-danger")));

        try {
            botaoExcluir.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", botaoExcluir);
        }

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }
}