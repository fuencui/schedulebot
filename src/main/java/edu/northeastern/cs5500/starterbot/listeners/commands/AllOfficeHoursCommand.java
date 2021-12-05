package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.Collection;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class AllOfficeHoursCommand implements Command {

    DiscordIdController discordIdController;

    public AllOfficeHoursCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
        ;
    }

    @Override
    public String getName() {
        return "allofficehour";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (user == null) {
            event.reply("Please register before checking office hour").queue();
            return;
        }
        if (user.isStaff() == false) {
            event.reply(
                            "Students are not allow to use allofficehour\n"
                                    + "please try again with alltaavailableofficehour\n"
                                    + "or listmyofficehour")
                    .queue();
            return;
        }
        Collection<NEUUser> taProfList = discordIdController.getAllTAProf();
        if (taProfList.isEmpty()) {
            event.reply("No office hours").queue();
            return;
        }

        sb.append("Office Hours: \n");
        for (NEUUser taProf : taProfList) {
            List<OfficeHour> officeHourList = taProf.getInvolvedOfficeHours();
            if (officeHourList == null || officeHourList.isEmpty()) {
                continue;
            } else {
                for (OfficeHour officeHour : officeHourList) {
                    if (officeHour.getAttendeeNUID() != null) {
                        sb.append("TA/Professor: " + taProf.getUserName() + "\t");
                        sb.append(officeHour.toString() + "\t");
                        sb.append("Type: " + officeHour.getOfficeHourType().getTypeName() + "\t");
                        sb.append(
                                "Participate Student: "
                                        + discordIdController
                                                .getNEUUserByNuid(officeHour.getAttendeeNUID())
                                                .getUserName()
                                        + "\n");
                    } else {
                        sb.append("TA/Professor: " + taProf.getUserName() + "\t");
                        sb.append(officeHour.toString() + "\t");
                        sb.append("Type: " + officeHour.getOfficeHourType().getTypeName() + "\t");
                        sb.append("Participate Student: undetermined" + "\n");
                    }
                }
                sb.append("\n");
            }
        }
        if (sb.toString().equals("Office Hours: \n")) {
            event.reply("No Office Hour").queue();
        } else {
            event.reply(sb.toString()).queue();
        }
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                getName(), "List all office hours of the class; ta/professors only.");
    }
}
