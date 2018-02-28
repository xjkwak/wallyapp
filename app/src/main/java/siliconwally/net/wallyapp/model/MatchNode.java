package siliconwally.net.wallyapp.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by jhamil on 05-02-18.
 */

public class MatchNode {
    ArrayList<LinkedHashMap> nid;
    ArrayList<LinkedHashMap> title;

    public MatchNode() {
    }

    public ArrayList<LinkedHashMap> getNid() {
        return nid;
    }

    public void setNid(ArrayList<LinkedHashMap> nid) {
        this.nid = nid;
    }

    public ArrayList<LinkedHashMap> getTitle() {
        return title;
    }

    public void setTitle(ArrayList<LinkedHashMap> title) {
        this.title = title;
    }
}
