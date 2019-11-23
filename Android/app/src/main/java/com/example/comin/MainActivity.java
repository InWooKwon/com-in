package com.example.comin;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.IslamicCalendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    static final float dp =  Resources.getSystem().getDisplayMetrics().density;
    private LinearLayout linear;
    ArrayList<Insurance> insuranceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linear = findViewById(R.id.linearLayout);
        getIsuraceList();
        /*for (Insurance ins : insuranceList)
        {
            Log.d("test", ins.getCompany());
            addInsuranceInfoView(ins);
        }*/
    }

    public void getIsuraceList(){
        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "insurances",null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray insurancesArray = jsonResponse.getJSONArray("insurances");
                    JSONArray coverageArray = jsonResponse.getJSONArray("coverages");

                    for (int i = 0; i<insurancesArray.length(); i++)
                    {
                        JSONObject insuranceObject = insurancesArray.getJSONObject(i);

                        Insurance insurance = new Insurance();

                        insurance.setIdx(insuranceObject.getInt("idx"));
                        insurance.setProductName((insuranceObject.getString("productName")));
                        insurance.setCompany(insuranceObject.getString("company"));
                        insurance.setProductType(insuranceObject.getString("productType"));
                        insurance.setMinAge(insuranceObject.getInt("minAge"));
                        insurance.setMaxAge(insuranceObject.getInt("maxAge"));
                        insurance.setPrice(insuranceObject.getInt("price"));
                        insurance.setScore(insuranceObject.getDouble("score"));

                        ArrayList<Coverage> coverages = new ArrayList<>();
                        for (int j = 0; j<coverageArray.length(); j++)
                        {
                            JSONObject coverageObject = coverageArray.getJSONObject(j);
                            if (coverageObject.getInt("insuranceIdx") != insurance.getIdx())
                                continue;
                            Coverage coverage = new Coverage();
                            coverage.setType(coverageObject.getString("coverageType"));
                            coverage.setAmount(coverageObject.getInt("amount"));
                            coverage.setContent(coverageObject.getString("content"));
                            coverages.add(coverage);
                        }
                        insurance.setCoverageList(coverages);

                        insuranceList.add(insurance);
                    }
                    for (Insurance ins : insuranceList)
                    {
                        addInsuranceInfoView(ins);
                    }


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


    public  int pxToDp(int px)
    {
        return  (int)(px*dp);
    }

    public void addInsuranceInfoView(Insurance ins){

        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.insurance, null);
        TextView type = (TextView) rl.findViewById(R.id.insType);
        type.setText(ins.getProductType());
        TextView name = (TextView) rl.findViewById(R.id.insName);
        name.setText(ins.getProductName());

        //후기수 필요
        TextView score = (TextView) rl.findViewById(R.id.score);
        score.setText(Double.toString(ins.getScore()));

        TextView price = (TextView) rl.findViewById(R.id.price);
        price.setText("보험료 : 월 " + Integer.toString(ins.getPrice()) + "원");

        StringBuilder sb = new StringBuilder();
        for (Coverage cover : ins.getCoverageList())
        {
            sb.append(cover.getType() + " : " + cover.getAmount() + "원\n");
        }
        TextView coverage = (TextView) rl.findViewById(R.id.coverage);
        coverage.setText(sb);


        rl.setTag((Insurance)ins);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoDetailActivity.class);
                intent.putExtra("insurance", (Insurance)v.getTag());
                startActivity(intent);
            }
        });

        linear.addView(rl);
    }

}
