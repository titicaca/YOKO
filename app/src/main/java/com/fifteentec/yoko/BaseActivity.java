package com.fifteentec.yoko;

import android.app.Activity;
import android.os.Bundle;

import com.Database.DBManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class BaseActivity extends Activity {
    protected RequestQueue requestQueue;
    protected DBManager dbManager;

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
}
