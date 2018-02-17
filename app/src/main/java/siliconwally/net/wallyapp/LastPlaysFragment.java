package siliconwally.net.wallyapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LastPlaysFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private HashMap<String, List<Match>> weekMatches;
    private MatchListAdapter userListAdapter;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    public LastPlaysFragment() {}

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_last_plays, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_last_plays);
        spinner = view.findViewById(R.id.wally_last_weeks);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadData(view);
        return view;
    }

    private void loadData(View view) {
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
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                System.out.println("Falló");
            }
        });
    }

    private void initSpinnerWeeks(List<Match> matches) {
        if (matches.size() == 0) {
            return;
        }

        userListAdapter = new MatchListAdapter(getContext(), matches, "0");
        recyclerView.setAdapter(userListAdapter);

        ArrayList<String> weeks = new ArrayList<>();
        weekMatches = new HashMap<>();
        for (Match match: matches) {
            if (match.getEstado().equals("Finalizado")) {
                if (!weekMatches.containsKey(match.getSemana())) {

                    weekMatches.put(match.getSemana(), new ArrayList<Match>());
                    weeks.add(match.getSemana());
                }
                weekMatches.get(match.getSemana()).add(match);
            }
        }

        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, weeks);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        spinner.setAdapter(arrayAdapter);

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
}
