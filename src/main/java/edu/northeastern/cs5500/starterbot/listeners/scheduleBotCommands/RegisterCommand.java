package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.DiscordIdLog;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RegisterCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String[] infoArr = event.getOption("content").getAsString().split("\\s+");
        String role = infoArr[2].toLowerCase();
        String discordId = event.getUser().getId();
        if (!discordIdController.isDiscordIdRegistered(discordId)) {
            if (role.equals("student")) {
                neuuser = new NEUUser(infoArr[0], infoArr[1]);
            } else if (role.equals("ta") || role.equals("professor")) {
                neuuser = new NEUUser(infoArr[0], infoArr[1]);
                neuuser.setStaff(true);
            } else {
                event.reply("Invalid input, try agian. ").queue();
            }
            userRepository.add(neuuser);
            discordIdLogRepository.add(new DiscordIdLog(discordId, infoArr[1]));
            event.reply("You have been registered!").queue();
            return;
        } else {
            String nuid = discordIdController.getNuidByDiscordiD(discordId);
            event.reply("NUID :" + nuid + " You already registered!").queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("register", "register a student by name,NUID, and role")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "format: {firstname} {NUID} {role(Student/TA/Professor)}")
                                .setRequired(true));
    }
}
