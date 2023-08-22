package com.doctobot.scraper.service;

import com.doctobot.common.model.Appointment;
import com.doctobot.scraper.config.ScraperConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class Scraper {

    public static final Duration PAGE_LOAD_WAIT = Duration.ofSeconds(3);
    private final ScraperConfig scraperConfig;
    private WebDriver webDriver;

    public void startWebDriver() {
        String urlText = "http://" + scraperConfig.getSeleniumHubHost() + ":4444/wd/hub";
        URL url;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            log.error("Selenium Hub URL is malformed: {}", urlText);
            return;
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--start-maximized", "--disable-extensions");
        options.addArguments("--disable-gpu", "--disable-crash-reporter", "--disable-in-process-stack-traces", "--disable-logging", "--output=/dev/null");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.114 Safari/537.36 OPR/89.0.4447.64");
        options.addArguments("--remote-allow-origins=*");
        webDriver = new RemoteWebDriver(url, options);
    }

    public void getPage(String url) throws InterruptedException {
        if (webDriver == null || webDriver.toString().contains("null")) {
            startWebDriver();
        }
        webDriver.get(url);
        Thread.sleep(PAGE_LOAD_WAIT.toMillis());
    }

    public void quitWebDriver() {
        if (webDriver == null) {
            log.warn("Failed to quit web driver, web driver is null");
            return;
        }
        webDriver.quit();
    }

    public boolean isAppointmentAvailable() {
        String mainHtml = webDriver.findElement(By.id("react-main")).getAttribute("innerHTML");
        if (mainHtml.contains("Aucune disponibilité en ligne.") || mainHtml.contains("Chargement...")) {
            return false;
        }
        if (!scraperConfig.isStrict()) return true;
        String date = scrapeAppointmentDate();
        String time = scrapeAppointmentTime();
        return !date.isBlank() && !time.isBlank();
    }

    public Appointment getAppointment() {
        String name = scrapeAppointmentName();
        String date = scrapeAppointmentDate();
        String time = scrapeAppointmentTime();
        return new Appointment(name, date, time, webDriver.getCurrentUrl());
    }

    private String scrapeAppointmentName() {
        try {
            return webDriver.findElement(By.xpath("//*[@id=\"react-main\"]/div/div[3]/div[2]/aside/div[1]/div[2]/p")).getText();
        } catch (Exception e) {
            return "";
        }
    }

    private String scrapeAppointmentDate() {
        try {
            return webDriver.findElement(By.xpath("//*[@id=\":r0:\"]/div/div")).getText();
        } catch (Exception e) {
            return "";
        }
    }

    private String scrapeAppointmentTime() {
        try {
            return webDriver.findElement(By.xpath("//*[@id=\"react-main\"]/div/div[3]/div[1]/div/div/div/div[2]/div/div[2]/div[1]/div[1]/div/div/button[1]/span/span")).getText();
        } catch (Exception e) {
            return "";
        }
    }
}
