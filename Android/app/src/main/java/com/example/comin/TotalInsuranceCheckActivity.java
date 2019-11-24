package com.example.comin;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.IslamicCalendar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TotalInsuranceCheckActivity extends AppCompatActivity {

    static final float dp =  Resources.getSystem().getDisplayMetrics().density;
    private LinearLayout linear;
    ArrayList<Insurance> insuranceList = new ArrayList<>();
    ArrayList<Insurance> insViewList = new ArrayList<>();
    static String selectCompany = "전체보기";
    static String selectType = "전체보기";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_insurance_check);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_menu);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navi1:
                        break;
                    case R.id.navi2:
                        Intent a = new Intent(TotalInsuranceCheckActivity.this , CommunityActivity.class);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        finish();
                    case R.id.navi3:
                        break;
                }
                return false;
            }
        });



        linear = findViewById(R.id.linearLayout);
        getIsuraceList();
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
                    Set<String> companySet = new LinkedHashSet<>();
                    companySet.add("전체보기");
                    Set<String> typeSet = new LinkedHashSet<>();
                    typeSet.add("전체보기");

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
                        companySet.add(insurance.getCompany());
                        typeSet.add(insurance.getProductType());

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
                    Spinner cs = (Spinner)findViewById(R.id.companSpinner);
                    ArrayList<String> companyList = new ArrayList<>(companySet);
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,companyList);
                    cs.setAdapter(companyAdapter);
                    cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            insViewList.clear();
                            selectCompany = (String)adapterView.getItemAtPosition(position);
                            for (Insurance ins : insuranceList)
                            {
                                if(!(ins.getCompany().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if(!(ins.getProductType().equals(selectType) || selectType.equals("전체보기")))
                                    continue;
                                insViewList.add(ins);
                                addInsuranceInfoView(ins);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                    Spinner ts = (Spinner)findViewById(R.id.typeSpinner);
                    ArrayList<String> typeList = new ArrayList<>(typeSet);
                    ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,typeList);
                    ts.setAdapter(typeAdapter);
                    ts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            insViewList.clear();
                            selectType = (String)adapterView.getItemAtPosition(position);
                            for (Insurance ins : insuranceList)
                            {
                                if(!(ins.getProductType().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if(!(ins.getCompany().equals(selectCompany) || selectCompany.equals("전체보기")))
                                    continue;
                                insViewList.add(ins);
                                addInsuranceInfoView(ins);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                    Spinner ss = (Spinner)findViewById(R.id.sortSpinner);
                    ArrayList<String> sortList = new ArrayList<>();
                    sortList.add("정렬");
                    sortList.add("평점순");
                    sortList.add("후기순");
                    ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,sortList);
                    ss.setAdapter(sortAdapter);
                    ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            linear.removeAllViews();
                            ArrayList<Insurance> iv = new ArrayList<>();
                            iv.addAll(insViewList);
                            if(adapterView.getItemAtPosition(position).equals("평점순")) {
                                Collections.sort(iv, new Comparator<Insurance>() {
                                    @Override
                                    public int compare(Insurance o1, Insurance o2) {
                                        if (o1.getScore() < o2.getScore())
                                            return 1;
                                        else if (o1.getScore() > o2.getScore())
                                            return -1;
                                        else
                                            return 0;
                                    }
                                });
                                for (Insurance ins : iv) {
                                    Log.d("cover", "!111");
                                    addInsuranceInfoView(ins);
                                }
                            }
                            else if(adapterView.getItemAtPosition(position).equals("정렬"))
                            {
                                for (Insurance ins : insViewList) {
                                    Log.d("cover", "!111");
                                    addInsuranceInfoView(ins);
                                }
                            }
                            //TODO: 후기순 필요
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });




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
