package siliconwally.net.wallyapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by cristian on 20-11-17.
 */

public class Match implements Serializable {

    private String date;
    private String teamA;
    private String teamB;
    private String arena;

    public Match(String date, String teamA, String teamB, String arena) {
        this.date = date;
        this.teamA = teamA;
        this.teamB = teamB;
        this.arena = arena;
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
}
