package com.example.smartroutinesystem;

public class User {
    private String email;
    private String fullName;
    private String phoneNumber;
    private String rollNumber;
    private String series;
    private String department;
    private String section;
    private String cr;
    private String admin;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String fullName, String phoneNumber, String rollNumber, String series, String department, String section, String cr, String admin) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.rollNumber = rollNumber;
        this.series = series;
        this.department = department;
        this.section=section;
        this.cr=cr;
        this.admin=admin;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSection() {
        return section;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public void setSection(String section) {
        this.section = section;
    }
}