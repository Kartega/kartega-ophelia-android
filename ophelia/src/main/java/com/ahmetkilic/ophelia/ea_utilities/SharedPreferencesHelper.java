package com.ahmetkilic.ophelia.ea_utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;

    /**
     * Create new instance
     *
     * @param context context
     * @param name    name of shared preferences
     */
    public SharedPreferencesHelper(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
    }

    // ------------- Base Operations -----------------//
    public void store(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void store(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public void store(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void store(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void store(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public void store(String key, Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).apply();
    }

    public int readInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public long readLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public String readString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean readBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public float readFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public Set<String> readSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    public Map<String, ?> readAll() {
        return sharedPreferences.getAll();
    }
}
