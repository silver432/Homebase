package com.jasen.kimjaeseung.homebase.data;

import java.io.Serializable;

/**
 * Created by kimjaeseung on 2018. 3. 10..
 */

public class Schedule implements Serializable{
    private String matchDate;
    private String matchPlace;
    private String opponentTeam;
    private String opponentScore;
    private String homeScore;
    private String sid;

    public Schedule(){}

    public Schedule(String matchDate, String matchPlace, String opponentTeam, String opponentScore, String homeScore) {
        this.matchDate = matchDate;
        this.matchPlace = matchPlace;
        this.opponentTeam = opponentTeam;
        this.opponentScore = opponentScore;
        this.homeScore = homeScore;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchPlace() {
        return matchPlace;
    }

    public void setMatchPlace(String matchPlace) {
        this.matchPlace = matchPlace;
    }

    public String getOpponentTeam() {
        return opponentTeam;
    }

    public void setOpponentTeam(String opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

    public String getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(String opponentScore) {
        this.opponentScore = opponentScore;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
