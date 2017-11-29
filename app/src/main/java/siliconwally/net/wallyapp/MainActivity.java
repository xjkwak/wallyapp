package siliconwally.net.wallyapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    }

    private void setSetName() {
        int sets = match.getPointsA().size();

        if (sets >= 0 && sets < Match.MAX_SETS) {
            Resources res = getResources();
            String setsName[] = res.getStringArray(R.array.sets);
            setName.setText(setsName[sets]);
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

        if (teamA >= 15 || teamB >= 15) {
            match.saveScore();
            setSetName();
            scoreA.setText(String.valueOf(match.getScoreA()));
            scoreB.setText(String.valueOf(match.getScoreB()));
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
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
