package com.fifteentec.yoko;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fifteentec.Component.User.UserServer;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ValidateActivity extends LoaderActivity implements OnClickListener {

    public final static String SMSSDK_APP_KEY = "93ca809b9373";
    public final static String SMSSDK_APP_SECRET = "5b30e106b61eb8638e4a5ba6d1374289";

    private AutoCompleteTextView mInputPhoneEt;
    private EditText mInputCodeEt;
    private Button mRequestCodeBtn;
    private Button mRegisterBtn;
    private Button mLoginBtn;
    private int resendWaitSecond = 30;
    private boolean isChangePassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);

        init();
    }

    @Override
    protected void setAdapter(ArrayAdapter<String> adapter) {
        mInputPhoneEt.setAdapter(adapter);
    }

    /**
     * 初始化控件
     */
    private void init() {
        String message = getIntent().getStringExtra("FROM_WHERE");
        if (message != null)
            isChangePassword = message.equals("LOGIN_ACTIVITY");

        mInputPhoneEt = (AutoCompleteTextView) findViewById(R.id.validate_input_phone_et);
        mInputCodeEt = (EditText) findViewById(R.id.validate_input_code_et);
        mRequestCodeBtn = (Button) findViewById(R.id.validate_request_code_btn);
        mRegisterBtn = (Button) findViewById(R.id.register_commit_btn);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mRequestCodeBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        if (isChangePassword) {
            ((RadioButton) findViewById(R.id.register_rbtn)).setText("修改密码");
            findViewById(R.id.have_an_account_tv).setVisibility(View.INVISIBLE);
            mLoginBtn.setVisibility(View.INVISIBLE);
            findViewById(R.id.try_app_tv).setVisibility(View.INVISIBLE);
            mRegisterBtn.setText("修改密码");
        }

        // 启动短信验证sdk
        SMSSDK.initSDK(this, SMSSDK_APP_KEY, SMSSDK_APP_SECRET);
        EventHandler eventHandler = new EventHandler() {
            /**
             * 在操作之后被触发
             *
             * @param event
             *            参数1 表示操作的类型
             *            EVENT_GET_VERIFICATION_CODE表示获取验证码
             *            EVENT_SUBMIT_VERIFICATION_CODE表示提交验证码
             * @param result
             *            参数2 表示操作的结果
             *            SMSSDK.RESULT_COMPLETE表示操作成功，
             *            SMSSDK.RESULT_ERROR表示操作失败
             * @param data
             *            事件操作返回的数据
             */
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                /* EventHandler的四个回调函数都不能在UI线程中运行，需要使用handler发送消息给UI线程处理 */
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        String phoneNums = mInputPhoneEt.getText().toString().replaceAll("[\\s\\-]+", "");
        switch (v.getId()) {
            case R.id.validate_request_code_btn:
                // 1. 通过规则判断手机号
                if (!InfoValidate.isPhoneValid(phoneNums)) {
                    Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                mRequestCodeBtn.setClickable(false);
                mRequestCodeBtn.setText("重新发送(" + resendWaitSecond + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; resendWaitSecond > 0; resendWaitSecond--) {
                            handler.sendEmptyMessage(-9);
                            if (resendWaitSecond <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

            case R.id.register_commit_btn:
                SMSSDK.submitVerificationCode("86", phoneNums, mInputCodeEt
                        .getText().toString());
                //createProgressBar();
                break;

            case R.id.login_btn:
                Intent intent = new Intent(ValidateActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                /* 跳转到LoginActivity页面后，当前页面调用了onStop()，资源仍然未释放，SMSSDK仍然注册eventhandler来监听消息
                 * LoginActivity页面重新跳转到ValidateActivity后，重新创建，SMSSDK重新注册eventhandler
                 * 存在两个eventhandler在后台监听消息，解决方法：当前ValidateActivity跳转到LoginActivity后调用finish()
                 * finish()会调用onDestroy()释放资源，同时当前Activity被移除出栈
                 */
                this.finish();          // will call onDestroy() to release resources
                break;
        }
    }

    /**
     *
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                mRequestCodeBtn.setText("重新发送(" + resendWaitSecond + ")");
            } else if (msg.what == -8) {
                mRequestCodeBtn.setText("获取验证码");
                mRequestCodeBtn.setClickable(true);
                resendWaitSecond = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回RegisterActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ValidateActivity.this,
                                isChangePassword ? ChangePswdActivity.class : RegisterActivity.class);
                        intent.putExtra("FROM_WHERE", "VALIDATE_ACTIVITY");
                        UserServer.getInstance().setPhone(
                                mInputPhoneEt.getText().toString().replaceAll("[\\s\\-]+", ""));
                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "验证码错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * progressbar
     */
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}