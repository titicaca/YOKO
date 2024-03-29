package com.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.Database.DBManager;
import com.Database.EventInvitationRecord;
import com.Database.FriendInfoRecord;
import com.Database.FriendInvitationRecord;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.fifteentec.Component.User.UserServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调

 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many

 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class BaiduPushMessageReceiver extends PushMessageReceiver {
    /** TAG to Log */
    public static final String TAG = BaiduPushMessageReceiver.class.getSimpleName();

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context
     *            BroadcastReceiver的执行Context
     * @param errorCode
     *            绑定接口返回值，0 - 成功
     * @param appid
     *            应用id。errorCode非0时为null
     * @param userId
     *            应用user id。errorCode非0时为null
     * @param channelId
     *            应用channel id。errorCode非0时为null
     * @param requestId
     *            向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 绑定成功
            UserServer.getInstance().setBaiduPushUserId(userId);
            UserServer.getInstance().setBaiduPushChannelId(channelId);
        }
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context
     *            上下文
     * @param message
     *            推送的消息
     * @param customContentString
     *            自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);
        try {
            JSONObject jsonMessage = new JSONObject(message.trim());
            int action = jsonMessage.getInt("action");
            JSONObject jsonMessageBody = jsonMessage.getJSONObject("body");

            DBManager dbManager = new DBManager(context);
            Intent intent;

            /**
             * 收到添加好友的actionCode为100
             * 收到好友确认的actionCode为101
             * 收到事件邀请的actionCode为200
             * 收到事件确认的actionCode为201
             */
            switch (action) {
                case 100:
                    saveNewFriendInvitation(jsonMessageBody, dbManager);

                    intent = new Intent(InvitationReceiver.ACTION_NEW_FRIEND_INVITATION);
                    intent.putExtra(InvitationReceiver.ACTION_KEY_MSG, "new friend invitation");
                    intent.putExtra(InvitationReceiver.ACTION_KEY_ACTION_CODE, InvitationReceiver.ACTION_CODE_NEW_FRIEND_INVITATION);

                    context.sendBroadcast(intent);
                    break;
                case 101:
                    saveNewFriendConfirm(jsonMessageBody, dbManager);

                    intent = new Intent(InvitationReceiver.ACTION_CONFIRM_FRIEND_INVITATION);
                    intent.putExtra(InvitationReceiver.ACTION_KEY_MSG, "confirm friend invitation");
                    intent.putExtra(InvitationReceiver.ACTION_KEY_ACTION_CODE, InvitationReceiver.ACTION_CODE_CONFIRM_FRIEND_INVITATION);

                    break;
                case 200:
                    saveNewEventInvitation(jsonMessageBody, dbManager);

                    intent = new Intent(InvitationReceiver.ACTION_NEW_EVENT_INVITATION);
                    intent.putExtra(InvitationReceiver.ACTION_KEY_MSG, "new event invitation");
                    intent.putExtra(InvitationReceiver.ACTION_KEY_ACTION_CODE, InvitationReceiver.ACTION_CODE_NEW_EVENT_INVITATION);

                    context.sendBroadcast(intent);

                    break;
                case 201:
                    saveNewEventConfirm(jsonMessageBody, dbManager);

                    intent = new Intent(InvitationReceiver.ACTION_CONFIRM_EVENT_INVITATION);
                    intent.putExtra(InvitationReceiver.ACTION_KEY_MSG, "confirm event invitation");
                    intent.putExtra(InvitationReceiver.ACTION_KEY_ACTION_CODE, InvitationReceiver.ACTION_CODE_CONFIRM_EVENT_INVITATION);

                    context.sendBroadcast(intent);

                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveNewFriendInvitation(JSONObject jsonMessageBody, DBManager dbManager) {
        try {
            long uid = jsonMessageBody.getLong("user_id");
            long fuid = jsonMessageBody.getLong("friend_id");
            String msg = jsonMessageBody.getString("msg");

            dbManager.getTableFriendInvitation().addFriendInvitation(new FriendInvitationRecord(uid, fuid, msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveNewFriendConfirm(JSONObject jsonMessageBody, DBManager dbManager) {
        try {
            long uid = UserServer.getInstance().getUserid();
            long fuid = jsonMessageBody.getLong("id");
            String nickname = jsonMessageBody.getString("nickname");
            String mobile = jsonMessageBody.getString("mobile");
            int sex = jsonMessageBody.getInt("sex");
            String location = jsonMessageBody.getString("location");
            String email = jsonMessageBody.getString("email");
            String qq = jsonMessageBody.getString("qq");
            String wechat = jsonMessageBody.getString("wechat");
            String weibo = jsonMessageBody.getString("weibo");
            String picturelink = jsonMessageBody.getString("picturelink");
            long createdtime = jsonMessageBody.getLong("createdtime");
            long logintime = jsonMessageBody.getLong("logintime");
            int status = jsonMessageBody.getInt("status");
            int collectnumber = jsonMessageBody.getInt("collectnumber");
            int enrollnumber = jsonMessageBody.getInt("enrollnumber");
            int friendnumber = jsonMessageBody.getInt("friendnumber");

            FriendInfoRecord friendInfoRecord = new FriendInfoRecord(uid, fuid, email, location, mobile, nickname, picturelink, qq, sex, wechat, weibo, collectnumber, enrollnumber, friendnumber, logintime);
            dbManager.getTableFriendInfo().addFriendInfo(friendInfoRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveNewEventInvitation(JSONObject jsonMessageBody, DBManager dbManager) {
        try {
            //todo
            long uid = jsonMessageBody.getLong("user_id");
            long fuid = jsonMessageBody.getLong("friend_id");
            String msg = jsonMessageBody.getString("msg");
            int type = jsonMessageBody.getInt("type");
            long eventId = jsonMessageBody.getLong("eventId");

            dbManager.getTableEventInvitation().addEventInvitation(new EventInvitationRecord(uid, fuid, msg, type, eventId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveNewEventConfirm(JSONObject jsonMessageBody, DBManager dbManager) {
        try {
            //todo
            long uid = jsonMessageBody.getLong("user_id");
            long fuid = jsonMessageBody.getLong("friend_id");
            String msg = jsonMessageBody.getString("msg");
            int type = jsonMessageBody.getInt("type");
            long eventId = jsonMessageBody.getLong("eventId");

            dbManager.getTableEventInvitation().deleteEventInvitation(uid, fuid, type, eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param successTags
     *            设置成功的tag
     * @param failTags
     *            设置失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> successTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + successTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

    }

    /**
     * delTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param successTags
     *            成功删除的tag
     * @param failTags
     *            删除失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> successTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + successTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

    }

    /**
     * listTags() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示列举tag成功；非0表示失败。
     * @param tags
     *            当前应用设置的所有tag。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 解绑定成功
        }
    }
}
