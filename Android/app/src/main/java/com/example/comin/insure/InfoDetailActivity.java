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
        companyImage.setImageResource(getCompanyImageId(ins.getCompany()));


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
