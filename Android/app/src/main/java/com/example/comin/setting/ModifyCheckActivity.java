package com.example.comin.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.comin.R;
import com.example.comin.login.User;


public class ModifyCheckActivity extends AppCompatActivity {

    static final String PREF_USER_ID = "username";
    static final String PREF_USER_PASSWORD = "password";
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_check);

        final TextView descriptionText = findViewById(R.id.descriptionText);
        final EditText idText = findViewById(R.id.idText);
        final EditText passwordText = findViewById(R.id.passwordText);
        final Button loginButton = findViewById(R.id.loginButton);

        descriptionText.setText("회원 정보 수정을 위해, 아이디 및 비밀번호 입력을 다시 한번 해주십시오. ");
        idText.setText("");
        passwordText.setText("");

        //login 요청
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = idText.getText().toString();
                String pw = passwordText.getText().toString();


                if(id.length()==0 || pw.length() ==0 || id.equals("") || pw.equals("")){
                    Log.d("test","111");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyCheckActivity.this);
                    builder.setMessage("아이디 및 비밀번호를 입력해주세요.")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();

                }
                else{
                    if (checkUser(id, pw)==true) {
                        Intent intent = new Intent(ModifyCheckActivity.this, ModifyUserActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyCheckActivity.this);
                        builder.setMessage("아이디 및 비밀번호를 다시 확인해주세요")
                                .setNegativeButton("다시 시도", null)
                                .create()
                                .show();
                    }
                }




            }
        });
    }
    public boolean checkUser(String id, String pw){

        Log.d("test","checkUser");

        if(user.getUserID(getApplicationContext()).equals(id)){
            if(user.getUserPassword(getApplicationContext()).equals(pw)){
                return true;
            }
            else return false;
        }
        else return false;
    }

}
