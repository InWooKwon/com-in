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
    static final String PREF_USER_ID = "username";
    static final String PREF_USER_PASSWORD = "password";
    static boolean is_autoLogin=false;

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

        if(getUserID(LoginActivity.this).length()==0 || getUserPassword(LoginActivity.this).length()==0){

        }
        else{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userID", getUserID(this).toString());
            startActivity(intent);
            this.finish();
        }


        isAutoLogin.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox)v).isChecked()) {
                    // TODO : CheckBox is checked.
                    setAutoLogin(LoginActivity.this,true);
                } else {
                    // TODO : CheckBox is unchecked.
                    setAutoLogin(LoginActivity.this,false);
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
                if(getAutoLogin(LoginActivity.this)) {
                    setUserID(LoginActivity.this,userID);
                    setUserPassword(LoginActivity.this,userPassword);
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


                        //key값에 따라 value값을 쪼개 받아옵니다.
                        Boolean result = jsonResponse.getBoolean("success");
                        JSONObject userObject = response.getJSONObject("user");


                        if (result) {
                            String userID = userObject.getString("id");
                            String userPassword=userObject.getString("pw");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            intent.putExtra("userID", userID);
                            intent.putExtra("userPassword", userPassword);

                            startActivity(intent);
                            finish();
                            Log.d("test", "login success");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("로그인에 실패하셨습니다.")
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
//자동로그인

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserID(Context ctx, String userID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.commit();
    }

    public static void setUserPassword(Context ctx, String userPassword) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public static void setAutoLogin(Context ctx, boolean isAutoLogin){
        is_autoLogin=isAutoLogin;
    }

    // 저장된 정보 가져오기
    public static String getUserID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    public static String getUserPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PASSWORD, "");
    }

    public static boolean getAutoLogin(Context ctx){
        return is_autoLogin;
    }
    // 로그아웃
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }


}
