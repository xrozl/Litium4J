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
                    for (String username : Main.accManager.getEnvironments().keySet()) {
                        if (Main.accManager.getTags().get(username) == null) {
                            continue;
                        }
                        String tagname = Main.accManager.getTags().get(username);
                        ChromeDriver driver = new ChromeManager(Main.accManager.getEnvironments().get(username)).getDriver();
                        driver.get("https://www.instagram.com/explore/tags/" + tagname + "/");
                        try {
                            Thread.sleep(5000);
                            List<WebElement> posts = driver.findElements(By.className("_aagw"));
                            Thread.sleep(5000);
                            String name = null;
                            boolean next = true;
                            while (next) {
                                if (posts.size() < Main.count) {
                                    driver.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                                    Thread.sleep(8000);
                                } else {
                                    posts.get(Main.count).click();
                                    Thread.sleep(5000);
                                    for (WebElement element : driver.findElements(By.tagName("a"))) {
                                        String clazz = "oajrlxb2 g5ia77u1 qu0x051f esr5mh6w e9989ue4 r7d6kgcz rq0escxv nhd2j8a9 nc684nl6 p7hjln8o kvgmc6g5 cxmmr5t8 oygrvhab hcukyx3x jb3vyjys rz4wbd8a qt6c0cv9 a8nywdso i1ao9s8h esuyzwwr f1sip0of lzcic4wl _acan _acao _acat _acaw _a6hd";
                                        if (element.getAttribute("class").equals(clazz)) {
                                            name = element.getText();
                                            if (!Main.sendUserManager.containsUser(name)) {
                                                next = false;
                                                break;
                                            } else {
                                                name = null;
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }

                            Thread.sleep(5000);

                            String url = "https://www.instagram.com/direct/inbox/";
                            driver.get(url);

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
                                    element.sendKeys(Main.accManager.getMessages().get(username));
                                    Thread.sleep(1000);
                                    element.sendKeys(Keys.ENTER);
                                    Thread.sleep(5000);
                                    Main.sendUserManager.addUser(name);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            continue;
                        } finally {
                            driver.quit();
                        }
                    }
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
}
