package com.example.comin;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CommunityActivity extends AppCompatActivity {

    ArrayList<Post> ReviewBoardList = new ArrayList<>();
    ArrayList<Post> QnABoardList = new ArrayList<>();
    ArrayList<Post> FreeBoardList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        getBoardList();
    }

    public void getBoardList(){
        //전송
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URL) + "board",null, new Response.Listener<JSONObject>() {

            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //받은 json형식의 응답을 받아
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    JSONArray boardsArray = jsonResponse.getJSONArray("board");
                    Log.d("test_length", Integer.toString(boardsArray.length()));
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
                        post.setTag1(boardObject.getInt("tag1"));
                        post.setTag2(boardObject.getInt("tag2"));
                        post.setTag3(boardObject.getInt("tag3"));
                        post.setTag4(boardObject.getInt("tag4"));
                        post.setTag5(boardObject.getInt("tag5"));

                        if(post.getType() == 1) {
                            ReviewBoardList.add(post);
                        }
                        else if(post.getType() == 2) {
                            QnABoardList.add(post);
                        }
                        else if(post.getType() == 3) {
                            FreeBoardList.add(post);
                        }
                    }
                    for (int i=0;i<2;i++)
                    {
                        addBoardPreView(1, ReviewBoardList.get(i));
                        addBoardPreView(2, QnABoardList.get(i));
                        addBoardPreView(3, FreeBoardList.get(i));
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

    public void addBoardPreView(int type, Post board){
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.boardsummary, null);

        LinearLayout pre = null;
        if(type == 1) {
            pre = findViewById(R.id.reviewlayer);
        }
        else if(type == 2) {
            pre = findViewById(R.id.qnalayer);
        }
        else if(type == 3) {
            pre = findViewById(R.id.freelayer);
        }

        TextView name = (TextView) rl.findViewById(R.id.nickName);
        name.setText(board.getAuthor());

        TextView time = (TextView) rl.findViewById(R.id.time);
        time.setText(formatTimeString(board.getDate()));

        TextView score = (TextView) rl.findViewById(R.id.rating);
        score.setText(Integer.toString(board.getScore()));

        TextView up = (TextView) rl.findViewById(R.id.ddabongcount);
        up.setText(Integer.toString(board.getUp()));
        Log.d("test_type1", board.getBody());

        TextView content = (TextView) rl.findViewById(R.id.content);
        content.setText(board.getBody());

        Button reviewbtn = (Button) findViewById(R.id.reviewbtn);
        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReviewBoardActivity.class);
                startActivity(intent);
            }
        });
        Button qnabtn = (Button) findViewById(R.id.qnabtn);
        qnabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QnABoardActivity.class);
                startActivity(intent);
            }
        });
        Button freebtn = (Button) findViewById(R.id.freebtn);
        freebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FreeBoardActivity.class);
                startActivity(intent);
            }
        });
        pre.addView(rl);
        //reviewpreview.addView(rl);
        //linear.addView(reviewpreview);
    }
    public static String formatTimeString(Date tempDate) {
        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;

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
