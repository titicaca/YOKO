package com.fifteentec.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
    RadioButton msg_invite;
    RadioButton msg_clear;
    private FragmentManager mFragmentManager;
    Fragment invited;
    Fragment inviting;
    Fragment home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_found_msgbox_layout, null);
        mFragmentManager = getFragmentManager();
        FragmentTransaction mFmTrans = mFragmentManager.beginTransaction();

        HideAllView(mFmTrans);
        if(home == null){
            home = new FoundMsgHome();
            mFmTrans.add(R.id.id_msg_content,home,"home");
        }
        else{
            mFmTrans.show(home);
        }
        mFmTrans.commit();
        return view;
    }
    private void InitUpperButtons(View parentView){
        msg_clear = (RadioButton) parentView.findViewById(R.id.button_clear);
        msg_invite = (RadioButton) parentView.findViewById(R.id.button_msg_box);
        msg_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void HideAllView(FragmentTransaction mFmTrans) {
        if(home != null){
            mFmTrans.hide(home);
        }
        if(inviting != null){
            mFmTrans.hide(inviting);
        }
        if(invited != null){
            mFmTrans.hide(invited);
        }
    }

}