package com.github.xrozl;

import com.github.xrozl.commands.*;
import com.github.xrozl.event.BotReadyListener;
import com.github.xrozl.manager.AccountManager;
import com.github.xrozl.manager.SendUserManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static AccountManager accManager;
    public static SendUserManager sendUserManager;
    public static JDA jda;
    public static String guildId;
    public static String channelId;
    public static int count;

    public static List<String> humanized = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello world!");

        accManager = new AccountManager();
        sendUserManager = new SendUserManager();

        // ignore
        // humanized
        // ignore

        count = 0;

        String token = null;

        File f = new File("config.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=");
                if (split[0].equals("token")) {
                    token = split[1];
                } else if (split[0].equals("guildId")) {
                    guildId = split[1];
                } else if (split[0].equals("channelId")) {
                    channelId = split[1];
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException");
            System.exit(1);
        }

        if (token == null) {
            System.out.println("token is not found.");
            System.exit(1);
        } else if (guildId == null) {
            System.out.println("guildId is not found.");
            System.exit(1);
        } else if (channelId == null) {
            System.out.println("channelId is not found.");
            System.exit(1);
        }

        System.out.println("token: " + token);

        try {
            JDABuilder builder = JDABuilder.createDefault(token);
            builder.setAutoReconnect(true);
            builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS);

            builder.addEventListeners(new BotReadyListener());
            builder.addEventListeners(new AccountsCommand());
            builder.addEventListeners(new AddAccountCommand());
            builder.addEventListeners(new RemoveAccountCommand());
            builder.addEventListeners(new TagAccountCommand());
            builder.addEventListeners(new MessageCommand());

            jda = builder.build().awaitReady();

            jda.updateCommands().addCommands(Commands.slash("accounts", "get registered accounts"))
                    .addCommands(Commands.slash("add-account", "add account")
                            .addOption(OptionType.STRING, "account-name", "Instagram Account Login ID", true)
                            .addOption(OptionType.STRING, "account-password", "Instagram Account Password", true))
                    .addCommands(Commands.slash("remove-account", "remove account")
                            .addOption(OptionType.STRING, "account-name", "Instagram Account Login ID", true))
                    .addCommands(Commands.slash("tag-account", "set tag")
                            .addOption(OptionType.STRING, "account-name", "Instagram Account Login ID", true)
                            .addOption(OptionType.STRING, "tag-name", "hash tag name (ex cat)", true))
                    .addCommands(Commands.slash("msg-account", "set message")
                            .addOption(OptionType.STRING, "account-name", "Instagram Account Login ID", true)
                            .addOption(OptionType.STRING, "message", "set message", true)).queue();
        } catch (LoginException e) {
            System.out.println("LoginException");
            System.exit(1);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
            System.exit(1);
        }


    }
}