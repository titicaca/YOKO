package com.fifteentec.yoko;

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
    private static Activity curActivity = null;
    private DataSyncServiceBinder dataSyncServiceBinder = null;

    private ServiceConnection dataSyncServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dataSyncServiceBinder = (DataSyncServiceBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataSyncServiceBinder = null;
        }
    };

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }

    public DBManager getDBManager() {
        return this.dbManager;
    }

    public DataSyncServiceBinder getDataSyncServiceBinder() {
        return this.dataSyncServiceBinder;
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
        super.onResume();
        setCurrentActivity(this);

        Intent intent = ((YOKOApplication) this.getApplication()).getDataSyncServiceIntent();
        bindService(intent, dataSyncServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        unbindService(dataSyncServiceConnection);

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

