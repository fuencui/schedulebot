package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import java.util.List;
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
    @Nonnull private String userName;
    @Nonnull private String nuid;
    @Nonnull private String discordId;
    private boolean staff = false;
    private boolean vaccinated = false;
    private boolean symptomatic = false;
    List<OfficeHour> involvedOfficeHours = new ArrayList<OfficeHour>();
}
