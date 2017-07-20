package com.mobiversal.practica.life;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by iancuu on 10.07.2017.
 */

class SectionPagerAdapter extends FragmentStatePagerAdapter {
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
                GroupFragment groupFragment=new GroupFragment();
                return groupFragment;
            case 2:
            SearchFragment searchFragment =new SearchFragment();
            return searchFragment;

            case 3:FriendsFragment friendsFragment= new  FriendsFragment();
                return friendsFragment;

            default: return null;

        }

    }

    @Override
    public int getCount() {
        return 4 ;
    }

    public CharSequence getPageTitle(int position){

        switch(position) {
            case 0:
                return "Chats";
            case 1:
                return "Groups";
            case 2:
                return "GeoS";
            case 3:
                return "Friends";
            default:
                return null;
        }


    }
}

