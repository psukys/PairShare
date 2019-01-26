package com.sliebald.pairshare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sliebald.pairshare.MyApplication;

class PreferenceUtils {
    private static final String PREFERENCE_KEY_SELECTED_EXPENSE = "PREFERENCE_KEY_SELECTED_EXPENSE";

    static String getSelectedSharedExpense() {
        Context context = MyApplication.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREFERENCE_KEY_SELECTED_EXPENSE, null);
    }
}
