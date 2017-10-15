package com.example.satyarth.encryptedqrcode;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by satyarth on 10/14/17.
 */

public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    //shared preferences mode
    int PRIVATE_MODE = 0;

    private static final String PREF_FILENAME = "welcome_screen";
    public static final String USER_NAME = "Username";
    public static final String PRIVATE_KEY = "PrivateKey";
    public static final String PUBLIC_KEY = "PublicKey";

    public PreferenceManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_FILENAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLauch(String username, String privateKey, String publicKey){
        editor.putString(USER_NAME, username);
        editor.putString(PRIVATE_KEY, privateKey);
        editor.putString(PUBLIC_KEY, publicKey);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return pref.contains(USER_NAME);
    }

}
