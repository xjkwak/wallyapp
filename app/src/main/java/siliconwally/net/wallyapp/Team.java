package siliconwally.net.wallyapp;

/**
 * Created by cristian on 21-11-17.
 */

public class Team {

    private String name;
    private String company;

    public Team(String name, String company) {
        this.name = name;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String toString() {
        return name + " " + company;
    }
}
