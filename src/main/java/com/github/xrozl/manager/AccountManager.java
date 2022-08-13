package com.github.xrozl.manager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    Map<String, String> loginDetails;

    File accountDetailFile;

    Map<String, String> tags;
    Map<String, String> environments;

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

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.accountDetailFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=");
                this.loginDetails.put(split[0], split[1]);
                this.tags.put(split[0], split[2]);
                this.environments.put(split[0], split[3]);
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
                bw.write(entry.getKey() + "=" + entry.getValue() + "=" + this.tags.get(entry.getKey()) + "=" + this.environments.get(entry.getKey()));
                bw.newLine();
            }
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean addAccount(String username, String password) {
        if (loginDetails.containsKey(username)) {
            return false;
        }

        loginDetails.put(username, password);
        tags.put(username, "");
        environments.put(username, "env-" + System.currentTimeMillis() + "-" + username);
        saveToFile();
        return true;
    }

    boolean removeAccount(String username) {
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

    boolean updateTag(String username, String tag) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        tags.put(username, tag);
        saveToFile();
        return true;
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
}
