package siliconwally.net.wallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
