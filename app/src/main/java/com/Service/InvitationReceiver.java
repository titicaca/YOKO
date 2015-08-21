package com.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by benbush on 15/8/19.
 */
public class InvitationReceiver extends BroadcastReceiver{
    public final static String ACTION_NEW_FRIEND_INVITATION = "com.Service.InvitationReceiver.NEW_FRIEND_INVITATION";
    public final static String ACTION_CONFIRM_FRIEND_INVITATION = "com.Service.InvitationReceiver.CONFIRM_FRIEND_INVITATION";
    public final static String ACTION_NEW_EVENT_INVITATION = "com.Service.InvitationReceiver.NEW_EVENT_INVITATION";
    public final static String ACTION_CONFIRM_EVENT_INVITATION = "com.Service.InvitationReceiver.CONFIRM_EVENT_INVITATION";

    public final static int ACTION_CODE_NEW_FRIEND_INVITATION = 100;
    public final static int ACTION_CODE_CONFIRM_FRIEND_INVITATION = 101;
    public final static int ACTION_CODE_NEW_EVENT_INVITATION = 200;
    public final static int ACTION_CODE_CONFIRM_EVENT_INVITATION = 201;

    public final static String ACTION_KEY_MSG = "msg";
    public final static String ACTION_KEY_ACTION_CODE = "action_code";

    @Override
    public void onReceive(Context context, Intent intent) {
        return;
    }
}
