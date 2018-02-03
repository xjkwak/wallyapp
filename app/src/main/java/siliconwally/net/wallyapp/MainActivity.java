package siliconwally.net.wallyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import siliconwally.net.wallyapp.model.Login;
import siliconwally.net.wallyapp.service.EndPointApi;
import siliconwally.net.wallyapp.service.RestApiAdapter;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private TextView setName;
    private TextView scoreA;
    private TextView scoreB;
    private EditText teamNameA;
    private EditText teamNameB;
    private Button scoreTeamA;
    private Button scoreTeamB;
    private Match match;
    private DatabaseReference mDatabase;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            match = (Match) extras.getSerializable("match");
        }
        setContentView(R.layout.activity_main);
        scoreTeamA = findViewById(R.id.pointsTeamA);
        scoreTeamB = findViewById(R.id.pointsTeamB);
        scoreA = findViewById(R.id.setScoreA);
        scoreB = findViewById(R.id.setScoreB);
        setName = findViewById(R.id.setName);

        mDatabase.child("matches").child(String.valueOf(match.getNid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                match = dataSnapshot.getValue(Match.class);
                scoreTeamA.setText(String.valueOf(match.getCountA()));
                scoreTeamB.setText(String.valueOf(match.getCountB()));
                System.out.println(match);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Chronometer chronometer = (Chronometer)findViewById(R.id.chronometer);
        chronometer.start();
        SessionManager session = new SessionManager(getApplicationContext());
        String username = session.getUserName();
        setTitle("WallyApp - Usuario: " + username);

        teamNameA = findViewById(R.id.teamNameA);
        teamNameB = findViewById(R.id.teamNameB);
        teamNameA.setKeyListener(null);
        teamNameB.setKeyListener(null);

        if (match != null) {
            teamNameA.setText(match.getTeamA());
            teamNameB.setText(match.getTeamB());
        }

        setSetName();

        imageView1 = (ImageView)findViewById(R.id.image1);
        imageView2 = (ImageView)findViewById(R.id.image2);
        imageView3 = (ImageView)findViewById(R.id.image3);
        imageView4 = (ImageView)findViewById(R.id.image4);
        imageView5 = (ImageView)findViewById(R.id.image5);
        imageView6 = (ImageView)findViewById(R.id.image6);
        imageView7 = (ImageView)findViewById(R.id.image7);
        imageView8 = (ImageView)findViewById(R.id.image8);
        String url = "http://siliconwally.net/sites/default/files/styles/thumbnail/public/2017-09/1aac20e1-5d9c-4085-97b3-334e0ed94d30.jpg";
        Picasso.with(getApplicationContext()).load(url).into(imageView1);
        Picasso.with(getApplicationContext()).load(url).into(imageView2);
        Picasso.with(getApplicationContext()).load(url).into(imageView3);
        Picasso.with(getApplicationContext()).load(url).into(imageView4);
        Picasso.with(getApplicationContext()).load(url).into(imageView5);
        Picasso.with(getApplicationContext()).load(url).into(imageView6);
        Picasso.with(getApplicationContext()).load(url).into(imageView7);
        Picasso.with(getApplicationContext()).load(url).into(imageView8);

    }

    private void setSetName() {
        int sets = match.getPointsA().size();

        if (sets >= 0 && sets < Match.MAX_SETS) {
            Resources res = getResources();
            String setsName[] = res.getStringArray(R.array.sets);
            setName.setText(setsName[sets]);
        }
    }

    public void reducePointsA(View view) {
        int teamA = Integer.parseInt(scoreTeamA.getText().toString());
        if (teamA >= 1) {
            teamA--;
            match.setCountA(teamA);
            mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
        }
    }

    public void reducePointsB(View view) {
        int teamB = Integer.parseInt(scoreTeamB.getText().toString());
        if (teamB >= 1) {
            teamB--;
            match.setCountB(teamB);
            mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
        }
    }

    public void addPoints(View view) {
        Button button = (Button)view;
        String value = button.getText().toString();
        int intValue = Integer.parseInt(value);
        intValue++;
        button.setText(String.valueOf(intValue));

        int teamA = Integer.parseInt(scoreTeamA.getText().toString());
        int teamB = Integer.parseInt(scoreTeamB.getText().toString());

        match.setCountA(teamA);
        match.setCountB(teamB);

        if (match.hasEndSet()) {
            match.updateScore();
            match.resetCounter();
            setSetName();
            scoreA.setText(String.valueOf(match.getScoreA()));
            scoreB.setText(String.valueOf(match.getScoreB()));

            if (match.hasFinished()) {
                scoreTeamA.setClickable(false);
                scoreTeamB.setClickable(false);
            }
        }


        mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.match_teams:
                showTeams();
                return true;
            case R.id.matches:
                showMatches();
                return true;
            case R.id.logout:
                showLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTeams() {
        Intent intent = new Intent(getApplicationContext(), TeamsActivity.class);
        startActivity(intent);
    }

    private void showMatches() {
        Intent intent = new Intent(getApplicationContext(), MatchesActivity.class);
        startActivity(intent);
    }

    private void showLogin() {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(this.getApplicationContext());
        service.logout().enqueue(new Callback<Login>() {

            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                System.out.println("response!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(response);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                System.out.print("falla!!!!!");
            }
        });

    }
}
