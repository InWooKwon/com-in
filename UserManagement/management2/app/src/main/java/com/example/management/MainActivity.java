
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


        //EditText idText = (EditText) findViewById(R.id.idText);
        //EditText passwordText = (EditText) findViewById(R.id.passwordText);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);

       // Button managementButton = (Button) findViewById(R.id.managementButton);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
       // String userpassword = intent.getStringExtra("userPassword");

        String message = userID + "님";
        Log.d("test","wecome : "+message);
        //idText.setText(userID);
        //passwordText.setText(userpassword);
        welcomeMessage.setText(message);

        /*
        if(!userID.equals("admin")){
            managementButton.setVisibility(View.GONE);
        }
*/
    }
}
