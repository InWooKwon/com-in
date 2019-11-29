package com.example.comin.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.R;
import com.example.comin.community.Post;
import com.example.comin.insure.Insurance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecentInsureActivity extends AppCompatActivity {
    LinearLayout hotLayout;
    LinearLayout recentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_insure);



        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        int rv1 = PreferenceManager.getDefaultSharedPreferences(this).getInt("recentView1", 0);
        Log.d("cpver123123",Integer.toString(rv1));
        if(rv1 != 0)
        {
            Log.d("cpver123123","insurances/ins/" + Integer.toString(rv1));
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances/ins/" + Integer.toString(rv1),null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONArray recentInsArray = jsonResponse.getJSONArray("recentIns");
                        JSONObject recentIns = recentInsArray.getJSONObject(0);

                        hotLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.hotinsure, null);


                        hotLayout.removeView(hotLayout.findViewById(R.id.hot));
                        TextView tv = hotLayout.findViewById(R.id.hotName);
                        tv.setText(recentIns.getString("productName"));

                        ImageView iv = hotLayout.findViewById(R.id.hotCompany);
                        iv.setImageResource(getCompanyImageId(recentIns.getString("company")));


                        recentLayout = findViewById(R.id.recentLayout1);
                        recentLayout.addView(hotLayout);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("test",error.toString());

                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        int rv2 = PreferenceManager.getDefaultSharedPreferences(this).getInt("recentView2", 0);
        if(rv2 != 0)
        {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances/ins/" + Integer.toString(rv2),null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONArray recentInsArray = jsonResponse.getJSONArray("recentIns");
                        JSONObject recentIns = recentInsArray.getJSONObject(0);

                        hotLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.hotinsure, null);


                        hotLayout.removeView(hotLayout.findViewById(R.id.hot));
                        TextView tv = hotLayout.findViewById(R.id.hotName);
                        tv.setText(recentIns.getString("productName"));

                        ImageView iv = hotLayout.findViewById(R.id.hotCompany);
                        iv.setImageResource(getCompanyImageId(recentIns.getString("company")));


                        recentLayout = findViewById(R.id.recentLayout2);
                        recentLayout.addView(hotLayout);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("test",error.toString());
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        int rv3 = PreferenceManager.getDefaultSharedPreferences(this).getInt("recentView3", 0);
        if(rv3 != 0)
        {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances/ins/" + Integer.toString(rv3),null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다
                        JSONArray recentInsArray = jsonResponse.getJSONArray("recentIns");
                        JSONObject recentIns = recentInsArray.getJSONObject(0);

                        hotLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.hotinsure, null);

                        hotLayout.removeView(hotLayout.findViewById(R.id.hot));
                        TextView tv = hotLayout.findViewById(R.id.hotName);
                        tv.setText(recentIns.getString("productName"));

                        ImageView iv = hotLayout.findViewById(R.id.hotCompany);
                        iv.setImageResource(getCompanyImageId(recentIns.getString("company")));


                        recentLayout = findViewById(R.id.recentLayout3);
                        recentLayout.addView(hotLayout);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("test",error.toString());
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        int rv4 = PreferenceManager.getDefaultSharedPreferences(this).getInt("recentView4", 0);
        if(rv4 != 0)
        {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances/ins/" + Integer.toString(rv4),null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONArray recentInsArray = jsonResponse.getJSONArray("recentIns");
                        JSONObject recentIns = recentInsArray.getJSONObject(0);

                        hotLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.hotinsure, null);


                        hotLayout.removeView(hotLayout.findViewById(R.id.hot));
                        TextView tv = hotLayout.findViewById(R.id.hotName);
                        tv.setText(recentIns.getString("productName"));

                        ImageView iv = hotLayout.findViewById(R.id.hotCompany);
                        iv.setImageResource(getCompanyImageId(recentIns.getString("company")));


                        recentLayout = findViewById(R.id.recentLayout4);
                        recentLayout.addView(hotLayout);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("test",error.toString());
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        int rv5 = PreferenceManager.getDefaultSharedPreferences(this).getInt("recentView5", 0);
        if(rv5 != 0)
        {
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances/ins/" + Integer.toString(rv5),null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONArray recentInsArray = jsonResponse.getJSONArray("recentIns");
                        JSONObject recentIns = recentInsArray.getJSONObject(0);

                        hotLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.hotinsure, null);


                        hotLayout.removeView(hotLayout.findViewById(R.id.hot));
                        TextView tv = hotLayout.findViewById(R.id.hotName);
                        tv.setText(recentIns.getString("productName"));

                        ImageView iv = hotLayout.findViewById(R.id.hotCompany);
                        iv.setImageResource(getCompanyImageId(recentIns.getString("company")));


                        recentLayout = findViewById(R.id.recentLayout5);
                        recentLayout.addView(hotLayout);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("test",error.toString());
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }

    }
    public int getCompanyImageId(String company)
    {
        if (company.equals("교보라이프플래닛생명"))
        {
            return R.drawable.kyobolife;
        }
        else if (company.equals("하나생명"))
        {
            return R.drawable.hanalife;

        }
        else if (company.equals("신한생명"))
        {
            return R.drawable.sinhanlife;
        }
        else if (company.equals("흥국생명"))
        {
            return R.drawable.heungkuk;
        }
        else if (company.equals("KDB생명"))
        {
            return R.drawable.kdblife;
        }
        else if (company.equals("라이나생명"))
        {
            return R.drawable.laina;
        }
        else if (company.equals("에이스손해보험"))
        {
            return R.drawable.ace;
        }
        else if (company.equals("DB손해보험"))
        {
            return R.drawable.dbsonhae;
        }
        else if (company.equals("동양생명"))
        {
            return R.drawable.dongyang;
        }
        else if (company.equals("삼성화재"))
        {
            return R.drawable.samsungfire;
        }
        else if (company.equals("MG손해보험"))
        {
            return R.drawable.mgsonhae;
        }
        else if (company.equals("KB손해보험"))
        {
            return R.drawable.kbsonhae;
        }
        else if (company.equals("한화생명"))
        {
            return R.drawable.hanalife;
        }
        else if (company.equals("미래에셋생명"))
        {
            return R.drawable.miraeasset;
        }
        else if (company.equals("한화손해보험"))
        {
            return R.drawable.hanhwasonhae;
        }
        else if (company.equals("NH농협손해보험"))
        {
            return R.drawable.nhsonhae;
        }
        else if (company.equals("삼성생명"))
        {
            return R.drawable.samsunglife;
        }
        else if (company.equals("AIG"))
        {
            return R.drawable.aig;
        }
        else if (company.equals("롯데손해보험"))
        {
            return R.drawable.lottesonhae;
        }
        else if (company.equals("현대해상"))
        {
            return R.drawable.hyundae;
        }
        else if (company.equals("메리츠화재"))
        {
            return R.drawable.meritz;
        }
        else
            return -1;
    }
}
