package com.example.projectsoc;

import java.util.Date;

public class Note {

    private String title;
    private String description;
    private String phoneNumber;
    private String date;
    private String userID;
    private String urlImage;

    public Note(){}

    public Note(String title, String description, String phoneNumber, String date, String userID, String urlImage) {
        this.title = title;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.userID = userID;
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
