package com.fifteentec.yoko;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.Display;

import com.Service.DataSyncService;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.fifteentec.Component.User.UserServer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

public class YOKOApplication extends Application {
    private static Intent dataSyncServiceIntent;
    public final static String ApplicationName = "YOKO";
    private final static String BaiduPushApiKey = "DktSnpqB2wljcjOeIYW4f2BI";

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 最先载入用户信息
         */
        SharedPreferences sp = this.getSharedPreferences(ApplicationName, Context.MODE_PRIVATE);
        //sp.edit().clear().commit();
        UserServer.getInstance().setSharedPreferences(sp);
        UserServer.getInstance().loadSharedPreferences();
        UserServer.getInstance().setApplication(this);

        /**
         * 设定universalImageLoader的configuration
         */
        File cacheDir = getCacheDir();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //.memoryCacheExtraOptions(480, 800) //default = device screen dimensions
                //.diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(this))
                .imageDecoder(new BaseImageDecoder(false))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();
        /**
         * 初始化universalImageLoader
         */
        ImageLoader.getInstance().init(config);

        /**
         * 开启数据上传服务器
         */
        dataSyncServiceIntent = new Intent(this, DataSyncService.class);
        startService(dataSyncServiceIntent);

        /**
         * 开启百度云推送服务器
         */
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, BaiduPushApiKey);
    }

    @Override
    public void onTerminate() {
        /**
         * 关闭百度云推送服务器
         */
        PushManager.stopWork(this);

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

    public final static DisplayImageOptions ImageLoaderDefaultOptions = new DisplayImageOptions.Builder()
            //.showImageOnLoading(R.drawable.ic_stub) // resource or drawable
            //.showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
            //.showImageOnFail(R.drawable.ic_error) // resource or drawable
            .resetViewBeforeLoading(false)
            .delayBeforeLoading(1000)
            .cacheInMemory(false)
            .cacheOnDisk(false)
            .considerExifParams(false)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .build();
}
