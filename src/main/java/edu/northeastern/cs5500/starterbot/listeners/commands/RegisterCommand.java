package edu.northeastern.cs5500.starterbot.listeners.commands;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

    Boolean isValidRole(String role) {
        switch (role) {
            case "student":
            case "ta":
            case "professor":
                return true;
            default:
                return false;
        }
    }

    Message getReply(
            @Nullable NEUUser user,
            @Nullable String name,
            @Nullable String nuid,
            @Nullable String role,
            String discordId) {
        MessageBuilder mb = new MessageBuilder();
        if (user == null) {
            return mb.setEmbed(createAUser(name, nuid, role, discordId)).build();
        } else {
            return mb.setEmbed(comeBackUser(user)).build();
        }
    }

    MessageEmbed comeBackUser(NEUUser user) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("Welcome back %s:", user.getUserName()));
        eb.setDescription("You have already been registered");
        return eb.build();
    }

    MessageEmbed createAUser(
            @Nullable String name, @Nullable String nuid, @Nullable String role, String discordId) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Creating A New User");
        NEUUser user = discordIdController.createNEUUser(name, nuid, role, discordId);
        if (!isValidRole(role)) {
            eb.setDescription("Invalid role." + "\n" + "Must be one of student/ta/professor !");
        } else {
            eb.addField("Name", String.format("Name: %s", user.getUserName()), true);
            eb.addField("Nuid", String.format("Nuid: %s", user.getNuid()), true);
        }
        return eb.build();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String name = event.getOption("name").getAsString();
        String nuid = event.getOption("nuid").getAsString();
        String role = event.getOption("role").getAsString().toLowerCase();
        String discordId = event.getUser().getId();
        NEUUser user;

        if (discordIdController.isDiscordIdRegistered(discordId)) {
            user = discordIdController.getNEUUser(discordId);
        } else {
            user = null;
        }

        final Message reply = getReply(user, name, nuid, role, discordId);
        event.reply(reply).queue();
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
