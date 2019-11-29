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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.comin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FreeBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);

        Intent intent = getIntent();
        ArrayList<Post> boardList = new ArrayList<>();
        boardList = (ArrayList<Post>) intent.getSerializableExtra("board");

        Button writebtn = (Button)findViewById(R.id.writebtn);
        writebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),FreeBoardWriteActivity.class);
                startActivity(intent);
            }
        });
        for(Post board : boardList){
            setBoardPreView(board);
        }
    }
    public void setBoardPreView(Post board){
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
                Intent intent =new Intent(v.getContext(), FreeBoardViewActivity.class);
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
