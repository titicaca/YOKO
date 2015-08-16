package com.fifteentec.yoko;

import android.content.Intent;
import android.os.Bundle;

import com.fifteentec.Component.User.UserServer;

public class BlankActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(BlankActivity.this,
                WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            finish();
        }
    }
}
