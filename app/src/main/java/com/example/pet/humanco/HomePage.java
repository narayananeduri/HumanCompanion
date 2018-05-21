package com.example.pet.humanco;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Thriveni on 5/9/2018.
 */

public class HomePage extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManagement.userId);
        String email = user.get(SessionManagement.useremail);
    }

    public void navigatePage(int path)
    {
        viewPager.setCurrentItem(path);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.petstoremenu, menu);
        return true;
    }

    public void LogOut_OnClick(MenuItem item) {
        finish();
        session.logoutUser();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void Adve_On_Click(MenuItem item) {
        startActivity(new Intent(this, My_Ads.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void myProfile(MenuItem item) {
        startActivity(new Intent(this, My_Profile.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void promotions_OnClick(MenuItem item) {
        startActivity(new Intent(this, My_PromotionsAd.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
