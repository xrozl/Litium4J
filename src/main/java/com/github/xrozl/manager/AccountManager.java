package com.github.xrozl.manager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    Map<String, String> loginDetails;

    File accountDetailFile;

    Map<String, String> tags;

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

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.accountDetailFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=");
                this.loginDetails.put(split[0], split[1]);
                this.tags.put(split[0], split[2]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean addAccount(String username, String password) {
        if (loginDetails.containsKey(username)) {
            return false;
        }

        loginDetails.put(username, password);
        return true;
    }

    boolean removeAccount(String username) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        loginDetails.remove(username);
        return true;
    }

    boolean updateAccount(String username, String password) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        loginDetails.put(username, password);
        return true;
    }

    boolean updateTag(String username, String tag) {
        if (!loginDetails.containsKey(username)) {
            return false;
        }

        tags.put(username, tag);
        return true;
    }

}
