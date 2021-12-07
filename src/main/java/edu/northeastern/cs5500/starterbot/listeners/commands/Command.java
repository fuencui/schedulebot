package edu.northeastern.cs5500.starterbot.listeners.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface Command {
    public String getName();

    public void onSlashCommand(SlashCommandEvent event);

    public CommandData getCommandData();
}
