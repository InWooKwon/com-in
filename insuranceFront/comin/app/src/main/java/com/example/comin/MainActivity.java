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
        /*
        linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        setContentView(linear, paramlinear);
         */


        String json = loadJSONFromAsset();

        /*
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray insurancesArray = jsonObject.getJSONArray("insurances");

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

                insuranceList.add(insurance);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }*/
        getIsuraceList();
        /*for (Insurance ins : insuranceList)
        {
            Log.d("test", ins.getCompany());
            addInsuranceInfoView(ins);
        }*/
    }

    public void getIsuraceList(){
        //전송
        //final ArrayList<Insurance> insuranceList = new ArrayList<>();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://192.168.6.4:9090/insurances",null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    Log.d("test234",response.toString());
                    Log.d("test345",jsonResponse.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray insurancesArray = jsonResponse.getJSONArray("insurances");


                    for (int i = 0; i<insurancesArray.length(); i++)
                    {
                        JSONObject insuranceObject = insurancesArray.getJSONObject(i);

                        Insurance insurance = new Insurance();

                        insurance.setIdx(insuranceObject.getInt("idx"));
                        Log.d("testidx",insuranceObject.getString("productName"));
                        insurance.setProductName((insuranceObject.getString("productName")));
                        insurance.setCompany(insuranceObject.getString("company"));
                        insurance.setProductType(insuranceObject.getString("productType"));
                        insurance.setMinAge(insuranceObject.getInt("minAge"));
                        insurance.setMaxAge(insuranceObject.getInt("maxAge"));
                        insurance.setPrice(insuranceObject.getInt("price"));
                        insurance.setScore(insuranceObject.getDouble("score"));

                        insuranceList.add(insurance);
                        Log.d("testcon",Integer.toString(insuranceList.size()));
                    }
                    for (Insurance ins : insuranceList)
                    {
                        Log.d("test", ins.getCompany());
                        addInsuranceInfoView(ins);
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

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("insurance.json");
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

    public void addInsuranceInfoView(Insurance ins){
        RelativeLayout rl = new RelativeLayout(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.setMargins(pxToDp(16),pxToDp(16),pxToDp(16),pxToDp(16));
        rl.setLayoutParams(params);
        rl.setBackgroundColor(0xff00ff00);

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.meritz) ;
        iv.setId(1);

        RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        ivParams.height= pxToDp(100);
        ivParams.width=pxToDp(100);
        iv.setLayoutParams(ivParams);

        TextView textView1 = new TextView(this);
        textView1.setId(2);
        textView1.setText(ins.getProductName());
        textView1.setTextSize(30);
        RelativeLayout.LayoutParams tvParam1 = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        tvParam1.addRule(RelativeLayout.RIGHT_OF, 1);
        textView1.setLayoutParams(tvParam1);


        TextView textView2 = new TextView(this);
        textView2.setId(3);
        textView2.setText("가격 : "+ ins.getPrice());
        textView2.setTextSize(15);
        RelativeLayout.LayoutParams tvParam2 = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        tvParam2.addRule(RelativeLayout.RIGHT_OF,1);
        tvParam2.addRule(RelativeLayout.BELOW, 2);
        textView2.setLayoutParams(tvParam2);

        rl.addView(iv);
        rl.addView(textView1);
        rl.addView(textView2);

        rl.setTag(ins.getIdx());
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoDetailActivity.class);
                intent.putExtra("InsuranceIdx", (Integer)v.getTag());
                startActivity(intent);
            }
        });

        linear.addView(rl);
    }

}
