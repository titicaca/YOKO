package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fifteentec.Adapter.commonAdapter.NewFriendsListAdapter;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.R;

public class NewFriendsListActivity extends Activity {

    private KeyboardLayout mainView; // 判断软键盘是否隐藏
    private EditText search; // 输入框
    private ListView new_friednslist_lv;
    private NewFriendsListAdapter nfladapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newfriends_list);
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_friendslist);
        search = (EditText) findViewById(R.id.new_friednslist_et_search);
        new_friednslist_lv = (ListView) findViewById(R.id.new_friednslist_lv);

        nfladapter = new NewFriendsListAdapter(this);
        new_friednslist_lv.setAdapter(nfladapter);

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
                // if (textView.getText().toString().equals("")) {
                // textView.setGravity(Gravity.CENTER);
                // } else {
                // textView.setGravity(Gravity.LEFT);
                // }
            }
        }
    };

    /**
     * 设置listview定高
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}