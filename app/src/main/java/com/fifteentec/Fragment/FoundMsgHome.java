package com.fifteentec.Fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
public class FoundMsgHome extends Fragment {
    private Button invited;
    private Button inviting;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFmTrans;
    private FoundMsgInvited mFoundMsgInvited;
    private FoundMsgInviting mFoundMsgInviting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_found_msgbox_home_layout, null);
        invited = (Button)view.findViewById(R.id.button_invited);
        inviting = (Button) view.findViewById(R.id.button_inviting);
        mFragmentManager = getFragmentManager();


        invited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFmTrans = mFragmentManager.beginTransaction();
                if(mFoundMsgInvited == null){
                    mFoundMsgInvited = new FoundMsgInvited();
                    mFmTrans.add(R.id.id_msg_content, mFoundMsgInvited,"invited");
                }
                else if(mFragmentManager.findFragmentByTag("invited")==null){
                    mFmTrans.add(R.id.id_msg_content, mFoundMsgInvited,"invited");
                }
                else{
                    mFmTrans.show(mFoundMsgInvited);
                }
                mFmTrans.addToBackStack("invited");
                mFmTrans.commit();
                mFmTrans.hide(FoundMsgHome.this);
            }
        });

        inviting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFmTrans = mFragmentManager.beginTransaction();
                if(mFoundMsgInviting == null){
                    mFoundMsgInviting = new FoundMsgInviting();
                    mFmTrans.add(R.id.id_msg_content, mFoundMsgInviting,"inviting");
                }
                else if(mFragmentManager.findFragmentByTag("inviting")==null){
                    mFmTrans.add(R.id.id_msg_content, mFoundMsgInviting,"inviting");
                }
                else{
                    mFmTrans.show(mFoundMsgInviting);
                }
                mFmTrans.addToBackStack("inviting");
                mFmTrans.commit();
            }
        });

        return view;
    }


}
