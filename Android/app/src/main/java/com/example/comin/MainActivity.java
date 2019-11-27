package com.example.comin;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.comin.community.CommunityFragment;
import com.example.comin.community.Post;
import com.example.comin.insure.TotalInsCheckFragment;
import com.example.comin.myinsure.MyInsureFragment;
import com.example.comin.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들

    //Log.d("insins","main1");
    private TotalInsCheckFragment totalInsCheckFragment = new TotalInsCheckFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private SettingFragment settingFragment = new SettingFragment();
     private MyInsureFragment myinsureFragment = new MyInsureFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //첫화면 지정
        fragmentManager.beginTransaction().add(R.id.frame_layout, totalInsCheckFragment).commit();
        //다른화면 add 후 hide
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
                    case R.id.navi1:
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().hide(settingFragment).commit();
                        fragmentManager.beginTransaction().hide(myinsureFragment).commit();
                        fragmentManager.beginTransaction().show(totalInsCheckFragment).commit();
                        break;
                    case R.id.navi2:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().hide(settingFragment).commit();
                        fragmentManager.beginTransaction().hide(myinsureFragment).commit();
                        fragmentManager.beginTransaction().show(communityFragment).commit();
                        break;
                    case R.id.navi3:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().hide(settingFragment).commit();
                        fragmentManager.beginTransaction().show(myinsureFragment).commit();
                        break;

                    case R.id.navi4:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().hide(myinsureFragment).commit();
                        fragmentManager.beginTransaction().show(settingFragment).commit();
                        break;

                }
                return true;
            }
        });
    }


}
