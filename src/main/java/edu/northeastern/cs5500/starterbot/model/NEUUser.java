package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class NEUUser implements Model {
    private ObjectId id;
    private boolean isStaff = false;
    @Nonnull private String userName;
    @Nonnull private String nuid;
    private boolean isVaccinated = false;
}
