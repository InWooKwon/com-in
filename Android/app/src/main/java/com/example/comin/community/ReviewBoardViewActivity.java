package com.example.comin.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReviewBoardViewActivity extends AppCompatActivity {

    boolean check=false;
    int useridx;
    TextView ddabongcnt;
    ArrayList<Insurance> insuranceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_board_view);

        Log.d("test090909","vvvvvvvvv");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = getIntent();
        int postIdx = (int) intent.getSerializableExtra("postIdx");
        Log.d("test11111",Integer.toString(postIdx));
        insuranceList = (ArrayList<Insurance>) intent.getSerializableExtra("insurancelist");

        User user=new User();
        useridx=user.getUserIdx(ReviewBoardViewActivity.this);
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "board/"+Integer.toString(postIdx)+"/"+Integer.toString(useridx),null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray boardsArray = jsonResponse.getJSONArray("board");
                    JSONObject boardObject = boardsArray.getJSONObject(0);
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
                    check=jsonResponse.getBoolean("check");
                    if(!boardObject.isNull("tag1")) {
                        post.setTag1(boardObject.getInt("tag1"));
                    }

                    setPost(post);
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
    public void setPost(Post post){
        TextView posttitle = (TextView)findViewById(R.id.posttitle);
        TextView postbody = (TextView)findViewById(R.id.postbody);
        ddabongcnt = (TextView)findViewById(R.id.ddabongcnt);

        posttitle.setText(post.getTitle());
        postbody.setText(post.getBody());
        final int postup = post.getUp();
        ddabongcnt.setText(Integer.toString(postup));

        float rate=post.getScore();
        RatingBar ratingbar = (RatingBar)findViewById(R.id.ratingBar2);
        ratingbar.setRating(rate);

        int tag1 = post.getTag1();
        LinearLayout taglayer = (LinearLayout)findViewById(R.id.taglayer);
        if(tag1 != 0){
            ImageView insimg = (ImageView) findViewById(R.id.insimg);
            TextView insname = (TextView)findViewById(R.id.insname);
            for(int i=0;i<insuranceList.size();i++) {
                Insurance ins=insuranceList.get(i);
                if(ins.getIdx() == tag1) {
                    String name = ins.getProductName();
                    insimg.setImageResource(getCompanyImageId(ins.getCompany()));
                    insname.setText(name);
                    break;
                }
            }
        }


        final int postIdx = post.getIdx();
        final ImageButton ddabongbtn = (ImageButton) findViewById(R.id.ddabongbtn);

        final JSONObject info=new JSONObject();
        try {
            info.put("index", postIdx);
            info.put("user", useridx);
            info.put("up", postup);
        }catch (Exception e){
            e.printStackTrace();
        }

        ddabongbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "board/";
                if (check) { // 눌러져있으면
                    ddabongbtn.setImageResource(R.drawable.ddabongon);
                    url = url + "down";
                } else { // 안눌러져있으면
                    ddabongbtn.setImageResource(R.drawable.ddabongoff);
                    url = url + "up";
                }
                final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + url, info, new Response.Listener<JSONObject>() {

                    //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //받은 json형식의 응답을 받아
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            int up = jsonResponse.getInt("up");
                            ddabongcnt.setText(Integer.toString(up));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", error.toString());
                        error.printStackTrace();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
            }
        });

        setCommentView(postIdx);

        ImageButton commentsend = (ImageButton) findViewById(R.id.commentsend);
        commentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText commenttext = (EditText) findViewById(R.id.commenttext);

                JSONObject commentjson = new JSONObject();
                try {
                    commentjson.put("index", postIdx);
                    commentjson.put("user", 1);
                    commentjson.put("content", commenttext.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL) + "board/reply",commentjson, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setCommentView(postIdx);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test",error.toString());
                        error.printStackTrace();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);

                commenttext.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });

    }
    public void setCommentView(int postIdx) {
        final LinearLayout commentview = (LinearLayout)findViewById(R.id.commentview);
        commentview.removeAllViews();

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "board/reply/reply/" + Integer.toString(postIdx), null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray replysArray = jsonResponse.getJSONArray("reply");
                    for (int i = 0; i < replysArray.length(); i++) {
                        JSONObject replyObject = replysArray.getJSONObject(i);
                        Comment comment = new Comment();

                        comment.setIdx(replyObject.getInt("idx"));
                        comment.setBoardIdx(replyObject.getInt("boardIdx"));
                        comment.setAuthorIdx(replyObject.getInt("authorIdx"));

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date writeDate = sdf.parse(replyObject.getString("date"));  ////////
                        comment.setDate(writeDate);
                        comment.setContent(replyObject.getString("content"));

                        setComment(comment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test", error.toString());
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void setComment(Comment comment){
        final LinearLayout commentview = (LinearLayout)findViewById(R.id.commentview);
        final RelativeLayout commentlayer = (RelativeLayout)getLayoutInflater().inflate(R.layout.commentviewlayer,null);

        TextView commentbody = (TextView)commentlayer.findViewById(R.id.commentbody);
        commentbody.setText(comment.getContent());

        TextView time = (TextView)commentlayer.findViewById(R.id.writetime);
        time.setText(formatTimeString(comment.getDate()));

        final int commentIdx=comment.getIdx();
        ImageButton deletebtn = (ImageButton)commentlayer.findViewById(R.id.deletebtn);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, getString(R.string.URL) + "board/reply/"+Integer.toString(commentIdx),null, new Response.Listener<JSONObject>() {

                    //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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

                commentview.removeView(commentlayer);
            }
        });

        commentview.addView(commentlayer);
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
