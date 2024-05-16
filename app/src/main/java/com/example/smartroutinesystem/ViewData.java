package com.example.smartroutinesystem;

// ViewData.java
public class ViewData {
    private String name;
    private String course;
    private String room;

    public ViewData(String name, String course, String room) {
        this.name = name;
        this.course = course;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getRoom() {
        return room;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
