package siliconwally.net.wallyapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siliconwally.net.wallyapp.adapter.ViewPagerAdapter;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private HashMap<String, List<Match>> weekMatches;
    private MatchListAdapter userListAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);


        viewPager = (ViewPager) findViewById(R.id.viewPagerMatch);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrentPlaysFragment(), "Partidos");
        adapter.addFragment(new LastPlaysFragment(), "Historico de partidos");

        viewPager.setAdapter(adapter);
        //viewPager.addOnAdapterChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        adapter.notifyDataSetChanged();

        tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        tabLayout.setupWithViewPager(viewPager);
        /*
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://siliconwally.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SiliconWally siliconWally = retrofit.create(SiliconWally.class);

        Call<List<Match>> matches = siliconWally.matches();
        final List<Match> list = new ArrayList<>();

        matches.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                List<Match> matches = response.body();

                System.out.println("Recuperó");
//                MatchesActivity.this.writeMatches(matches);
                initSpinnerWeeks(matches);
                System.out.println(matches.toString());
                SessionManager session = new SessionManager(getApplicationContext());
                String userUid = session.getUserId();

                userListAdapter = new MatchListAdapter(MatchesActivity.this, matches, userUid);
                MatchesActivity.this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                MatchesActivity.this.recyclerView.setAdapter(userListAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MatchesActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                MatchesActivity.this.recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                System.out.println("Falló");

            }
        });*/

    }

    private void initSpinnerWeeks(List<Match> matches) {
        ArrayList<String> weeks = new ArrayList<>();
        weekMatches = new HashMap<>();

        for (Match match: matches) {
            if (!weekMatches.containsKey(match.getSemana())) {
                weekMatches.put(match.getSemana(), new ArrayList<Match>());
                weeks.add(match.getSemana());
            }
            weekMatches.get(match.getSemana()).add(match);
        }

        Spinner spinner = findViewById(R.id.wally_weeks);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                System.out.println("El item es: " + item);
                userListAdapter.setList(weekMatches.get(item));
                userListAdapter.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void writeMatches(List<Match> matches) {

        for (Match match: matches) {
            mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
        }
    }
}
