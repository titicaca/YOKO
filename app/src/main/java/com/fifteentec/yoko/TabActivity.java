package com.fifteentec.yoko;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        TabSelector(R.integer.SelectorCal);
        BaseActivity.getDataSyncService().syncFriends(0);
        BaseActivity.getDataSyncService().getEvents(0);
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
                Toast d = Toast.makeText(this, "Calendar",
                        Toast.LENGTH_SHORT);
                d.setDuration(Toast.LENGTH_SHORT);
                d.show();
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
                    mFmTrans.add(R.id.id_content, friendsFragment, "cal");
                } else {
                    mFmTrans.show(friendsFragment);
                }


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
}
