package com.jasen.kimjaeseung.homebase.data;

/**
 * Created by kimjaeseung on 2018. 3. 10..
 */

public class Schedule {
    private String matchDate;
    private String matchPlace;
    private String opponentTeam;

    public Schedule(){}

    public Schedule(String matchDate, String matchPlace, String opponentTeam) {
        this.matchDate = matchDate;
        this.matchPlace = matchPlace;
        this.opponentTeam = opponentTeam;
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
}
