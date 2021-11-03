package edu.northeastern.cs5500.starterbot.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Date timestamp;
        
        switch (event.getName()) {
            case "say":
                event.reply(event.getOption("content").getAsString()).queue();

            case "time":
                timestamp = new Date();
                DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
                df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                event.reply(df.format(timestamp)).queue();
        }
    }
}
