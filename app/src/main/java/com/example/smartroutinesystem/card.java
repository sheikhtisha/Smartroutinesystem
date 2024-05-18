package com.example.smartroutinesystem;

public class card {
    private String name;
    private String course;
    private String room;

    public card(String name, String course, String room){
        this.name=name;
        this.room=room;
        this.course=course;
    }
    public String getName(){return name;}
    public String getCourse(){return course;}
    public String getRoom(){return room;}
}
