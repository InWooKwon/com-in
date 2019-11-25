package com.example.comin.insure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comin.insure.Coverage;
import com.example.comin.R;

public class InfoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);

        Intent intent = getIntent();
        Insurance ins = (Insurance) intent.getSerializableExtra("insurance");

        ImageView companyImage = findViewById(R.id.company);
        //TODO : 이미지추가 후 맞게 수정
        companyImage.setImageResource(R.drawable.meritz);


        TextView insName = findViewById(R.id.name);
        insName.setText(ins.getProductName());

        StringBuilder sb = new StringBuilder();
        for (Coverage cov : ins.getCoverageList())
        {
            sb.append(cov.getContent() + "\n");
        }
        TextView insContent = findViewById(R.id.content);
        insContent.setText(sb);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 후기게시판 activity 가게 설정
                /*
                Intent intent = new Intent(v.getContext(), InfoDetailActivity.class);
                intent.putExtra("InsuranceIdx", (Integer)v.getTag());
                startActivity(intent);
                 */
            }
        });
    }
}
