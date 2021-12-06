package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
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

public class ListAllOfficeHourCommand extends GetScheduleCommand {

    private DiscordIdController discordIdController;

    public ListAllOfficeHourCommand(DiscordIdController discordIdController) {
        super(discordIdController);
        this.discordIdController = discordIdController;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
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
            return mb.setEmbed(getSingleDayReply(userOfficeHourList, dayOfWeek.toLowerCase()))
                    .build();
        }
    }
    /**
     * Filter all valid office hour
     *
     * @param userOfficeHourList contains all current user's office hours.
     * @param dayOfWeek The target day of week user want to check.
     * @return a MessageEmbed contians all valid office hours.
     */
    @Override
    MessageEmbed getSingleDayReply(List<OfficeHour> userOfficeHourList, String dayOfWeek) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("Your appointments for %s:", dayOfWeek));
        OfficeHour officeHour = null;
        for (OfficeHour oh : userOfficeHourList) {
            if (dayOfWeek.equalsIgnoreCase(oh.getDayOfWeek().toString().toLowerCase())) {
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
                                    "%d:00 to %d:00; %s; Host: %s",
                                    hour.getStartHour(),
                                    hour.getEndHour(),
                                    hour.getOfficeHourType().getTypeName(),
                                    discordIdController
                                            .getNEUUserByNuid(hour.getHostNUID())
                                            .getUserName()),
                            // discordIdController
                            //         .getNEUUser(hour.getHostNUID())
                            //         .getUserName()),
                            false);
            }
        }
        return eb.build();
    }

    @Override
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
                                "%d:00 to %d:00; %s; Host: %s",
                                hour.getStartHour(),
                                hour.getEndHour(),
                                hour.getOfficeHourType().getTypeName(),
                                discordIdController
                                        .getNEUUserByNuid(hour.getHostNUID())
                                        .getUserName()),
                        // discordIdController
                        //         .getNEUUser(hour.getHostNUID())
                        //         .getUserName()),
                        false);
            }
        }
        return eb.build();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Get all office hour you involved with.")
                .addOptions(
                        new OptionData(
                                OptionType.STRING,
                                "dayofweek",
                                "Monday/Tuesday/Wednesday/Thursday/Friday/Saturday/Sunday; if empty, the entire week is displayed"));
    }

    // @Override
    // public void onSlashCommand(SlashCommandEvent event) {
    //     StringBuilder sb = new StringBuilder();
    //     String discordId = event.getUser().getId();
    //     NEUUser user = discordIdController.getNEUUser(discordId);
    //     if (user == null) {
    //         event.reply("User list is empty").queue();
    //         return;
    //     }
    //     List<OfficeHour> officeHourList = user.getInvolvedOfficeHours();
    //     if (officeHourList.size() == 0) {
    //         event.reply("NULL").queue();
    //         return;
    //     }
    //     for (OfficeHour officeHour : officeHourList) {
    //         sb.append(officeHour.toString() + "\n");
    //     }
    //     event.reply(sb.toString()).queue();
    //     return;
    // }

    // @Override
    // public CommandData getCommandData() {
    //     return new CommandData("listmyofficehour", "List all office hours you involve with.");
    // }
}
