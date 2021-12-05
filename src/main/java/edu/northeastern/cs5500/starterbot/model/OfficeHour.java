package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class OfficeHour implements Model, Comparable<OfficeHour> {
    private ObjectId id;
    @Nonnull private DayOfWeek dayOfWeek;
    @Nonnull private OfficeHourType officeHourType;
    @Nonnull private int startHour; // 0 = midnight, 23 = 11pm
    @Nonnull private int endHour; // 0 = midnight, 23 = 11pm
    @Nonnull private String hostNUID;
    private String attendeeNUID;

    @Override
    public int compareTo(OfficeHour other) {
        int a = this.getDayOfWeek().toInt();
        int b = other.getDayOfWeek().toInt();
        if (a != b) {
            return a - b;
        } else {
            return this.getStartHour() - other.getStartHour();
        }
    }
}
