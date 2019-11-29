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
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QnABoardViewActivity extends AppCompatActivity {

    ArrayList<Insurance> insuranceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_board_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = getIntent();
        int postIdx = (int) intent.getSerializableExtra("postIdx");
        insuranceList = (ArrayList<Insurance>) intent.getSerializableExtra("insurancelist");

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "board/"+Integer.toString(postIdx),null, new Response.Listener<JSONObject>() {

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
        TextView ddabongcnt = (TextView)findViewById(R.id.ddabongcnt);

        posttitle.setText(post.getTitle());
        postbody.setText(post.getBody());
        ddabongcnt.setText(Integer.toString(post.getScore()));

        int tag1 = post.getTag1();
        int tag2 = post.getTag2();
        int tag3 = post.getTag3();
        int tag4 = post.getTag4();
        int tag5 = post.getTag5();

        Map<String,Object> insimage=new HashMap<>();
        insimage.put("메리츠",R.drawable.meritz);
        insimage.put("삼성화재",R.drawable.samsungfire);
        insimage.put("동부화재",R.drawable.dbsonhae);

        LinearLayout taglayer = (LinearLayout)findViewById(R.id.taglayer);
        final RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.tagpannel, null);
        if(tag1 != 0){
            String insname = insuranceList.get(tag1).getProductName();
            Log.d("test00001",insname);
            TextView tagname = (TextView) rl.findViewById(R.id.tagname);
            tagname.setText(insname);
            taglayer.addView(rl);
        }
        if(tag2 != 0){
            String insname = insuranceList.get(tag2).getProductName();
            TextView tagname = (TextView) rl.findViewById(R.id.tagname);
            tagname.setText(insname);
            Log.d("test00002",insname);
            taglayer.addView(rl);
            Log.d("test00008",Integer.toString(123123));

        }
        Log.d("test00003",Integer.toString(123123));
        Log.d("test00003",Integer.toString(tag3));
        if(tag3 != 0){
            String insname = insuranceList.get(tag3).getProductName();
            Log.d("test00003",insname);
            TextView tagname = (TextView) rl.findViewById(R.id.tagname);
            tagname.setText(insname);
            taglayer.addView(rl);
        }
        if(tag4 != 0){
            String insname = insuranceList.get(tag4).getProductName();
            Log.d("test00004",insname);
            TextView tagname = (TextView) rl.findViewById(R.id.tagname);
            tagname.setText(insname);
            taglayer.addView(rl);
        }
        if(tag5 != 0){
            String insname = insuranceList.get(tag5).getProductName();
            Log.d("test00005",insname);
            TextView tagname = (TextView) rl.findViewById(R.id.tagname);
            tagname.setText(insname);
            taglayer.addView(rl);
        }

        final int postIdx = post.getIdx();

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
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "board/reply/" + Integer.toString(postIdx), null, new Response.Listener<JSONObject>() {

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

}