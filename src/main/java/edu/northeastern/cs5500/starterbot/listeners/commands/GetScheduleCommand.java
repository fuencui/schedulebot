package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class GetScheduleCommand implements Command {

    private DiscordIdController discordIdController;

    public GetScheduleCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /**
     * Returns the command name as a string.
     *
     * @return The command name "schedule"
     */
    @Override
    public String getName() {
        return "schedule";
    }

    MessageEmbed getReply(String inputDayOfWeek) {
        EmbedBuilder eb =
                new EmbedBuilder()
                        .setTitle("Scheduled Appointments for " + inputDayOfWeek)
                        .setDescription("Description Placeholder")
                        .setColor(new Color(15102474))
                        .setTimestamp(OffsetDateTime.now())
                        .setFooter("footer text", "https://cdn.discordapp.com/embed/avatars/0.png")
                        .setThumbnail("https://cdn.discordapp.com/embed/avatars/0.png")
                        .setImage("https://cdn.discordapp.com/embed/avatars/0.png")
                        .setAuthor(
                                "author name",
                                "https://discordapp.com",
                                "https://cdn.discordapp.com/embed/avatars/0.png")
                        .addField("🤔", "some of these properties have certain limits...", false)
                        .addField("😱", "try exceeding some of them!", false)
                        .addField(
                                "🙄",
                                "an informative error should show up, and this view will remain as-is until all issues are fixed",
                                false)
                        .addField("<:thonkang:219069250692841473>", "these last two", true)
                        .addField("<:thonkang:219069250692841473>", "are inline fields", true);
        return eb.build();
    }

    /**
     * Returns a String follows format: 1. First character is in upper case. 2. Rest of character is
     * in lower case.
     *
     * @param str a String that need to format.
     * @return a formatted String
     */
    static String toTitleCase(@Nonnull String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 1).toUpperCase());
        sb.append(str.substring(1, str.length()).toLowerCase());
        return sb.toString();
    }

    Message getReply(@Nullable NEUUser user, @Nullable String dayOfWeek) {
        MessageBuilder mb = new MessageBuilder();
        if (user == null) {
            return mb.append("You are not registered; please try /register first.").build();
        }
        List<OfficeHour> userOfficeHourList = user.getInvolvedOfficeHours();
        if (userOfficeHourList == null || userOfficeHourList.isEmpty()) {
            return mb.append("You have no booked office hours for this week.").build();
        }

        if (dayOfWeek == null) {
            // reply with the entire week
            return mb.setEmbed(getEntireWeekReply(userOfficeHourList)).build();
        } else {
            // reply with just the requested day
            if (!isValidDayOfWeek(dayOfWeek)) {
                return mb.append(
                                "Please enter a valid day of the week (case-insensitive); e.g. 'Monday'")
                        .build();
            }
            return mb.setEmbed(getSingleDayReply(userOfficeHourList, dayOfWeek)).build();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        final NEUUser user = discordIdController.getNEUUser(event.getUser().getId());
        final OptionMapping dayOfWeekOption = event.getOption("dayofweek");
        final String dayOfWeek;

        if (dayOfWeekOption == null) {
            dayOfWeek = null;
        } else {
            dayOfWeek = toTitleCase(dayOfWeekOption.getAsString().toLowerCase());
        }

        final Message reply = getReply(user, dayOfWeek);
        event.reply(reply).queue();
    }

    /**
     * Check if the passed in String is valid day of week.
     *
     * @param dayOfWeek a String that need to be check.
     * @return true if passed in String is valid; Otherwise false.
     */
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

    MessageEmbed getSingleDayReply(List<OfficeHour> userOfficeHourList, String dayOfWeek) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("Your appointments for %s:", dayOfWeek));
        OfficeHour officeHour = null;
        for (OfficeHour oh : userOfficeHourList) {
            if (dayOfWeek.equalsIgnoreCase(oh.getDayOfWeek().toString())) {
                officeHour = oh;
                break;
            }
        }
        if (officeHour == null) {
            eb.setDescription("(no appointments for this day)");
        } else {
            Collections.sort(userOfficeHourList);
            for (OfficeHour hour : userOfficeHourList) {
                if (hour.getDayOfWeek().toString().toLowerCase().equals(dayOfWeek.toLowerCase()))
                    eb.addField(
                            hour.getDayOfWeek().toString(),
                            String.format(
                                    "%d:00 to %d:00; %s",
                                    hour.getStartHour(),
                                    hour.getEndHour(),
                                    hour.getOfficeHourType().getTypeName()),
                            false);
            }
        }
        return eb.build();
    }

    MessageEmbed getEntireWeekReply(List<OfficeHour> userOfficeHourList) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your appointments for the week:");
        if (userOfficeHourList == null || userOfficeHourList.isEmpty()) {
            eb.setDescription("(no office hours for the week)");
        } else {
            Collections.sort(userOfficeHourList);
            for (OfficeHour hour : userOfficeHourList) {
                eb.addField(
                        hour.getDayOfWeek().toString(),
                        String.format(
                                "%d:00 to %d:00; %s",
                                hour.getStartHour(),
                                hour.getEndHour(),
                                hour.getOfficeHourType().getTypeName()),
                        false);
            }
        }
        return eb.build();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        getName(), "Get your scheduled office hours for the week or a given day.")
                .addOptions(
                        new OptionData(
                                OptionType.STRING,
                                "dayofweek",
                                "Monday/Tuesday/Wednesday/Thursday/Friday/Saturday/Sunday; if empty, the entire week is displayed"));
    }
}
