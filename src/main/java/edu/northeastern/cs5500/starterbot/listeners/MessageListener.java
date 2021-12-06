package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.listeners.commands.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class MessageListener extends ListenerAdapter {
    private Map<String, Command> commandsHashMap;

    public MessageListener(DiscordIdController discordIdController) {
        commandsHashMap = new HashMap<>();
        Command time = new TimeCommand();
        Command register = new RegisterCommand(discordIdController);
        Command reserve = new ReserveCommand(discordIdController);
        Command vaccinate = new VaccinateCommand(discordIdController);
        Command covidsymptom = new SymptomCommand(discordIdController);
        Command createOfficeHour = new CreateOfficeHourCommand(discordIdController);
        Command listAllOfficeHour = new ListAllOfficeHourCommand(discordIdController);
        Command deleteOfficeHour = new DeleteOfficeHourCommand(discordIdController);
        Command allTaAvailableOfficeHour = new AllTaAvailableOfficeHourCommand(discordIdController);
        Command staffDailyOfficeHour = new GetScheduleCommand(discordIdController);
        Command allOfficeHours = new AllOfficeHoursCommand(discordIdController);
        Command getSchedule = new GetScheduleCommand(discordIdController);

        commandsHashMap.put(time.getName(), time);
        commandsHashMap.put(register.getName(), register);
        commandsHashMap.put(reserve.getName(), reserve);
        commandsHashMap.put(vaccinate.getName(), vaccinate);
        commandsHashMap.put(covidsymptom.getName(), covidsymptom);
        commandsHashMap.put(createOfficeHour.getName(), createOfficeHour);
        commandsHashMap.put(listAllOfficeHour.getName(), listAllOfficeHour);
        commandsHashMap.put(deleteOfficeHour.getName(), deleteOfficeHour);
        commandsHashMap.put(allTaAvailableOfficeHour.getName(), allTaAvailableOfficeHour);
        commandsHashMap.put(staffDailyOfficeHour.getName(), staffDailyOfficeHour);
        commandsHashMap.put(allOfficeHours.getName(), allOfficeHours);
        commandsHashMap.put(getSchedule.getName(), getSchedule);
    }

    public Collection<CommandData> getCommandData() {
        return commandsHashMap.values().stream()
                .map(c -> c.getCommandData())
                .collect(Collectors.toList());
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
