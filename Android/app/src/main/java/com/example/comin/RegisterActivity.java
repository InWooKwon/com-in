package com.example.comin;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText nickText = (EditText) findViewById(R.id.nickText);
        final EditText birthText = (EditText) findViewById(R.id.birthText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText emailText = (EditText) findViewById(R.id.emailText);

        Button registerButton = (Button) findViewById(R.id.registerButton);


        // 데이터 업로
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                String userPassword=passwordText.getText().toString();
                String userName = nameText.getText().toString();
                String userNick = nickText.getText().toString();
                String userBirth = birthText.getText().toString();
                String userPhone = phoneText.getText().toString();
                String userEmail = emailText.getText().toString();
                String userAuth = "12345";

                JSONObject testjson = new JSONObject();
                try {
                    //edittext의 id와 pw값을 받아와 put
                    testjson.put("userID", userID);
                    testjson.put("userPassword", userPassword);
                    testjson.put("userName",userName);
                    testjson.put("userNick",userNick);
                    testjson.put("userBirth",userBirth);
                    testjson.put("userPhone",userPhone);
                    testjson.put("userEmail",userEmail);
                    testjson.put("AuthKey",userAuth);
                    String jsonString = testjson.toString(); //완성된 json 포맷
                    Log.d("test",jsonString);

                    //전송
                    final RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "register",testjson, new Response.Listener<JSONObject>() {

                        //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //받은 json형식의 응답을 받아
                                JSONObject jsonResponse = new JSONObject(response.toString());

                                //key값에 따라 value값을 쪼개 받아옵니다.
                                String result = jsonResponse.getString("success");

                                if(result.equals("YES")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 성공하셨습니다.")
                                            .setPositiveButton("확인",null)
                                            .create()
                                            .show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 실하셨습니다.")
                                            .setNegativeButton("다시 시도",null)
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


        });


    }
}
