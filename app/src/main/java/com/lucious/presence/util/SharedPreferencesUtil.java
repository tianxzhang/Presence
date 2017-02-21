package com.lucious.presence.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lucious.presence.BaseApplication;

/**
 * Created by ztx on 2017/1/15.
 */

public final class SharedPreferencesUtil {
    private static SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("presence", Context.MODE_PRIVATE);

    public static void clear() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }

    public static void put(String key,String value) {putString(key,value);}
    public static void put(String key,int value) {putInt(key,value);}
    public static void put(String key,long value) {putLong(key,value);}
    public static void put(String key,float value) {putFloat(key,value);}
    public static void put(String key,boolean value) {putBoolean(key,value);}
    public static boolean hasString(String key) {return sharedPreferences.contains(key);}
    public static void putString(String key,String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void putLong(String key,long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        editor.commit();
    }
    public static void putBoolean(String key,boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public static void putFloat(String key,float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.commit();
    }
    public static void putInt(String key,int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }
    public static int getInt(String key,int defValue) {
        return sharedPreferences.getInt(key,defValue);
    }
    public static boolean getBoolean(String key,boolean defValue) {
        return sharedPreferences.getBoolean(key,defValue);
    }
    public static String getString(String key,String defValue) {
        return sharedPreferences.getString(key,defValue);
    }
    public static long getLong(String key,long defValue) {
        return sharedPreferences.getLong(key,defValue);
    }
    public static float getFloat(String key,float defValue) {
        return sharedPreferences.getFloat(key,defValue);
    }
    public static void remove(String... keys) {
        if(keys != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for(String key : keys) {
                editor.remove(key);
            }
            editor.commit();
        }
    }
}
