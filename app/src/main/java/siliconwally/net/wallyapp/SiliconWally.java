package siliconwally.net.wallyapp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by cristian on 21-11-17.
 */

public interface SiliconWally {
    @GET("/equiposjson")
    Call<List<Team>> teams();

    @GET("/api/v1/matches")
    Call<List<Match>> matches();

    @GET("/api/v1/teams/{nid}/players")
    Call<ArrayList<Player>> players(@Path("nid") int nid);
}
