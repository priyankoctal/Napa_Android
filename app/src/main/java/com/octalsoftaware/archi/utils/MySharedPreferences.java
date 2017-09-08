package com.octalsoftaware.archi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.octalsoftaware.archi.utils.constants.S.napa_sharedpreferencename;

/**
 * Created by Anand Jain on 7/17/2015.
 */
public class MySharedPreferences {

    public static void clearAll(@NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(napa_sharedpreferencename, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void setPreferences(@NonNull Context context, String unit, String pref_name) {
        SharedPreferences sp = context.getSharedPreferences(napa_sharedpreferencename, Context.MODE_PRIVATE);
        sp.edit().putString(pref_name, unit).apply();
    }

    @Nullable
    public static String getPreferences(@NonNull Context context, String pref_name) {
        SharedPreferences sp = context.getSharedPreferences(napa_sharedpreferencename, Context.MODE_PRIVATE);
        return sp.getString(pref_name, "");
    }
}
