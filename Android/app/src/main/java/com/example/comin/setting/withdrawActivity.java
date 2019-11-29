package com.example.comin.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.R;
import com.example.comin.login.LoginActivity;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class withdrawActivity extends AppCompatActivity {

    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        final EditText idText = findViewById(R.id.idText);
        final EditText pwText = findViewById(R.id.pwText);
        final Button withdrawButton = findViewById(R.id.withdrawButton);

        idText.setText("");
        pwText.setText("");

        Log.d("test" ," withdraw entry");

        //register로 화면 전환
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idText.getText().toString();
                String pw = pwText.getText().toString();

                String today = new SimpleDateFormat("yyyy년 MM월 dd일").format(new Date());
                String alert=today+"자로 회원 탈퇴를\n 하시겠습니까?\n";

                Log.d("test","withdraw id pw :"+id+pw);
                Log.d("test","withdraw id length :"+id.length());

                if(id.length()==0 || pw.length() ==0 || id.equals("") || pw.equals("")){
                    Log.d("test","111");
                    AlertDialog.Builder builder = new AlertDialog.Builder(withdrawActivity.this);
                    builder.setMessage("아이디 및 비밀번호를 입력해주세요.")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();

                }
                else {
                    Log.d("test","check user result" + String.valueOf(checkUser(id,pw)));
                    if (checkUser(id, pw)==true) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(withdrawActivity.this);
                        builder.setMessage(alert)
                                .setNegativeButton("아니요", null);

                        builder.setPositiveButton("확인",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                withdraw(user.getUserIdx(withdrawActivity.this));

                            }
                        });
                        builder.create();
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(withdrawActivity.this);
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

    public void withdraw(int idx){
        Log.d("test","idx" + Integer.toString(idx));
        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, getString(R.string.URL) + "register/"+Integer.toString(idx), null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    Boolean result = jsonResponse.getBoolean("success");

                    if (result) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(withdrawActivity.this);
                        builder.setMessage("회원 탈퇴에 성공하셨습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        user.clearUserName(withdrawActivity.this);
                        Intent intent = new Intent(withdrawActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                        Log.d("test","withdraw success");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(withdrawActivity.this);
                        builder.setMessage("회원 탈퇴에 실패하셨습니다.")
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
}
