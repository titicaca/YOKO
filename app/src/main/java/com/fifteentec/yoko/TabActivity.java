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

import com.fifteentec.Component.User.UserServer;
import com.fifteentec.FoundFragment.FoundFragment;
import com.fifteentec.Fragment.FriendsFragment;
import com.fifteentec.Fragment.MyPageFragment;
import com.fifteentec.Fragment.CalViewFragment;
import com.Service.InvitationReceiver;
import com.fifteentec.Fragment.TabButtonFragment;
import com.fifteentec.TestRicheng.TestFragment;

public class TabActivity extends BaseActivity implements TabButtonFragment.Ibutton {
    private FragmentManager mFragmentManager;
    private final int EnterPage = 0;
    private TabButtonFragment mbuttonfg;
    private FriendsFragment friendsFragment;
    private MyPageFragment myPageFragment;
    private TestFragment tf;
    private CalViewFragment mCalViewFragment;
    private FoundFragment mFoundFragment;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = this.getFragmentManager();
        setContentView(R.layout.activity_tab_main_layout);
        mbuttonfg = (TabButtonFragment) mFragmentManager.findFragmentById(R.id.tab_main_botton);
        mbuttonfg.setButton(this);
        //TabSelector(R.integer.SelectorCir);
        TabSelector(R.integer.SelectorCal);

        Log.v("uid", UserServer.getInstance().getUserid() + "");
        //设置系统状态监听过滤器IntentFilter
        IntentFilter mFilter = new IntentFilter();
        //设定监听内容为网络状态改变
        mFilter.addAction(InvitationReceiver.ACTION_NEW_FRIEND_INVITATION);
        mFilter.addAction(InvitationReceiver.ACTION_CONFIRM_FRIEND_INVITATION);
        mFilter.addAction(InvitationReceiver.ACTION_NEW_EVENT_INVITATION);
        mFilter.addAction(InvitationReceiver.ACTION_CONFIRM_EVENT_INVITATION);
        //注册绑定BroadcastReceiver监听相应的系统状态
        registerReceiver(invitationReceiver, mFilter);
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

                if(mCalViewFragment ==null){
                    mCalViewFragment = new CalViewFragment();
                    mFmTrans.add(R.id.id_content,mCalViewFragment,"cal");
                }
                else{
                    mFmTrans.show(mCalViewFragment);
                }
                break;
            case R.integer.SelectorFrd:
                if(mFoundFragment == null){
                    mFoundFragment = new FoundFragment();
                    mFmTrans.add(R.id.id_content, mFoundFragment,"found");
                }
                else{
                    mFmTrans.show(mFoundFragment);
                }


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
                     mFmTrans.add(R.id.id_content, friendsFragment, "friend");
                }else {
                    mFmTrans.show( friendsFragment);
                }
//                    mFmTrans.show(friendsFragment);


                break;

            case R.integer.SelectorMe:
                Toast c = Toast.makeText(this, "Count",
                        Toast.LENGTH_SHORT);
                c.setDuration(Toast.LENGTH_SHORT);
                c.show();
                if (myPageFragment == null) {
                    myPageFragment = new MyPageFragment();
                    mFmTrans.add(R.id.id_content, myPageFragment, "home");
                } else {
                    mFmTrans.show(myPageFragment);
                }
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
        unregisterReceiver(invitationReceiver);
    }

    private void HideAllView(FragmentTransaction mFmTrans) {
        if (mCalViewFragment != null) mFmTrans.hide(mCalViewFragment);
        if (myPageFragment != null) mFmTrans.hide(myPageFragment);
        if (friendsFragment != null) mFmTrans.hide(friendsFragment);
    }

    private InvitationReceiver invitationReceiver = new InvitationReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("Invitation", "action: " + action);
            String msg = intent.getExtras().getString(InvitationReceiver.ACTION_KEY_MSG);
            Log.v("Invitation", "msg: " + msg);
            int action_code = intent.getExtras().getInt(InvitationReceiver.ACTION_KEY_ACTION_CODE);
            Log.v("Invitation", "code: " + action_code);
        }
    };
}
