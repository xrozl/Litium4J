package com.github.xrozl.commands;

import com.github.xrozl.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.github.xrozl.Main.accManager;

public class AddAccountCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        if (event.getName().equals("add-account")) {
            String user, pass;
            try {
                user = event.getOption("account-name").getAsString();
                pass = event.getOption("account-password").getAsString();
            } catch (Exception e) {
                event.reply("Invalid arguments").queue();
                return;
            }

            event.reply("ログインしています...").queue();
            if (accManager.addAccount(user, pass)) {
                event.getChannel().sendMessage("Account added").queue();
            } else {
                event.getChannel().sendMessage("アカウントが既に存在するか、ログインできませんでした。").queue();
            }


        }
    }
}
