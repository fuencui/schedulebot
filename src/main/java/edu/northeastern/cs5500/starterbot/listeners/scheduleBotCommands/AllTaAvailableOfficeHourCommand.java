package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.Deque;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class AllTaAvailableOfficeHourCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "alltaavailableofficehour";
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
        if (taProfList.size() == 0) {
            event.reply("No office hours available").queue();
            return;
        }
        while (taProfList.size() != 0) {
            NEUUser taProf = taProfList.removeFirst();
            List<OfficeHour> officeHourList = taProf.getInvolvedOfficeHours();
            if (officeHourList == null || officeHourList.size() == 0) {
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
        if (sb.toString() == "") {
            event.reply("No office hours available").queue();
            return;
        }
        event.reply(sb.toString()).queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("alltaavailableofficehour", "List all Available TA office Hour.");
    }
}
