package siliconwally.net.wallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://siliconwally.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SiliconWally siliconWally = retrofit.create(SiliconWally.class);

        Call<List<Match>> teams = siliconWally.matches();
        final List<Match> list = new ArrayList<>();

        teams.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                List<Match> teams = response.body();

                System.out.println("Recuperó");
                System.out.println(teams.toString());

                MatchListAdapter userListAdapter = new MatchListAdapter(MatchesActivity.this, teams);
                MatchesActivity.this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                MatchesActivity.this.recyclerView.setAdapter(userListAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MatchesActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                MatchesActivity.this.recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                System.out.println("Falló");
            }
        });
    }
}
