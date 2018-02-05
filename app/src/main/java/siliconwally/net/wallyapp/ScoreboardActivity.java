package siliconwally.net.wallyapp;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScoreboardActivity extends BaseActivity {

    private TextView nameA;
    private TextView nameB;
    private TextView setScoreA;
    private TextView setScoreB;
    private TextView detailedScoreA;
    private TextView detailedScoreB;
    private TextView detailedScoreASets;
    private TextView detailedScoreBSets;
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

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scoreboard);

        nameA = findViewById(R.id.nameA);
        nameB = findViewById(R.id.nameB);
        setScoreA = findViewById(R.id.setScoreA);
        setScoreB = findViewById(R.id.setScoreB);
        detailedScoreA = findViewById(R.id.detailedScoreA);
        detailedScoreB = findViewById(R.id.detailedScoreB);
        detailedScoreASets = findViewById(R.id.detailedScoreASets);
        detailedScoreBSets = findViewById(R.id.detailedScoreBSets);

//        updateScoreboard();

        final MediaPlayer pointsAsound = MediaPlayer.create(this, R.raw.pointsa);
        final MediaPlayer pointsBsound = MediaPlayer.create(this, R.raw.pointsb);

        mDatabase.child("matches").child(String.valueOf(match.getNid())).child("countA").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pointsAsound.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("matches").child(String.valueOf(match.getNid())).child("countB").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pointsBsound.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("matches").child(String.valueOf(match.getNid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                match = dataSnapshot.getValue(Match.class);
                System.out.println(match);
                updateScoreboard();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateScoreboard() {
        nameA.setText(match.getTeamA());
        nameB.setText(match.getTeamB());
        setScoreA.setText(String.valueOf(match.getCountA()));
        setScoreB.setText(String.valueOf(match.getCountB()));
        detailedScoreA.setText(match.getTeamA());
        detailedScoreB.setText(match.getTeamB());
        detailedScoreASets.setText(match.getPointsA().toString());
        detailedScoreBSets.setText(match.getPointsB().toString());
    }
}
