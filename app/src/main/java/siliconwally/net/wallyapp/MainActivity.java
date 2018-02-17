package siliconwally.net.wallyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.HashMap;
import java.util.List;

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
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;
    private static final String FINALIZED  = "43";

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

        createPlayersAvatars();
    }

    private void createPlayersAvatars() {

        imageView1 = (ImageView)findViewById(R.id.image1);
        imageView2 = (ImageView)findViewById(R.id.image2);
        imageView3 = (ImageView)findViewById(R.id.image3);
        imageView4 = (ImageView)findViewById(R.id.image4);
        imageView5 = (ImageView)findViewById(R.id.image5);
        imageView6 = (ImageView)findViewById(R.id.image6);
        imageView7 = (ImageView)findViewById(R.id.image7);
        imageView8 = (ImageView)findViewById(R.id.image8);

        final String baseUrl = "https://siliconwally.net";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SiliconWally siliconWally = retrofit.create(SiliconWally.class);

        Call<ArrayList<Player>> players = siliconWally.players(match.getNidA());

        players.enqueue(new Callback<ArrayList<Player>>() {
            @Override
            public void onResponse(Call<ArrayList<Player>> call, Response<ArrayList<Player>> response) {
                final ArrayList<Player> players = response.body();
               /* Picasso.with(getApplicationContext()).load(baseUrl+players.get(0).getPhoto()).into(imageView1);
                Picasso.with(getApplicationContext()).load(baseUrl+players.get(1).getPhoto()).into(imageView2);
                Picasso.with(getApplicationContext()).load(baseUrl+players.get(2).getPhoto()).into(imageView3);
                Picasso.with(getApplicationContext()).load(baseUrl+players.get(3).getPhoto()).into(imageView4);*/
                imageView1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, PlayerPickerActivity.class);
                        i.putExtra("players", players);
                        startActivityForResult(i, 1);

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Player>> call, Throwable t) {
                System.out.println("Falló");
            }
        });

        players = siliconWally.players(match.getNidB());

        players.enqueue(new Callback<ArrayList<Player>>() {
            @Override
            public void onResponse(Call<ArrayList<Player>> call, Response<ArrayList<Player>> response) {
                final ArrayList<Player> players = response.body();
                Picasso.with(getApplicationContext()).load(baseUrl+players.get(0).getPhoto()).into(imageView5);
                Picasso.with(getApplicationContext()).load(baseUrl+players.get(1).getPhoto()).into(imageView6);
                Picasso.with(getApplicationContext()).load(baseUrl+players.get(2).getPhoto()).into(imageView7);
                Picasso.with(getApplicationContext()).load(baseUrl+players.get(3).getPhoto()).into(imageView8);
                imageView5.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, PlayerPickerActivity.class);
                        i.putExtra("players", players);
                        startActivityForResult(i, 2);

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Player>> call, Throwable t) {
                System.out.println("Falló");
            }
        });
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
                state.addProperty("target_id", FINALIZED);
                JsonArray arrayState = new JsonArray();
                arrayState.add(state);

                data.add("nid", arrayNid);
                data.add("type", arrayType);
                data.add("field_partido_estado", arrayState);

                service.updateStateMatch(nid, data).enqueue(new Callback<MatchNode>() {

                    @Override
                    public void onResponse(Call<MatchNode> call, Response<MatchNode> response) {
                        if (response.isSuccessful()) {
                            MatchNode node = response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<MatchNode> call, Throwable t) {
                        System.out.println("Error update node!!!!!!");
                    }
                });

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
                HashMap<String, Player> players = (HashMap)data.getSerializableExtra("players");
                updatePlayerPictures(players);
                
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private void updatePlayerPictures(HashMap<String, Player> players) {
    }
}
