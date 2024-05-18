package com.example.smartroutinesystem;

public class Routine {
    private String dept;
    private String batch;
    private String section;
    private String day;
    private String time;
    private String teacher;
    private String course;
    private String room;


    public Routine() {
        // Default constructor required for Firebase
    }

    public Routine(String dept, String batch, String section, String day, String time, String teacher, String course, String room) {
        this.dept = dept;
        this.batch = batch;
        this.section = section;
        this.day = day;
        this.time = time;
        this.teacher = teacher;
        this.course = course;
        this.room = room;
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
