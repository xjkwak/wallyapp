package siliconwally.net.wallyapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

    private TextView setA;
    private TextView setB;
    private TextView setColorA;
    private TextView setColorB;
    private Match match;
    private DatabaseReference mDatabase;
    private int fistSetPoint;
    private int secondSetPoint;
    private TableRow llDetailA;
    private TableRow llDetailB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            match = (Match) extras.getSerializable("match");
            fistSetPoint = match.getPointsSet();
            secondSetPoint = match.getPointsTie();
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

        setA = findViewById(R.id.textViewSetA);
        setB = findViewById(R.id.textViewSetB);
        setColorA = findViewById(R.id.textViewSetColorA);
        setColorB = findViewById(R.id.textViewSetColorB);

        llDetailA = (TableRow) findViewById(R.id.llDetailA);
        llDetailB = (TableRow) findViewById(R.id.llDetailB);

        final MediaPlayer pointsAsound = MediaPlayer.create(this, R.raw.pointsa);
        final MediaPlayer pointsBsound = MediaPlayer.create(this, R.raw.pointsb);


        addSetTeam(match, llDetailA, llDetailB);

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
        //Color of sets
        int colorA = ContextCompat.getColor(this, R.color.colorAccent);
        int colorB = ContextCompat.getColor(this, R.color.colorPrimaryDark) ;

        nameA.setText(match.getTeamA());
        nameB.setText(match.getTeamB());
        setScoreA.setText(String.valueOf(match.getCountA()));
        setScoreB.setText(String.valueOf(match.getCountB()));

        setA.setText(String.valueOf(match.getScoreA()));
        setB.setText(String.valueOf(match.getScoreB()));
        setColorA.setBackgroundColor(colorA);
        setColorB.setBackgroundColor(colorB);


        fillDetailScore(llDetailA, match.getPointsA(), colorA , colorB);
        fillDetailScore(llDetailB, match.getPointsB(), colorB, colorA);
    }

    private void fillDetailScore(TableRow linear, ArrayList<Integer> list,  int colorA, int colorB) {
        int count = linear.getChildCount(); // TableRow already contain TexView of the team name
        if (alreadyRenderPoint(count, list.size())) {
            return;
        }

        TableRow.LayoutParams ltwSets = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltwSets.setMargins(5,0,5,0);
        for (int i = count-1; i<list.size(); i++) {
            Integer item = list.get(i);
            TextView text = new TextView(this);
            text.setText(String.valueOf(item));

            if (item == fistSetPoint || item == secondSetPoint) {
                text.setBackgroundColor(colorA);
            }
            if (item != fistSetPoint && item != secondSetPoint){
                text.setBackgroundColor(colorB);
            }

            text.setPadding(5,5,25,0);
            text.setTextColor(Color.WHITE);
            text.setLayoutParams(ltwSets);
            text.setTextSize(22);
            linear.addView(text);
        }
    }

    private boolean alreadyRenderPoint(int count, int size) {
        if (count != 1 && count - 1 == size) {
            return true;
        }
        return false;
    }

    private int getMiddleScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/2;
        return width;
    }

    private void addSetTeam(Match match, TableRow llDetailA, TableRow llDetailB) {
        int width = getMiddleScreenWidth();
        TableRow.LayoutParams ltw = new TableRow.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        ltw.setMargins(0,0,20,0);
        setTextName(this, match.getTeamA(), llDetailA, ltw);
        setTextName(this, match.getTeamB(), llDetailB, ltw);
    }

    private void setTextName(Context context , String team, TableRow linear, TableRow.LayoutParams ltw) {
        TextView nameTeam = new TextView(context);
        nameTeam.setText(team);
        nameTeam.setLayoutParams(ltw);
        nameTeam.setPadding(0,5,10,0);
        nameTeam.setTextSize(22);
        nameTeam.setMaxLines(1);
        nameTeam.setTextColor(Color.WHITE);
        linear.addView(nameTeam);
    }
}
