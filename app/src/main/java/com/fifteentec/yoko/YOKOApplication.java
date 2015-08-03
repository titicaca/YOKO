package com.fifteentec.yoko;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.Service.NetworkService;
import com.fifteentec.Component.User.UserServer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by benbush on 15/8/3.
 */
public class YOKOApplication extends Application {
    Intent networkServiceIntent;

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

        SharedPreferences sp = this.getSharedPreferences(this.getApplicationInfo().name, 0);
        //sp.edit().clear().commit();
        UserServer.getInstance().setSharedPreferences(sp);
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
    public void onLowMemory(){
        super.onLowMemory();
    }
}
