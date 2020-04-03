package com.doggocity.projectsoc;

public class User {

    private String nameSurname;
    private String mail;
    private String dogNumber;
    private boolean organization;

    public User(){}

    public User(String nameSurname, String mail, String dogNumber,  boolean organization) {
        this.nameSurname = nameSurname;
        this.mail = mail;
        this.dogNumber = dogNumber;
        this.organization = organization;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDogNumber() {
        return dogNumber;
    }

    public void setDogNumber(String dogNumber) {
        this.dogNumber = dogNumber;
    }

    public boolean getOrganization() {
        return organization;
    }

    public void setOrganization(boolean organization) {
        this.organization = organization;
    }
}
