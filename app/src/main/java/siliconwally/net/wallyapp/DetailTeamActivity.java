package siliconwally.net.wallyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailTeamActivity extends AppCompatActivity {

    private Match match;

    private TextView titleMatch;
    private TextView time;
    private TextView date;
    private Button btnArbitrar;
    private String userId;
    public DetailTeamActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_team);

        titleMatch = findViewById(R.id.textViewTitle);
        time = findViewById(R.id.txtViewTime);
        date = findViewById(R.id.textViewDate);
        btnArbitrar = findViewById(R.id.btnArbitrar);

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
}
