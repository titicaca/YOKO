package com.fifteentec.yoko;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.fifteentec.Fragment.CalViewFragment;
import com.fifteentec.Fragment.FindFragment;
import com.fifteentec.Fragment.FindGroup;
import com.fifteentec.Fragment.TabButtonFragment;

public class TabActivity extends FragmentActivity implements TabButtonFragment.Ibutton{
    private FragmentManager mFragmentManager;
    private final int EnterPage= 0;
    private TabButtonFragment mbuttonfg;
    private CalViewFragment mCalViewFragment;
    private FindFragment mFindFragment;
    private FragmentTransaction mFmTrans;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main_layout);
        mFragmentManager= this.getSupportFragmentManager();
        mbuttonfg=(TabButtonFragment)mFragmentManager.findFragmentById(R.id.tab_main_botton);
        mbuttonfg.setButton(this);
        TabSelector(R.integer.SelectorCal);

    }


    @Override
    public void TabSelector(int id) {
        FragmentTransaction mFmTrans = mFragmentManager.beginTransaction();
       HideAllView(mFmTrans);
        switch (id)
        {
            case R.integer.SelectorCal:
                Toast d = Toast.makeText(this, "Calendar",
                        Toast.LENGTH_SHORT);
                d.setDuration(Toast.LENGTH_SHORT);
                d.show();
/*               if(mCalViewFragment ==null){
                    mCalViewFragment = new CalViewFragment();
                    mFmTrans.add(R.id.id_content,mCalViewFragment,"cal");
                }
                else{
                    mFmTrans.show(mCalViewFragment);
                }*/
                break;
            case R.integer.SelectorFrd:
                if(mFindFragment == null){
                    mFindFragment = new FindFragment();
                    mFmTrans.add(R.id.id_content,mFindFragment,"find");
                }
                else{
                    mFmTrans.show(mFindFragment);
                }

                Toast a = Toast.makeText(this, "Found",
                        Toast.LENGTH_SHORT);
                a.setDuration(Toast.LENGTH_SHORT);
                a.show();
                break;
            case R.integer.SelectorCir:
                Toast b =Toast.makeText(this, "Friend",
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
        if(mCalViewFragment != null){
            mFmTrans.hide(mCalViewFragment);
        }
        if(mFindFragment != null){
            mFmTrans.hide(mFindFragment);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
