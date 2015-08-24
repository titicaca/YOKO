package com.fifteentec.FoundFragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fifteentec.yoko.R;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundMsgBoxFragment extends Fragment {
    private RelativeLayout tabInvited,tabInviting;
    private TextView numInvited,numInviting;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFmTrans;
    private FoundMsgInvited mFoundMsgInvited;
    private FoundMsgInviting mFoundMsgInviting;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_found_msgbox_layout, null);

        mFragmentManager = FoundMsgBoxFragment.this.getParentFragment().getFragmentManager();

        tabInvited = (RelativeLayout) view.findViewById(R.id.layout_invited);
        tabInviting = (RelativeLayout) view.findViewById(R.id.layout_inviting);
        numInvited = (TextView)view.findViewById(R.id.number_invited);
        numInviting = (TextView)view.findViewById(R.id.number_inviting);

        tabInvited.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                if(mFoundMsgInvited==null){
                    mFoundMsgInvited = new FoundMsgInvited();
                }
                if(mFragmentManager.findFragmentByTag("invited")!=null){
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("invited"));
                }


                mFmTrans.add(R.id.id_content,mFoundMsgInvited, "invited");

                mFmTrans.addToBackStack("invited");
                mFmTrans.commit();
                mFmTrans.hide(FoundMsgBoxFragment.this.getParentFragment());

            }
        });

        tabInviting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                if(mFoundMsgInviting==null){
                    mFoundMsgInviting = new FoundMsgInviting();
                }
                if(mFragmentManager.findFragmentByTag("inviting")!=null){
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("inviting"));
                }

                mFmTrans.add(R.id.id_content,mFoundMsgInviting, "inviting");

                mFmTrans.addToBackStack("inviting");
                mFmTrans.commit();
                mFmTrans.hide(FoundMsgBoxFragment.this.getParentFragment());


            }
        });
        return view;
    }

}
