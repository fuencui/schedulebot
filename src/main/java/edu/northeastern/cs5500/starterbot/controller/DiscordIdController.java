package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DiscordIdController {
    @Nonnull private GenericRepository<NEUUser> neuUserRepository;

    public String getNuidByDiscordId(String discordId) {
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.getDiscordId().equals(discordId)) {
                return user.getNuid();
            }
        }
        return null;
    }

    public boolean isDiscordIdRegistered(String discordId) {
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.getDiscordId().equals(discordId)) {
                return true;
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

    public NEUUser createNEUUser(String name, String nuid, String role) {
        boolean isStaff = false;
        switch (role) {
            case "ta":
            case "professor":
                isStaff = true;
                break;
            case "student":
                isStaff = false;
                break;
            default:
                log.warn("Invalid role requested: {}", role);
                return null;
        }

        NEUUser user = new NEUUser(name, nuid, role);
        user.setStaff(isStaff);
        return neuUserRepository.add(user);
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
