package com.example.comin.login;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.MainActivity;
import com.example.comin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final TextView registerButton = (TextView) findViewById(R.id.registerButton);
        final CheckBox isAutoLogin = (CheckBox) findViewById(R.id.isAutoLogin);

        /*
        if (Build.VERSION.SDK_INT >= 23) {
            CheckPermission.checkPermissions();
        }
        */

        if(user.getUserID(LoginActivity.this).length()==0 || user.getUserPassword(LoginActivity.this).length()==0){

        }
        else{
            finish();
            login(user.getUserID(LoginActivity.this),user.getUserPassword(LoginActivity.this));
        }


        isAutoLogin.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox)v).isChecked()) {
                    // TODO : CheckBox is checked.
                    user.setAutoLogin(LoginActivity.this,true);
                } else {
                    // TODO : CheckBox is unchecked.
                    user.setAutoLogin(LoginActivity.this,false);
                }
            }
        }) ;


        //register로 화면 전환
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        //login 요청
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                if(user.getAutoLogin(LoginActivity.this)) {
                    user.setUserID(LoginActivity.this,userID);
                    user.setUserPassword(LoginActivity.this,userPassword);
                    login(userID, userPassword);
                }
                else
                    login(userID,userPassword);
            }

        });
    }


    public void login(String userID, String userPassword){
        JSONObject testjson = new JSONObject();
        try {
            //id와 pw값을 받아와 put
            testjson.put("userID", userID);
            testjson.put("userPassword", userPassword);
            String jsonString = testjson.toString(); //완성된 json 포맷;
            Log.d("test", jsonString);

            //전송
            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "login", testjson, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //받은 json형식의 응답을 받아
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        Log.d("test",response.toString());


                        //key값에 따라 value값을 쪼개 받아옵니다.
                        Boolean result = jsonResponse.getBoolean("success");
                        JSONObject userObject = response.getJSONObject("user");

                        Log.d("test",userObject.toString());
                        int idx = userObject.getInt("idx");
                        String userName = userObject.getString("realName");
                        String userNick=userObject.getString("nickName");

                        Log.d("test",result.toString());
                        if (result) {
                            Log.d("test","success");
                            user.setUserIdx(LoginActivity.this,idx);
                            user.setUserName(LoginActivity.this,userName);
                            user.setUserNick(LoginActivity.this,userNick);
                            Log.d("test","setshared");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);


                            startActivity(intent);
                            finish();
                            Log.d("test", "login success");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("아이디 및 비밀번호를 다시 확인해주세요.")
                                    .setNegativeButton("다시시도", null)
                                    .create()
                                    .show();

                            Log.d("test", "login fail");
                        }

                    } catch (Exception e) {
                        Log.d("test", "error");
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("test", "json object error");
        }
    }

}
