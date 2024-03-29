package com.fifteentec.Fragment.FoundFragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Adapter.commonAdapter.MyFragmentPagerAdapter;
import com.fifteentec.yoko.R;

import java.util.ArrayList;

/**
 * Created by cj on 2015/8/7.
 */
public class FoundFragment extends Fragment {
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private int currIndex = 0;
    private TextView tabGroup, tabActivity,tabFavorite,tabMsgBox;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private ImageView bottomLine;
    private int bottomLineWidth;
    public final static int num = 3 ;
    Fragment group;
    Fragment activity;
    Fragment favorite;
    Resources resources;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_found_main_layout, null);
        resources = getResources();
        InitViewPager(view);
        InitTabs(view);
        TranslateAnimation animation = new TranslateAnimation(position_one, offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        bottomLine.startAnimation(animation);

        return view;
    }


    private void InitTabs(View parentView){
        tabGroup = (TextView) parentView.findViewById(R.id.tab_group);
        tabActivity = (TextView) parentView.findViewById(R.id.tab_activity);
        tabFavorite = (TextView) parentView.findViewById(R.id.tab_favorite);

        tabGroup.setOnClickListener(new MyOnClickListener(0));
        tabActivity.setOnClickListener(new MyOnClickListener(1));
        tabFavorite.setOnClickListener(new MyOnClickListener(2));

        bottomLine = (ImageView) parentView.findViewById(R.id.bottom_line);
        bottomLineWidth = bottomLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset =  (screenW / num - bottomLineWidth) / 2;
        int avg = screenW / num;
        position_one = avg+offset;
        position_two = avg*2+offset;
    }

    @SuppressLint("NewApi")
    private void InitViewPager(View parentView) {
        mPager = (ViewPager) parentView.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();

        group = new FoundGroup();
        activity = new FoundEvent();
        favorite = new FoundFavorite();

        fragmentsList.add(activity);
        fragmentsList.add(group);
        fragmentsList.add(favorite);

        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);

    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, offset, 0, 0);
                        tabGroup.setTextColor(resources.getColor(R.color.gray));
                    }
                    if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two,offset, 0, 0);
                        tabFavorite.setTextColor(resources.getColor(R.color.gray));
                    }
                    tabActivity.setTextColor(resources.getColor(R.color.black));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        tabActivity.setTextColor(resources.getColor(R.color.gray));
                    }
                    if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        tabFavorite.setTextColor(resources.getColor(R.color.gray));
                    }
                    tabGroup.setTextColor(resources.getColor(R.color.black));
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        tabActivity.setTextColor(resources.getColor(R.color.gray));
                    }
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        tabGroup.setTextColor(resources.getColor(R.color.gray));
                    }
                    tabFavorite.setTextColor(resources.getColor(R.color.black));
                    break;

            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            bottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}