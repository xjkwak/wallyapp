package siliconwally.net.wallyapp.service.interceptor;

// Original written by tsuharesu
// Adapted to create a "drop it in and watch it work" approach by Nikhil Jha.
// Just add your package statement and drop it in the folder with all your other classes.

import android.content.Context;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import siliconwally.net.wallyapp.SessionManager;

/**
 * This interceptor put all the Cookies in Preferences in the Request.
 * Your implementation on how to get the Preferences may ary, but this will work 99% of the time.
 */
public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        SessionManager session = new SessionManager(this.context);
        HashSet<String> preferences = session.getCookies();

        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }
        builder.addHeader("Content-Type", "application/json");

        String token = session.getToken();
        if (token != null) {
            builder.addHeader("X-CSRF-Token", token);
        }
        return chain.proceed(builder.build());
    }
}
