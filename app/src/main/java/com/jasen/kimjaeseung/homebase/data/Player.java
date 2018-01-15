package com.jasen.kimjaeseung.homebase.data;

import java.util.Date;

/**
 * Created by kimjaeseung on 2018. 1. 15..
 */

public class Player {
    private String name;
    private String image;
    private int backNumber;
    private String batPosition;
    private String pitchPosition;
    private Date joinedAt;
    private Date outedAt;

    public Player(){}

    public Player(String name, String image, int backNumber, String batPosition, String pitchPosition, Date joinedAt, Date outedAt) {
        this.name = name;
        this.image = image;
        this.backNumber = backNumber;
        this.batPosition = batPosition;
        this.pitchPosition = pitchPosition;
        this.joinedAt = joinedAt;
        this.outedAt = outedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getBackNumber() {
        return backNumber;
    }

    public void setBackNumber(int backNumber) {
        this.backNumber = backNumber;
    }

    public String getBatPosition() {
        return batPosition;
    }

    public void setBatPosition(String batPosition) {
        this.batPosition = batPosition;
    }

    public String getPitchPosition() {
        return pitchPosition;
    }

    public void setPitchPosition(String pitchPosition) {
        this.pitchPosition = pitchPosition;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Date getOutedAt() {
        return outedAt;
    }

    public void setOutedAt(Date outedAt) {
        this.outedAt = outedAt;
    }
}
