package siliconwally.net.wallyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void enter(View view) {

        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        if (username.equals("arbitro") && password.equals("1234")) {
            SessionManager session = new SessionManager(getApplicationContext());
            session.saveUser(username);

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(getApplicationContext(), "Credenciales incorrectas. Intente nuevamente.", Toast.LENGTH_LONG).show();
        }
    }
}
