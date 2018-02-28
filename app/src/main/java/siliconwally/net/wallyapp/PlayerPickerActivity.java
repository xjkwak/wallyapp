package siliconwally.net.wallyapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayerPickerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_picker);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            players = (ArrayList<Player>)extras.getSerializable("players");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        PlayerListAdapter teamListAdapter = new PlayerListAdapter(players, PlayerPickerActivity.this.getApplicationContext());
        PlayerPickerActivity.this.recyclerView = findViewById(R.id.recyclerView);
        PlayerPickerActivity.this.recyclerView.setAdapter(teamListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayerPickerActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        PlayerPickerActivity.this.recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void selectPlayer(View view) {
        Intent returnIntent = getIntent();
        returnIntent.putExtra("players", this.players);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
