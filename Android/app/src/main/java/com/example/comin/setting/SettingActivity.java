package com.example.comin.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.comin.MainActivity;
import com.example.comin.R;
import com.example.comin.login.LoginActivity;


public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TextView logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.clearUserName(SettingActivity.this);
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
