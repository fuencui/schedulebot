package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VaccinateCommand implements ScheduleBotCommandsInterface {

    @Override
    public String getName() {
        return "vaccinated";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        OptionMapping vaccinated = event.getOption("vaccinated");

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Your status is: ");

        if (vaccinated != null) {
            responseBuilder.append(vaccinated.getAsBoolean());
        } else {
            responseBuilder.append("UNKNOWN");
        }
        event.reply(responseBuilder.toString()).queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("vaccinated", "Get or set your own vaccination status.")
                .addOptions(
                        new OptionData(
                                        OptionType.BOOLEAN,
                                        "vaccinated",
                                        "true if you are vaccinated; false if you are not")
                                .setRequired(false));
    }
}
