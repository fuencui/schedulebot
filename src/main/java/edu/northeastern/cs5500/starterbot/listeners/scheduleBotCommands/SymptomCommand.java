package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SymptomCommand implements Command {

    @Override
    public String getName() {
        return "covidsymptom";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        OptionMapping covidsymptom = event.getOption("covidsymptom");

        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        user.setSymptom(covidsymptom.getAsBoolean());
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("You are experiencing covid symptom: ");
        responseBuilder.append(covidsymptom.getAsBoolean());
        userRepository.update(user);
        event.reply(responseBuilder.toString()).queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        "covidsymptom", "Please indicate if you are experiencing covid symptom.")
                .addOptions(
                        new OptionData(
                                        OptionType.BOOLEAN,
                                        "covidsymptom",
                                        "true if you are experiencing covid symptom; false if you are not")
                                .setRequired(false));
    }
}
