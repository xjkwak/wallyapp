package siliconwally.net.wallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://siliconwally.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SiliconWally siliconWally = retrofit.create(SiliconWally.class);

        Call<List<Match>> matches = siliconWally.matches();
        final List<Match> list = new ArrayList<>();

        matches.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                List<Match> matches = response.body();

                System.out.println("Recuperó");
               // MatchesActivity.this.writeMatches(matches);
                System.out.println(matches.toString());

                MatchListAdapter userListAdapter = new MatchListAdapter(MatchesActivity.this, matches);
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

    private void writeMatches(List<Match> matches) {

        for (Match match: matches) {
            mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
        }
    }
}
