package com.fifteentec.Fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fifteentec.Adapter.commonAdapter.MyFragmentPagerAdapter;
import com.fifteentec.yoko.R;

import java.util.ArrayList;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundMsgBoxFragment extends Fragment {
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private TextView tabInvited,tabInviting;
    private ImageView bottomLine;
    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    private int num = 2;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFmTrans;
    private FoundMsgInvited mFoundMsgInvited;
    private FoundMsgInviting mFoundMsgInviting;
    RadioButton clear;
    RadioButton msg_box;
    Resources resources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_found_msgbox_layout, null);
        resources = getResources();
        InitViewPager(view);
        InitUpperButtons(view);
        InitTabs(view);
        TranslateAnimation animation = new TranslateAnimation(position_one, offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        bottomLine.startAnimation(animation);
        return view;
    }
    private void InitUpperButtons(View parentView){
        clear = (RadioButton) parentView.findViewById(R.id.button_clear);
        msg_box = (RadioButton) parentView.findViewById(R.id.button_msg_box);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear.setChecked(false);
                if(currIndex==0){
                    mFoundMsgInvited.getInvitedList().clear();
                }
                if(currIndex==1){
                    mFoundMsgInviting.getInvitingList().clear();

                }
            }
        });
    }

    private void InitTabs(View parentView){
        tabInvited = (TextView) parentView.findViewById(R.id.tab_invited);
        tabInviting = (TextView) parentView.findViewById(R.id.tab_inviting);

        tabInvited.setOnClickListener(new MyOnClickListener(0));
        tabInviting.setOnClickListener(new MyOnClickListener(1));
        bottomLine = (ImageView) parentView.findViewById(R.id.bottom_line);
        bottomLineWidth = bottomLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / num - bottomLineWidth) / 2);
        int avg = (int) (screenW / num);
        position_one = avg+2*offset;
    }


    @SuppressLint("NewApi")
    private void InitViewPager(View parentView) {
        mPager = (ViewPager) parentView.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();

        mFoundMsgInvited = new FoundMsgInvited();
        mFoundMsgInviting = new FoundMsgInviting();


        fragmentsList.add(mFoundMsgInvited);
        fragmentsList.add(mFoundMsgInviting);

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
                        animation = new TranslateAnimation(position_one+offset, offset, 0, 0);
                        tabInviting.setTextColor(resources.getColor(R.color.gray));
                    }
                    tabInvited.setTextColor(resources.getColor(R.color.black));
                    msg_box.setText(resources.getString(R.string.button_invited));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one-offset, 0, 0);
                        tabInvited.setTextColor(resources.getColor(R.color.gray));
                    }
                    tabInviting.setTextColor(resources.getColor(R.color.black));
                    msg_box.setText(resources.getString(R.string.button_inviting));
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
