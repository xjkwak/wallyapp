package siliconwally.net.wallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;
import java.util.Date;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        ArrayList<Match> list = new ArrayList<>();
        list.add(new Match(new Date(1511632800000L), "JalaSoft", "Truextend", "A"));
        list.add(new Match(new Date(1511719200000L), "AssureSoft", "Digital Harbor", "B"));
        list.add(new Match(new Date(1511632800000L), "CodeRoad", "Thomson Reuters", "A"));
        list.add(new Match(new Date(1511719200000L), "Angular", "Laravel", "B"));
        list.add(new Match(new Date(1511632800000L), "JalaSoft", "Truextend", "A"));
        list.add(new Match(new Date(1511719200000L), "AssureSoft", "Digital Harbor", "B"));
        list.add(new Match(new Date(1511632800000L), "CodeRoad", "Thomson Reuters", "A"));
        list.add(new Match(new Date(1511719200000L), "Angular", "Laravel", "B"));
        list.add(new Match(new Date(1511632800000L), "JalaSoft", "Truextend", "A"));
        list.add(new Match(new Date(1511719200000L), "AssureSoft", "Digital Harbor", "B"));
        list.add(new Match(new Date(1511632800000L), "CodeRoad", "Thomson Reuters", "A"));
        list.add(new Match(new Date(1511719200000L), "Angular", "Laravel", "B"));
        list.add(new Match(new Date(1511632800000L), "JalaSoft", "Truextend", "A"));
        list.add(new Match(new Date(1511719200000L), "AssureSoft", "Digital Harbor", "B"));
        list.add(new Match(new Date(1511632800000L), "CodeRoad", "Thomson Reuters", "A"));
        list.add(new Match(new Date(1511719200000L), "Angular", "Laravel", "B"));
        list.add(new Match(new Date(1511632800000L), "JalaSoft", "Truextend", "A"));
        list.add(new Match(new Date(1511719200000L), "AssureSoft", "Digital Harbor", "B"));
        list.add(new Match(new Date(1511632800000L), "CodeRoad", "Thomson Reuters", "A"));
        list.add(new Match(new Date(1511719200000L), "Angular", "Laravel", "B"));
        list.add(new Match(new Date(1511632800000L), "JalaSoft", "Truextend", "A"));
        list.add(new Match(new Date(1511719200000L), "AssureSoft", "Digital Harbor", "B"));
        list.add(new Match(new Date(1511632800000L), "CodeRoad", "Thomson Reuters", "A"));
        list.add(new Match(new Date(1511719200000L), "Angular", "Laravel", "B"));
        list.add(new Match(new Date(1511632800000L), "JalaSoft", "Truextend", "A"));
        list.add(new Match(new Date(1511719200000L), "AssureSoft", "Digital Harbor", "B"));
        list.add(new Match(new Date(1511632800000L), "CodeRoad", "Thomson Reuters", "A"));
        list.add(new Match(new Date(1511719200000L), "Angular", "Laravel", "B"));

        MatchListAdapter userListAdapter = new MatchListAdapter(list);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView.setAdapter(userListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(linearLayoutManager);
    }
}
