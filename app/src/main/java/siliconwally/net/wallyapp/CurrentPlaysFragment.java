package siliconwally.net.wallyapp;

import android.content.Context;
import android.net.Uri;
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
import siliconwally.net.wallyapp.service.ConstantsRestApi;
import siliconwally.net.wallyapp.service.EndPointApi;
import siliconwally.net.wallyapp.service.RestApiAdapter;


public class CurrentPlaysFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private HashMap<String, List<Match>> weekMatches;
    private MatchListAdapter userListAdapter;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;

    public CurrentPlaysFragment() {}

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_current_plays, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_current_plays);
        /*userListAdapter = new MatchListAdapter(getActivity(), matches);
        recyclerView.setAdapter(userListAdapter);*/

        spinner = view.findViewById(R.id.wally_weeks);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadData(view);
        return view;
    }

    private void loadData(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(getContext());

        Call<List<Match>> matches = service.matches();

        matches.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                List<Match> matches = response.body();

                System.out.println("Recuperó");
                //CurrentPlaysFragment.this.writeMatches(matches);
                initSpinnerWeeks(matches);
                System.out.println(matches.toString());
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                System.out.println("Falló");
            }
        });
    }

    /*private void writeMatches(List<Match> matches) {

        for (Match match: matches) {
            mDatabase.child("matches").child(String.valueOf(match.getNid())).setValue(match);
        }
    }*/

    private void initSpinnerWeeks(List<Match> matches) {
        if (matches.size() == 0) {
            return;
        }

        SessionManager session = new SessionManager(getContext());
        String userUid = session.getUserId();

        userListAdapter = new MatchListAdapter(getContext(), matches, userUid);
        recyclerView.setAdapter(userListAdapter);

        ArrayList<String> weeks = new ArrayList<>();
        weekMatches = new HashMap<>();
        for (Match match: matches) {
            if (!match.getEstado().equals("Finalizado")) {
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

