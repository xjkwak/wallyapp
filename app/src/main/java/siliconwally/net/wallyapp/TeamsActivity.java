package siliconwally.net.wallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeamsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_teams);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://siliconwally.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SiliconWally siliconWally = retrofit.create(SiliconWally.class);

        Call<List<Team>> teams = siliconWally.teams();
        final List<Team> list = new ArrayList<>();

        teams.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                List<Team> teams = response.body();

                TeamsActivity.this.writeTeams(teams);
                System.out.println("Recuperó");
                System.out.println(teams.toString());

                TeamListAdapter teamListAdapter = new TeamListAdapter(teams);
                TeamsActivity.this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                TeamsActivity.this.recyclerView.setAdapter(teamListAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TeamsActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                TeamsActivity.this.recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                System.out.println("Falló");
            }
        });
    }

    private void writeTeams(List<Team> teams) {

        for (Team team: teams) {
            mDatabase.child("teams").child(String.valueOf(team.getNid())).setValue(team);
        }
    }
}
