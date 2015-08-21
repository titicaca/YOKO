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

import com.Service.FriendInvitationReceiver;
import com.fifteentec.Fragment.FriendsFragment;
import com.fifteentec.Fragment.TabButtonFragment;
import com.fifteentec.TestRicheng.TestFragment;

public class TabActivity extends BaseActivity implements TabButtonFragment.Ibutton {
    private FragmentManager mFragmentManager;
    private final int EnterPage = 0;
    private TabButtonFragment mbuttonfg;
    private FriendsFragment friendsFragment;
    private TestFragment tf;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = this.getFragmentManager();
        setContentView(R.layout.activity_tab_main_layout);
        mbuttonfg = (TabButtonFragment) mFragmentManager.findFragmentById(R.id.tab_main_botton);
        mbuttonfg.setButton(this);
        TabSelector(R.integer.SelectorCir);
        TabSelector(R.integer.SelectorCal);
        BaseActivity.getDataSyncService().syncFriends(0);
        BaseActivity.getDataSyncService().getEvents(0);

        //设置系统状态监听过滤器IntentFilter
        IntentFilter mFilter = new IntentFilter();
        //设定监听内容为网络状态改变
        mFilter.addAction("com.Service.FriendInvitationReceiver.NEW_FRIEND_INVITATION");
        //注册绑定BroadcastReceiver监听相应的系统状态
        registerReceiver(friendInvitationReceiver, mFilter);
    }


    public interface MyListener {
        public void showMessage(String str);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void TabSelector(int id) {
        FragmentTransaction mFmTrans = mFragmentManager.beginTransaction();
        HideAllView(mFmTrans);
//        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
//        localFragmentTransaction.replace(R.id.fragment_container, tabFiveFragment, TAB_FIVE);
//        localFragmentTransaction.addToBackStack(null);// wp add
//        localFragmentTransaction.commit();
//            mFmTrans.add(R.id.id_content, friendsFragment, "cal");

        switch (id) {
            case R.integer.SelectorCal:
                Toast d = Toast.makeText(this, "Calendar",
                        Toast.LENGTH_SHORT);
                d.setDuration(Toast.LENGTH_SHORT);
                d.show();
                if (null == tf) {
                    tf = new TestFragment();
                }
                mFmTrans.replace(R.id.id_content, tf, null);

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
                //testfragment  &  friendsFragment
//                if (null == tf) {
//                    tf = new TestFragment();
//                    mFmTrans.add(R.id.id_content, tf, "frd");
//                } else {
//                    mFmTrans.show(tf);
//                }

                if (friendsFragment == null) {
                    friendsFragment = new FriendsFragment();
//                    mFmTrans.add(R.id.id_content, friendsFragment, "cal");
                }
                mFmTrans.replace(R.id.id_content, friendsFragment, null);
//                    mFmTrans.show(friendsFragment);


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(friendInvitationReceiver);
    }

    private void HideAllView(FragmentTransaction mFmTrans) {
    }

    private FriendInvitationReceiver friendInvitationReceiver = new FriendInvitationReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("Friend Invitation", "action: " + action);
            String msg = intent.getExtras().getString("msg");
            Log.v("Friend Invitation", "msg: " + msg);
        }
    };
}
