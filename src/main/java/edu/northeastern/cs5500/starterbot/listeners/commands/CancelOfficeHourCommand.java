package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CancelOfficeHourCommand implements Command {

    private DiscordIdController discordIdController;

    public CancelOfficeHourCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    @Override
    public String getName() {
        return "cancelofficehour";
    }

    static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 1).toUpperCase());
        sb.append(str.substring(1, str.length()).toLowerCase());
        return sb.toString();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (user.isStaff()) {
            event.reply("Only students can cancel office hours they reserved.").queue();
            return;
        }

        final OptionMapping dayOfWeekOption = event.getOption("dayofweek");
        final DayOfWeek dayOfWeek;
        String dayOfWeekString;
        if (dayOfWeekOption == null) {
            dayOfWeekString = null;
        } else {
            dayOfWeekString = toTitleCase(dayOfWeekOption.getAsString());
        }
        switch (dayOfWeekString) {
            case "Monday":
                dayOfWeek = DayOfWeek.MONDAY;
                break;
            case "Tuesday":
                dayOfWeek = DayOfWeek.TUESDAY;
                break;
            case "Wednesday":
                dayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case "Thursday":
                dayOfWeek = DayOfWeek.THURSDAY;
                break;
            case "Friday":
                dayOfWeek = DayOfWeek.FRIDAY;
                break;
            case "Saturday":
                dayOfWeek = DayOfWeek.SATURDAY;
                break;
            case "Sunday":
                dayOfWeek = DayOfWeek.SUNDAY;
                break;
            default:
                event.reply("Please enter a valid day").queue();
                return;
        }

        int startHour = Integer.parseInt(event.getOption("start").getAsString());
        int endHour = Integer.parseInt(event.getOption("end").getAsString());
        String staffNameString = event.getOption("staffname").getAsString();
        final String staffName = toTitleCase(staffNameString);

        List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
        for (int i = 0; i < involvedOfficeHours.size(); i++) {
            if (involvedOfficeHours.get(i).getDayOfWeek().equals(dayOfWeek)
                    && involvedOfficeHours.get(i).getStartHour() == startHour
                    && involvedOfficeHours.get(i).getEndHour() == endHour
                    && toTitleCase(
                                    discordIdController
                                            .getNEUUserByNuid(
                                                    involvedOfficeHours.get(i).getHostNUID())
                                            .getUserName())
                            .equals(staffName)) {
                List<OfficeHour> staffOfficeHours =
                        discordIdController
                                .getNEUUserByNuid(involvedOfficeHours.get(i).getHostNUID())
                                .getInvolvedOfficeHours();
                for (int j = 0; j < staffOfficeHours.size(); j++) {
                    if (staffOfficeHours.get(j).getDayOfWeek().equals(dayOfWeek)
                            && staffOfficeHours.get(j).getStartHour() == startHour
                            && staffOfficeHours.get(j).getEndHour() == endHour) {
                        staffOfficeHours.get(j).setAttendeeNUID(null);
                    }
                    involvedOfficeHours.remove(i);
                    discordIdController.setInvolvedOfficeHours(discordId, involvedOfficeHours);
                    event.reply(
                                    "You have successfully canceled this office hour!"
                                            + "\n"
                                            + "It is now available to all students.")
                            .queue();
                    return;
                }
            }
        }
        event.reply(
                        "Cannot cancel this office hour."
                                + "\n"
                                + "Please use /schedule to check your reserved office hours.")
                .queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Cancel the office hour you reserved")
                .addOptions(
                        new OptionData(OptionType.STRING, "dayofweek", "Enter day of the week")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "start", "Enter start time")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "end", "Enter end time")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "staffname", "Enter staff name")
                                .setRequired(true));
    }
}
