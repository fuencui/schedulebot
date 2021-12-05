package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ReserveCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "reserve";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        NEUUser taProf = null;
        String[] infoArr = event.getOption("content").getAsString().split("\\s+");
        String staffName = infoArr[0].toLowerCase();
        String dayOfWeek = infoArr[1].toLowerCase();
        String type = infoArr[2].toLowerCase();
        String startTime = infoArr[3];
        String endTime = infoArr[4];
        if (type.equals("inperson")) {
            if (user.isSymptom()) {
                event.reply(
                                "In person appoinment not supported. Please visit https://news.northeastern.edu/coronavirus/ for more information.")
                        .queue();
                return;
            }
            if (!user.isVaccinated()) {
                event.reply(
                                "InPerson request require you to be vaccinated or have a waiver."
                                        + "If you have been vaccinated, please update your vaccined status by using /vaccinated command.")
                        .queue();
                return;
            }
        }
        Deque<NEUUser> taProfList = discordIdController.getAllTAProf();
        if (taProfList.isEmpty()) {
            event.reply("No office hours available").queue();
            return;
        }
        while (!taProfList.isEmpty()) {
            taProf = taProfList.poll();
            if (taProf.getUserName().toLowerCase().equals(staffName)) {
                break;
            }
        }
        if (!taProf.getUserName().equals(staffName)) {
            event.reply("Unable to find the TA/Professor name.").queue();
            return;
        }
        List<OfficeHour> taProfOfficeHours = taProf.getInvolvedOfficeHours();

        // Only Student can make a reservation.
        if (user.isStaff()) {
            event.reply("Only student can make reservation.").queue();
            return;
        }
        List<OfficeHour> userOfficeHour = user.getInvolvedOfficeHours();
        // Check if the user has a duplicate office hour
        for (OfficeHour officeHour : userOfficeHour) {
            if (officeHour.getDayOfWeek().toString().toLowerCase().equals(dayOfWeek)
                    && officeHour.getStartHour() == Integer.parseInt(startTime)
                    && officeHour.getEndHour() == Integer.parseInt(endTime)) {
                event.reply(
                                "You already have a reservation on "
                                        + dayOfWeek
                                        + " from "
                                        + startTime
                                        + " to "
                                        + endTime
                                        + ".")
                        .queue();
                return;
            }
        }
        // successFlag is to check if the input office hour exist.
        boolean successFlag = false;
        for (OfficeHour officeHour : taProfOfficeHours) {
            if (officeHour.getDayOfWeek().toString().toLowerCase().equals(dayOfWeek)
                    && officeHour.getStartHour() == Integer.parseInt(startTime)
                    && officeHour.getEndHour() == Integer.parseInt(endTime)
                    && (officeHour.getOfficeHourType().getTypeName() == type
                            || officeHour.getOfficeHourType().getTypeName() == "hybrid")) {
                officeHour.setOfficeHourType(new OfficeHourType(type));
                officeHour.setAttendeeNUID(user.getNuid());
                userOfficeHour.add(officeHour);
                successFlag = true;
                break;
            }
        }

        if (successFlag) {
            // If input office hour exist and available, update repository.
            taProf.setInvolvedOfficeHours(taProfOfficeHours);
            Collections.sort(userOfficeHour);
            user.setInvolvedOfficeHours(userOfficeHour);
            userRepository.update(taProf);
            userRepository.update(user);
            event.reply("You made a reservation!").queue();
            return;
        } else {
            event.reply("No such office hour session.").queue();
            return;
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("reserve", "Make a reservation")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "content",
                                        "format: {TAsName} {WhitchDAY} {inPerson/remote} {StartTime} {EndTime}")
                                .setRequired(true));
    }
}
