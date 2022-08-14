package com.github.xrozl.commands;

import com.github.xrozl.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class RemoveAccountCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        if (event.getName().equals("remove-account")) {
            String user;
            try {
                user = event.getOption("account-name").getAsString();
            } catch (Exception e) {
                event.reply("Invalid arguments").queue();
                return;
            }

            if (Main.accManager.removeAccount(user)) {
                event.reply("Account removed").queue();
            } else {
                event.reply("Account not found").queue();
            }
        }
    }
}
