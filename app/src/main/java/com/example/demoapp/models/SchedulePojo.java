package com.example.demoapp.models;

public class SchedulePojo {
    private String month;
    private String date;
    private String day;
    private String fullDate;
    private TimeslotsPojo[] timeslots;

    public SchedulePojo() {
    }

    public SchedulePojo(String month, String date, String day, String fullDate, TimeslotsPojo[] timeslots) {
        this.month = month;
        this.date = date;
        this.day = day;
        this.fullDate = fullDate;
        this.timeslots = timeslots;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public TimeslotsPojo[] getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(TimeslotsPojo[] timeslots) {
        this.timeslots = timeslots;
    }
}
