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
import com.API.APIUserServer;
import com.Common.NetworkState;
import com.Database.DBManager;
import com.Database.EventRecord;
import com.Database.FriendInfoRecord;
import com.Database.FriendTagRecord;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fifteentec.Component.Parser.DataSyncServerParser;

import org.json.JSONException;
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
                    DataSyncService.this.uploadEvents(0);
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
        Log.v("Data Sync Service", "bind");
        return new DataSyncServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("Data Sync Service", "unbind");
        return super.onUnbind(intent);
        //return true;
    }

    public class DataSyncServiceBinder extends Binder {
        public DataSyncService getService() {
            return DataSyncService.this;
        }
    }

    public void syncFriends(final long uid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new APIUserServer.JsonGet(APIUrl.URL_SYNC_FRIENDS, null, null, new APIJsonCallbackResponse(){
                    @Override
                    public void run() {
                        if (this.getResponse() == null) return;
                        Log.v("Data Sync Service", this.getResponse().toString());
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
                for (FriendTagRecord friendTagRecord : friendTagRecords) {
                    Log.v("Data Sync Server", "friend tags: " + " rid = " + friendTagRecord.rid +
                            " uid = " + friendTagRecord.uid +
                            " fuid = " + friendTagRecord.fuid +
                            " tagid = " + friendTagRecord.tagId +
                            " tagname = " + friendTagRecord.tagName);
                }

                for (FriendInfoRecord friendInfoRecord : friendInfoRecords) {
                    Log.v("Data Sync Server", "friends info: " + " rid = " + friendInfoRecord.rid +
                            " uid = " + friendInfoRecord.uid +
                            " fuid = " + friendInfoRecord.fuid +
                            " email = " + friendInfoRecord.email +
                            " location = " + friendInfoRecord.location +
                            " mobile = " + friendInfoRecord.mobile +
                            " nickname = " + friendInfoRecord.nickname +
                            " picturelink = " + friendInfoRecord.picturelink +
                            " qq = " + friendInfoRecord.qq +
                            " sex = " + friendInfoRecord.sex +
                            " wechat = " + friendInfoRecord.wechat +
                            " weibo = " + friendInfoRecord.weibo +
                            " collectnumber = " + friendInfoRecord.collectnumber +
                            " enrollnumber = " + friendInfoRecord.enrollnumber +
                            " friendnumber = " + friendInfoRecord.friendnumber +
                            " logintime = " + friendInfoRecord.logintime);
                }
                dbManager.getTableFriendTag().syncUser(uid, friendTagRecords);
                dbManager.getTableFriendInfo().syncUser(uid, friendInfoRecords);
            }
        }).start();
    }

    public void getEvents(final long uid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new APIUserServer.JsonGet(APIUrl.URL_EVENT_GET, null, null,
                        new APIJsonCallbackResponse() {
                            @Override
                            public void run() {
                                //todo
                            }
                        }, requestQueue, null).send();
            }
        }).start();
    }

    public void uploadEvents(final long uid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<EventRecord> eventRecords= dbManager.getTableEvent().queryEvent(uid, 1);

                for (EventRecord eventRecord : eventRecords) {
                    //todo 先上传图片到七牛，之后上传事件主体
                    if (eventRecord.modified == 0) continue;

                    JSONObject event = new JSONObject();
                    try {
                        event.put(APIKey.KEY_EVENT_SERVERID, eventRecord.serverid);
                        event.put(APIKey.KEY_EVENT_UID, eventRecord.uid);
                        event.put(APIKey.KEY_EVENT_INTRODUCTION,eventRecord.introduction);
                        event.put(APIKey.KEY_EVENT_LOCALPICTURELINK,eventRecord.localpicturelink);
                        event.put(APIKey.KEY_EVENT_REMOTEPICTURELINK,eventRecord.remotepitcurelink);
                        event.put(APIKey.KEY_EVENT_REMIND,eventRecord.remind);
                        event.put(APIKey.KEY_EVENT_TIMEBEGIN,eventRecord.timebegin);
                        event.put(APIKey.KEY_EVENT_TIMEEND,eventRecord.timeend);
                        event.put(APIKey.KEY_EVENT_TYPE,eventRecord.type);
                        event.put(APIKey.KEY_EVENT_PROPERTY,eventRecord.property);
                        event.put(APIKey.KEY_EVENT_DETAILLINK,eventRecord.detaillink);
                        event.put(APIKey.KEY_EVENT_STATUS,eventRecord.status);
                        event.put(APIKey.KEY_EVENT_UPDATETIME,eventRecord.updatetime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (eventRecord.serverid == 0) {
                        //post
                        new APIUserServer.JsonPost(APIUrl.URL_EVENT_UPDATE, event, null,
                                new APIJsonCallbackResponse() {
                                    @Override
                                    public void run() {
                                        //todo
                                    }
                                }, requestQueue, null).send();
                    } else {
                        if (eventRecord.status == 0) {
                            //put
                            new APIUserServer.JsonPut(APIUrl.URL_EVENT_UPDATE, event, null,
                                    new APIJsonCallbackResponse() {
                                        @Override
                                        public void run() {
                                            //todo
                                        }
                                    }, requestQueue, null).send();
                        } else {
                            //del
                            new APIUserServer.JsonDel(APIUrl.URL_EVENT_UPDATE, event, null,
                                    new APIJsonCallbackResponse() {
                                        @Override
                                        public void run() {
                                            //todo
                                        }
                                    }, requestQueue, null).send();
                        }
                    }
                }
            }
        }).start();
    }

    private void updateEvent() {
        //todo
    }
}
