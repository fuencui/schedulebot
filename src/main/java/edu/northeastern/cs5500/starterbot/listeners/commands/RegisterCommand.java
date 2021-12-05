package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RegisterCommand implements Command {

    private DiscordIdController discordIdController;

    public RegisterCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String name = event.getOption("name").getAsString();
        String nuid = event.getOption("nuid").getAsString();
        String role = event.getOption("role").getAsString().toLowerCase();
        String discordId = event.getUser().getId();

        final NEUUser user;
        if (discordIdController.isDiscordIdRegistered(discordId)) {
            user = discordIdController.getNEUUser(discordId);
            // TODO: Make this into an embed
            event.reply(
                            "Welcome back:  "
                                    + user.getUserName()
                                    + "\n(You have already been registered)")
                    .queue();
            return;
        }

        user = discordIdController.createNEUUser(name, nuid, role);

        if (user == null) {
            event.reply("Invalid role; must be one of student/ta/professor").queue();
        }
        event.reply("You have been registered!").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("register", "register yourself with the bot")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "name",
                                        "The name you would like to be referred to as")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "nuid", "Your NUID (numbers only)")
                                .setRequired(true),
                        new OptionData(
                                        OptionType.STRING,
                                        "role",
                                        "Your role: one of (student, ta, professor)")
                                .setRequired(true));
    }
}
