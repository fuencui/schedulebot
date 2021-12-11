package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.awt.Color;
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

/** This class represents the /deleteofficehour function for instructors' use. */
public class DeleteOfficeHourCommand implements Command {

    private DiscordIdController discordIdController;

    /**
     * Constructs a DeleteOfficeHourCommand.
     *
     * @param discordIdController
     */
    public DeleteOfficeHourCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /** Returns the command name as a string. */
    @Override
    public String getName() {
        return "deleteofficehour";
    }

    /**
     * Returns a String with its first character in upper case, followed by rest of characters in
     * lower case.
     *
     * @param str a String
     * @return a formatted String
     */
    static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 1).toUpperCase());
        sb.append(str.substring(1, str.length()).toLowerCase());
        return sb.toString();
    }

    /**
     * Performs a slash command method.
     *
     * @param event an event from Discord's slash command
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        final OptionMapping dayOfWeekOption = event.getOption("dayofweek");
        String dayOfWeekString;
        if (dayOfWeekOption == null) {
            dayOfWeekString = null;
        } else {
            dayOfWeekString = toTitleCase(dayOfWeekOption.getAsString());
        }
        int startHour = Integer.parseInt(event.getOption("start").getAsString());
        int endHour = Integer.parseInt(event.getOption("end").getAsString());
        String discordId = event.getUser().getId();

        final Message reply = getReply(dayOfWeekString, startHour, endHour, discordId);
        event.reply(reply).queue();
    }

    /**
     * Builds a Message for onSlashCommand method to reply depending on the arguments, then decides
     * what MessageEmbed method to call
     *
     * @param dayOfWeekString String of day of week
     * @param startHour int of office hour start time
     * @param endHour int of office hour end time
     * @param discordId String of user discordId
     * @return Message for onSlashCommand method
     */
    public Message getReply(
            @Nonnull String dayOfWeekString,
            @Nonnull int startHour,
            @Nonnull int endHour,
            @Nonnull String discordId) {
        MessageBuilder mb = new MessageBuilder();

        NEUUser user = discordIdController.getNEUUser(discordId);
        if (!user.isStaff()) {
            return mb.append("Only instructors can delete (their own) office hours.").build();
        }

        final DayOfWeek dayOfWeek;
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

        return mb.setEmbed(deleteOfficeHour(dayOfWeek, startHour, endHour, discordId)).build();
    }

    /**
     * Builds an Embed for Message to delete a single office hour from user's list and sets up an
     * advanced display format
     *
     * @param dayOfWeek enum of DayOfWeek
     * @param startHour int of office hour start time
     * @param endHour int of office hour end time
     * @param discordId String of user discordId
     * @return A MessageEmbed for Message method to build
     */
    MessageEmbed deleteOfficeHour(
            DayOfWeek dayOfWeek, int startHour, int endHour, String discordId) {
        NEUUser user = discordIdController.getNEUUser(discordId);
        List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Delete an office hour");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        for (int i = 0; i < involvedOfficeHours.size(); i++) {
            if (involvedOfficeHours.get(i).getDayOfWeek().equals(dayOfWeek)
                    && involvedOfficeHours.get(i).getStartHour() == startHour
                    && involvedOfficeHours.get(i).getEndHour() == endHour
                    && involvedOfficeHours.get(i).getAttendeeNUID() == null) {
                OfficeHour officeHour = involvedOfficeHours.get(i);
                involvedOfficeHours.remove(i);
                discordIdController.setInvolvedOfficeHours(discordId, involvedOfficeHours);
                eb.addField(
                        "",
                        ":partying_face:"
                                + "Success! You have deleted this office hour on "
                                + officeHour.getDayOfWeek().toString().toLowerCase()
                                + " from "
                                + startHour
                                + " to "
                                + endHour,
                        true);
                return eb.build();
            } else if (involvedOfficeHours.get(i).getDayOfWeek().equals(dayOfWeek)
                    && involvedOfficeHours.get(i).getStartHour() == startHour
                    && involvedOfficeHours.get(i).getEndHour() == endHour
                    && involvedOfficeHours.get(i).getAttendeeNUID() != null) {
                eb.addField("", "Reserved office hours cannot be deleted.", true);
                return eb.build();
            }
        }
        eb.addField("", "This office hour has not been created.", true);
        return eb.build();
    }

    /**
     * Constructs a command from guided user input.
     *
     * @return CommandData
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Delete your office hour if it is not reserved")
                .addOptions(
                        new OptionData(OptionType.STRING, "dayofweek", "Enter day of the week")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "start", "Enter start time")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "end", "Enter end time")
                                .setRequired(true));
    }
}
