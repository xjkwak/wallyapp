package siliconwally.net.wallyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaCas;
import android.preference.PreferenceManager;

import java.util.HashSet;

public class SessionManager {

    //Shared Preferences
    private SharedPreferences pref;
    //Editor for Shared Preferences
    private SharedPreferences.Editor editor;
    //Sharedpref file name
    private static final String PREF_NAME = "WallyApp";
    //Context
    private Context _context;
    //Shared pref mode
    private int PRIVATE_MODE = 0;
    private String KEY_USERNAME = "username";
    private String KEY_USERID = "userId";
    private String KEY_TOKEN = "token";

    private HashSet<String> cookies;

    // Constructor
    public SessionManager(Context context) {
        _context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveUserName(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void saveUserId(String userId) {
        editor.putString(KEY_USERID, userId);
        editor.commit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(KEY_USERNAME, null);
    }
    public String getUserId() {
        return pref.getString(KEY_USERID, null);
    }
    public String getToken() {return pref.getString(KEY_TOKEN, null);}


    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public HashSet<String> getCookies() {
        HashSet<String> cookies = (HashSet<String>)pref.getStringSet(PREF_NAME, new HashSet<String>());
        return cookies;
    }

    public void saveCookies(HashSet<String> cookies) {
        editor.putStringSet(PREF_NAME, cookies).apply();
        editor.commit();
    }
}

