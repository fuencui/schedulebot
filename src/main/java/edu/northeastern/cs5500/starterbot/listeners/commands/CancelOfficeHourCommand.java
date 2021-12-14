package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
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

/** This class represents the /cancelofficehour function for students' use. */
public class CancelOfficeHourCommand implements Command {

    private DiscordIdController discordIdController;

    /**
     * Constructs a CancelOfficeHourCommand.
     *
     * @param discordIdController
     */
    public CancelOfficeHourCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /** Returns the command name as a string. */
    @Override
    public String getName() {
        return "cancelofficehour";
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
        String staffNameString = event.getOption("staffname").getAsString();
        final String staffName = toTitleCase(staffNameString);
        String discordId = event.getUser().getId();

        final Message reply = getReply(dayOfWeekString, startHour, endHour, staffName, discordId);
        event.reply(reply).queue();
    }

    /**
     * Builds a Message for onSlashCommand method to reply depending on the arguments, then decides
     * what MessageEmbed method to call
     *
     * @param dayOfWeekString String of day of week
     * @param startHour int of office hour start time
     * @param endHour int of office hour end time
     * @param staffName String of staff that hosts this office hour
     * @param discordId String of user discordId
     * @return Message for onSlashCommand method
     */
    public Message getReply(
            @Nonnull String dayOfWeekString,
            @Nonnull int startHour,
            @Nonnull int endHour,
            @Nonnull String staffName,
            @Nonnull String discordId) {
        MessageBuilder mb = new MessageBuilder();

        NEUUser user = discordIdController.getNEUUser(discordId);
        if (user.isStaff()) {
            return mb.append("Only students can cancel office hours they reserved.").build();
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

        return mb.setEmbed(cancelOfficeHour(dayOfWeek, startHour, endHour, staffName, discordId))
                .build();
    }

    /**
     * Builds an Embed for Message to cancel a single office hour from user's list and sets up an
     * advanced display format
     *
     * @param dayOfWeek enum of DayOfWeek
     * @param startHour int of office hour start time
     * @param endHour int of office hour end time
     * @param staffName String of staff that hosts this office hour
     * @param discordId String of user discordId
     * @return A MessageEmbed for Message method to build
     */
    MessageEmbed cancelOfficeHour(
            DayOfWeek dayOfWeek, int startHour, int endHour, String staffName, String discordId) {
        NEUUser user = discordIdController.getNEUUser(discordId);
        List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Cancel an office hour");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
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
                NEUUser staff =
                        discordIdController.getNEUUserByNuid(
                                involvedOfficeHours.get(i).getHostNUID());
                List<OfficeHour> staffOfficeHours = staff.getInvolvedOfficeHours();
                for (int j = 0; j < staffOfficeHours.size(); j++) {
                    if (staffOfficeHours.get(j).getDayOfWeek().equals(dayOfWeek)
                            && staffOfficeHours.get(j).getStartHour() == startHour
                            && staffOfficeHours.get(j).getEndHour() == endHour) {
                        staffOfficeHours.get(j).setOfficeHourType(new OfficeHourType("Online"));
                        staffOfficeHours.get(j).setAttendeeNUID(null);
                        discordIdController.setInvolvedOfficeHours(
                                staff.getDiscordId(), staffOfficeHours);
                    }
                }
                OfficeHour officeHour = involvedOfficeHours.get(i);
                involvedOfficeHours.remove(i);
                discordIdController.setInvolvedOfficeHours(discordId, involvedOfficeHours);
                eb.addField(
                        "",
                        ":partying_face:"
                                + "Success! You have canceled this office hour on "
                                + officeHour.getDayOfWeek().toString().toLowerCase()
                                + " from "
                                + startHour
                                + " to "
                                + endHour
                                + " with "
                                + staff.getUserName().toString()
                                + "\n"
                                + "It is now available for reservation to all students",
                        true);
                return eb.build();
            }
        }
        eb.addField(
                "",
                "Cannot cancel this office hour."
                        + "\n"
                        + "Please use /schedule to check your reserved office hours.",
                true);
        return eb.build();
    }

    /**
     * Constructs a command from guided user input.
     *
     * @return CommandData
     */
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
