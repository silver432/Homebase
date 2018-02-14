package com.jasen.kimjaeseung.homebase.data;

/**
 * Created by kimjaeseung on 2018. 1. 12..
 */

public class User {
    private String provider;
    private String name;
    private String birth;
    private String email;
    private String image;
    private String teamCode;

    public User() {
    }

    public User(String provider, String name, String birth, String email, String image, String teamCode) {
        this.provider = provider;
        this.name = name;
        this.birth = birth;
        this.email = email;
        this.image = image;
        this.teamCode = teamCode;
    }


    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
