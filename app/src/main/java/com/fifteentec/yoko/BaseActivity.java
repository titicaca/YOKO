package com.fifteentec.yoko;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.Database.DBManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.Service.DataSyncService.DataSyncServiceBinder;

public abstract class BaseActivity extends Activity {
    protected RequestQueue requestQueue;
    protected DBManager dbManager;
    private static BaseActivity curActivity;

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }

    public DBManager getDBManager() {
        return this.dbManager;
    }

    public DataSyncServiceBinder getDataSyncServiceBinder() {
        return ((YOKOApplication)this.getApplication()).getDataSyncServiceBinder();
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

        if ((this instanceof LoginActivity) || (this instanceof TestActivity)||
                (this instanceof TabActivity)) {
            Intent intent = new Intent(getCurrentActivity(), BlankActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentActivity(this);
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

    public static BaseActivity getCurrentActivity() {
        return curActivity;
    }

    public static void setCurrentActivity(BaseActivity activity) {
        curActivity = activity;
    }

}