package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DiscordIdLog;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayDeque;
import java.util.Deque;
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

    public Deque<NEUUser> getAllTAProf() {
        Deque<NEUUser> taProfList = new ArrayDeque<>();
        for (NEUUser user : this.NEUUserRepository.getAll()) {
            if (user.isStaff() == true) {
                taProfList.add(user);
            }
        }
        return taProfList;
    }
}
