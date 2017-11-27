package siliconwally.net.wallyapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cristian on 20-11-17.
 */

public class Match implements Serializable {

    private int nid;
    private String date;
    private String teamA;
    private String teamB;
    private String arena;
    private int countA;
    private int countB;
    private ArrayList<String> scores;

    public Match() {
        this.scores = new ArrayList<>();
    }

    public Match(int nid, String date, String teamA, String teamB, String arena) {
        this();
        this.nid = nid;
        this.date = date;
        this.teamA = teamA;
        this.teamB = teamB;
        this.arena = arena;
        this.countA = 0;
        this.countB = 0;
    }

    public ArrayList<String> getScores() {
        return scores;
    }

    public void setScores(ArrayList<String> scores) {
        this.scores = scores;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getCountA() {
        return countA;
    }

    public void setCountA(int countA) {
        this.countA = countA;
    }

    public int getCountB() {
        return countB;
    }

    public void setCountB(int countB) {
        this.countB = countB;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getArena() {
        return arena;
    }

    public void setArena(String arena) {
        this.arena = arena;
    }

    public String getTeams() {
        return teamA + " vs. " + teamB;
    }

    public String toString() {
        return "[" + teamA + "(" + countA + ") vs " + teamB + "(" + countB + ")]";

    }

    public void saveScore() {
        this.getScores().add(countA+","+countB);
        countA=0;
        countB=0;
    }

    //TODO: Refactor how to save score.
    public String getScoreA() {
        return "1";
    }

    public String getScoreB() {
        return "1";
    }
}
