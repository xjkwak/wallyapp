package siliconwally.net.wallyapp;

import java.io.Serializable;

/**
 * Created by cristian on 27-01-18.
 */

public class Player implements Serializable{
    private int nid;
    private String name;
    private String number;
    private String photo;
    private boolean enabled;

    public int getNid() {
        return nid;
    }

    public void setNid(int id) {
        this.nid = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return this.getName() + "-" + getNumber();
    }
}
