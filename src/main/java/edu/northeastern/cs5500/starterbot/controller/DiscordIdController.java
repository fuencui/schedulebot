package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DiscordIdLog;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import javax.annotation.Nonnull;
import lombok.Data;

@Data
public class DiscordIdController {
    @Nonnull private GenericRepository<DiscordIdLog> discordIdLogRepository;

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
}
