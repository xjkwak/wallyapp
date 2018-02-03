package siliconwally.net.wallyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import siliconwally.net.wallyapp.model.Login;
import siliconwally.net.wallyapp.service.EndPointApi;
import siliconwally.net.wallyapp.service.RestApiAdapter;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
       // loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("On Sucess");
                SessionManager session = new SessionManager(getApplicationContext());
                session.saveUser("Facebook User");

                Intent i = new Intent(LoginActivity.this, MatchesActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("On Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("On Error");
                // App code
            }
        });
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void enter(View view) {
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(this.getApplicationContext());
        System.out.println(username + ": "+ password);
        JsonObject data = new JsonObject();
        data.addProperty("name", username);
        data.addProperty("pass", password);

        service.login(data).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
              if (response.isSuccessful()) {
                  String userName = response.body().getCurrentUser().get("name").toString();
                  System.out.println(userName);
                  SessionManager session = new SessionManager(getApplicationContext());
                  session.saveUser(userName);
                  Intent i = new Intent(LoginActivity.this, MatchesActivity.class);
                  startActivity(i);
              }
              else {
                  Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.incorrect_credentials), Toast.LENGTH_LONG).show();
              }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                System.out.println("FAIL!!!!");
            }
        });
    }
}
