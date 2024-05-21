package com.example.smartroutinesystem;

public class Routine {

    private String teacher;
    private String course;
    private String room;


    public Routine() {
        // Default constructor required for Firebase
    }

    public Routine(String teacher, String course, String room) {
        this.teacher = teacher;
        this.course = course;
        this.room = room;
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
