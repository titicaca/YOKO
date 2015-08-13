package com.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.API.APIJsonCallbackResponse;
import com.API.APIKey;
import com.API.APIServer;
import com.API.APIUrl;
import com.Common.NetworkState;
import com.Database.DBManager;
import com.Database.FriendInfoRecord;
import com.Database.FriendTagRecord;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fifteentec.Component.Parser.DataSyncServerParser;
import com.fifteentec.Component.User.UserServer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSyncService extends Service {
    private RequestQueue requestQueue;
    private DBManager dbManager;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("Data Sync Service", "action: " + action);
            //判断是否接受的事件为网络状态改变事件
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                //判断当前网络是否链接
                if (NetworkState.isWifiConnected(context)) {
                    Log.v("Data Sync Service", "Wi-Fi connect");
                } else {
                    Log.v("Data Sync Service", "Wi-Fi disconnect");
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        //获取Volley发送队列
        requestQueue = Volley.newRequestQueue(this);
        //获取数据库对象
        dbManager = new DBManager(this);

        //设置系统状态监听过滤器IntentFilter
        IntentFilter mFilter = new IntentFilter();
        //设定监听内容为网络状态改变
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //注册绑定BroadcastReceiver监听相应的系统状态
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        //解除注册监听
        unregisterReceiver(mReceiver);

        //停止Volley队列工作
        requestQueue.stop();
        //关闭数据库对象
        dbManager.closeDB();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Data Sync Service", "start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DataSyncServiceBinder();
    }

    public class DataSyncServiceBinder extends Binder {
        public void uploadBaiduPushIdentification() {
            //todo
        }

        public void syncFriends() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put(APIKey.KEY_AUTHORIZATION, null);
                    new APIServer.JsonGet(APIUrl.URL_SYNC_FRIENDS, null, headers, new APIJsonCallbackResponse(){
                        @Override
                        public void run() {
                            //todo
                            Log.v("Data Sync Service", this.getResponse().toString());
                            long uid = 0;
                            syncFriends(uid, this.getResponse());
                        }
                    }, requestQueue, null).send();
                }
            }).start();
        }

        private void syncFriends(final long uid, final JSONObject response) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<FriendTagRecord> friendTagRecords = DataSyncServerParser.parseFriendResponseToFriendTag(uid, response);
                    List<FriendInfoRecord> friendInfoRecords = DataSyncServerParser.parseFriendResponseToFriendInfo(uid, response);
                    dbManager.getTableFriendTag().syncUser(uid, friendTagRecords);
                    dbManager.getTableFriendInfo().syncUser(uid, friendInfoRecords);
                }
            }).start();
        }
    }
}
