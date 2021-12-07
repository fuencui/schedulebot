package edu.northeastern.cs5500.starterbot.listeners.commands;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ReserveCommand implements Command {

    private DiscordIdController discordIdController;

    public ReserveCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    @Override
    public String getName() {
        return "reserve";
    }

    static String toTitleCase(@Nonnull String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 1).toUpperCase());
        sb.append(str.substring(1, str.length()).toLowerCase());
        return sb.toString();
    }

    boolean isValidDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Monday":
            case "Tuesday":
            case "Wednesday":
            case "Thursday":
            case "Friday":
            case "Saturday":
            case "Sunday":
                return true;
            default:
                return false;
        }
    }

    boolean isValidType(String type) {
        switch (type) {
            case "Inperson":
            case "Remote":
                return true;
            default:
                return false;
        }
    }

    Message getReply(
            @Nullable NEUUser user,
            @Nonnull String dayOfWeek,
            @Nonnull String type,
            @Nonnull Integer startTime,
            @Nonnull Integer endTime,
            @Nonnull String staffName) {
        MessageBuilder mb = new MessageBuilder();
        if (user == null) {
            return mb.append("You are not registered; please try /register first.").build();
        }
        if (user.isStaff() == true) {
            return mb.append("You are a Staff; this command is only useful for Students.").build();
        }
        if (type.equals("Inperson")) {
            if (user.isSymptomatic()) {
                return mb.append(
                                "In person appoinment not supported for symptomatic attendees. Please visit https://news.northeastern.edu/coronavirus/ for more information.")
                        .build();
            }
            if (!user.isVaccinated()) {
                return mb.append(
                                "InPerson request require you to be vaccinated or have a waiver."
                                        + "If you have been vaccinated, please update your vaccined status by using /vaccinated command.")
                        .build();
            }
        }
        return mb.setEmbed(getReserveReply(type, dayOfWeek, startTime, endTime, staffName, user))
                .build();
    }

    MessageEmbed getReserveReply(
            String type,
            String dayOfWeek,
            Integer startTime,
            Integer endTime,
            String staffName,
            NEUUser user) {
        NEUUser taProf = null;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        Collection<NEUUser> taProfList = discordIdController.getAllTAProf();
        if (taProfList.isEmpty()) {
            return eb.setDescription(
                            "No office hours available right now, please check with the instructors")
                    .build();
        }
        for (NEUUser neuUser : taProfList) {
            if (toTitleCase(neuUser.getUserName()).equals(staffName)) {
                taProf = neuUser;
                break;
            }
        }
        if (taProf == null) {
            return eb.setDescription("Unable to find the TA/Professor name.").build();
        }
        List<OfficeHour> taProfOfficeHours = taProf.getInvolvedOfficeHours();
        List<OfficeHour> userOfficeHour = user.getInvolvedOfficeHours();
        // Check if the user has a duplicate office hour
        for (OfficeHour officeHour : userOfficeHour) {
            if (officeHour.getDayOfWeek().toString().equals(dayOfWeek)
                    && officeHour.getStartHour() == startTime
                    && officeHour.getEndHour() == endTime) {
                return eb.setDescription(
                                "You already have a reservation on "
                                        + String.format(
                                                "%s, from %s:00 to %s:00, make sure to be here on time",
                                                dayOfWeek, startTime, endTime))
                        .build();
            }
        }
        // successFlag is to check if the input office hour exist.
        boolean successFlag = false;
        for (OfficeHour officeHour : taProfOfficeHours) {
            if (officeHour.getDayOfWeek().toString().equals(dayOfWeek)
                    && officeHour.getStartHour() == startTime
                    && officeHour.getEndHour() == endTime) {
                officeHour.setOfficeHourType(new OfficeHourType(type));
                officeHour.setAttendeeNUID(user.getNuid());
                userOfficeHour.add(officeHour);
                successFlag = true;
                break;
            }
        }

        if (successFlag) {
            // If input office hour exist and available, update repository.
            discordIdController.setInvolvedOfficeHours(taProf.getDiscordId(), taProfOfficeHours);
            Collections.sort(userOfficeHour);
            discordIdController.setInvolvedOfficeHours(user.getDiscordId(), userOfficeHour);
            user.setInvolvedOfficeHours(userOfficeHour);
            return eb.setDescription(
                            "You have successfully scheduled a office hour appointment with: "
                                    + staffName
                                    + "\n"
                                    + String.format(
                                            "%s: %s:00 to %s:00", dayOfWeek, startTime, endTime))
                    .build();
        } else {
            return eb.setDescription(
                            "There is no matching office hour, try another time by checking the /ListAllOfficeHour")
                    .build();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // to do
        NEUUser user = discordIdController.getNEUUser(event.getUser().getId());
        String dayOfWeek = event.getOption("dayofweek").getAsString();
        String type = event.getOption("type").getAsString();
        Integer startTime = Integer.parseInt(event.getOption("starttime").getAsString());
        Integer endTime = Integer.parseInt(event.getOption("endtime").getAsString());
        String staffName = event.getOption("staffname").getAsString();
        dayOfWeek = toTitleCase(dayOfWeek);
        type = toTitleCase(type);
        staffName = toTitleCase(staffName);
        if (!isValidDayOfWeek(dayOfWeek)) {
            event.reply(
                            "Please try again, enter a valid day of week: Monday/Tuesday/Wednesday/Thusday/Friday/Saturday/Sunday")
                    .queue();
        }
        if (!isValidType(type)) {
            event.reply("Please try again, enter a valid meeting type: Remote/Inperson").queue();
        }
        Message reply = getReply(user, dayOfWeek, type, startTime, endTime, staffName);
        event.reply(reply).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("reserve", "Make a reservation")
                .addOptions(
                        new OptionData(OptionType.STRING, "dayofweek", "Enter day of week")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "starttime", "Enter start time")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "endtime", "Enter end time")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "staffname", "Enter Staff name")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "type", "Enter type").setRequired(true));
    }
}
