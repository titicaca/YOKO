package com.fifteentec.yoko;

import android.os.Bundle;

import com.fifteentec.Component.User.UserServer;

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserServer.getInstance().autoLogin(WelcomeActivity.this);
    }
}
