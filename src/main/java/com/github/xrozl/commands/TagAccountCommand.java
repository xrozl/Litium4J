package com.github.xrozl.commands;

import com.github.xrozl.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TagAccountCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        if (event.getName().equals("tag-account")) {
            String user, tag;
            try {
                user = event.getOption("account-name").getAsString();
                tag = event.getOption("tag-name").getAsString();
            } catch (Exception e) {
                event.reply("Invalid arguments").queue();
                return;
            }

            if (Main.accManager.updateTag(user, tag)) {
                event.reply("set account tag").queue();
            } else {
                event.reply("Account not found").queue();
            }


        }
    }
}
