package siliconwally.net.wallyapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import java.util.HashMap;
import java.util.List;

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

    }
}
