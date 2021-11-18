package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.CreateOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.RegisterCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ReserveCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ScheduleBotCommandsInterface;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ScheduleBotCommandsWithRepositoryAbstract;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.TimeCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.UpcomingCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.VaccinateCommand;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageListener extends ListenerAdapter {
    private Map<String, ScheduleBotCommandsInterface> commandsHashMap;
    @Nonnull ScheduleBotCommandsInterface time;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract register;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract reserve;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract vaccinate;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract upcoming;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract createOfficeHour;

    public MessageListener() {
        commandsHashMap = new HashMap<>();
        time = new TimeCommand();
        register = new RegisterCommand();
        reserve = new ReserveCommand();
        vaccinate = new VaccinateCommand();
        upcoming = new UpcomingCommand();
        createOfficeHour = new CreateOfficeHourCommand();
        commandsHashMap.put(time.getName(), time);
        commandsHashMap.put(register.getName(), register);
        commandsHashMap.put(reserve.getName(), reserve);
        commandsHashMap.put(vaccinate.getName(), vaccinate);
        commandsHashMap.put(upcoming.getName(), upcoming);
        commandsHashMap.put(createOfficeHour.getName(), createOfficeHour);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (commandsHashMap.containsKey(event.getName())) {
            ScheduleBotCommandsInterface scheduleBotCommands = commandsHashMap.get(event.getName());
            scheduleBotCommands.onSlashCommand(event);
            return;
        }
    }
}
