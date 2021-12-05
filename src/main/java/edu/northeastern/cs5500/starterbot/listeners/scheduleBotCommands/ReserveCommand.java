package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ReserveCommand implements Command {

    @Override
    public String getName() {
        return "reserve";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String[] infoArr = event.getOption("content").getAsString().split("\\s+");
        String dayOfWeek = infoArr[1].toLowerCase();
        String type = infoArr[2].toLowerCase();
        String startTime = infoArr[3];
        String endTime = infoArr[4];
        if (dayOfWeek.equals("sunday")) {
            officeHour =
                    new OfficeHour(
                            DayOfWeek.SUNDAY,
                            new OfficeHourType(type),
                            Integer.parseInt(startTime),
                            Integer.parseInt(endTime),
                            "");
        } else if (dayOfWeek.equals("monday")) {
            officeHour =
                    new OfficeHour(
                            DayOfWeek.MONDAY,
                            new OfficeHourType(type),
                            Integer.parseInt(startTime),
                            Integer.parseInt(endTime),
                            "");
        } else if (dayOfWeek.equals("tuesday")) {
            officeHour =
                    new OfficeHour(
                            DayOfWeek.TUESDAY,
                            new OfficeHourType(type),
                            Integer.parseInt(startTime),
                            Integer.parseInt(endTime),
                            "");
        } else if (dayOfWeek.equals("wednesday")) {
            officeHour =
                    new OfficeHour(
                            DayOfWeek.WEDNESDAY,
                            new OfficeHourType(type),
                            Integer.parseInt(startTime),
                            Integer.parseInt(endTime),
                            "");
        } else if (dayOfWeek.equals("thursday")) {
            officeHour =
                    new OfficeHour(
                            DayOfWeek.THURSDAY,
                            new OfficeHourType(type),
                            Integer.parseInt(startTime),
                            Integer.parseInt(endTime),
                            "");
        } else if (dayOfWeek.equals("friday")) {
            officeHour =
                    new OfficeHour(
                            DayOfWeek.FRIDAY,
                            new OfficeHourType(type),
                            Integer.parseInt(startTime),
                            Integer.parseInt(endTime),
                            "");
        } else if (dayOfWeek.equals("saturday")) {
            officeHour =
                    new OfficeHour(
                            DayOfWeek.SATURDAY,
                            new OfficeHourType(type),
                            Integer.parseInt(startTime),
                            Integer.parseInt(endTime),
                            "");
        } else {
            event.reply("You have error in your input, please try agian.").queue();
            return;
        }
        officeHourRepository.add(officeHour);

        event.reply("You made a reservation!").queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("reserve", "Make a reservation")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "format: {TAsName} {WhitchDAY} {inPerson/Online} {StartTime} {EndTime}")
                                .setRequired(true));
    }
}
