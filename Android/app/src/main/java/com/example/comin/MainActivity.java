package com.example.comin;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.comin.community.CommunityFragment;
import com.example.comin.insure.TotalInsCheckFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    private TotalInsCheckFragment totalInsCheckFragment = new TotalInsCheckFragment();
    private CommunityFragment communityFragment = new CommunityFragment();

    // private Menu3Fragment menu3Fragment = new Menu3Fragment();
    // private Menu4Fragment menu4Fragment = new Menu4Fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("cover11", "11111");


        //첫화면 지정
        fragmentManager.beginTransaction().add(R.id.frame_layout, totalInsCheckFragment).commit();
        //다른화면 add 후 hide
        fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragment).commit();
        fragmentManager.beginTransaction().hide(communityFragment).commit();

        //하단바 리스너
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_menu);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("cover11", "4444");
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navi1:
                        fragmentManager.beginTransaction().hide(communityFragment).commit();
                        fragmentManager.beginTransaction().show(totalInsCheckFragment).commit();
                        break;
                    case R.id.navi2:
                        fragmentManager.beginTransaction().hide(totalInsCheckFragment).commit();
                        fragmentManager.beginTransaction().show(communityFragment).commit();
                        break;
                    case R.id.navi3:
                        break;
                }
                return true;
            }
        });
    }


}
