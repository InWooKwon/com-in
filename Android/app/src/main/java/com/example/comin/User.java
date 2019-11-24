package com.example.comin;

public class User {
    String dup_id_check;
    String dup_nick_check;

    String userBirth;

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



}
