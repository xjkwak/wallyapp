package siliconwally.net.wallyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siliconwally.net.wallyapp.model.MatchNode;
import siliconwally.net.wallyapp.service.EndPointApi;
import siliconwally.net.wallyapp.service.RestApiAdapter;


public class MainActivity extends BaseActivity {

    private TextView setName;
    private TextView scoreA;
    private TextView scoreB;
    private EditText teamNameA;
    private EditText teamNameB;
    private Button scoreTeamA;
    private Button scoreTeamB;
    private Match match;
    private DatabaseReference mDatabase;
    private ImageView imageViewA[];
    private ImageView imageViewB[];
    private static final String NOT_INITIALIZE  = "41";
    private static final String INPROGRESS = "42";
    private static final String FINALIZED  = "43";
    private ArrayList<Player> playersA;
    private ArrayList<Player> playersB;

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
                if (match != null ) {
                    scoreTeamA.setText(String.valueOf(match.getCountA()));
                    scoreTeamB.setText(String.valueOf(match.getCountB()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Chronometer chronometer = findViewById(R.id.chronometer);
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

        createPlayersAvatars();
    }

    private void createPlayersAvatars() {

        imageViewA = new ImageView[4];
        imageViewB = new ImageView[4];

        imageViewA[0] = findViewById(R.id.image1);
        imageViewA[1] = findViewById(R.id.image2);
        imageViewA[2] = findViewById(R.id.image3);
        imageViewA[3] = findViewById(R.id.image4);

        imageViewB[0] = findViewById(R.id.image5);
        imageViewB[1] = findViewById(R.id.image6);
        imageViewB[2] = findViewById(R.id.image7);
        imageViewB[3] = findViewById(R.id.image8);


        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(this.getApplicationContext());


        Call<ArrayList<Player>> players = service.players(match.getNidA());

        players.enqueue(new Callback<ArrayList<Player>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Player>> call, @NonNull Response<ArrayList<Player>> response) {
                MainActivity.this.playersA = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Player>> call, @NonNull Throwable t) {
                System.out.println("Falló");
            }
        });

        players = service.players(match.getNidB());

        players.enqueue(new Callback<ArrayList<Player>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Player>> call, @NonNull Response<ArrayList<Player>> response) {
                MainActivity.this.playersB = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Player>> call, @NonNull Throwable t) {
                System.out.println("Falló");
            }
        });
    }

    private void setSetName() {
        int sets = match.getPointsA().size();

        if (sets >= 0 && sets < match.getSetsToWin()) {
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

                updateNodeTeamStatus(match, FINALIZED);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                this.playersA = (ArrayList<Player>)data.getSerializableExtra("players");
                updatePlayerPictures(this.playersA, imageViewA);
            }
        }
        else if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                this.playersB = (ArrayList<Player>)data.getSerializableExtra("players");
                updatePlayerPictures(this.playersB, imageViewB);
            }
        }
    }//onActivityResult

    private void updatePlayerPictures(ArrayList<Player> players, ImageView imageView[]) {

        int index = 0;

        final String baseUrl = "http://dev.siliconwally.net";

        for(Player player: players) {
            if (player.isEnabled() && index < imageView.length) {
                Picasso.with(getApplicationContext()).load(baseUrl+player.getPhoto()).into(imageView[index]);
                index++;
            }
        }

        for (int i = index; i < imageView.length; i++) {
            imageView[i].setImageDrawable(getResources().getDrawable(R.drawable.default_player));
        }
    }

    public void selectPlayersTeamA(View view) {
        Intent i = new Intent(MainActivity.this, PlayerPickerActivity.class);
        i.putExtra("players", this.playersA);
        startActivityForResult(i, 1);
    }

    public void selectPlayersTeamB(View view) {
        Intent i = new Intent(MainActivity.this, PlayerPickerActivity.class);
        i.putExtra("players", this.playersB);
        startActivityForResult(i, 2);
    }

    public void updateNodeTeamStatus(Match match, String status) {
        // Call to service
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(this.getApplicationContext());
        JsonObject data = new JsonObject();
        int nid = match.getNid();
        String nodeId = String.valueOf(nid);

        //Create Json Payload
        JsonObject nidObject = new JsonObject();
        nidObject.addProperty("value", nodeId);
        JsonArray arrayNid = new JsonArray();
        arrayNid.add(nidObject);

        JsonObject type = new JsonObject();
        type.addProperty("target_id", "partido");
        JsonArray arrayType = new JsonArray();
        arrayType.add(type);


        JsonObject state = new JsonObject();
        state.addProperty("target_id", status);
        JsonArray arrayState = new JsonArray();
        arrayState.add(state);

        data.add("nid", arrayNid);
        data.add("type", arrayType);
        data.add("field_partido_estado", arrayState);

        service.updateStateMatch(nid, data).enqueue(new Callback<MatchNode>() {

            @Override
            public void onResponse(@NonNull Call<MatchNode> call, @NonNull Response<MatchNode> response) {
                if (response.isSuccessful()) {
                    MatchNode node = response.body();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MatchNode> call, @NonNull Throwable t) {
                System.out.println("Error update node!!!!!!");
            }
        });
    }
}
