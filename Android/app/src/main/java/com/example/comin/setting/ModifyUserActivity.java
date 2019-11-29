package com.example.comin.setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.R;
import com.example.comin.login.User;

import org.json.JSONException;
import org.json.JSONObject;

public class ModifyUserActivity extends AppCompatActivity {

    String dup_nick_check;
    String userNick;
    String pw;
    String pw_check;
    String email;
    boolean result;


    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

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



        final EditText nickText = findViewById(R.id.nickText);
        Button dup_nickButton = findViewById(R.id.dup_nickButton);

        final EditText pwText = findViewById(R.id.pwText);
        final EditText pw_checkText = findViewById(R.id.pw_checkText);
        final EditText emailText = findViewById(R.id.emailText);

        Button check1Button = findViewById(R.id.check1);
        Button check2Button = findViewById(R.id.check2);
        Button check3Button = findViewById(R.id.check3);

        userNick="";
        pw="";
        pw_check="";
        email="";

        setDup_nick_check("");

        check1Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNick=nickText.getText().toString();
                Log.d("test","uesrNick :"+userNick);

                //null 처리
                if(userNick.length()==0 || userNick.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                    builder.setMessage("변경할 닉네임을 입력해주세요.")
                            .setNegativeButton("다시시도", null)
                            .create()
                            .show();

                }
                else{

                    if(user.getUserNick(ModifyUserActivity.this).equals(userNick)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                        builder.setMessage("현재 사용중인 닉네임입니다. 다시 시도해주세요.")
                                .setNegativeButton("다시시도", null)
                                .create()
                                .show();

                    }
                    else {
                        if (userNick.equals(getDup_nick_check())) {
                            if (request_modify(1, userNick)) {
                                Log.d("test","succcess modify nickname");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                                builder.setMessage("닉네임 변경에 성공하였습니다.")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                                builder.setMessage("닉네임 변경에 실패하였습니다.")
                                        .setNegativeButton("다시시도", null)
                                        .create()
                                        .show();
                            }
                        }
                    }

                }
            }
        });

        check2Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean is_pw_check = false;
                pw=pwText.getText().toString();
                pw_check=pw_checkText.getText().toString();

                //null 처리
                if(pw.length()!=0 || !(pw_check.equals(""))){

                    if (check_pw_request(pw,pw_check)) {
                        if (request_modify(2, pw)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                                builder.setMessage("비밀번호 변경에 성공하였습니다.")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                            builder.setMessage("비밀번호 변경에 실패하였습니다.")
                                    .setNegativeButton("다시시도", null)
                                    .create()
                                    .show();
                        }
                    }

                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                    builder.setMessage("변경할 닉네임을 입력해주세요.")
                            .setNegativeButton("다시시도", null)
                            .create()
                            .show();
                }

            }
        });

        check3Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=emailText.getText().toString();

                if(email.length()!=0 || !(email.equals(""))) {
                    if (user.getUserEmail(ModifyUserActivity.this).equals(email)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                        builder.setMessage("현재 이메일입니다. 다시 시도해주세요.")
                                .setNegativeButton("다시시도", null)
                                .create()
                                .show();

                    } else {
                        if(request_modify(3,email)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                            builder.setMessage("이메일 변경에 성공하였습니다.")
                                    .setNegativeButton("확인", null)
                                    .create()
                                    .show();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                            builder.setMessage("이메일 변경에 실패하였습니다.")
                                    .setNegativeButton("다시시도", null)
                                    .create()
                                    .show();
                        }

                    }



                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                    builder.setMessage("변경할 이메일을 입력해주세요.")
                            .setNegativeButton("다시시도", null)
                            .create()
                            .show();
                }

            }
        });


        dup_nickButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
                userNick = nickText.getText().toString();
                JSONObject testjson = new JSONObject();

                if(userNick.equals("")){
                    setDup_nick_check("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
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
                        final RequestQueue requestQueue = Volley.newRequestQueue(ModifyUserActivity.this);
                        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "register", testjson, new Response.Listener<JSONObject>() {

                            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //받은 json형식의 응답을 받아
                                    JSONObject jsonResponse = new JSONObject(response.toString());

                                    //key값에 따라 value값을 쪼개 받아옵니다.
                                    Boolean result = jsonResponse.getBoolean("success");
                                    Log.d("test","check dup"+String.valueOf(result));


                                    if (result) {
                                        setDup_nick_check(userNick);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                                        builder.setMessage("사용가능한 닉네임 입니다.")
                                                .setNegativeButton("확인", null)
                                                .create()
                                                .show();
                                    } else {
                                        setDup_nick_check("");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
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

    }

    public boolean request_modify(int case_modify, String contents){



        try {
            JSONObject testjson = new JSONObject();
            testjson.put("content", contents);
            testjson.put("case_modify", case_modify);


            //전송
            final RequestQueue requestQueue = Volley.newRequestQueue(ModifyUserActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, getString(R.string.URL) + "register/"+Integer.toString(user.getUserIdx(ModifyUserActivity.this)), testjson, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //받은 json형식의 응답을 받아
                        JSONObject jsonResponse = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        result = jsonResponse.getBoolean("success");


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

        return result;

    }

    public boolean check_pw_request(String pw_request, String pw_check_request){
        if(user.getUserPassword(ModifyUserActivity.this).equals(pw_request)){
            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
            builder.setMessage("현재 확인 중인 비밀번호입니다. 다시 확인해주세요.")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
            return false;
        }
        else {
            if (pw_request.equals(pw_check_request)) {
                return true;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyUserActivity.this);
                builder.setMessage("비밀번호가 일치하지 않습니다. 다시 확인해주세요.")
                        .setNegativeButton("다시 시도", null)
                        .create()
                        .show();

                return false;
            }
        }
    }

    public String getDup_nick_check() {
        return dup_nick_check;
    }

    public void setDup_nick_check(String dup_nick_check) {
        this.dup_nick_check = dup_nick_check;
    }

}
