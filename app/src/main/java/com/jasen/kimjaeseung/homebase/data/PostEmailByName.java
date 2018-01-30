package com.jasen.kimjaeseung.homebase.data;

/**
 * Created by kimjaeseung on 2018. 1. 30..
 */

public class PostEmailByName {
    private String name;
    private String email;

    public PostEmailByName(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
