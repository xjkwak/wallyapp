package siliconwally.net.wallyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import siliconwally.net.wallyapp.service.EndPointApi;
import siliconwally.net.wallyapp.service.RestApiAdapter;

public class DetailTeamActivity extends AppCompatActivity {

    private Match match;

    private TextView titleMatch;
    private TextView time;
    private TextView date;
    private Button btnArbitrar;
    private Button btnReset;
    private String userId;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_team);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        titleMatch = findViewById(R.id.textViewTitle);
        time = findViewById(R.id.txtViewTime);
        date = findViewById(R.id.textViewDate);
        btnArbitrar = findViewById(R.id.btnArbitrar);
        btnReset = findViewById(R.id.btnReset);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            match = (Match) extras.getSerializable("match");
            userId = extras.getString("userId");
        }


        titleMatch.setText(match.getTeams());
        time.setText(match.getTime());
        date.setText(match.getDate());
        match.getUidArbitro();

        if (!match.getUidArbitro().equals(this.userId)) {
            btnArbitrar.setVisibility(View.INVISIBLE);
        }
        else {
            btnArbitrar.setVisibility(View.VISIBLE);
        }

        SessionManager session = new SessionManager(getApplicationContext());
        String userUid = session.getUserId();

        if (userUid.equals("1")) {
            btnReset.setVisibility(View.VISIBLE);
        }
        else {
            btnReset.setVisibility(View.INVISIBLE);
        }


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void seeMatch(View view) {
        Intent intent = new Intent(getApplicationContext(), ScoreboardActivity.class);
        intent.putExtra("match", match);
        this.startActivity(intent);

    }

    public void runMatch(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("match", match);
        this.startActivity(intent);

    }

    public void resetMatch(View view) {

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(getApplicationContext());

        Call<List<Match>> matches = service.matches(this.match.getNid());

        matches.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                List<Match> matches = response.body();

                if (matches != null && !matches.isEmpty()) {
                    DetailTeamActivity.this.match = matches.get(0);
                    mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
                }
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                System.out.println("Falló");
            }
        });
    }
}
