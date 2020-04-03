package com.doggocity.projectsoc;

public class HelpRequest {

    private String title;
    private String phone;
    private String description;
    private String date;
    private String userID;
    private String urlImage;

    public HelpRequest(){}

    public HelpRequest(String title, String phone, String description, String date, String userID, String urlImage){
        this.title = title;
        this.phone = phone;
        this.description = description;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
