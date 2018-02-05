package siliconwally.net.wallyapp.service;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siliconwally.net.wallyapp.service.interceptor.AddCookiesInterceptor;
import siliconwally.net.wallyapp.service.interceptor.ReceivedCookiesInterceptor;

/**
 * Created by jhamil on 23-01-18.
 */

public class RestApiAdapter {

    public EndPointApi connexionToApi(Context context) {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new AddCookiesInterceptor(context));
        builder.addInterceptor(new ReceivedCookiesInterceptor(context));
        client = builder.build();

        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl(ConstantsRestApi.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPointApi service = retrofit.create(EndPointApi.class);
        return service;
    }
}
