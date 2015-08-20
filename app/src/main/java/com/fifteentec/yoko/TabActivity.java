package com.fifteentec.yoko;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fifteentec.Fragment.CalViewFragment;
import com.Service.InvitationReceiver;
import com.fifteentec.Fragment.TabButtonFragment;

public class TabActivity extends BaseActivity implements TabButtonFragment.Ibutton {
    private FragmentManager mFragmentManager;
    private final int EnterPage = 0;
    private TabButtonFragment mbuttonfg;
    private CalViewFragment mCalViewFragment;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = this.getFragmentManager();
        setContentView(R.layout.activity_tab_main_layout);
        mbuttonfg = (TabButtonFragment) mFragmentManager.findFragmentById(R.id.tab_main_botton);
        mbuttonfg.setButton(this);
        TabSelector(R.integer.SelectorCal);
        BaseActivity.getDataSyncService().syncFriends(0);
        BaseActivity.getDataSyncService().getEvents(0);

        //设置系统状态监听过滤器IntentFilter
        IntentFilter mFilter = new IntentFilter();
        //设定监听内容为网络状态改变
        mFilter.addAction("com.Service.FriendInvitationReceiver.NEW_FRIEND_INVITATION");
        //注册绑定BroadcastReceiver监听相应的系统状态
        registerReceiver(invitationReceiver, mFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void TabSelector(int id) {
        FragmentTransaction mFmTrans = mFragmentManager.beginTransaction();
        HideAllView(mFmTrans);
        switch (id) {
            case R.integer.SelectorCal:

                if(mCalViewFragment ==null){
                    mCalViewFragment = new CalViewFragment();
                    mFmTrans.add(R.id.id_content,mCalViewFragment,"cal");
                }
                else{
                    mFmTrans.show(mCalViewFragment);
                }
                break;
            case R.integer.SelectorFrd:
                Toast a = Toast.makeText(this, "Found",
                        Toast.LENGTH_SHORT);
                a.setDuration(Toast.LENGTH_SHORT);
                a.show();
                break;
            case R.integer.SelectorCir:
                Toast b = Toast.makeText(this, "Friend",
                        Toast.LENGTH_SHORT);
                b.setDuration(Toast.LENGTH_SHORT);
                b.show();

                break;

            case R.integer.SelectorMe:
                Toast c = Toast.makeText(this, "Count",
                        Toast.LENGTH_SHORT);
                c.setDuration(Toast.LENGTH_SHORT);
                c.show();
                break;
            default:
                Log.e("Error", "Wrong TabActivity Selector");
                break;

        }
        mFmTrans.commit();
    }

    private void HideAllView(FragmentTransaction mFmTrans) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(invitationReceiver);
    }

    private InvitationReceiver invitationReceiver = new InvitationReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("Friend Invitation", "action: " + action);
            String msg = intent.getExtras().getString("msg");
            Log.v("Friend Invitation", "msg: " + msg);
        }
    };
}
