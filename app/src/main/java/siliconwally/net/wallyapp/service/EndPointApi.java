package siliconwally.net.wallyapp.service;

/**
 * Created by jhamil on 23-01-18.
 */

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import siliconwally.net.wallyapp.Match;
import siliconwally.net.wallyapp.Player;
import siliconwally.net.wallyapp.Team;
import siliconwally.net.wallyapp.model.Login;
import siliconwally.net.wallyapp.model.MatchNode;

public interface EndPointApi {

    @POST("/user/login?_format=json")
    Call<Login> login(@Body JsonObject data);

    @GET("/user/logout?_format=json")
    Call<Login> logout();

    @PATCH("/node/{nid}")
    Call<MatchNode> updateStateMatch(@Path("nid") int id, @Body JsonObject data);

    @GET("/equiposjson")
    Call<List<Team>> teams();

    @GET("/api/v1/matches")
    Call<List<Match>> matches();

    @GET("/api/v1/matches/{nid}")
    Call<List<Match>> matches(@Path("nid") int id);

    @GET("/api/v1/teams/{nid}/players")
    Call<ArrayList<Player>> players(@Path("nid") int nid);
}
