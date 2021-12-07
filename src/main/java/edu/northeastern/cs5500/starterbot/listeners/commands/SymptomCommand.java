package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SymptomCommand implements Command {

    private DiscordIdController discordIdController;

    public SymptomCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    @Override
    public String getName() {
        return "covidsymptom";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // TODO: handle the parameter not being passed since it is an optional parameter
        OptionMapping covidsymptom = event.getOption("covidsymptom");
        final boolean covidSymptomStatus;

        String discordId = event.getUser().getId();

        if (covidsymptom != null) {
            if (!discordIdController.updateSymptomatic(discordId, covidsymptom.getAsBoolean())) {
                event.reply(
                                "Unable to determine whether you have covid symptom or not; have you registered?")
                        .queue();
                return;
            }
            covidSymptomStatus = covidsymptom.getAsBoolean();
        } else {
            NEUUser user = discordIdController.getNEUUser(discordId);
            covidSymptomStatus = user.isSymptomatic();
        }

        discordIdController.updateSymptomatic(discordId, covidsymptom.getAsBoolean());
        if (covidSymptomStatus) {
            event.reply("Your are experiencing covid symptom").queue();
        } else {
            event.reply("Your are NOT experiencing covid symptom").queue();
        }
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
