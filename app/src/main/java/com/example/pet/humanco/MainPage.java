package com.example.pet.humanco;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

public class MainPage extends AppCompatActivity {

    TabLayout tl;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String name = user.get(SessionManagement.userId);
        // email
        String email = user.get(SessionManagement.useremail);
        tl = findViewById(R.id.tl);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                int pos = tab.getPosition();
                switch (pos)
                {
                    case 0:
                    {
                        FragmentSearchPet search_pet = new FragmentSearchPet();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.fg1,search_pet);
                        ft.commit();
                        break;
                    }
                    case 1:
                    {

                        FragmentAdvPet adv_pet = new FragmentAdvPet();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.fg1,adv_pet);
                        ft.commit();
                        break;
                    }
                    case 2:
                    {
                        FragmentMyPromotions adv_pet = new FragmentMyPromotions();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(R.id.fg1,adv_pet);
                        ft.commit();
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.petstoremenu, menu);
        return true;
    }

    public void LogOut_OnClick(MenuItem item)
    {
        session.logoutUser();
       overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void Adve_On_Click(MenuItem item)
    {
        startActivity(new Intent(this,My_Ads.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void myProfile(MenuItem item)
    {
        startActivity(new Intent(this,My_Profile.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    public void promotions_OnClick(MenuItem item)
    {
        startActivity(new Intent(this,My_PromotionsAd.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
