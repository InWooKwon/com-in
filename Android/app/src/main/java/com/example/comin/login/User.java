package com.example.comin.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class User {




    static final String PREF_USER_ID = "username";
    static final String PREF_USER_PASSWORD = "password";
    static final String PREF_USER_IDX = "idx";
    static final String PREF_USER_NICK = "nick";
    static final String PREF_USER_NAME = "name";
    static boolean is_autoLogin=false;

//자동로그인

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserID(Context ctx, String userID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.commit();
    }

    public static void setUserPassword(Context ctx, String userPassword) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public static void setUserIdx(Context ctx, int idx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_IDX, idx);
        editor.commit();
    }

    public static void setUserNick(Context ctx, String userNick) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NICK, userNick);
        editor.commit();
    }
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setAutoLogin(Context ctx, boolean isAutoLogin){
        is_autoLogin=isAutoLogin;
    }

    // 저장된 정보 가져오기
    public static String getUserID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static String getUserPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PASSWORD, "");
    }

    public static int getUserIdx(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_USER_IDX, 0);
    }
    public static String getUserNick(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NICK, "");
    }
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static boolean getAutoLogin(Context ctx){
        return is_autoLogin;
    }
    // 로그아웃
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }






}
