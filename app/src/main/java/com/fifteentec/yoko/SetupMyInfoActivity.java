package com.fifteentec.yoko;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fifteentec.Component.User.UserServer;

/**
 * Created by Administrator on 2015/8/24.
 */
public class SetupMyInfoActivity extends BaseActivity {

    //登出按钮
    private TextView loginout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setupmyinfo);
        loginout = (TextView) findViewById(R.id.loginout);
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登出
                UserServer.getInstance().userLogout(SetupMyInfoActivity.this);
            }
        });
    }
}
