package com.example.comin.login;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText nickText = (EditText) findViewById(R.id.nickText);
        final DatePicker userBirth = (DatePicker)findViewById(R.id.userBirth);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);
        final TextView birthText = (TextView) findViewById(R.id.birthText);
        final TextView registerText = (TextView) findViewById(R.id.registerText);
        final TextView userText = (TextView) findViewById(R.id.userText);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        Button dup_idButton = (Button) findViewById(R.id.dup_idButton);
        Button dup_nickButton = (Button) findViewById(R.id.dup_nickButton);

        user.setUserBirth("2019-01-01");
        user.setDup_id_check("");
        user.setDup_nick_check("");


        userBirth.init(2019, 01, 01, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = year + "-" + monthOfYear + "-" + dayOfMonth;
                user.setUserBirth(date);

            }
        });


        dup_idButton.setOnClickListener(new Button.OnClickListener() { //id 중복확
            @Override
            public void onClick(View view) {
                // TODO : click event
                final String userID = idText.getText().toString();
                JSONObject testjson = new JSONObject();
                try {
                    testjson.put("userID", userID);
                    testjson.put("dup", "1");

                    if(userID.equals("")){
                        user.setDup_nick_check("");
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("아이디를 입력해주세요.")
                                .setNegativeButton("다시시도", null)
                                .create()
                                .show();
                    }
                    else {
                        //전송
                        final RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "register", testjson, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //받은 json형식의 응답을 받아
                                    JSONObject jsonResponse = new JSONObject(response.toString());

                                    //key값에 따라 value값을 쪼개 받아옵니다.
                                    Boolean result = jsonResponse.getBoolean("dup_id");

                                    if (!result) {
                                        user.setDup_id_check(userID);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("사용가능한 아이디 입니다.")
                                                .setNegativeButton("확인", null)
                                                .create()
                                                .show();
                                    } else {
                                        user.setDup_id_check("");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("중복된 id 입니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();

                                    }
                                } catch (Exception e) {
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
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dup_nickButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
                final String userNick = nickText.getText().toString();
                JSONObject testjson = new JSONObject();

                if(userNick.equals("")){
                    user.setDup_nick_check("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("닉네임을 입력해주세요.")
                            .setNegativeButton("다시시도", null)
                            .create()
                            .show();
                }
                else {
                    try {
                        testjson.put("userNick", userNick);
                        testjson.put("dup", 2);


                        //전송
                        final RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "register", testjson, new Response.Listener<JSONObject>() {

                            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //받은 json형식의 응답을 받아
                                    JSONObject jsonResponse = new JSONObject(response.toString());

                                    //key값에 따라 value값을 쪼개 받아옵니다.
                                    Boolean result = jsonResponse.getBoolean("dup_nick");

                                    if (!result) {
                                        user.setDup_nick_check(userNick);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("사용가능한 닉네임 입니다.")
                                                .setNegativeButton("확인", null)
                                                .create()
                                                .show();
                                    } else {
                                        user.setDup_nick_check("");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("중복된 닉네임 입니다.")
                                                .setNegativeButton("다시 시도", null)
                                                .create()
                                                .show();

                                    }
                                } catch (Exception e) {
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
                    }
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = idText.getText().toString();
                    String userPassword = passwordText.getText().toString();
                    String userName = nameText.getText().toString();
                    String userNick = nickText.getText().toString();
                    String userBirth = user.getUserBirth();
                    String userPhone = phoneText.getText().toString();
                    String userEmail = emailText.getText().toString();
                    String userAuth = "12345";


                    if (userID.equals("") || userPassword.equals("") || userName.equals("") || userNick.equals("") || (userBirth.length()==0) || (userBirth == null) || userPhone.equals("") || userEmail.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("회원 등록에 실패하셨습니다. 입력하였는지 확인해주세요")
                                .setPositiveButton("다시시도", null)
                                .create()
                                .show();

                    }
                    else {
                        if (user.getDup_id_check().equals(userID) && user.getDup_nick_check().equals(userNick)){

                            JSONObject testjson = new JSONObject();
                            try {
                                //edittext의 id와 pw값을 받아와 put
                                testjson.put("userID", userID);
                                testjson.put("userPassword", userPassword);
                                testjson.put("userName", userName);
                                testjson.put("userNick", userNick);
                                testjson.put("userBirth", userBirth);
                                testjson.put("userPhone", userPhone);
                                testjson.put("userEmail", userEmail);
                                testjson.put("AuthKey", userAuth);
                                testjson.put("dup", 3);
                                String jsonString = testjson.toString(); //완성된 json 포맷
                                Log.d("test", jsonString);

                                //전송
                                final RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "register", testjson, new Response.Listener<JSONObject>() {

                                    //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            //받은 json형식의 응답을 받아
                                            JSONObject jsonResponse = new JSONObject(response.toString());

                                            //key값에 따라 value값을 쪼개 받아옵니다.
                                            Boolean result = jsonResponse.getBoolean("success");

                                            if (result) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                builder.setMessage("회원 등록에 성공하셨습니다.")
                                                        .setPositiveButton("확인", null)
                                                        .create()
                                                        .show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                RegisterActivity.this.startActivity(intent);
                                                finish();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                builder.setMessage("회원 등록에 실패하셨습니다.")
                                                        .setNegativeButton("다시 시도", null)
                                                        .create()
                                                        .show();

                                            }
                                        } catch (Exception e) {
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
                            }

                        } else if(user.getDup_id_check()==null||user.getDup_nick_check()==null||(user.getDup_id_check().length() == 0) || (user.getDup_nick_check().length()==0)||(user.getDup_id_check().equals(""))||(user.getDup_nick_check().equals(""))){
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("아이디 및 닉네임 중복을 확인해주세요.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();

                        }

                    }
                }
            });


        }
    }



