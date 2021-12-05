package edu.northeastern.cs5500.starterbot.listeners.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class TimeCommand implements Command {

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Date timestamp = new Date();
        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String temp = df.format(timestamp);
        event.reply(temp).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("time", "Display current time");
    }
}
