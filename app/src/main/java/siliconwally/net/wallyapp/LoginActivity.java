package siliconwally.net.wallyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;

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
                SessionManager session = new SessionManager(getApplicationContext());
                session.saveUserName("Facebook User");
                session.saveUserId("-1");

                Intent i = new Intent(LoginActivity.this, MatchesActivity.class);
                startActivity(i);
                finish();
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
        logout();
    }

    private void logout() {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(this.getApplicationContext());
        service.logout().enqueue(new Callback<Login>() {

            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                System.out.println("ON RESPONSE");
                clearCookies();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                System.out.println("ON FAILURE");
            }
        });
        LoginManager.getInstance().logOut();
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void enter(View view) {
        clearCookies();
        String username = ((EditText)findViewById(R.id.username)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(this.getApplicationContext());
        JsonObject data = new JsonObject();
        data.addProperty("name", username);
        data.addProperty("pass", password);

        service.login(data).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
              if (response.isSuccessful()) {
                  LinkedHashMap currentUser = response.body().getCurrentUser();
                  String userName = currentUser.get("name").toString();
                  String userId = currentUser.get("uid").toString();
                  String token = response.body().getCsrfToken();
                  System.out.println(userName);
                  SessionManager session = new SessionManager(getApplicationContext());
                  session.saveUserName(userName);
                  session.saveUserId(userId);
                  session.saveToken(token);
                  Intent i = new Intent(LoginActivity.this, MatchesActivity.class);
                  startActivity(i);
                  finish();
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

    private void clearCookies() {
        SessionManager session = new SessionManager(getApplicationContext());
        session.clearSession();
    }
}
