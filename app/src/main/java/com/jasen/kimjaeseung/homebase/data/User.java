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

    public User() {
    }

    public User(String provider, String name, String birth, String email, String image) {
        this.provider = provider;
        this.name = name;
        this.birth = birth;
        this.email = email;
        this.image = image;
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
