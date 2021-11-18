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
    private boolean isStaff = false;
    @Nonnull private String userName;
    @Nonnull private String nuid;
    private boolean isVaccinated = false;
    // @Nonnull private Schedule schedule;
    List<OfficeHour> involvedOfficeHours = new ArrayList<OfficeHour>();
    // PriorityQueue<OfficeHour> involvedOfficeHours =
    //         new PriorityQueue<OfficeHour>(
    //                 new Comparator<OfficeHour>() {
    //                     @Override
    //                     public int compare(OfficeHour officeHourA, OfficeHour officeHourB) {
    //                         int a = setDayOfWeek(officeHourA);
    //                         int b = setDayOfWeek(officeHourB);
    //                         if (a != b) {
    //                             return a - b;
    //                         } else {
    //                             return officeHourA.getStartHour() - officeHourB.getStartHour();
    //                         }
    //                     }

    //                     private int setDayOfWeek(OfficeHour officeHour) {
    //                         int result;
    //                         DayOfWeek dayOfWeek = officeHour.getDayOfWeek();
    //                         switch (dayOfWeek) {
    //                             case SUNDAY:
    //                                 result = 0;
    //                                 break;
    //                             case MONDAY:
    //                                 result = 1;
    //                                 break;
    //                             case TUESDAY:
    //                                 result = 2;
    //                                 break;
    //                             case WEDNESDAY:
    //                                 result = 3;
    //                                 break;
    //                             case THURSDAY:
    //                                 result = 4;
    //                                 break;
    //                             case FRIDAY:
    //                                 result = 5;
    //                                 break;
    //                             case SATURDAY:
    //                                 result = 6;
    //                                 break;
    //                             default:
    //                                 result = -1;
    //                                 break;
    //                         }
    //                         return result;
    //                     }
    //                 });
}
