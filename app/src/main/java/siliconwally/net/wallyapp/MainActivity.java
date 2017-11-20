package siliconwally.net.wallyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Chronometer chronometer = (Chronometer)findViewById(R.id.chronometer);
        chronometer.start();
        SessionManager session = new SessionManager(getApplicationContext());
        String username = session.getUserName();
        setTitle("WallyApp - Usuario: " + username);
    }

    public void addPoints(View view) {
        Button button = (Button)view;
        String value = button.getText().toString();
        int intValue = Integer.parseInt(value);
        intValue++;
        button.setText(String.valueOf(intValue));
        System.out.println("Valor:  " + value);

        String nameTeamA = ((EditText)findViewById(R.id.teamNameA)).getText().toString();
        String nameTeamB = ((EditText)findViewById(R.id.teamNameB)).getText().toString();
        int teamA = Integer.parseInt(((Button)findViewById(R.id.pointsTeamA)).getText().toString());
        int teamB = Integer.parseInt(((Button)findViewById(R.id.pointsTeamB)).getText().toString());

        if (teamA >= 5 || teamB >= 5) {
            String score = "Resultados:\n"+nameTeamA+"("+teamA+")\n"+nameTeamB+"("+teamB+")\n";
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, score);

            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(),"Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
