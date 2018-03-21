package com.jasen.kimjaeseung.homebase.data;

import java.util.List;

/**
 * Created by kimjaeseung on 2018. 2. 5..
 */

public class Team {
    private String name;
    private String logo;
    private String description;
    private Object members;

    public Team(String name, String logo, String description, Object members) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getMembers() {
        return members;
    }

    public void setMembers(Object members) {
        this.members = members;
    }
}
