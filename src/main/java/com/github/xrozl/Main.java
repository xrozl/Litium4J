package com.github.xrozl;

import com.github.xrozl.commands.AccountsCommand;
import com.github.xrozl.manager.AddAccountCommand;
import com.github.xrozl.event.BotReadyListener;
import com.github.xrozl.manager.AccountManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.*;

public class Main {

    public static AccountManager accManager;
    public static JDA jda;

    public static void main(String[] args) {
        System.out.println("Hello world!");

        accManager = new AccountManager();

        String token = null;

        File f = new File("config.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=");
                if (split[0].equals("token")) {
                    token = split[1];
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
        }

        System.out.println("token: " + token);

        try {
            JDABuilder builder = JDABuilder.createDefault(token);
            builder.setAutoReconnect(true);
            builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS);

            builder.addEventListeners(new BotReadyListener());
            builder.addEventListeners(new AccountsCommand());
            builder.addEventListeners(new AddAccountCommand());

            jda = builder.build().awaitReady();

            jda.updateCommands().addCommands(Commands.slash("accounts", "get registered accounts"))
                    .addCommands(Commands.slash("add-account", "add account")
                            .addOption(OptionType.STRING, "account-name", "Instagram Account Login ID", true)
                            .addOption(OptionType.STRING, "account-password", "Instagram Account Password", true))
                    .addCommands(Commands.slash("remove-account", "remove account")
                            .addOption(OptionType.STRING, "account-name", "Instagram Account Login ID", true)
                    ).queue();
        } catch (LoginException e) {
            System.out.println("LoginException");
            System.exit(1);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
            System.exit(1);
        }


    }
}