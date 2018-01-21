package com.jasen.kimjaeseung.homebase.data;

import java.util.Date;

/**
 * Created by kimjaeseung on 2018. 1. 15..
 */

public class Player {
    private String name;
    private String image;
    private String position;
    private int backNumber;
    private double height;
    private double weight;
    private String batPosition;
    private String pitchPosition;
    private String joinedAt;
    private String outedAt;

    public Player() {
    }

    public Player(String name, String image, String position, int backNumber, double height, double weight, String batPosition, String pitchPosition, String joinedAt, String outedAt) {
        this.name = name;
        this.image = image;
        this.position = position;
        this.backNumber = backNumber;
        this.height = height;
        this.weight = weight;
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

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getOutedAt() {
        return outedAt;
    }

    public void setOutedAt(String outedAt) {
        this.outedAt = outedAt;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
