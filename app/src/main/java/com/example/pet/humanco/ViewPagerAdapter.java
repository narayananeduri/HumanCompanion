package com.example.pet.humanco;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Thriveni on 5/9/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new FragmentSearchPet();
        } else if (position == 1) {
            fragment = new FragmentAdvPet();
        } else if (position == 2) {
            fragment = new FragmentMyPromotions();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Search pet";
        } else if (position == 1) {
            title = "Post Pet";
        } else if (position == 2) {
            title = "Promotions";
        }
        return title;
    }
}