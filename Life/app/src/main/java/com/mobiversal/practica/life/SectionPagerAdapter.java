package com.mobiversal.practica.life;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by iancuu on 10.07.2017.
 */

class SectionPagerAdapter extends FragmentPagerAdapter{
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                ChatFragment chatFragment =new ChatFragment();
                return chatFragment;
            case 1:
                SearchFragment searchFragment =new SearchFragment();
                return searchFragment;
            case 2:


        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
