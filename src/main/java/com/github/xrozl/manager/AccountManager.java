package com.github.xrozl.manager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    Map<String, String> loginDetails;

    File accountDetailFile;

    Map<String, String> tags;
    Map<String, String> environments;
    Map<String, String> messages;
    Map<String, ChromeManager> chromeManagers;

    public AccountManager() {
        this.loginDetails = new HashMap<>();

        this.accountDetailFile = new File("accounts.txt");

        if (!this.accountDetailFile.exists()) {
            try {
                this.accountDetailFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.tags = new HashMap<>();
        this.environments = new HashMap<>();
        this.messages = new HashMap<>();
        this.chromeManagers = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.accountDetailFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=");
                this.loginDetails.put(split[0], split[1]);
                this.tags.put(split[0], split[2]);
                this.environments.put(split[0], split[3]);
                this.messages.put(split[0], split[4]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.accountDetailFile));
            for (Map.Entry<String, String> entry : this.loginDetails.entrySet()) {
                bw.write(entry.getKey() + "=" + entry.getValue() + "=" + this.tags.get(entry.getKey()) + "=" + this.environments.get(entry.getKey()) + "=" + this.messages.get(entry.getKey()));
                bw.newLine();
            }
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean canLogin(String username) {
        ChromeDriver driver = chromeManagers.get(username).getDriver();
        driver.get("https://www.instagram.com/");
        try {
            Thread.sleep(4000);
            for (WebElement element : driver.findElements(By.tagName("input"))) {
                if (element.getAttribute("name").equals("username")) {
                    element.sendKeys(username);
                }
                if (element.getAttribute("name").equals("password")) {
                    element.sendKeys(this.loginDetails.get(username));
                }
            }

            for (WebElement element : driver.findElements(By.tagName("button"))) {
                if (element.getAttribute("type").equals("submit")) {
                    element.click();
                }
            }

            Thread.sleep(4000);
            boolean confirmed = false;

            for (WebElement element : driver.findElements(By.tagName("button"))) {
                String text = "情報を保存";
                if (element.getAttribute("type").equals("button") && element.getText().equals(text)) {
                    element.click();
                    confirmed = true;
                }
            }

            Thread.sleep(5500);

            driver.quit();

            return confirmed;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addAccount(String username, String password) {
        if (loginDetails.containsKey(username)) {
            return false;
        }
        loginDetails.put(username, password);
        tags.put(username, null);
        environments.put(username, "env-" + System.currentTimeMillis() + "-" + username);
        messages.put(username, null);
        chromeManagers.put(username, new ChromeManager(environments.get(username)));
        if (canLogin(username)) {
            saveToFile();
            return true;
        } else {
            loginDetails.remove(username);
            tags.remove(username);
            environments.remove(username);
            messages.remove(username);
            chromeManagers.remove(username);
            return false;
        }
    }

    public boolean removeAccount(String username) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        loginDetails.remove(username);
        saveToFile();
        return true;
    }

    boolean updateAccount(String username, String password) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        loginDetails.put(username, password);
        saveToFile();
        return true;
    }

    public boolean updateTag(String username, String tag) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        tags.put(username, tag);
        saveToFile();
        return true;
    }

    public boolean updateEnvironment(String username, String environment) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        environments.put(username, environment);
        saveToFile();
        return true;
    }

    public boolean updateMessage(String username, String message) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        messages.put(username, message);
        saveToFile();
        return true;
    }

    public Map<String, String> getEnvironments() {
        return environments;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public File getAccountDetailFile() {
        return accountDetailFile;
    }

    public Map<String, String> getLoginDetails() {
        return loginDetails;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public Map<String, ChromeManager> getChromeManagers() {
        return chromeManagers;
    }
}
