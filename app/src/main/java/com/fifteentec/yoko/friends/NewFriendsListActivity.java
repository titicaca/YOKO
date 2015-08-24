package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */


import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ListView;

import com.Database.DBManager;
import com.Database.FriendInvitationRecord;
import com.fifteentec.Adapter.commonAdapter.NewFriendsListAdapter;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.List;

public class NewFriendsListActivity extends BaseActivity {
    // 判断软键盘是否隐藏
    private KeyboardLayout mainView;
    // 搜索输入框
    private EditText search;
    //好友添加的列表listview
    private ListView newFriednslistLv;
    //好友添加的列表适配器
    private NewFriendsListAdapter newFriendsListAdapter;
    private BaseActivity activity;
    private DBManager dbManager;
    //好友添加的列表信息list
    private List<FriendInvitationRecord> listdata = new ArrayList<FriendInvitationRecord>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newfriends_list);
        this.activity = (BaseActivity) this;
        this.dbManager = this.activity.getDBManager();
        listdata = dbManager.getTableFriendInvitation().queryFriendInvitation(UserServer.getInstance().getUserid());
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_friendslist);
        search = (EditText) findViewById(R.id.new_friednslist_et_search);
        newFriednslistLv = (ListView) findViewById(R.id.new_friednslist_lv);
        //添加保护判断，getTableFriendInvitation有可能会为null
        if (listdata == null) {
            listdata = new ArrayList<FriendInvitationRecord>();
            newFriendsListAdapter = new NewFriendsListAdapter(this, listdata, dbManager, activity);
            newFriednslistLv.setAdapter(newFriendsListAdapter);
        } else {
            newFriendsListAdapter = new NewFriendsListAdapter(this, listdata, dbManager, activity);
            newFriednslistLv.setAdapter(newFriendsListAdapter);
        }


        mainView.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {

            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    // 软键盘隐藏
                    case KeyboardLayout.KEYBOARD_STATE_HIDE:
                        mainView.setFocusable(true);
                        mainView.setFocusableInTouchMode(true);
                        mainView.requestFocus();
                        break;
                    // 软键盘弹起
                    case KeyboardLayout.KEYBOARD_STATE_SHOW:
                        break;
                }
            }
        });
        search.setOnFocusChangeListener(onFocusAutoClearHintListener);
    }

    /**
     * 焦点监听
     */
    private OnFocusChangeListener onFocusAutoClearHintListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText textView = (EditText) v;
            String hint;
            if (hasFocus) {
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }
        }
    };

}