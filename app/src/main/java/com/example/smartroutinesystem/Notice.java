package com.example.smartroutinesystem;
public class Notice {
    private String sender;
    private String title;
    private String body;

    public Notice() {
        // Default constructor required for calls to DataSnapshot.getValue(Notice.class)
    }

    public Notice(String sender, String title, String body) {
        this.sender = sender;
        this.title = title;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

