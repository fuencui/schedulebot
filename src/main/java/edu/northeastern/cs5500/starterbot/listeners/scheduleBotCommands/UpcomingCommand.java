package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class UpcomingCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "upcoming";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String discordId = event.getUser().getId();
        if (!discordIdController.isDiscordIdRegistered(discordId)) {
            event.reply("Please register!").queue();
        } else {
            event.reply(this.discordIdController.getUpcoming(discordId)).queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("upcoming", "Provide information of upcoming office hours");
    }
}
