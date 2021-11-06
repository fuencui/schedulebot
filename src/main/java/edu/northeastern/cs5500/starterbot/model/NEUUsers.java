package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class NEUUsers implements Model {
    private ObjectId id;
    @Nonnull private String userName;
    @Nonnull private String nuid;
    @Nonnull private Role userRoles;
}
