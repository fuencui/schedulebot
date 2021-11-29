package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Staffdailyofficehour extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "staffdailyofficehour";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        String discordId = event.getUser().getId();
        NEUUser botUser = discordIdController.getNEUUser(discordId);
        if (botUser == null) {
            event.reply("You have to register first!").queue();
            return;
        }

        if (botUser.isStaff() == false) {
            event.reply("This command is designed for TA/Prof checking daily appointments.")
                    .queue();
            return;
        }

        String inputDayOfWeek = event.getOption("content").getAsString().toUpperCase();

        List<OfficeHour> botUserOfficeHourList = botUser.getInvolvedOfficeHours();
        if (botUserOfficeHourList == null) {
            event.reply("You have no booked office hour for this week.");
        }
        for (OfficeHour oh : botUserOfficeHourList) {
            if (oh.getDayOfWeek().toString().toUpperCase() == inputDayOfWeek) {
                sb.append("Your office hour appointments for" + inputDayOfWeek + "is: ");
                sb.append(oh.toString());
                sb.append("Type: " + oh.getOfficeHourType().getTypeName() + "\n");
                sb.append("\n");
                event.reply(sb.toString()).queue();
            }
            if (inputDayOfWeek != "MONDAY"
                    || inputDayOfWeek != "TUESDAY"
                    || inputDayOfWeek != "Wednesday"
                    || inputDayOfWeek != "Thursday"
                    || inputDayOfWeek != "FRIDAY"
                    || inputDayOfWeek != "SATURDAY"
                    || inputDayOfWeek != "SUNDAY") {
                event.reply("Please enter a valid day from MONDAY to SUNDAY.");
            } else {
                event.reply("You have no appointment on" + inputDayOfWeek);
            }
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("staffdailyofficehour", "Input day of the week.")
                .addOptions(
                        new OptionData(OptionType.STRING, "content", "format: {day of the week}")
                                .setRequired(true));
    }
}
