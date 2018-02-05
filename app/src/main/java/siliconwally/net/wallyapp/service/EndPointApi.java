package siliconwally.net.wallyapp.service;

/**
 * Created by jhamil on 23-01-18.
 */

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import siliconwally.net.wallyapp.model.Login;

public interface EndPointApi {

    @POST("/user/login?_format=json")
    Call<Login> login(@Body JsonObject data);

    @GET("/user/logout?_format=json")
    Call<Login> logout();
}
