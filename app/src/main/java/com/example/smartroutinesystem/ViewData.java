package com.example.smartroutinesystem;

// ViewData.java
public class ViewData {
    private String name;
    private String course;
    private String room;
    private String time;

    public ViewData(String name, String course, String room, String time) {
        this.name = name;
        this.course = course;
        this.room = room;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
