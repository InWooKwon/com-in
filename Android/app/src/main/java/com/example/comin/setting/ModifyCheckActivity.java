package com.example.comin.setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(true);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);			//홈 아이콘을 숨김처리합니다.
        actionBar.setDisplayUseLogoEnabled(false);


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.custombar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("회원 정보 수정");

        ImageButton backButton = (ImageButton) mCustomView.findViewById(R.id.btnBack);
        backButton.setVisibility(View.INVISIBLE);

        actionBar.setCustomView(mCustomView);


        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));


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
