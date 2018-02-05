package com.jasen.kimjaeseung.homebase.data;

import java.util.List;

/**
 * Created by kimjaeseung on 2018. 2. 5..
 */

public class Team {
    private String name;
    private String logo;
    private String description;
    private String homeStadium;

    public Team(String name, String logo, String description, String homeStadium) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.homeStadium = homeStadium;
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

    public String getHomeStadium() {
        return homeStadium;
    }

    public void setHomeStadium(String homeStadium) {
        this.homeStadium = homeStadium;
    }

}
