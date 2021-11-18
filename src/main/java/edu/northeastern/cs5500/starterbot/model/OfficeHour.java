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

    @Override
    public int compareTo(OfficeHour other) {
        int a = this.getDay();
        int b = other.getDay();
        if (a != b) {
            return a - b;
        } else {
            return this.getStartHour() - other.getStartHour();
        }
    }

    private int getDay() {
        int result;
        DayOfWeek dayOfWeek = this.getDayOfWeek();
        switch (dayOfWeek) {
            case SUNDAY:
                result = 0;
                break;
            case MONDAY:
                result = 1;
                break;
            case TUESDAY:
                result = 2;
                break;
            case WEDNESDAY:
                result = 3;
                break;
            case THURSDAY:
                result = 4;
                break;
            case FRIDAY:
                result = 5;
                break;
            case SATURDAY:
                result = 6;
                break;
            default:
                result = -1;
                break;
        }
        return result;
    }
}
