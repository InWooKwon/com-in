package com.example.comin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class InfoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("testINfo","111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        LinearLayout linear = findViewById(R.id.linearLayout);

        Intent intent = getIntent();
        int k = intent.getIntExtra("InsuranceIdx", 0);


        Log.d("testINfo","111");


        Log.d("testINfo","222");
        TextView view1 = new TextView(this);
        Log.d("testINfo","222");
        view1.setText(Integer.toString(k));
        Log.d("testINfo","33");
        view1.setTextSize(30);
        Log.d("testINfo","33");
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param2.gravity= Gravity.CENTER;
        linear.addView(view1, param2);

        Log.d("testINfo","222");
/*
        String json = loadJSONFromAsset();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray coverageArray = jsonObject.getJSONArray("coverage");

            for (int i = 0; i<coverageArray.length(); i++)
            {
                JSONObject coverageObject = coverageArray.getJSONObject(i);
                if (coverageObject.getInt("insuranceIdx") != k)
                    continue;
                String s = coverageObject.getString("content");
                TextView view1 = new TextView(this);
                view1.setText(s);
                view1.setTextSize(30);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                param2.gravity= Gravity.CENTER;
                linear.addView(view1, param2);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
*/

        Button button = new Button(this);
        button.setText("후기 게시판 가기");
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param3.gravity= Gravity.RIGHT;
        linear.addView(button, param3);


    }



    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("coverage.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
