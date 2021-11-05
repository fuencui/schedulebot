package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.model.User;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private GenericRepository<User> userRepository;

    public void setUserRepositoty(GenericRepository<User> user) {
        this.userRepository = user;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        switch (event.getName()) {
            case "register":
                {
                    String name = event.getOption("content").getAsString();
                    User user = new User(name);
                    user.setUserName(name);
                    userRepository.add(user);
                    event.reply("Done!").queue();
                    break;
                }
            case "time":
                {
                    Date timestamp = new Date();
                    DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
                    df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                    String temp = df.format(timestamp);
                    event.reply(temp).queue();
                    break;
                }
            case "say":
                {
                    event.reply(event.getOption("content").getAsString()).queue();
                    break;
                }
        }
    }
}
