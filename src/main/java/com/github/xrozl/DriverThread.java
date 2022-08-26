package com.github.xrozl;

import com.github.xrozl.manager.ChromeManager;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class DriverThread extends Thread {

    @Override
    public void run() {
        super.run();

        try {
            Main.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Thread.sleep(1000 * 60 * 60);

                try {
                    int total = 0;
                    int success = 0;
                    for (String username : Main.accManager.getEnvironments().keySet()) {
                        total++;
                        if (Main.accManager.getTags().get(username) == null) {
                            continue;
                        } else if (Main.accManager.getTags().get(username).equals("")) {
                            continue;
                        } else if (Main.accManager.getMessages().get(username) == null) {
                            continue;
                        } else if (Main.accManager.getMessages().get(username).equals("")) {
                            continue;
                        }
                        String tagname = Main.accManager.getTags().get(username);
                        ChromeDriver driver = new ChromeManager(Main.accManager.getEnvironments().get(username)).getDriver();
                        if (send(driver, tagname, Main.accManager.getMessages().get(username))) {
                            System.out.println("ACID:" + username + " メッセージを送信しました。");
                            success++;
                        } else {
                            System.out.println("ACID:" + username + " ユーザーが見つかりませんでした。");
                        }
                    }
                    System.out.println("送信成功数:" + success + " / " + total);
                } catch (Exception ex) {
                    continue;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Thread interrupted");
                System.exit(0);
            }
        }
    }

    public static boolean send(ChromeDriver driver, String tagname, String message) {
        System.out.println("タグ名:" + tagname);
        driver.get("https://www.instagram.com/explore/tags/" + tagname + "/");
        try {
            Thread.sleep(5000);
            List<WebElement> posts = driver.findElements(By.className("_aagw"));
            Thread.sleep(5000);
            String name = null;
            boolean next = true;
            while (next) {
                posts = driver.findElements(By.className("_aagw"));
                if (posts.size() < Main.count) {
                    driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                    Thread.sleep(8000);
                } else {
                    posts.get(Main.count).click();
                    Thread.sleep(4500);
                    for (WebElement element : driver.findElements(By.tagName("a"))) {
                        String clazz = "qi72231t nu7423ey n3hqoq4p r86q59rh b3qcqh3k fq87ekyn bdao358l fsf7x5fv rse6dlih s5oniofx m8h3af8h l7ghb35v kjdc1dyq kmwttqpk srn514ro oxkhqvkx rl78xhln nch0832m cr00lzj9 rn8ck1ys s3jn8y49 icdlwmnq _acan _acao _acat _acaw _a6hd";
                        if (element.getAttribute("class").equals(clazz)) {
                            name = element.getText();
                            System.out.println("名前:" + name);
                            if (!Main.sendUserManager.containsUser(name)) {
                                next = false;
                                System.out.println("ユーザーが見つかりませんでした。");
                                break;
                            } else {
                                name = null;
                                System.out.println("ユーザーが見つかりました。");
                                continue;
                            }
                        }
                    }
                }
            }

            Thread.sleep(5000);

            String url = "https://www.instagram.com/direct/inbox/";
            driver.get(url);

            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            for (WebElement element : buttons) {
                if (element.getText().equals("後で")) {
                    element.click();
                    break;
                }
            }

            Thread.sleep(5000);

            List<WebElement> svgs = driver.findElements(By.tagName("svg"));
            for (WebElement element : svgs) {
                if (element.getAttribute("aria-label").equals("新規メッセージ")) {
                    element.click();
                }
            }

            Thread.sleep(5000);

            List<WebElement> inputs = driver.findElements(By.tagName("input"));

            for (WebElement element : inputs) {
                if (element.getAttribute("placeholder").equals("検索...")) {
                    element.sendKeys(name);
                    Thread.sleep(1000 * 15);
                    break;
                }
            }

            Thread.sleep(5000);

            List<WebElement> circles = driver.findElements(By.tagName("circle"));
            for (WebElement element : circles) {
                String cx = "12.008";
                String r = "11.25";
                if (element.getAttribute("cx").equals(cx) && element.getAttribute("r").equals(r)) {
                    element.click();
                    break;
                }
            }

            Thread.sleep(5000);

            List<WebElement> divs = driver.findElements(By.tagName("div"));
            for (WebElement element : divs) {
                String clazz = "_aagz";
                String text = "次へ";
                if (element.getAttribute("class").equals(clazz) && element.getText().equals(text)) {
                    element.click();
                    break;
                }
            }

            Thread.sleep(5000);

            List<WebElement> textareas = driver.findElements(By.tagName("textarea"));
            for (WebElement element : textareas) {
                String placeholder = "メッセージ...";
                if (element.getAttribute("placeholder").equals(placeholder)) {
                    element.sendKeys(message);
                    Thread.sleep(1000);
                    element.sendKeys(Keys.ENTER);
                    Thread.sleep(5000);
                    Main.sendUserManager.addUser(name);
                    break;
                }
            }
            driver.quit();
            return true;
        } catch (Exception ex) {
            driver.quit();
            return false;
        }
    }
}
