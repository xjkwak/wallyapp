package siliconwally.net.wallyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;

/**
 * Created by cristian on 05-02-18.
 */

public class BaseActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.match_teams:
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.match_teams:
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
}
