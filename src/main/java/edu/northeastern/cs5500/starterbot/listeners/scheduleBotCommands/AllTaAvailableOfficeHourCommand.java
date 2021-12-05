package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.Deque;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class AllTaAvailableOfficeHourCommand implements Command {

    @Override
    public String getName() {
        return "getavailable";
    }

    private DiscordIdController discordIdController;

    public AllTaAvailableOfficeHourCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
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
        if (user.isStaff() == true) {
            event.reply("TA/Prof please use other command-line function to check").queue();
            return;
        }
        Deque<NEUUser> taProfList = discordIdController.getAllTAProf();
        if (taProfList.isEmpty()) {
            event.reply("No office hours available").queue();
            return;
        }

        sb.append("Available Office Hours: \n");
        while (!taProfList.isEmpty()) {
            NEUUser taProf = taProfList.poll();
            List<OfficeHour> officeHourList = taProf.getInvolvedOfficeHours();
            if (officeHourList == null || officeHourList.isEmpty()) {
                continue;
            } else {
                for (OfficeHour officeHour : officeHourList) {
                    if (officeHour.getAttendeeNUID() != null) continue;
                    sb.append("TA/Professor: " + taProf.getUserName() + "\t");
                    sb.append(officeHour.toString() + "\t");
                    sb.append("Type: " + officeHour.getOfficeHourType().getTypeName() + "\n");
                }
                sb.append("\n");
            }
        }
        if (sb.toString().equals("Available Office Hours: \n")) {
            event.reply("No Available Office Hours").queue();
        } else {
            event.reply(sb.toString()).queue();
        }
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "List all Available TA office Hour.");
    }
}
