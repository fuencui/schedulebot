package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VaccinateCommand implements Command {

    private DiscordIdController discordIdController;

    public VaccinateCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    @Override
    public String getName() {
        return "vaccinated";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        OptionMapping vaccinated = event.getOption("vaccinated");
        final boolean vaccinationStatus;

        String discordId = event.getUser().getId();

        if (vaccinated != null) {
            if (!discordIdController.updateVaccination(discordId, vaccinated.getAsBoolean())) {
                event.reply("Unable to determine your vaccination status; have you registered?")
                        .queue();
                return;
            }
            vaccinationStatus = vaccinated.getAsBoolean();
        } else {
            NEUUser user = discordIdController.getNEUUser(discordId);
            vaccinationStatus = user.isVaccinated();
        }

        discordIdController.updateVaccination(discordId, vaccinated.getAsBoolean());
        if (vaccinationStatus) {
            event.reply("Your status is: vaccinated").queue();
        } else {
            event.reply("Your status is: NOT vaccinated").queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("vaccinated", "Get or set your own vaccination status.")
                .addOptions(
                        new OptionData(
                                        OptionType.BOOLEAN,
                                        "vaccinated",
                                        "true if you are vaccinated or have a waiver; false if you are not")
                                .setRequired(false));
    }
}
