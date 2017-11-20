package siliconwally.net.wallyapp;

import android.content.Context;
import android.content.SharedPreferences;

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
   private String KEY_USER = "user";

   // Constructor
   public SessionManager(Context context) {
       _context = context;
       pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
       editor = pref.edit();
   }

   public void saveUser(String username) {
       editor.putString(KEY_USER, username);
       editor.commit();
   }
   public String getUserName() {
       return pref.getString(KEY_USER, null);
   }


}

