package com.fifteentec.yoko;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.Database.DBManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class BaseActivity extends Activity {
    protected RequestQueue requestQueue;
    protected DBManager dbManager;
    private static Activity curActivity = null;

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }

    public DBManager getDBManager() {
        return this.dbManager;
    }

    public Intent getServiceIntent() {
        YOKOApplication application = (YOKOApplication) this.getApplication();
        return application.getNetworkServiceIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 初始化volley请求队列
         */
        requestQueue = Volley.newRequestQueue(this);
        /**
         * 初始化数据库管理模块
         */
        dbManager = new DBManager(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if ((this instanceof LoginActivity) || (this instanceof TestActivity)) {
            Intent intent = new Intent(getCurrentActivity(), BlankActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        setCurrentActivity(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        /**
         * 关闭请求队列
         */
        requestQueue.stop();
        /**
         * 关闭数据库
         */
        dbManager.closeDB();
        super.onDestroy();
    }

    public static Activity getCurrentActivity() {
        return curActivity;
    }

    public static void setCurrentActivity(Activity activity) {
        curActivity = activity;
    }
}