package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
class Schedule implements Model {
    ObjectId id;
    @Nonnull NEUUser host;
    @Nonnull ArrayList<OfficeHour> weeklyHours;
}
