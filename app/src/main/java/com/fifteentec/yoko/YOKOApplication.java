package com.fifteentec.yoko;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.Service.DataSyncService;
import com.fifteentec.Component.User.UserServer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class YOKOApplication extends Application {
    Intent dataSyncServiceIntent;
    public final static String applicationName = "YOKO";

    public Intent getDataSyncServiceIntent() {
        return this.dataSyncServiceIntent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 开启数据上传服务器
         */
        dataSyncServiceIntent = new Intent(this, DataSyncService.class);
        startService(dataSyncServiceIntent);
        /**
         * 初始化universalImageLoader
         * todo 请自定义初始化设置
         */
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        SharedPreferences sp = this.getSharedPreferences(applicationName, Context.MODE_PRIVATE);
        //sp.edit().clear().commit();
        UserServer.getInstance().setSharedPreferences(sp);
        UserServer.getInstance().loadSharedPreferences();
        UserServer.getInstance().setApplication(this);
    }

    @Override
    public void onTerminate() {
        /**
         * 关闭数据上传服务器
         */
        stopService(dataSyncServiceIntent);
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
