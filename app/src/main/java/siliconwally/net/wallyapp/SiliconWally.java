package siliconwally.net.wallyapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cristian on 21-11-17.
 */

public interface SiliconWally {
    @GET("/equiposjson")
    Call<List<Team>> teams();
}
