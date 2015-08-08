package com.fifteentec.yoko;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.Service.NetworkService;
import com.fifteentec.Component.User.UserServer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class YOKOApplication extends Application {
    Intent networkServiceIntent;
    public final static String applicationName = "YOKO";

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 开启数据上传服务器
         */
        networkServiceIntent = new Intent(this, NetworkService.class);
        startService(networkServiceIntent);
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
        stopService(networkServiceIntent);
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
