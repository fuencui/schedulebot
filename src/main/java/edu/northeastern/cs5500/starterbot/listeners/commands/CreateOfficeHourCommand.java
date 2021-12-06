package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
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

public class CreateOfficeHourCommand implements Command {

    @Override
    public String getName() {
        return "createofficehour";
    }

    DiscordIdController discordIdController;

    public CreateOfficeHourCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        /**
         * TODO: use the individual parameters instead of splitting an array
         */

        String dayOfWeekString = event.getOption("dayOfWeek").getAsString();
        String start = event.getOption("startTime").getAsString();
        String end = event.getOption("endTime").getAsString();
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        

        if (!user.isStaff()) {
            event.reply("Only instructor can create office hour.").queue();
            return;
        }

        // final DayOfWeek dayOfWeek;
        DayOfWeek dayOfWeek;
        /**
         * TODO: convert this into a switch/case statement
         */
        switch (dayOfWeekString) {
            case "Monday":
                dayOfWeek = DayOfWeek.MONDAY;
            case "Tuesday":
                dayOfWeek = DayOfWeek.TUESDAY;
            case "Wednesday":
                dayOfWeek = DayOfWeek.WEDNESDAY;
            case "Thursday":
                dayOfWeek = DayOfWeek.THURSDAY;
            case "Friday":
                dayOfWeek = DayOfWeek.FRIDAY;
            case "Saturday":
                dayOfWeek = DayOfWeek.SATURDAY;
            case "Sunday":
                dayOfWeek = DayOfWeek.SUNDAY;
            default:
                event.reply("Please enter a valid day").queue();
                return;
        }


        int startTime = Integer.parseInt(start);
        int endTime = Integer.parseInt(end);

        if (Math.abs(endTime - startTime) == 1) {
            OfficeHour officeHour =
                    new OfficeHour(
                            dayOfWeek,
                            new OfficeHourType("Online"),
                            startTime,
                            endTime,
                            user.getNuid());
            List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
            involvedOfficeHours.add(officeHour);
            Collections.sort(involvedOfficeHours);
            discordIdController.setInvolvedOfficeHours(discordId, involvedOfficeHours);

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
                                new OfficeHourType("Online"),
                                startTime,
                                startTime + 1,
                                user.getNuid());
                List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
                involvedOfficeHours.add(officeHour);
                Collections.sort(involvedOfficeHours);
                discordIdController.setInvolvedOfficeHours(discordId, involvedOfficeHours);

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
                                        /**
                                         * TODO: this should reflect the actual parameter description
                                         */
                                        "dayOfWeek",
                                        "Enter day of the week")
                                .setRequired(true),
                        new OptionData(
                                        OptionType.INTEGER,
                                        /**
                                         * TODO: this should reflect the actual parameter description
                                         */
                                         
                                        "startTime",
                                        "Enter start time")
                                .setRequired(true),
                        new OptionData(
                                        OptionType.INTEGER,
                                        /**
                                         * TODO: this should reflect the actual parameter description
                                         */
                                        
                                        "endTime",
                                        "Enter end time")
                                .setRequired(true));
    }
}
