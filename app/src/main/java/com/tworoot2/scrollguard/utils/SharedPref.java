package com.tworoot2.scrollguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static void saveData(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(key, value);
        myEdit.commit();
    }

    public static String getSavedData(Context context, String key) {

        SharedPreferences sh = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sh.getString(key, "");
    }

}
