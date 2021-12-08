package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
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
        int startTime = Integer.parseInt(event.getOption("start").getAsString());
        int endTime = Integer.parseInt(event.getOption("end").getAsString());
        final OptionMapping dayOfWeekOption = event.getOption("dayofweek");
        String discordId = event.getUser().getId();

        final Message reply = getReply(dayOfWeekOption, startTime, endTime, discordId);
        event.reply(reply).queue();
    }

    Message getReply(
            @Nonnull OptionMapping dayOfWeekOption,
            @Nonnull int startTime,
            @Nonnull int endTime,
            @Nonnull String discordId) {
        MessageBuilder mb = new MessageBuilder();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (!user.isStaff()) {
            return mb.append("Only instructor can create office hour.").build();
        }

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
                return mb.append("Please enter a valid day").build();
        }

        if (endTime - startTime < 0) {
            int temp = startTime;
            startTime = endTime;
            endTime = temp;
        }

        if (Math.abs(endTime - startTime) == 1) {
            return mb.setEmbed(createSingleOfficeHour(dayOfWeek, startTime, endTime, discordId))
                    .build();
        } else {
            return mb.setEmbed(createMultipleOfficeHour(dayOfWeek, startTime, endTime, discordId))
                    .build();
        }
    }

    MessageEmbed createSingleOfficeHour(
            DayOfWeek dayOfWeek, int startTime, int endTime, String discordId) {
        NEUUser user = discordIdController.getNEUUser(discordId);
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
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Create an office hour");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.addField(
                "OfficeHour",
                ":partying_face:"
                        + "Success! You created an office hour at "
                        + officeHour.getDayOfWeek().toString().toLowerCase()
                        + " from "
                        + startTime
                        + " to "
                        + endTime,
                true);
        return eb.build();
    }
    ;

    MessageEmbed createMultipleOfficeHour(
            DayOfWeek dayOfWeek, int startTime, int endTime, String discordId) {
        NEUUser user = discordIdController.getNEUUser(discordId);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Create Multiple office hours");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
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
            int tempEndTime = startTime + 1;
            eb.addField(
                    "OfficeHour",
                    ":partying_face:"
                            + "You created an office hour at "
                            + officeHour.getDayOfWeek().toString().toLowerCase()
                            + " from "
                            + startTime
                            + " to "
                            + tempEndTime,
                    true);
            startTime++;
        }
        return eb.build();
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
