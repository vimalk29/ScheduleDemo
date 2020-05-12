package com.example.demoapp.models;

public class TimeslotsPojo {
    private int id;
    private String name;
    private int minTime;
    private String deleted;
    private String disable;

    public TimeslotsPojo() {
    }

    public TimeslotsPojo(int id, String name, int minTime, String deleted, String disable) {
        this.id = id;
        this.name = name;
        this.minTime = minTime;
        this.deleted = deleted;
        this.disable = disable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }
}
