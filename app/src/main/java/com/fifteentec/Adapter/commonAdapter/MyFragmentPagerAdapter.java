package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by cj on 2015/8/7.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;



public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {

        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {

        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {

       // return super.getItemPosition(object);
        return PagerAdapter.POSITION_NONE;
    }

}

