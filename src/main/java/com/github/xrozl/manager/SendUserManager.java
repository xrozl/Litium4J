package com.github.xrozl.manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SendUserManager {

    File f;
    List<String> users;

    public SendUserManager() {
        f = new File("users.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        users = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                users.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String user) {
        users.add(user);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (String u : users) {
                bw.write(u + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeUser(String user) {
        users.remove(user);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (String u : users) {
                bw.write(u + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean containsUser(String user) {
        return users.contains(user);
    }

}
