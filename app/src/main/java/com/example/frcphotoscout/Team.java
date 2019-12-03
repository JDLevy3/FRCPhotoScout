package com.example.frcphotoscout;
//holds team information including:
//team name
//team number
//team key?
//captured image
//written scouting report text
//bitmap thumbnail

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Team {

    private String number;
    private String name;
    private String key;
    private String report;
    private Bitmap thumbnail;
    private Drawable image;

    public Team(String number, String name, String key) {
        this.number = number;
        this.name = name;
        this.key = key;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

}
