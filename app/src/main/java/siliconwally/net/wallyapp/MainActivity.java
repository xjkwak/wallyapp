package siliconwally.net.wallyapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

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
        Bundle extras = getIntent().getExtras();

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        int sets = match.getScores().size();

        if (sets >= 0 && sets < 5) {
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
        System.out.println("Valor:  " + value);

        String nameTeamA = teamNameA.getText().toString();
        String nameTeamB = teamNameB.getText().toString();
        int teamA = Integer.parseInt(scoreTeamA.getText().toString());
        int teamB = Integer.parseInt(scoreTeamB.getText().toString());

        mDatabase.child("matches").child(String.valueOf(match.getNid())).child("countA").setValue(teamA);
        mDatabase.child("matches").child(String.valueOf(match.getNid())).child("countB").setValue(teamB);

        if (teamA >= 15 || teamB >= 15) {
            match.saveScore();
            setSetName();
            mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
            scoreA.setText(match.getScoreA());
            scoreB.setText(match.getScoreB());


//            String score = String.format(getApplicationContext().getString(R.string.wally_results), nameTeamA, teamA, nameTeamB, teamB);
//            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//            whatsappIntent.setType("text/plain");
//            whatsappIntent.setPackage("com.whatsapp");
//            whatsappIntent.putExtra(Intent.EXTRA_TEXT, score);
//            try {
//                startActivity(whatsappIntent);
//            } catch (android.content.ActivityNotFoundException ex) {
//                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.whatsapp_not_installed), Toast.LENGTH_LONG).show();
//            }
        }
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
            case R.id.teams:
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
