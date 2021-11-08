package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.model.NEUUsers;
import edu.northeastern.cs5500.starterbot.model.Student;
import edu.northeastern.cs5500.starterbot.model.TAProfessor;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MessageListener extends ListenerAdapter {
    private GenericRepository<NEUUsers> userRepository;
    private NEUUsers user;

    public void setUserId(NEUUsers user) {
        this.user = user;
    }

    public void setNEUUserRepository(GenericRepository<NEUUsers> user) {
        this.userRepository = user;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        // setUserId(event.getUser());

        switch (event.getName()) {
            case "register":
                {
                    String[] infoArr = event.getOption("content").getAsString().split("\\s+");
                    String role = infoArr[2].toLowerCase();
                    if (role.equals("student")) {
                        user = new Student(infoArr[0], infoArr[1]);
                    } else if (role.equals("ta") || role.equals("professor")) {
                        user = new TAProfessor(infoArr[0], infoArr[1]);
                    } else {
                        event.reply("Invalid input, try agian. ").queue();
                        break;
                    }
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
                    // event.reply(this.user.getId()).queue();
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
                    event.reply(responseBuilder.toString()).queue();
                    break;
                }
        }
    }
}
