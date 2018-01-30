package com.jasen.kimjaeseung.homebase.data;

/**
 * Created by kimjaeseung on 2018. 1. 26..
 */

public class PostRequestEmail {
    private String name;
    private String birth;

    public PostRequestEmail(String name, String birth) {
        this.name = name;
        this.birth = birth;
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
}
