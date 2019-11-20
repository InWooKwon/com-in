
package com.example.management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);


        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");


        final String message = userID + "님";
        Log.d("test","welcome : "+message);

        welcomeMessage.setText(message);

        logoutButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.clearUserName(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
