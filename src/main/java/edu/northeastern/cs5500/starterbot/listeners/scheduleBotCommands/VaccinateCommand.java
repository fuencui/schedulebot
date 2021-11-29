package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class VaccinateCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "vaccinated";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        OptionMapping vaccinated = event.getOption("vaccinated");

        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        user.setVaccinated(vaccinated.getAsBoolean());
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Your status is: ");
        responseBuilder.append(vaccinated.getAsBoolean());
        userRepository.update(user);
        event.reply(responseBuilder.toString()).queue();
        return;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        "vaccinated",
                        "Get or set your own vaccination status. Please answer True or False only")
                .addOptions(
                        new OptionData(
                                        OptionType.BOOLEAN,
                                        "vaccinated",
                                        "true if you are vaccinated; false if you are not")
                                .setRequired(false));
    }
}
