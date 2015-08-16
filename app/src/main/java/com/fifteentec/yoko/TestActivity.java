package com.fifteentec.yoko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fifteentec.Component.User.UserServer;

public class TestActivity extends BaseActivity {
    private Button mTestBtn;
    private EditText mTokenTv;
    private EditText mTestJsonTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTestBtn = (Button) findViewById(R.id.test_btn);
        mTokenTv = (EditText) findViewById(R.id.token_tv);
        mTestJsonTv = (EditText) findViewById(R.id.json_test_tv);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
    }

    public void test() {
        UserServer.getInstance().userGetUserInfo(TestActivity.this);
        mTokenTv.setText("access_token: " + UserServer.getInstance().getAccessToken() + "\n" +
                "refresh_token: " + UserServer.getInstance().getRefreshToken());

    }
}
