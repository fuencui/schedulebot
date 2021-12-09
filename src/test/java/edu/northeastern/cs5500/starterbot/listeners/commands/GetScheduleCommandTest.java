package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetScheduleCommandTest {
    private GetScheduleCommand getSchedule;
    private DiscordIdController discordIdController;
    private InMemoryRepository<NEUUser> inMemoryRepository;

    @BeforeEach
    void initialize() {
        inMemoryRepository = new InMemoryRepository<>();
        discordIdController = new DiscordIdController(inMemoryRepository);
        getSchedule = new GetScheduleCommand(discordIdController);
    }

    @Test
    public void testGetName() {
        assertEquals(getSchedule.getName(), "schedule");
    }

    @Test
    public void testToTitleCase() {
        assertEquals(GetScheduleCommand.toTitleCase("testString"), "Teststring");
    }

    @Test
    public void testGetCommandData() {
        assertEquals(
                getSchedule.getCommandData().getDescription(),
                "Get your scheduled office hours for the week or a given day.");
        assertFalse(getSchedule.getCommandData().getOptions().isEmpty());
        assertEquals(getSchedule.getCommandData().getOptions().get(0).getName(), "dayofweek");
        assertEquals(
                getSchedule.getCommandData().getOptions().get(0).getDescription(),
                "Monday/Tuesday/Wednesday/Thursday/Friday/Saturday/Sunday; if empty, the entire week is displayed");
    }
}
