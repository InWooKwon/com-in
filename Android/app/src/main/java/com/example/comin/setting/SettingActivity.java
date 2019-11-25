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
        TextView withdrawButton = findViewById(R.id.withdrawButton);
        TextView modifyButton = findViewById(R.id.modifyButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.clearUserName(SettingActivity.this);
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ModifyUserActivity.class);
                finish();
                startActivity(intent);
            }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, withdrawActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
