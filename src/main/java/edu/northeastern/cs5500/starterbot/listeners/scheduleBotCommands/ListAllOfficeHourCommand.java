package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ListAllOfficeHourCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "listmyofficehour";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (user == null) {
            event.reply("User list is empty").queue();
            return;
        }
        List<OfficeHour> officeHourList = user.getInvolvedOfficeHours();
        if (officeHourList.size() == 0) {
            event.reply("NULL").queue();
            return;
        }
        for (OfficeHour officeHour : officeHourList) {
            sb.append(officeHour.toString() + "\n");
        }
        event.reply(sb.toString()).queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("listmyofficehour", "List all office hours you involve with.");
    }
}
