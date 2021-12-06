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

        /** TODO: use the individual parameters instead of splitting an array */
        String dayOfWeekString = event.getOption("dayofweek").getAsString();
        int startTime = Integer.parseInt(event.getOption("start").getAsString());
        int endTime = Integer.parseInt(event.getOption("end").getAsString());
     
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        

        if (!user.isStaff()) {
            event.reply("Only instructor can create office hour.").queue();
            return;
        }


        /** TODO: 字符串处理 第一位大写 比如 Monday @番茄 */
        
        final DayOfWeek dayOfWeek;

        /** TODO: convert this into a switch/case statement */
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
        return new CommandData(getName(), "Create a new office hour session")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        /**
                                         * TODO: this should reflect the actual parameter
                                         * description
                                         */
                                        "dayofweek",
                                        "Enter day of the week")
                                .setRequired(true),
                        new OptionData(
                                        OptionType.INTEGER,
                                        /**
                                         * TODO: this should reflect the actual parameter
                                         * description
                                         */
                                        "start",
                                        "Enter start time")
                                .setRequired(true),
                        new OptionData(
                                        OptionType.INTEGER,
                                        /**
                                         * TODO: this should reflect the actual parameter
                                         * description
                                         */
                                        "end",
                                        "Enter end time")
                                .setRequired(true));
    }
}
