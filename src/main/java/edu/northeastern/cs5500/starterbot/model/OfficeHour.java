package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
public class OfficeHour implements Model {
    private ObjectId id;
    @Nonnull private DayOfWeek dayOfWeek;
    @Nonnull private OfficeHourType officeHourType;
    @Nonnull private int startHour; // 0 = midnight, 23 = 12pm
    @Nonnull private int endHour; // 0 = midnight, 23 = 12pm
}
