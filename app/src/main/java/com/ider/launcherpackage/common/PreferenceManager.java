package com.ider.launcherpackage.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ider-eric on 2016/12/27.
 */

public class PreferenceManager {

    private SharedPreferences preferences;


    private static PreferenceManager INSTANCE;

    private PreferenceManager(Context context) {
        this.preferences = context.getSharedPreferences("launcher_config", Context.MODE_PRIVATE);
    }

    public static PreferenceManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PreferenceManager(context);
        }
        return INSTANCE;
    }

    public synchronized String getString(String key) {
        return preferences.getString(key, null);
    }

    public synchronized void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public synchronized void remove(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
