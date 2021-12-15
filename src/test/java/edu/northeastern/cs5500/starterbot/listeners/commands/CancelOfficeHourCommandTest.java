package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CancelOfficeHourCommandTest {
    private CreateOfficeHourCommand createOfficeHourCommand;
    private CancelOfficeHourCommand cancelOfficeHourCommand;
    private DiscordIdController discordIdController;

    private NEUUser student1;
    private NEUUser ta1;

    private GenericRepository<NEUUser> userRepository = new InMemoryRepository<NEUUser>();

    private MessageBuilder mb1;
    private MessageBuilder mb2;
    private MessageBuilder mb3;

    @BeforeEach
    void initialize() {
        student1 = new NEUUser("Student1", "nuid0001", "discordId0001");
        ta1 = new NEUUser("TA1", "nuidTA1", "discordIdTA1");

        student1.setVaccinated(true);
        student1.setSymptomatic(false);

        ta1.setVaccinated(true);
        ta1.setSymptomatic(false);
        ta1.setStaff(true);

        userRepository.add(student1);
        userRepository.add(ta1);

        discordIdController = new DiscordIdController(userRepository);
        createOfficeHourCommand = new CreateOfficeHourCommand(discordIdController);
        cancelOfficeHourCommand = new CancelOfficeHourCommand(discordIdController);

        createOfficeHourCommand.getReply("Monday", 9, 10, "discordIdTA1");
        createOfficeHourCommand.getReply("Tuesday", 10, 11, "discordIdTA1");
        createOfficeHourCommand.getReply("Sunday", 14, 15, "discordIdTA1");

        mb1 = new MessageBuilder();
        mb1.append("Only students can cancel office hours they reserved.").build();

        mb2 = new MessageBuilder();
        EmbedBuilder eb2 = new EmbedBuilder();
        eb2.setTitle("Cancel an office hour");
        eb2.setColor(Color.CYAN);
        eb2.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb2.addField(
                "",
                ":partying_face:"
                        + "Success! You have canceled this office hour on "
                        + "monday"
                        + " from "
                        + 9
                        + " to "
                        + 10
                        + " with "
                        + "TA1"
                        + "\n"
                        + "It is now available for reservation to all students",
                true);
        mb2.setEmbed(eb2.build());

        mb3 = new MessageBuilder();
        EmbedBuilder eb3 = new EmbedBuilder();
        eb3.setTitle("Cancel an office hour");
        eb3.setColor(Color.CYAN);
        eb3.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb3.addField(
                "",
                "Cannot cancel this office hour."
                        + "\n"
                        + "Please use /schedule to check your reserved office hours.",
                true);
        mb3.setEmbed(eb3.build());
    }

    @Test
    void testGetName() {
        assertEquals(cancelOfficeHourCommand.getName(), "cancelofficehour");
    }

    @Test
    void testToTitleCase() {
        assertEquals(cancelOfficeHourCommand.toTitleCase("testString"), "Teststring");
        assertEquals(cancelOfficeHourCommand.toTitleCase(""), "");
    }

    @Test
    void testGetReplyWhenIsStaff() {
        assertEquals(
                cancelOfficeHourCommand.getReply("Monday", 9, 10, "TA1", "discordIdTA1"),
                mb1.build());
    }

    @Test
    void testGetReplyWithInvalidDay() {
        MessageBuilder mb5 = new MessageBuilder();
        assertEquals(
                cancelOfficeHourCommand.getReply("thursday", 1, 2, "TA1", "discordId0001"),
                mb5.append("Please enter a valid day").build());
    }

    @Test
    void testGetReplyByCancelAnUnreservedOfficeHour() {
        assertEquals(
                cancelOfficeHourCommand.getReply("Tuesday", 10, 11, "TA1", "discordId0001"),
                mb3.build());
    }

    @Test
    void testGetReplyByCancelAReservedOfficeHour() {
        discordIdController
                .getNEUUser("discordIdTA1")
                .getInvolvedOfficeHours()
                .get(1)
                .setAttendeeNUID("nuid0001");
        OfficeHour officeHour =
                discordIdController.getNEUUser("discordIdTA1").getInvolvedOfficeHours().get(1);
        List<OfficeHour> officeHours = new ArrayList<>();
        officeHours.add(officeHour);
        discordIdController.getNEUUser("discordId0001").setInvolvedOfficeHours(officeHours);
        assertEquals(
                cancelOfficeHourCommand.getReply("Monday", 9, 10, "TA1", "discordId0001"),
                mb2.build());
    }

    @Test
    void testGetReplyByCancelAnNonExistingOfficeHour() {
        assertEquals(
                cancelOfficeHourCommand.getReply("Friday", 17, 18, "TA1", "discordId0001"),
                mb3.build());
        assertEquals(
                cancelOfficeHourCommand.getReply("Sunday", 17, 18, "TA1", "discordId0001"),
                mb3.build());
        assertEquals(
                cancelOfficeHourCommand.getReply("Wednesday", 17, 18, "TA1", "discordId0001"),
                mb3.build());
        assertEquals(
                cancelOfficeHourCommand.getReply("Thursday", 17, 18, "TA1", "discordId0001"),
                mb3.build());
        assertEquals(
                cancelOfficeHourCommand.getReply("Saturday", 17, 18, "TA1", "discordId0001"),
                mb3.build());
    }

    @Test
    void testGetCommandData() {
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getDescription(),
                "Cancel the office hour you reserved");
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getOptions().get(0).getName(),
                "dayofweek");
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getOptions().get(0).getDescription(),
                "Enter day of the week");
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getOptions().get(1).getName(), "start");
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getOptions().get(1).getDescription(),
                "Enter start time");
        assertEquals(cancelOfficeHourCommand.getCommandData().getOptions().get(2).getName(), "end");
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getOptions().get(2).getDescription(),
                "Enter end time");
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getOptions().get(3).getName(),
                "staffname");
        assertEquals(
                cancelOfficeHourCommand.getCommandData().getOptions().get(3).getDescription(),
                "Enter staff name");
    }
}
