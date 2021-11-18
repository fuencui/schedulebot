package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DiscordIdLog;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import javax.annotation.Nonnull;
import lombok.Data;

@Data
public class DiscordIdController {
    @Nonnull private GenericRepository<DiscordIdLog> discordIdLogRepository;
    @Nonnull private GenericRepository<NEUUser> NEUUserRepository;

    public String getNuidByDiscordiD(String discordId) {
        if (!discordIdLogRepository.getAll().isEmpty()) {
            for (DiscordIdLog d : discordIdLogRepository.getAll()) {
                if (d.getDiscordId().equals(discordId)) {
                    return d.getNuid();
                }
            }
        }
        return "null";
    }

    public boolean isDiscordIdRegistered(String discordId) {
        if (!discordIdLogRepository.getAll().isEmpty()) {
            for (DiscordIdLog d : discordIdLogRepository.getAll()) {
                if (d.getDiscordId().equals(discordId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public NEUUser getNEUUser(String discordId) {
        String nuid = this.getNuidByDiscordiD(discordId);
        for (NEUUser user : this.NEUUserRepository.getAll()) {
            if (user.getNuid().equals(nuid)) {
                return user;
            }
        }
        return null;
    }

    public String getUpcoming(String discordId) {
        StringBuilder sb = new StringBuilder();
        String nuid = this.getNuidByDiscordiD(discordId);
        NEUUser user = null;
        for (NEUUser u : this.NEUUserRepository.getAll()) {
            if (u.getNuid().equals(nuid)) {
                user = u;
            }
        }
        return "null";
        /* if (user == null) return "null";
        PriorityQueue<OfficeHour> scheduledOfficeHours =
                user.getSchedule().getScheduledOfficeHours();

        while (!scheduledOfficeHours.isEmpty()) {
            OfficeHour officeHour = scheduledOfficeHours.poll();
            sb.append(officeHour.getDayOfWeek().toString());
            sb.append(" : ");
            sb.append("From : " + officeHour.getStartHour() + " To ");
            sb.append(officeHour.getEndHour() + "\n");
        }
        return sb.toString() == "" ? "null" : sb.toString(); */
    }
}
