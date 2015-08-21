package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIKey;
import com.API.APIServer;
import com.API.APIUrl;
import com.fifteentec.Adapter.commonAdapter.NewFriendAdapter;
import com.fifteentec.Component.Parser.JsonFriendAdd;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewFriendActivity extends BaseActivity implements OnClickListener {

    private KeyboardLayout mainView; // 判断软键盘是否隐藏
    private EditText search; // 输入框
    private ListView new_friends_lv;
    private ArrayList<JsonFriendList> list = new ArrayList<JsonFriendList>();
    private ImageView new_friedns_tv_search;
    private String number[] = new String[]{"110", "120", "119", "114"};
    private NewFriendAdapter fadapter;
    private RelativeLayout new_friends_rl_check;
    private TextView new_friends_tv_send;
    private EditText new_friends_et_check_gone;
    private TextView new_friedns_back;
    private ImageView new_friedns_back_iv;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friends);
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_friends);
        new_friends_rl_check = (RelativeLayout) findViewById(R.id.new_friends_rl_check);
        new_friedns_tv_search = (ImageView) findViewById(R.id.new_friends_iv_search);
        search = (EditText) findViewById(R.id.new_friedns_et_search);
        new_friends_tv_send = (TextView) findViewById(R.id.new_friends_tv_send);
        new_friends_lv = (ListView) findViewById(R.id.new_friends_lv);
        new_friends_et_check_gone = (EditText) findViewById(R.id.new_friends_et_check_gone);
        new_friedns_back = (TextView) findViewById(R.id.new_friedns_back);
        new_friedns_back_iv = (ImageView) findViewById(R.id.new_friedns_back_iv);

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
        new_friedns_back_iv.setOnClickListener(this);
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
            case R.id.new_friends_iv_search:
                if (search.getText().toString().trim().equals("")) {
                    Toast.makeText(NewFriendActivity.this, "请输入搜索内容",
                            Toast.LENGTH_SHORT).show();
                } else {

                    SearchFriends();

                }
                break;
            case R.id.new_friends_tv_send:
                if (!new_friends_et_check_gone.getText().toString().trim().equals("")) {
                    new_friends_rl_check.setVisibility(View.GONE);
                    SendCheckInfo(new_friends_et_check_gone.getText().toString());
                    new_friends_et_check_gone.setText("");

                }
                break;
            case R.id.new_friedns_back:
                finish();
                break;
            case R.id.new_friedns_back_iv:
                finish();
                break;
            default:
                break;
        }
    }

    public void SearchFriends() {
        Map<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String searchstr = search.getText().toString();
        JSONObject ob = new JSONObject();
        try {
            ob.put("mobile", searchstr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIServer.JsonPost(APIUrl.URL_SEARCHFRIENDS, ob, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject response = this.getResponse();
                if (response == null) {
                    new_friends_lv.setVisibility(View.INVISIBLE);
                    new_friends_rl_check.setVisibility(View.INVISIBLE);
                    Toast.makeText(NewFriendActivity.this, "没有此用户", Toast.LENGTH_SHORT).show();
                } else {
                    JsonFriendAdd jp = new JsonFriendAdd();
                    try {
                        jp.JsonParsing(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    id = jp.list.get(0).id + "";
                    fadapter = new NewFriendAdapter(NewFriendActivity.this, jp.list, new_friends_rl_check);
                    new_friends_lv.setAdapter(fadapter);
                    new_friends_lv.setVisibility(View.VISIBLE);

                }
            }
        }, this.getRequestQueue(), null).send();
    }

    private void SendCheckInfo(String strEdit) {
        Map<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject ob = new JSONObject();
        try {
            ob.put("msg", strEdit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIServer.JsonPost(APIUrl.URL_FRIENDSADDCHECKINFO + id, ob, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject response = this.getResponse();
                Toast.makeText(NewFriendActivity.this, "已发送",
                        Toast.LENGTH_SHORT).show();
            }
        }, this.getRequestQueue(), null).send();

    }
}