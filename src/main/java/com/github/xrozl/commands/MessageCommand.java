package com.github.xrozl.commands;

import com.github.xrozl.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        if (event.getName().equals("message")) {
            String user, message;
            try {
                user = event.getOption("account-name").getAsString();
                message = event.getOption("message").getAsString();
            } catch (Exception e) {
                event.reply("Invalid arguments").queue();
                return;
            }

            if (Main.accManager.updateMessage(user, message)) {
                event.reply("Message set").queue();
            } else {
                event.reply("Account not found").queue();
            }


        }
    }
}
