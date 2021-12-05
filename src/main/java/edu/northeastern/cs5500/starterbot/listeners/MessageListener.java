package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.AllTaAvailableOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.CreateOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.DeleteOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.GetStaffDailyOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ListAllOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.RegisterCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ReserveCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.RulesCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ScheduleBotCommandsInterface;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.SymptomCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.TimeCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.VaccinateCommand;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class MessageListener extends ListenerAdapter {
    private Map<String, ScheduleBotCommandsInterface> commandsHashMap;

    public MessageListener() {
        commandsHashMap = new HashMap<>();
        ScheduleBotCommandsInterface time = new TimeCommand();
        ScheduleBotCommandsInterface register = new RegisterCommand();
        ScheduleBotCommandsInterface reserve = new ReserveCommand();
        ScheduleBotCommandsInterface vaccinate = new VaccinateCommand();
        ScheduleBotCommandsInterface covidsymptom = new SymptomCommand();
        ScheduleBotCommandsInterface createOfficeHour = new CreateOfficeHourCommand();
        ScheduleBotCommandsInterface listAllOfficeHour = new ListAllOfficeHourCommand();
        ScheduleBotCommandsInterface deleteOfficeHour = new DeleteOfficeHourCommand();
        ScheduleBotCommandsInterface rules = new RulesCommand();
        ScheduleBotCommandsInterface alltaavailableofficehour =
                new AllTaAvailableOfficeHourCommand();
        ScheduleBotCommandsInterface staffdailyofficehour = new GetStaffDailyOfficeHourCommand();

        commandsHashMap.put(time.getName(), time);
        commandsHashMap.put(register.getName(), register);
        commandsHashMap.put(reserve.getName(), reserve);
        commandsHashMap.put(vaccinate.getName(), vaccinate);
        commandsHashMap.put(covidsymptom.getName(), covidsymptom);
        commandsHashMap.put(createOfficeHour.getName(), createOfficeHour);
        commandsHashMap.put(listAllOfficeHour.getName(), listAllOfficeHour);
        commandsHashMap.put(deleteOfficeHour.getName(), deleteOfficeHour);
        commandsHashMap.put(rules.getName(), rules);
        commandsHashMap.put(alltaavailableofficehour.getName(), alltaavailableofficehour);
        commandsHashMap.put(staffdailyofficehour.getName(), staffdailyofficehour);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (commandsHashMap.containsKey(event.getName())) {
            ScheduleBotCommandsInterface scheduleBotCommands = commandsHashMap.get(event.getName());
            scheduleBotCommands.onSlashCommand(event);
            return;
        }
        log.warn("Could not find slash command handler for event name {}", event.getName());
    }
}
