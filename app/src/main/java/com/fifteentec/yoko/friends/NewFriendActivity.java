package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fifteentec.Adapter.commonAdapter.FriendsAdapter;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.R;

import java.util.ArrayList;

public class NewFriendActivity extends Activity implements OnClickListener {

    private KeyboardLayout mainView; // 判断软键盘是否隐藏
    private EditText search; // 输入框
    private ListView new_friends_lv;
    private ArrayList<JsonParsing> list = new ArrayList<JsonParsing>();
    private TextView new_friedns_tv_search;
    private String number[] = new String[]{"110", "120", "119", "114"};
    private FriendsAdapter fadapter;
    private RelativeLayout new_friends_rl_check;
    private TextView new_friends_tv_send;
    private EditText new_friends_et_check;
    private TextView new_friedns_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friends);
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_friends);
        new_friends_rl_check = (RelativeLayout) findViewById(R.id.new_friends_rl_check);
        new_friedns_tv_search = (TextView) findViewById(R.id.new_friedns_tv_search);
        search = (EditText) findViewById(R.id.new_friedns_et_search);
        new_friends_tv_send = (TextView) findViewById(R.id.new_friends_tv_send);
        new_friends_lv = (ListView) findViewById(R.id.new_friends_lv);
        new_friends_et_check = (EditText) findViewById(R.id.new_friends_et_check);
        new_friedns_back = (TextView) findViewById(R.id.new_friedns_back);

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
        new_friedns_tv_search.setOnClickListener(this);
        new_friends_tv_send.setOnClickListener(this);
        new_friedns_back.setOnClickListener(this);
        setListViewHeightBasedOnChildren(new_friends_lv);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_friedns_tv_search:
                if (search.getText().toString().trim().equals("")) {
                    Toast.makeText(NewFriendActivity.this, "请输入搜索内容",
                            Toast.LENGTH_SHORT).show();
                } else {
                    list = new ArrayList<JsonParsing>();
                    for (int i = 0; i < number.length; i++) {
                        if (search.getText().toString().trim().equals(number[i])) {
                            // new_friends_lv.setVisibility(View.VISIBLE);
                            // list = new ArrayList<JsonParsing>();
                            JsonParsing jp = new JsonParsing();
//                            jp.name = number[i];
//                            jp.id = i + "";
                            list.add(jp);
                            break;
                        }
                    }
                    if (list.size() != 0) {
                        new_friends_lv.setVisibility(View.VISIBLE);
                        fadapter = new FriendsAdapter(this, list, "1",
                                new_friends_rl_check);
                        new_friends_lv.setAdapter(fadapter);
                    } else {
                        fadapter = new FriendsAdapter(this, list, "0",
                                new_friends_rl_check);
                        new_friends_lv.setAdapter(fadapter);
                        Toast.makeText(NewFriendActivity.this, "没找到结果",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.new_friends_tv_send:
                if (!new_friends_et_check.getText().toString().trim().equals("")) {
                    new_friends_rl_check.setVisibility(View.GONE);
                    new_friends_et_check.setText("");
                    Toast.makeText(NewFriendActivity.this, "已发送",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.new_friedns_back:
                finish();
                break;
            default:
                break;
        }
    }
}