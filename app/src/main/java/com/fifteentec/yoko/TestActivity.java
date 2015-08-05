package com.fifteentec.yoko;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIKey;
import com.API.APIServer;
import com.API.APIUrl;
import com.fifteentec.Component.User.UserServer;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button mTestBtn = (Button) findViewById(R.id.test_btn);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
    }

    public void test(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccess_token());

        APIServer.JsonGet jsonGet = new APIServer.JsonGet(APIUrl.URL_REQUEST_USER_INFO,
                null, headers, new APIJsonCallbackResponse() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), ((this.getResponse() == null)
                                        ? "测试Token!失败!\n"
                                        : "测试Token!成功!\n" + this.getResponse().toString()),
                                Toast.LENGTH_LONG).show();
                    }
                }, getRequestQueue(), null);
    }
}
