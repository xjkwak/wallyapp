package siliconwally.net.wallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayerPickerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_picker);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://siliconwally.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SiliconWally siliconWally = retrofit.create(SiliconWally.class);

        Call<List<Player>> players = siliconWally.players();
        final List<Player> list = new ArrayList<>();

        players.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                List<Player> players = response.body();

                PlayerListAdapter teamListAdapter = new PlayerListAdapter(players, PlayerPickerActivity.this.getApplicationContext());
                PlayerPickerActivity.this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                PlayerPickerActivity.this.recyclerView.setAdapter(teamListAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayerPickerActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                PlayerPickerActivity.this.recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
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
