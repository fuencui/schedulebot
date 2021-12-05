package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CreateOfficeHourCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "createofficehour";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String[] infoArr = event.getOption("content").getAsString().split("\\s+");
        String dayOfWeekString = infoArr[0].toUpperCase();
        String discordId = event.getUser().getId();
        String type = infoArr[3].toLowerCase();
        NEUUser user = discordIdController.getNEUUser(discordId);

        if (!user.isStaff()) {
            event.reply("Only instructor can create office hour.").queue();
            return;
        }
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
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

        if (Math.abs(endTime - startTime) == 1) {
            OfficeHour officeHour =
                    new OfficeHour(
                            dayOfWeek,
                            new OfficeHourType(type),
                            startTime,
                            endTime,
                            user.getNuid());
            List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
            involvedOfficeHours.add(officeHour);
            Collections.sort(involvedOfficeHours);
            user.setInvolvedOfficeHours(involvedOfficeHours);
            userRepository.update(user);

            event.reply(
                            "Success! You created an office hour at "
                                    + officeHour.getDayOfWeek().toString().toLowerCase()
                                    + " from "
                                    + startTime
                                    + " to "
                                    + endTime)
                    .queue();
            return;
        } else {
            if (endTime - startTime < 0) {
                int temp = startTime;
                startTime = endTime;
                endTime = temp;
            }
            StringBuilder sb = new StringBuilder();
            while (endTime - startTime > 0) {
                OfficeHour officeHour =
                        new OfficeHour(
                                dayOfWeek,
                                new OfficeHourType(type),
                                startTime,
                                startTime + 1,
                                user.getNuid());
                List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
                involvedOfficeHours.add(officeHour);
                Collections.sort(involvedOfficeHours);
                user.setInvolvedOfficeHours(involvedOfficeHours);
                userRepository.update(user);

                sb.append(
                        "You created an office hour at "
                                + officeHour.getDayOfWeek().toString().toLowerCase()
                                + " from "
                                + startTime
                                + " to "
                                + (startTime + 1)
                                + "\n");
                startTime++;
            }
            event.reply("Success! " + sb.toString()).queue();
            return;
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("createofficehour", "Create a new office hour session")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "format: {DayofWeek} {StartTime} {EndTime} {Type: hybrid/remote/inPerson}")
                                .setRequired(true));
    }
}
