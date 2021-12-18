package edu.northeastern.cs5500.starterbot.listeners.commands;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckInPersonOfficeHourCommandTest {
    private CheckInPersonOfficeHourCommand checkInPersonOfficeHourCommand;
    private DiscordIdController discordIdController;
    private InMemoryRepository<NEUUser> inMemoryRepository;

    /** Test statement setup before each test */
    @BeforeEach
    void initialize() {
        inMemoryRepository = new InMemoryRepository<>();
        discordIdController = new DiscordIdController(inMemoryRepository);
        checkInPersonOfficeHourCommand = new CheckInPersonOfficeHourCommand(discordIdController);
    }

    /** Testing getName function */
    @Test
    void testGetName() {
        assertThat(checkInPersonOfficeHourCommand.getName()).isNotEmpty();
    }

    /** Test GetCommandData function */
    @Test
    void testGetCommandData() {
        assertThat(checkInPersonOfficeHourCommand.getCommandData()).isNotNull();
    }

    @Test
    void testGetCommandDataIsConsistent() {
        assertThat(checkInPersonOfficeHourCommand.getCommandData().getName())
                .isEqualTo(checkInPersonOfficeHourCommand.getName());
    }

    /** Test getSingleDayReply function when the user is not registed. */
    @Test
    void getSingleDayReplyTestForNonRegisteredUser() {
        assertThat(checkInPersonOfficeHourCommand.getReply(null, null)).isNotNull();
    }

    /** Test GetReply function when the user has no reserved office hour. */
    @Test
    public void testGetReplyForUserWithNoOfficeHour() {
        assertThat(checkInPersonOfficeHourCommand.getReply(new NEUUser(), null)).isNotNull();
    }

    /** Test GetReply function when user passed in a invalid day of week. */
    @Test
    public void testGetReplyForInvaliedDayOfWeek() {
        NEUUser neuUser = new NEUUser();
        OfficeHour officeHour = new OfficeHour();
        List<OfficeHour> officeHours = new ArrayList<>();
        officeHours.add(officeHour);
        neuUser.setInvolvedOfficeHours(officeHours);
        assertThat(checkInPersonOfficeHourCommand.getReply(neuUser, "THIS_IS_A_WRONG_DAY_OF_WEEK"))
                .isNotNull();
    }

    /** Test toTitleCase function */
    @Test
    void testToTitleCase() {
        assertThat(CheckInPersonOfficeHourCommand.toTitleCase("testString"))
                .isEqualTo("Teststring");
        assertThat(CheckInPersonOfficeHourCommand.toTitleCase("")).isEmpty();
    }

    /** Test GetEntireWeekReply function when user has no reserved office hour. */
    @Test
    public void testGetEntireWeekReplyForEmptyListGiven() {
        List<OfficeHour> officeHours = new ArrayList<>();
        MessageEmbed messageEmbed = checkInPersonOfficeHourCommand.getEntireWeekReply(officeHours);
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(messageEmbed.getFields()).isEmpty();
    }

    /** Test GetEntireWeekReply function. */
    @Test
    public void testGetEntireWeekReply() {
        List<OfficeHour> officeHours = new ArrayList<>();

        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.MONDAY, new OfficeHourType("remote"), 12, 13, "1234");
        officeHours.add(officeHour);
        MessageEmbed messageEmbed = checkInPersonOfficeHourCommand.getEntireWeekReply(officeHours);
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(!messageEmbed.getFields().isEmpty());
    }

    /** Test GetSingleDayReply function when user has no reserved office hour. */
    @Test
    public void testGetSingleDayReplyForEmptyListGiven() {
        List<OfficeHour> officeHours = new ArrayList<>();
        // Check for empty office hour list.
        MessageEmbed messageEmbed =
                checkInPersonOfficeHourCommand.getSingleDayReply(officeHours, "firday");
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(messageEmbed.getFields()).isEmpty();
    }

    /** Test GetSingleDayReply function */
    @Test
    public void testGetSingleDayReply() {
        List<OfficeHour> officeHours = new ArrayList<>();

        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.FRIDAY, new OfficeHourType("remote"), 2, 3, "1234");
        officeHours.add(officeHour);
        MessageEmbed messageEmbed =
                checkInPersonOfficeHourCommand.getSingleDayReply(officeHours, "FRIDAY");
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(!messageEmbed.getFields().isEmpty());
    }
}
