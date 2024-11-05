package com.example.praveen_investate.model;

public class Notification {
    private String clientName;
    private String clientPhoneNumber;
    private String message;

    public Notification(String clientName, String clientPhoneNumber, String message) {
        this.clientName = clientName;
        this.clientPhoneNumber = clientPhoneNumber;
        this.message = message;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public String getMessage() {
        return message;
    }
}
