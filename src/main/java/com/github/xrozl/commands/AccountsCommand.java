package com.github.xrozl.commands;

import com.github.xrozl.Main;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AccountsCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        if (event.getName().equals("accounts")) {
            Map<String, String> accounts = Main.accManager.getLoginDetails();
            StringBuilder sb = new StringBuilder();
            sb.append(accounts.size() + " accounts found:\n");
            sb.append("```\n");
            for (Map.Entry<String, String> entry : accounts.entrySet()) {
                sb.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
            }
            sb.append("```");
            event.reply(sb.toString()).queue();
        }
    }
}
