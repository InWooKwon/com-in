package com.example.comin.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class User {
    String dup_id_check;
    String dup_nick_check;

    String userBirth;
    static final String PREF_USER_ID = "username";
    static final String PREF_USER_PASSWORD = "password";
    static boolean is_autoLogin=false;


    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getDup_nick_check() {
        return dup_nick_check;
    }

    public void setDup_nick_check(String dup_nick_check) {
        this.dup_nick_check = dup_nick_check;
    }



    public String getDup_id_check() {
        return dup_id_check;
    }

    public void setDup_id_check(String dup_id_check) {
        this.dup_id_check = dup_id_check;
    }


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
