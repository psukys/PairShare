package com.sliebald.pairshare.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sliebald.pairshare.MyApplication;

public class PreferenceUtils {
    public static final String PREFERENCE_KEY_SELECTED_EXPENSE = "PREFERENCE_KEY_SELECTED_LIST";

    /**
     * Returns the id of the currently selected shared expense list. Returns null if none was
     * ever selected.
     *
     * @return The id of the currently selected expense.
     */
    public static String getSelectedSharedExpenseListID() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext())
                .getString(PREFERENCE_KEY_SELECTED_EXPENSE, null);
    }

    /**
     * Sets the id of the currently selected shared expense list.
     *
     * @param listID The id of the list that is currently selected.
     */
    public static void setSelectedSharedExpenseListID(String listID) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCE_KEY_SELECTED_EXPENSE, listID);
        editor.apply();

    }

    public static void registerActiveListChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterSelectedListChangedListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).unregisterOnSharedPreferenceChangeListener(listener);
    }

}
