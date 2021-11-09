package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.model.*;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MessageListener extends ListenerAdapter {
    private GenericRepository<NEUUser> userRepository;
    private GenericRepository<OfficeHour> OHRepository;
    private NEUUser user;
    OfficeHour oh;

    public void setUserId(NEUUser user) {
        this.user = user;
    }

    public void setNEUUserRepository(GenericRepository<NEUUser> user) {
        this.userRepository = user;
    }

    public void setOHRepository(GenericRepository<OfficeHour> oh) {
        this.OHRepository = oh;
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
                        user = new NEUUser(infoArr[0], infoArr[1]);
                    } else if (role.equals("ta") || role.equals("professor")) {
                        user = new NEUUser(infoArr[0], infoArr[1]);
                        user.setStuff(true);
                    } else {
                        event.reply("Invalid input, try agian. ").queue();
                        break;
                    }
                    userRepository.add(user);
                    event.reply("You have been registered!").queue();
                    break;
                }
            case "reserve":
                {
                    String[] infoArr = event.getOption("content").getAsString().split("\\s+");
                    String dayOfWeek = infoArr[1].toLowerCase();
                    String type = infoArr[2].toLowerCase();
                    String startTime = infoArr[3];
                    String endTime = infoArr[4];
                    if (dayOfWeek.equals("sunday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.SUNDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("monday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.MONDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("tuesday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.TUESDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("wednesday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.WEDNESDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("thursday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.THURSDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("friday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.FRIDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("saturday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.SATURDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else {
                        event.reply("You have error in your input, please try agian.").queue();
                        break;
                    }
                    this.OHRepository.add(oh);

                    event.reply("You made a reservation!").queue();
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
                // case "say":
                //     {
                //         event.reply(event.getOption("content").getAsString()).queue();
                //         // event.reply(this.user.getId()).queue();
                //         break;
                //     }
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
