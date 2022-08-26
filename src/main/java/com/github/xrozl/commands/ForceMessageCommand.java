package com.github.xrozl.commands;

import com.github.xrozl.DriverThread;
import com.github.xrozl.Main;
import com.github.xrozl.manager.ChromeManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.chrome.ChromeDriver;

public class ForceMessageCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        if (event.getName().equals("force-message")) {
            String user, tag, message;
            try {
                user = event.getOption("account-name").getAsString();
                tag = event.getOption("tag-name").getAsString();
                message = event.getOption("message").getAsString();
            } catch (Exception e) {
                event.reply("Invalid arguments").queue();
                return;
            }

            event.reply("メッセージを送信しています...").queue();
            ChromeDriver driver = new ChromeManager(Main.accManager.getEnvironments().get(user)).getDriver();
            if (DriverThread.send(driver, tag, message)) {
                event.getChannel().sendMessage("メッセージを送信しました。").queue();
            } else {
                event.getChannel().sendMessage("ユーザーが見つかりませんでした。").queue();
            }


        }
    }
}
