package com.example.fulltaskc4i.ModelsPackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SaveModel {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String IS_SIGNED_IN = "isSignedIn";
    private String RES_ID = "resId";
    private String PREFERENCES_NAME = "appInfo";

    @SuppressLint("CommitPrefEdits")
    public SaveModel(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveSignedInStatus(boolean isSignedIn) {
        editor.putBoolean(IS_SIGNED_IN, isSignedIn);
        editor.apply();
    }


    public boolean isSignedIn() {
        return preferences.getBoolean(IS_SIGNED_IN, false);
    }


    public void saveTheme(int resId) {
        editor.putInt(RES_ID, resId);
        editor.apply();
    }

    public int getThemeId() {
        return preferences.getInt(RES_ID, 0);
    }
}
