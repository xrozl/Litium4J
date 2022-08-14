package com.github.xrozl.manager;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeManager {

    ChromeDriver driver;

    public ChromeManager(String env) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + env);
        driver = new ChromeDriver(options);
    }

    public ChromeDriver getDriver() {
        return driver;
    }
}
