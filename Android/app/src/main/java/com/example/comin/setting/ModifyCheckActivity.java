package com.example.comin.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.comin.MainActivity;
import com.example.comin.R;
import com.example.comin.login.LoginActivity;


public class ModifyCheckActivity extends AppCompatActivity {

    static final String PREF_USER_ID = "username";
    static final String PREF_USER_PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_check);

        final TextView decriptionText = findViewById(R.id.descriptionText);
        final EditText idText = findViewById(R.id.idText);
        final EditText passwordText = findViewById(R.id.passwordText);
        final Button loginButton = findViewById(R.id.loginButton);

        //login 요청
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = idText.getText().toString();
                String password = passwordText.getText().toString();

                if(id.equals(getUserID(ModifyCheckActivity.this)) && password.equals(getUserPassword(ModifyCheckActivity.this))){
                    Intent intent = new Intent(ModifyCheckActivity.this, ModifyUserActivity.class);



                    startActivity(intent);
                    finish();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyCheckActivity.this);
                    builder.setMessage("아이디 및 비밀번호를 다시 확인해주세요.")
                            .setNegativeButton("다시시도", null)
                            .create()
                            .show();
                }




            }
        });


    }
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getUserID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static String getUserPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PASSWORD, "");
    }
}
