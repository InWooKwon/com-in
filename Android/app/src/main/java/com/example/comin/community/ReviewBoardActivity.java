package com.example.comin.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import java.text.SimpleDateFormat;
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

        //Intent intent = getIntent();
        //boardList = (ArrayList<Post>) intent.getSerializableExtra("board");

        getBoardList();

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
                                boolean check1=false;
                                boolean check2=false;
                                for(int i=0;i<insuranceList.size();i++){
                                    Insurance ins=insuranceList.get(i);
                                    if(ins.getIdx() == board.getTag1() && ins.getCompany().equals(adapterView.getItemAtPosition(position))){
                                        check1=true;
                                        break;
                                    }
                                    if(ins.getIdx() == board.getTag1() && ins.getProductType().equals(selectType)){
                                        check2=true;
                                        break;
                                    }
                                }
                                if((check1 || position == 0) && (check2 || selectType.equals("전체보기"))){
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
                                boolean check1=false;
                                boolean check2=false;
                                while(it.hasNext()){
                                    Insurance ins=it.next();
                                    if(ins.getIdx() == board.getTag1() && ins.getCompany().equals(adapterView.getItemAtPosition(position))){
                                        check1=true;
                                        break;
                                    }
                                    if(ins.getIdx() == board.getTag1() && ins.getProductType().equals(selectCompany)){
                                        check2=true;
                                        break;
                                    }
                                }
                                if((check1 || position == 0) && (check2 || selectCompany.equals("전체보기"))){
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

        TextView name = (TextView) rl.findViewById(R.id.title);
        name.setText(board.getTitle());

        TextView time = (TextView) rl.findViewById(R.id.time);
        time.setText(formatTimeString(board.getDate()));

        float rate=board.getScore();
        RatingBar ratingbar = (RatingBar)rl.findViewById(R.id.ratingBar3);
        ratingbar.setRating(rate);

        TextView up = (TextView) rl.findViewById(R.id.ddabongcount);
        up.setText(Integer.toString(board.getUp()));

        TextView content = (TextView) rl.findViewById(R.id.content);
        content.setText(board.getBody());

        TextView insname = (TextView)rl.findViewById(R.id.insname);
        for(int i=0;i<insuranceList.size();i++) {
            Insurance ins = insuranceList.get(i);
            if (ins.getIdx() == board.getTag1()) {
                insname.setText(ins.getProductName());
                ImageView iv = rl.findViewById(R.id.insimg);
                iv.setImageResource(getCompanyImageId(ins.getCompany()));
                break;
            }
        }

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
    public void getBoardList(){
        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "board",null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray boardsArray = jsonResponse.getJSONArray("board");
                    for (int i = 0; i<boardsArray.length(); i++)
                    {
                        JSONObject boardObject = boardsArray.getJSONObject(i);

                        Post post = new Post();

                        post.setIdx(boardObject.getInt("idx"));
                        post.setType(boardObject.getInt("type"));
                        post.setTitle(boardObject.getString("title"));
                        post.setScore(boardObject.getInt("score"));
                        post.setBody(boardObject.getString("body"));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date writeDate = sdf.parse(boardObject.getString("date"));  ////////
                        post.setDate(writeDate);
                        post.setAuthor(boardObject.getString("author"));
                        post.setUp(boardObject.getInt("up"));
                        if(!boardObject.isNull("tag1")) {
                            post.setTag1(boardObject.getInt("tag1"));
                        }
                        if(!boardObject.isNull("tag2")) {
                            post.setTag2(boardObject.getInt("tag2"));
                        }
                        if(!boardObject.isNull("tag3")) {
                            post.setTag3(boardObject.getInt("tag3"));
                        }
                        if(!boardObject.isNull("tag4")) {
                            post.setTag4(boardObject.getInt("tag4"));
                        }
                        if(!boardObject.isNull("tag5")) {
                            post.setTag5(boardObject.getInt("tag5"));
                        }
                        if(post.getType() == 1) {
                            boardList.add(post);
                        }
                    }

                    for(Post board : boardList){
                        addBoardView(board);
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