package com.fifteentec.yoko;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.Database.DBManager;
import com.Service.DataSyncService.DataSyncServiceBinder;
import com.Service.DataSyncService;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class BaseActivity extends Activity {
    protected RequestQueue requestQueue;
    protected DBManager dbManager;
    private static BaseActivity curActivity;

    private static DataSyncServiceBinder dataSyncServiceBinder;
    private static DataSyncService dataSyncService;

    private static ServiceConnection dataSyncServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v("Base Activity", "Service Connect");
            dataSyncServiceBinder = (DataSyncServiceBinder)service;
            dataSyncService = dataSyncServiceBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v("Base Activity", "Service Disconnect");
            dataSyncServiceBinder = null;
            dataSyncService = null;
        }
    };

    public static DataSyncService getDataSyncService() {
        return dataSyncService;
    }

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }

    public DBManager getDBManager() {
        return this.dbManager;
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

        Intent intent = new Intent(this, DataSyncService.class);
        bindService(intent, dataSyncServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() >0) super.onBackPressed();
        else {
            if ((this instanceof LoginActivity) || (this instanceof TestActivity) ||
                    (this instanceof TabActivity)) {
                Intent intent = new Intent(getCurrentActivity(), BlankActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        Log.v("Base Activity", "resume");
        super.onResume();
        setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        setCurrentActivity(null);
        Log.v("Base Activity", "pause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unbindService(dataSyncServiceConnection);
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

