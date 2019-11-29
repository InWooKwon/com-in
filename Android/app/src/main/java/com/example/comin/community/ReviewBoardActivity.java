package com.example.comin.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comin.R;
import com.example.comin.insure.Insurance;
import com.example.comin.login.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReviewBoardActivity extends AppCompatActivity {

    private LinearLayout postlayer;
    ArrayList<Insurance> insuranceList = new ArrayList<>(); //보험목록
    ArrayList<Post> boardList = new ArrayList<>();          //게시글 목록
    ArrayList<Post> boardViewList = new ArrayList<>();      //보여질 게시글 목록
    Set<String> companySet = new LinkedHashSet<>();         //회사 목록
    Set<String> typeSet = new LinkedHashSet<>();            //보험 타입 목록
    static String selectCompany = "전체보기";
    static String selectType = "전체보기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review_board);

        Intent intent = getIntent();
        boardList = (ArrayList<Post>) intent.getSerializableExtra("board");

        companySet.add("전체보기");
        typeSet.add("전체보기");

        Button writebtn = (Button)findViewById(R.id.writebtn);
        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ReviewBoardWriteActivity.class);
                intent.putExtra("insurancelist",insuranceList);
                startActivity(intent);
            }
        });

        User user=new User();
        int useridx = user.getUserIdx(ReviewBoardActivity.this);
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "myinsure/review/"+Integer.toString(useridx),null, new Response.Listener<JSONObject>() {
            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray insurancesArray = jsonResponse.getJSONArray("insurances");


                    for (int i = 0; i < insurancesArray.length(); i++) {
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

                        insuranceList.add(insurance);
                    }
                    postlayer=findViewById(R.id.postlayer); //게시글 들어갈 layer

                    Spinner cs = (Spinner)findViewById(R.id.companSpinner);
                    ArrayList<String> companyList = new ArrayList<>(companySet);
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,companyList);
                    cs.setAdapter(companyAdapter);
                    cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            postlayer.removeAllViews();
                            boardViewList.clear();
                            selectCompany = (String)adapterView.getItemAtPosition(position);
                            for (Post board : boardList)
                            {
                                Iterator<Insurance> it = insuranceList.iterator();
                                boolean check=false;
                                while(it.hasNext()){
                                    Insurance ins=it.next();
                                    if(ins.getIdx() == board.getTag1() && ins.getCompany().equals(adapterView.getItemAtPosition(position))){
                                        check=true;
                                        break;
                                    }
                                    if(ins.getIdx() == board.getTag1() && ins.getProductType().equals(selectType)){
                                        check=true;
                                        break;
                                    }
                                }
                                if(check || position == 0 || selectType.equals("전체보기")){
                                    boardViewList.add(board);
                                    addBoardView(board);
                                }/*
                                if(!(insuranceList.get(board.getTag1()).getCompany().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if(!(insuranceList.get(board.getTag1()).getProductType().equals(selectType) || selectType.equals("전체보기")))
                                    continue;
                                boardViewList.add(board);
                                addBoardView(board);*/
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
                            postlayer.removeAllViews();
                            boardViewList.clear();
                            selectType = (String) adapterView.getItemAtPosition(position);
                            for (Post board : boardList) {
                                Iterator<Insurance> it = insuranceList.iterator();
                                boolean check=false;
                                while(it.hasNext()){
                                    Insurance ins=it.next();
                                    if(ins.getIdx() == board.getTag1() && ins.getCompany().equals(adapterView.getItemAtPosition(position))){
                                        check=true;
                                        break;
                                    }
                                    if(ins.getIdx() == board.getTag1() && ins.getProductType().equals(selectCompany)){
                                        check=true;
                                        break;
                                    }
                                }
                                if(check || position == 0 || selectCompany.equals("전체보기")){
                                    boardViewList.add(board);
                                    addBoardView(board);
                                }
                                /*
                                if (!(insuranceList.get(board.getTag1()).getProductType().equals(adapterView.getItemAtPosition(position)) || position == 0))
                                    continue;
                                if (!(insuranceList.get(board.getTag1()).getCompany().equals(selectCompany) || selectCompany.equals("전체보기")))
                                    continue;
                                boardViewList.add(board);
                                addBoardView(board);*/
                            }
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
    public void addBoardView(Post board){
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.boardsummary, null);
        LinearLayout pre = findViewById(R.id.postlayer);

        TextView name = (TextView) rl.findViewById(R.id.nickName);
        name.setText(board.getAuthor());

        TextView time = (TextView) rl.findViewById(R.id.time);
        time.setText(formatTimeString(board.getDate()));

        TextView score = (TextView) rl.findViewById(R.id.rating);
        score.setText(Integer.toString(board.getScore()));

        TextView up = (TextView) rl.findViewById(R.id.ddabongcount);
        up.setText(Integer.toString(board.getUp()));

        TextView content = (TextView) rl.findViewById(R.id.content);
        content.setText(board.getBody());

        final int boardIdx=board.getIdx();
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(v.getContext(), ReviewBoardViewActivity.class);
                intent.putExtra("insurancelist",insuranceList);
                intent.putExtra("postIdx",boardIdx);
                startActivity(intent);
            }
        });
        pre.addView(rl);
    }

    public static String formatTimeString(Date tempDate) {
        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = Math.abs(curTime - regTime) / 1000;

        String msg = null;
        if (diffTime < 60) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= 60) < 60) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= 60) < 24) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= 24) < 30) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= 30) < 12) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }
}