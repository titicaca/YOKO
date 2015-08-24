package com.fifteentec.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Component.User.UserServer;
import com.fifteentec.yoko.R;
import com.fifteentec.yoko.SetupMyInfoActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2015/8/21.
 */
public class MyPageFragment extends Fragment {

    //头像控件
    private ImageView mypageIvicon;
    //名字控件
    private TextView mypageRLInformationTvName;
    //地址控件
    private TextView mypageRLInformationTvAddress;
    //数据类
    private UserServer us;
    //设置按钮
    private ImageView mypageAdapterset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        us = UserServer.getInstance();
        View v = inflater.inflate(R.layout.my_page_fragment, container, false);
        mypageAdapterset = (ImageView) v.findViewById(R.id.mypage_adapter_set);
        mypageIvicon = (ImageView) v.findViewById(R.id.mypage_iv_icon);
        mypageRLInformationTvName = (TextView) v.findViewById(R.id.mypage_rL_information_tv_name);
        mypageRLInformationTvAddress = (TextView) v.findViewById(R.id.mypage_rL_information_tv_address);
        ImageLoader.getInstance().displayImage(us.getPictureLink(), mypageIvicon);
        mypageRLInformationTvName.setText(us.getNickname());
        mypageRLInformationTvAddress.setText(us.getLocation());

        mypageAdapterset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setClass(getActivity(), SetupMyInfoActivity.class);
                startActivity(in);
            }
        });


        return v;

    }
}
