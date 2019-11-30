package com.example.comin.community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.content.Intent;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FreeBoardActivity extends AppCompatActivity {

    ArrayList<Post> boardList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);

        Intent intent = getIntent();
        //boardList = new ArrayList<>();
        //boardList = (ArrayList<Post>) intent.getSerializableExtra("board");

        getBoardList();

        Button writebtn = (Button)findViewById(R.id.writebtn);
        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),FreeBoardWriteActivity.class);
                intent.putExtra("board",boardList);
                startActivity(intent);
            }
        });
    }
    public void setBoardPreView(Post board){
        RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.boardsummary, null);
        LinearLayout pre = findViewById(R.id.postlayer);

        TextView name = (TextView) rl.findViewById(R.id.title);
        name.setText(board.getTitle());

        TextView time = (TextView) rl.findViewById(R.id.time);
        time.setText(formatTimeString(board.getDate()));

        RatingBar ratingbar = (RatingBar)rl.findViewById(R.id.ratingBar3);
        ratingbar.setVisibility(View.GONE);

        TextView up = (TextView) rl.findViewById(R.id.ddabongcount);
        up.setText(Integer.toString(board.getUp()));

        TextView content = (TextView) rl.findViewById(R.id.content);
        content.setText(board.getBody());

        final int boardIdx=board.getIdx();
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(v.getContext(), FreeBoardViewActivity.class);
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
                        if(post.getType() == 3) {
                            boardList.add(post);
                        }
                    }

                    for(Post board : boardList){
                        setBoardPreView(board);
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
}
