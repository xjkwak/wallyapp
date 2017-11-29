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
    private String time;
    private String teamA;
    private String teamB;
    private String arena;
    private int countA;
    private int countB;
    private int scoreA;
    private int scoreB;
    private ArrayList<Integer> pointsA;
    private ArrayList<Integer> pointsB;
    public static final int MAX_SETS = 5;

    public Match() {
        pointsA = new ArrayList<>();
        pointsB = new ArrayList<>();
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public int getScoreA() {
        return scoreA;
    }

    public int getScoreB() {
        return scoreB;
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

    public ArrayList<Integer> getPointsA() {
        return pointsA;
    }

    public void setPointsA(ArrayList<Integer> pointsA) {
        this.pointsA = pointsA;
    }

    public ArrayList<Integer> getPointsB() {
        return pointsB;
    }

    public void setPointsB(ArrayList<Integer> pointsB) {
        this.pointsB = pointsB;
    }

    public String toString() {
        return "[" + teamA + "(" + countA + ") vs " + teamB + "(" + countB + ")]";

    }

    public String getPointsAasString() {
       return pointsA.toString();
    }

    public String getPointsBasString() {
        return pointsB.toString();
    }

    public void saveScore() {

        pointsA.add(countA);
        pointsB.add(countB);

        if (countA > countB) {
            scoreA++;
        }
        else {
            scoreB++;
        }

        countA=0;
        countB=0;
    }

}
