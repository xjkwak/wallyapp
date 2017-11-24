package siliconwally.net.wallyapp;

import android.content.Intent;
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
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private EditText teamNameA;
    private EditText teamNameB;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            match = (Match) extras.getSerializable("match");
        }
        setContentView(R.layout.activity_main);
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
        int teamA = Integer.parseInt(((Button)findViewById(R.id.pointsTeamA)).getText().toString());
        int teamB = Integer.parseInt(((Button)findViewById(R.id.pointsTeamB)).getText().toString());

        if (teamA >= 5 || teamB >= 5) {
            String score = String.format(getApplicationContext().getString(R.string.wally_results), nameTeamA, teamA, nameTeamB, teamB);
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, score);

            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.whatsapp_not_installed), Toast.LENGTH_LONG).show();
            }
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
