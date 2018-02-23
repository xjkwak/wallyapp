package siliconwally.net.wallyapp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        //detailedScoreA = findViewById(R.id.detailedScoreA);
        //detailedScoreB = findViewById(R.id.detailedScoreB);

        //detailedScoreASets = findViewById(R.id.detailedScoreASets);
        //detailedScoreBSets = findViewById(R.id.detailedScoreBSets);

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

        //detailedScoreA.setText(match.getTeamA());
        //detailedScoreB.setText(match.getTeamB());

        //detailedScoreASets.setText(match.getPointsA().toString());

        TableRow llDetailA = (TableRow) findViewById(R.id.llDetailA);
        fillDetailScore(llDetailA, match.getPointsA(), match.getTeamA());

        //detailedScoreBSets.setText(match.getPointsB().toString());
        TableRow llDetailB = (TableRow) findViewById(R.id.llDetailB);
        fillDetailScore(llDetailB, match.getPointsB(), match.getTeamB());
    }

    private void fillDetailScore(TableRow linear, ArrayList<Integer> list, String team) {
        TextView nameTeam = new TextView(this);
        nameTeam.setText(team);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/2;
        TableRow.LayoutParams ltw = new TableRow.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltw.setMargins(0,0,20,0);

        nameTeam.setLayoutParams(ltw);
        nameTeam.setPadding(0,0,10,0);
        nameTeam.setTextSize(22);
        nameTeam.setMaxLines(1);
        linear.addView(nameTeam);
        for (int item: list) {
            TextView text = new TextView(this);
            text.setText(String.valueOf(item));
            //text.setBackgroundColor(Color.CYAN);
            text.setPadding(5,0,25,0);
            text.setTextSize(22);
            linear.addView(text);
        }
    }
}
