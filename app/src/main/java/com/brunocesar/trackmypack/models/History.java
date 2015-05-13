package com.brunocesar.trackmypack.models;

public class History {

    private long id;
    private long idPackage;

    private String date;
    private String place;
    private String action;
    private String details;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdPackage() {
        return idPackage;
    }

    public void setIdPackage(long idPackage) {
        this.idPackage = idPackage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
