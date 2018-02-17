package siliconwally.net.wallyapp.service.interceptor;

// Original written by tsuharesu
// Adapted to create a "drop it in and watch it work" approach by Nikhil Jha.
// Just add your package statement and drop it in the folder with all your other classes.

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;
import siliconwally.net.wallyapp.SessionManager;

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;
    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            SessionManager session = new SessionManager(context);
            HashSet<String> cookies = session.getCookies();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            session.saveCookies(cookies);
        }

        return originalResponse;
    }
}