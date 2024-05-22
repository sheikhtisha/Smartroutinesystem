package com.example.smartroutinesystem;

public class Cr {
    private String uid;
    private String name;
    private String rollNumber;
    private String department;
    private String series;
    private String section;

    public Cr() {
        // Default constructor required for calls to DataSnapshot.getValue(Cr.class)
    }

    public Cr(String uid, String name, String rollNumber, String department, String series, String section) {
        this.uid = uid;
        this.name = name;
        this.rollNumber = rollNumber;
        this.department = department;
        this.series = series;
        this.section = section;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public String getSeries() {
        return series;
    }

    public String getSection() {
        return section;
    }
}
