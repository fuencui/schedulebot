package edu.northeastern.cs5500.starterbot.model;

public enum DayOfWeek {
    SUNDAY(0, "Sunday"),
    MONDAY(1, "Monday"),
    TUESDAY(2, "Tuesday"),
    WEDNESDAY(3, "Wednesday"),
    THURSDAY(4, "Thursday"),
    FRIDAY(5, "Friday"),
    SATURDAY(6, "Saturday");

    private int dayOfWeek;
    private String dayOfWeekName;

    private DayOfWeek(int dayOfWeek, String dayOfWeekName) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfWeekName = dayOfWeekName;
    }

    public int toInt() {
        return dayOfWeek;
    }

    public String toString() {
        return dayOfWeekName;
    }
}
