package com.jasen.kimjaeseung.homebase.data;

/**
 * Created by kimjaeseung on 2018. 5. 4..
 */

public class Record {
    private int ER;
    private int RBI;
    private int baseOnBalls;
    private int doubleHit;
    private int flyBall;
    private int groundBall;
    private int hitBatters;
    private int hitByPitch;
    private int hits;
    private int hold;
    private int homeRun;
    private int homeRuns;
    private int inning;
    private int lose;
    private int run;
    private int sacrificeHit;
    private int save;
    private int singleHit;
    private int stolenBase;
    private int strikeOut;
    private int strikeOuts;
    private int tripleHit;
    private int walks;
    private int win;
    private String uid;

    public Record() {
        this.ER = 0;
        this.RBI = 0;
        this.baseOnBalls = 0;
        this.doubleHit = 0;
        this.flyBall = 0;
        this.groundBall = 0;
        this.hitBatters = 0;
        this.hitByPitch = 0;
        this.hits = 0;
        this.hold = 0;
        this.homeRun = 0;
        this.homeRuns = 0;
        this.inning = 0;
        this.lose = 0;
        this.run = 0;
        this.sacrificeHit = 0;
        this.save = 0;
        this.singleHit = 0;
        this.stolenBase = 0;
        this.strikeOut = 0;
        this.strikeOuts = 0;
        this.tripleHit = 0;
        this.walks = 0;
        this.win = 0;
    }

    public Record(int ER, int RBI, int baseOnBalls, int doubleHit, int flyBall, int groundBall, int hitBatters, int hitByPitch, int hits, int hold, int homeRun, int homeRuns, int inning, int lose, int run, int sacrificeHit, int save, int singleHit, int stolenBase, int strikeOut, int strikeOuts, int tripleHit, int walks, int win) {
        this.ER = ER;
        this.RBI = RBI;
        this.baseOnBalls = baseOnBalls;
        this.doubleHit = doubleHit;
        this.flyBall = flyBall;
        this.groundBall = groundBall;
        this.hitBatters = hitBatters;
        this.hitByPitch = hitByPitch;
        this.hits = hits;
        this.hold = hold;
        this.homeRun = homeRun;
        this.homeRuns = homeRuns;
        this.inning = inning;
        this.lose = lose;
        this.run = run;
        this.sacrificeHit = sacrificeHit;
        this.save = save;
        this.singleHit = singleHit;
        this.stolenBase = stolenBase;
        this.strikeOut = strikeOut;
        this.strikeOuts = strikeOuts;
        this.tripleHit = tripleHit;
        this.walks = walks;
        this.win = win;
    }

    public int getER() {
        return ER;
    }

    public void setER(int ER) {
        this.ER = ER;
    }

    public int getRBI() {
        return RBI;
    }

    public void setRBI(int RBI) {
        this.RBI = RBI;
    }

    public int getBaseOnBalls() {
        return baseOnBalls;
    }

    public void setBaseOnBalls(int baseOnBalls) {
        this.baseOnBalls = baseOnBalls;
    }

    public int getDoubleHit() {
        return doubleHit;
    }

    public void setDoubleHit(int doubleHit) {
        this.doubleHit = doubleHit;
    }

    public int getFlyBall() {
        return flyBall;
    }

    public void setFlyBall(int flyBall) {
        this.flyBall = flyBall;
    }

    public int getGroundBall() {
        return groundBall;
    }

    public void setGroundBall(int groundBall) {
        this.groundBall = groundBall;
    }

    public int getHitBatters() {
        return hitBatters;
    }

    public void setHitBatters(int hitBatters) {
        this.hitBatters = hitBatters;
    }

    public int getHitByPitch() {
        return hitByPitch;
    }

    public void setHitByPitch(int hitByPitch) {
        this.hitByPitch = hitByPitch;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getHold() {
        return hold;
    }

    public void setHold(int hold) {
        this.hold = hold;
    }

    public int getHomeRun() {
        return homeRun;
    }

    public void setHomeRun(int homeRun) {
        this.homeRun = homeRun;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public int getInning() {
        return inning;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getSacrificeHit() {
        return sacrificeHit;
    }

    public void setSacrificeHit(int sacrificeHit) {
        this.sacrificeHit = sacrificeHit;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public int getSingleHit() {
        return singleHit;
    }

    public void setSingleHit(int singleHit) {
        this.singleHit = singleHit;
    }

    public int getStolenBase() {
        return stolenBase;
    }

    public void setStolenBase(int stolenBase) {
        this.stolenBase = stolenBase;
    }

    public int getStrikeOut() {
        return strikeOut;
    }

    public void setStrikeOut(int strikeOut) {
        this.strikeOut = strikeOut;
    }

    public int getStrikeOuts() {
        return strikeOuts;
    }

    public void setStrikeOuts(int strikeOuts) {
        this.strikeOuts = strikeOuts;
    }

    public int getTripleHit() {
        return tripleHit;
    }

    public void setTripleHit(int tripleHit) {
        this.tripleHit = tripleHit;
    }

    public int getWalks() {
        return walks;
    }

    public void setWalks(int walks) {
        this.walks = walks;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
