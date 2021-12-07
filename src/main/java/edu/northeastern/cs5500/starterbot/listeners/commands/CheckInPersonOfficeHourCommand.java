package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CheckInPersonOfficeHourCommand extends GetScheduleCommand {

    public CheckInPersonOfficeHourCommand(DiscordIdController discordIdController) {
        super(discordIdController);
    }

    @Override
    public String getName() {
        return "checkinperson";
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
                if (hour.getDayOfWeek().toString().toLowerCase().equals(dayOfWeek.toLowerCase())
                        && hour.getOfficeHourType().getTypeName().toLowerCase().equals("inperson"))
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
    MessageEmbed getEntireWeekReply(List<OfficeHour> userOfficeHourList) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your appointments for the week:");
        if (userOfficeHourList == null || userOfficeHourList.isEmpty()) {
            eb.setDescription("(no office hours for the week)");
        } else {
            Collections.sort(userOfficeHourList);
            for (OfficeHour hour : userOfficeHourList) {
                if (hour.getOfficeHourType().getTypeName().toLowerCase().equals("inperson")) {
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
        }
        return eb.build();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        getName(),
                        "Get your in-person office hours for the week or a given day (TA/Professor only).")
                .addOptions(
                        new OptionData(
                                OptionType.STRING,
                                "dayofweek",
                                "Monday/Tuesday/Wednesday/Thursday/Friday/Saturday/Sunday; if empty, the entire week is displayed"));
    }

    // DiscordIdController discordIdController;

    // public CheckInPersonOfficeHourCommand(DiscordIdController discordIdController) {
    //     this.discordIdController = discordIdController;
    // }

    // @Override
    // public String getName() {
    //     return "checkinpersonoh";
    // }

    // @Override
    // public void onSlashCommand(SlashCommandEvent event) {
    //     StringBuilder sb = new StringBuilder();
    //     String discordId = event.getUser().getId();
    //     NEUUser botUser = discordIdController.getNEUUser(discordId);
    //     if (botUser == null) {
    //         event.reply("You have to register first!").queue();
    //         return;
    //     }
    //     if (botUser.isStaff() == false) {
    //         event.reply("This command only allows TA/Prof to check their in-perosn
    // appointments.")
    //                 .queue();
    //         return;
    //     }
    //     List<OfficeHour> botUserOfficeHourList = botUser.getInvolvedOfficeHours();
    //     for (OfficeHour oh : botUserOfficeHourList) {
    //         if (oh.getOfficeHourType().getTypeName().toString().toUpperCase() == "INPERSON") {
    //             sb.append("In-person OH: " + oh.toString());
    //             sb.append("\n");
    //             event.reply(sb.toString()).queue();
    //         }

    //         event.reply("You don't have inperson office hour appointments.").queue();
    //     }
    // }

    // @Override
    // public CommandData getCommandData() {
    //     return new CommandData("checkinpersonoh", "List all of my in-person meeting.");
    // }
}
