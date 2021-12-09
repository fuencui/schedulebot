package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class GetAvailableCommand extends GetScheduleCommand {

    /**
     * Returns the command name as a string.
     *
     * @return The command name "getavailable"
     */
    @Override
    public String getName() {
        return "getavailable";
    }

    private DiscordIdController discordIdController;

    public GetAvailableCommand(DiscordIdController discordIdController) {
        super(discordIdController);
        this.discordIdController = discordIdController;
    }

    @Override
    Message getReply(@Nullable NEUUser user, @Nullable String dayOfWeek) {
        MessageBuilder mb = new MessageBuilder();
        if (user == null) {
            return mb.append("You are not registered; please try /register first.").build();
        }

        if (user.isStaff() == true) {
            return mb.append("This command is only useful for student.").build();
        }

        Collection<NEUUser> taProfList = discordIdController.getAllTAProf();

        if (taProfList == null || taProfList.isEmpty()) {
            return mb.append("Can't find instructor in this class.").build();
        }

        if (dayOfWeek == null) {
            // reply with the entire week
            return mb.setEmbed(getEntireWeekReply(taProfList)).build();
        } else {
            // reply with just the requested day
            if (!isValidDayOfWeek(dayOfWeek)) {
                return mb.append(
                                "Please enter a valid day of the week (case-insensitive); e.g. 'Monday'")
                        .build();
            }
            return mb.setEmbed(getSingleDayReply(taProfList, dayOfWeek)).build();
        }
    }

    MessageEmbed getSingleDayReply(Collection<NEUUser> taProfList, String dayOfWeek) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("Available office hours for %s:", dayOfWeek));
        for (NEUUser user : taProfList) {
            List<OfficeHour> officeHours = user.getInvolvedOfficeHours();
            if (officeHours == null || officeHours.isEmpty()) {
                continue;
            }
            Collections.sort(officeHours);
            for (OfficeHour officeHour : officeHours) {
                if (officeHour
                                .getDayOfWeek()
                                .toString()
                                .toLowerCase()
                                .equals(dayOfWeek.toLowerCase())
                        && officeHour.getAttendeeNUID() == null) {
                    eb.addField(
                            officeHour.getDayOfWeek().toString(),
                            String.format(
                                    "%d:00 to %d:00; %s; Host: %s",
                                    officeHour.getStartHour(),
                                    officeHour.getEndHour(),
                                    officeHour.getOfficeHourType().getTypeName(),
                                    discordIdController
                                            .getNEUUserByNuid(officeHour.getHostNUID())
                                            .getUserName()),
                            false);
                }
            }
        }
        return eb.build();
    }

    MessageEmbed getEntireWeekReply(Collection<NEUUser> taProfList) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Available office hours for the week:");
        for (NEUUser user : taProfList) {
            List<OfficeHour> officeHours = user.getInvolvedOfficeHours();
            Collections.sort(officeHours);
            for (OfficeHour officeHour : officeHours) {
                if (officeHour.getAttendeeNUID() == null) {
                    eb.addField(
                            officeHour.getDayOfWeek().toString(),
                            String.format(
                                    "%d:00 to %d:00; %s; Host: %s",
                                    officeHour.getStartHour(),
                                    officeHour.getEndHour(),
                                    officeHour.getOfficeHourType().getTypeName(),
                                    discordIdController
                                            .getNEUUserByNuid(officeHour.getHostNUID())
                                            .getUserName()),
                            false);
                }
            }
        }
        return eb.build();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        getName(), "Get available hours for the week or a given day (For Student).")
                .addOptions(
                        new OptionData(
                                OptionType.STRING,
                                "dayofweek",
                                "Monday/Tuesday/Wednesday/Thursday/Friday/Saturday/Sunday; if empty, the entire week is displayed"));
    }
}
