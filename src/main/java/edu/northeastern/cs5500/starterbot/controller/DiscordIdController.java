package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DiscordIdLog;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Data;

@Data
public class DiscordIdController {
    @Nonnull private GenericRepository<DiscordIdLog> discordIdLogRepository;
    @Nonnull private GenericRepository<NEUUser> neuUserRepository;

    public String getNuidByDiscordId(String discordId) {
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
        String nuid = getNuidByDiscordId(discordId);
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.getNuid().equals(nuid)) {
                return user;
            }
        }
        return null;
    }

    public Collection<NEUUser> getAllTAProf() {
        Deque<NEUUser> taProfList = new ArrayDeque<>();
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.isStaff() == true) {
                taProfList.add(user);
            }
        }
        return taProfList;
    }

    public boolean updateVaccination(String discordId, boolean vaccinated) {
        NEUUser user = getNEUUser(discordId);
        if (user == null) {
            return false;
        }

        user.setVaccinated(vaccinated);
        neuUserRepository.update(user);
        return true;
    }

    public boolean setInvolvedOfficeHours(String discordId, List<OfficeHour> involvedOfficeHours) {
        NEUUser user = getNEUUser(discordId);
        if (user == null) {
            return false;
        }

        user.setInvolvedOfficeHours(involvedOfficeHours);
        neuUserRepository.update(user);
        return true;
    }

    @Nullable
    public NEUUser getNEUUserByNuid(String nuid) {
        for (NEUUser user : this.neuUserRepository.getAll()) {
            if (user.getNuid().equals(nuid)) {
                return user;
            }
        }
        return null;
    }
}
