package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class DiscordIdLog implements Model {
    private ObjectId id;
    @Nonnull private String discordId;
    @Nonnull private String nuid;
}
