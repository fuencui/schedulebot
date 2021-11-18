package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Schedule implements Model {
    ObjectId id;
    @Nonnull NEUUser host;
    @Nonnull ArrayList<OfficeHour> weeklyHours;

    @Nonnull
    PriorityQueue<OfficeHour> scheduledOfficeHours =
            new PriorityQueue<OfficeHour>(
                    new Comparator<OfficeHour>() {
                        @Override
                        public int compare(OfficeHour officeHourA, OfficeHour officeHourB) {
                            int a = setDayOfWeek(officeHourA);
                            int b = setDayOfWeek(officeHourB);
                            if (a != b) {
                                return a - b;
                            } else {
                                return officeHourA.getStartHour() - officeHourB.getStartHour();
                            }
                        }

                        private int setDayOfWeek(OfficeHour officeHour) {
                            int result;
                            DayOfWeek dayOfWeek = officeHour.getDayOfWeek();
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
                    });
}
