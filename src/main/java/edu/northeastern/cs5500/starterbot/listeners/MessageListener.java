package edu.northeastern.cs5500.starterbot.listeners;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.cs5500.starterbot.listeners.commands.AllOfficeHoursCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.AllTaAvailableOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.Command;
import edu.northeastern.cs5500.starterbot.listeners.commands.CreateOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.DeleteOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.GetScheduleCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.ListAllOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.RegisterCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.ReserveCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.RulesCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.SymptomCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.TimeCommand;
import edu.northeastern.cs5500.starterbot.listeners.commands.VaccinateCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class MessageListener extends ListenerAdapter {
    private Map<String, Command> commandsHashMap;

    public MessageListener() {
        commandsHashMap = new HashMap<>();
        Command time = new TimeCommand();
        Command register = new RegisterCommand();
        Command reserve = new ReserveCommand();
        Command vaccinate = new VaccinateCommand();
        Command covidsymptom = new SymptomCommand();
        Command createOfficeHour = new CreateOfficeHourCommand();
        Command listAllOfficeHour = new ListAllOfficeHourCommand();
        Command deleteOfficeHour = new DeleteOfficeHourCommand();
        Command rules = new RulesCommand();
        Command allTaAvailableOfficeHour = new AllTaAvailableOfficeHourCommand();
        Command staffDailyOfficeHour = new GetScheduleCommand();
        Command allOfficeHours = new AllOfficeHoursCommand();

        commandsHashMap.put(time.getName(), time);
        commandsHashMap.put(register.getName(), register);
        commandsHashMap.put(reserve.getName(), reserve);
        commandsHashMap.put(vaccinate.getName(), vaccinate);
        commandsHashMap.put(covidsymptom.getName(), covidsymptom);
        commandsHashMap.put(createOfficeHour.getName(), createOfficeHour);
        commandsHashMap.put(listAllOfficeHour.getName(), listAllOfficeHour);
        commandsHashMap.put(deleteOfficeHour.getName(), deleteOfficeHour);
        commandsHashMap.put(rules.getName(), rules);
        commandsHashMap.put(allTaAvailableOfficeHour.getName(), allTaAvailableOfficeHour);
        commandsHashMap.put(staffDailyOfficeHour.getName(), staffDailyOfficeHour);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (commandsHashMap.containsKey(event.getName())) {
            Command scheduleBotCommands = commandsHashMap.get(event.getName());
            scheduleBotCommands.onSlashCommand(event);
            return;
        }
        log.warn("Could not find slash command handler for event name {}", event.getName());
    }
}
