package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DeleteOfficeHourCommand implements Command {

    @Override
    public String getName() {
        return "deleteofficehour";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (!user.isStaff()) {
            event.reply("Only instructors can delete (their own) office hours.").queue();
            return;
        }

        String[] infoArr = event.getOption("content").getAsString().split("\\s+");
        String dayOfWeekString = infoArr[0].toUpperCase();
        DayOfWeek dayOfWeek;
        if (dayOfWeekString.equals("SUNDAY")) {
            dayOfWeek = DayOfWeek.SUNDAY;
        } else if (dayOfWeekString.equals("MONDAY")) {
            dayOfWeek = DayOfWeek.MONDAY;
        } else if (dayOfWeekString.equals("TUESDAY")) {
            dayOfWeek = DayOfWeek.TUESDAY;
        } else if (dayOfWeekString.equals("WEDNESDAY")) {
            dayOfWeek = DayOfWeek.WEDNESDAY;
        } else if (dayOfWeekString.equals("THURSDAY")) {
            dayOfWeek = DayOfWeek.THURSDAY;
        } else if (dayOfWeekString.equals("FRIDAY")) {
            dayOfWeek = DayOfWeek.FRIDAY;
        } else if (dayOfWeekString.equals("SATURDAY")) {
            dayOfWeek = DayOfWeek.SATURDAY;
        } else {
            event.reply("Please enter a valid day.").queue();
            return;
        }
        int startTime = Integer.parseInt(infoArr[1]);
        int endTime = Integer.parseInt(infoArr[2]);
        StringBuilder sb = new StringBuilder();
        sb.append(
                dayOfWeek.toString()
                        + " from "
                        + startTime
                        + ":00 to "
                        + endTime
                        + ":00."); // same format as OfficeHour's toString()

        List<OfficeHour> officeHourList = user.getInvolvedOfficeHours();
        for (int i = 0; i < officeHourList.size(); i++) {
            if (officeHourList.get(i).toString().equals(sb.toString())
                    && officeHourList.get(i).getAttendeeNUID() == null) {
                officeHourList.remove(i);
                user.setInvolvedOfficeHours(officeHourList);
                userRepository.update(user);
                event.reply("This office hour has been deleted.").queue();
                return;
            } else if (officeHourList.get(i).toString().equals(sb.toString())
                    && officeHourList.get(i).getAttendeeNUID() != null) {
                event.reply("Reserved office hours cannot be deleted.").queue();
                return;
            }
        }
        event.reply("This office hour has not been created.").queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("deleteofficehour", "Delete your office hour if it is not reserved")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "format: {DayofWeek} {StartTime} {EndTime}")
                                .setRequired(true));
    }
}
