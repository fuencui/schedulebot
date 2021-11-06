package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.model.Role;
import edu.northeastern.cs5500.starterbot.model.User;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MessageListener extends ListenerAdapter {

    private GenericRepository<User> userRepository;

    public void setUserRepository(GenericRepository<User> user) {
        this.userRepository = user;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        switch (event.getName()) {
            case "register":
                {
                    String[] infoArr = event.getOption("content").getAsString().split("\\s+");
                    Role role = Role.valueOf(infoArr[2]);
                    User user = new User(infoArr[0], infoArr[1], role);
                    user.setUserName(infoArr[0]);
                    user.setNuid(infoArr[1]);
                    user.setUserRoles(role);
                    userRepository.add(user);
                    event.reply("You have been registered!").queue();
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
            case "vaccinated":
                {
                    OptionMapping vaccinated = event.getOption("vaccinated");

                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("Your status is: ");

                    if (vaccinated != null) {
                        responseBuilder.append(vaccinated.getAsBoolean());
                    } else {
                        responseBuilder.append("UNKNOWN");
                    }
                }
        }
    }
}
