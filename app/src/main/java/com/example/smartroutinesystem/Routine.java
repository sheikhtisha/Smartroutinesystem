package com.example.smartroutinesystem;

public class Routine {
    private String dept;
    private String batch;
    private String section;
    private String day;
    private String time;
    private String location;

    public Routine() {
        // Default constructor required for Firebase
    }

    public Routine(String dept, String batch, String section, String day, String time, String location) {
        this.dept = dept;
        this.batch = batch;
        this.section = section;
        this.day = day;
        this.time = time;
        this.location = location;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
