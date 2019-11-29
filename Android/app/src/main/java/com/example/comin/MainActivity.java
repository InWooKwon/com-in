package com.example.comin;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.comin.community.CommunityFragment;
import com.example.comin.insure.TotalInsCheckFragment;
import com.example.comin.myinsure.MyInsureFragment;
import com.example.comin.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    TextView mTitleTextView;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들

    //Log.d("insins","main1");
    private TotalInsCheckFragment totalInsCheckFragment = new TotalInsCheckFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private MyInsureFragment myinsureFragment = new MyInsureFragment();
    private HomeFragment homeFragment = new HomeFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);			//액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(true);		//액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);			//홈 아이콘을 숨김처리합니다.
        actionBar.setDisplayUseLogoEnabled(false);


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.custombar, null);
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.title);
        mTitleTextView.setText("HOT 보험");


        //첫화면 지정
        fragmentManager.beginTransaction().add(R.id.frame_layout, homeFragment).commit();

        //다른화면 add 후 hide
        fragmentManager.beginTransaction().add(R.id.frame_layout, totalInsCheckFragment).commit();
        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();

        fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragment).commit();
        fragmentManager.beginTransaction().hide(communityFragment).commit();

        fragmentManager.beginTransaction().add(R.id.frame_layout, settingFragment).commit();
        fragmentManager.beginTransaction().hide(settingFragment).commit();

        fragmentManager.beginTransaction().add(R.id.frame_layout, myinsureFragment).commit();
        fragmentManager.beginTransaction().hide(myinsureFragment).commit();





        //하단바 리스너
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_menu);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.home:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().hide(myinsureFragment).commit();
                        fragmentManager.beginTransaction().hide(settingFragment).commit();
                        fragmentManager.beginTransaction().show(homeFragment).commit();
                        mTitleTextView.setText("최근 인기 보험");
                        break;
                    case R.id.insure:
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().hide(settingFragment).commit();
                        fragmentManager.beginTransaction().hide(myinsureFragment).commit();
                        fragmentManager.beginTransaction().hide(homeFragment).commit();
                        fragmentManager.beginTransaction().show(totalInsCheckFragment).commit();
                        mTitleTextView.setText("보험통합조회");
                        break;
                    case R.id.community:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().hide(settingFragment).commit();
                        fragmentManager.beginTransaction().hide(myinsureFragment).commit();
                        fragmentManager.beginTransaction().hide(homeFragment).commit();
                        fragmentManager.beginTransaction().show(communityFragment).commit();
                        mTitleTextView.setText("커뮤니티");
                        break;
                    case R.id.myinsure:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().hide(settingFragment).commit();
                        fragmentManager.beginTransaction().hide(homeFragment).commit();
                        fragmentManager.beginTransaction().show(myinsureFragment).commit();
                        mTitleTextView.setText("내보험통합조회");
                        break;

                    case R.id.setting:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().hide(myinsureFragment).commit();
                        fragmentManager.beginTransaction().hide(homeFragment).commit();
                        fragmentManager.beginTransaction().show(settingFragment).commit();
                        mTitleTextView.setText("설정");
                        break;
                }
                return true;
            }
        });


        ImageButton backButton = (ImageButton) mCustomView.findViewById(R.id.btnBack);
        backButton.setVisibility(View.INVISIBLE);

        actionBar.setCustomView(mCustomView);


        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));

    }
}
