package com.github.xrozl.manager;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class ChromeManager {

    ChromeDriver driver;

    public ChromeManager(String env) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:" + File.separator + "envs"+ File.separator + env);
        //options.addArguments("--headless");
        options.addArguments("--lang=ja-JP");
        System.setProperty("webdriver.chrome.driver", "lib" + File.separator + "chromedriver.exe");
        driver = new ChromeDriver(options);
    }

    public ChromeDriver getDriver() {
        return driver;
    }
}
