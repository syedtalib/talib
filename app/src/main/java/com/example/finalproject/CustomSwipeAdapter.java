package com.example.finalproject;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2/23/2018.
 */

public class CustomSwipeAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;

    public CustomSwipeAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
