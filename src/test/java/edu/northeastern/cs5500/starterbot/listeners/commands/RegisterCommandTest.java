package edu.northeastern.cs5500.starterbot.listeners.commands;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterCommandTest {
    private RegisterCommand registerCommand;
    private DiscordIdController discordIdController;

    private NEUUser student;

    @BeforeEach
    void initialize() {
        student = new NEUUser("Lisa", "nuid111", "discordId111");
        student.setStaff(false);

        discordIdController = new DiscordIdController(new InMemoryRepository<NEUUser>());
        discordIdController.isDiscordIdRegistered("discordId111");
        registerCommand = new RegisterCommand(discordIdController);
    }

    @Test
    void testInvalidRole() {
        MessageEmbed messageEmbed =
                registerCommand.createAUser("Lily", "nuid222", "notTA", "discordId222");
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(messageEmbed.getDescription()).isNotEmpty();
    }

    @Test
    void testCreateAUser() {
        MessageEmbed messageEmbed =
                registerCommand.createAUser("John", "nuid333", "ta", "discordId333");
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(messageEmbed.getFields()).isNotEmpty();
    }

    @Test
    void testComeBackUser() {
        MessageBuilder mb1 = new MessageBuilder();
        EmbedBuilder eb1 = new EmbedBuilder();
        eb1.setTitle(String.format("Welcome back %s:", student.getUserName()));
        eb1.setDescription("You have already been registered");
        mb1.setEmbed(eb1.build());
        assertEquals(
                registerCommand.getReply(student, "Lisa", "nuid111", "student", "discordId111"),
                mb1.build());
    }

    @Test
    void testGetName() {
        assertThat(registerCommand.getName()).isNotEmpty();
    }

    @Test
    void testGetName2() {
        assertEquals(registerCommand.getName(), "register");
    }

    @Test
    void testGetCommandData1() {
        assertEquals(
                registerCommand.getCommandData().getDescription(),
                "register yourself with the bot");
    }

    @Test
    void testGetCommandData2() {
        assertEquals(registerCommand.getCommandData().getOptions().get(0).getName(), "name");
        assertEquals(
                registerCommand.getCommandData().getOptions().get(0).getDescription(),
                "The name you would like to be referred to as");
    }

    @Test
    void testGetCommandData3() {
        assertEquals(registerCommand.getCommandData().getOptions().get(1).getName(), "nuid");
        assertEquals(
                registerCommand.getCommandData().getOptions().get(1).getDescription(),
                "Your NUID (numbers only)");
    }

    @Test
    void testGetCommandData4() {
        assertEquals(registerCommand.getCommandData().getOptions().get(2).getName(), "role");
        assertEquals(
                registerCommand.getCommandData().getOptions().get(2).getDescription(),
                "Your role: one of (student, ta, professor)");
    }
}
