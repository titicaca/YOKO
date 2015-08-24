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
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewFriendActivity extends BaseActivity implements OnClickListener {

    // 判断软键盘是否隐藏
    private KeyboardLayout mainView;
    // 手机号输入框
    private EditText search;
    //搜索后显示的好友信息列表
    private ListView newFriendsLv;
    //输入框右面的放大镜，提供点击后搜索输入框的内容
    private ImageView newFriednsTvSearch;
    //搜索后显示的好友信息列表的适配器
    private NewFriendAdapter newFriendAdapter;
    //点击添加好友时，显示下方验证信息的输入框
    private RelativeLayout newFriendsRlCheck;
    //验证信息的发送按钮
    private TextView newFriendsTvSend;
    //验证信息的输入框
    private EditText newFriendsEtCheckGone;
    //title的返回按钮
    private TextView newFriednsBack;
    //title的返回按钮
    private ImageView newFriednsBackIv;
    //发送好友请求时好友的id
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friends);
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_friends);
        newFriendsRlCheck = (RelativeLayout) findViewById(R.id.new_friends_rl_check);
        newFriednsTvSearch = (ImageView) findViewById(R.id.new_friends_iv_search);
        search = (EditText) findViewById(R.id.new_friedns_et_search);
        newFriendsTvSend = (TextView) findViewById(R.id.new_friends_tv_send);
        newFriendsLv = (ListView) findViewById(R.id.new_friends_lv);
        newFriendsEtCheckGone = (EditText) findViewById(R.id.new_friends_et_check_gone);
        newFriednsBack = (TextView) findViewById(R.id.new_friedns_back);
        newFriednsBackIv = (ImageView) findViewById(R.id.new_friedns_back_iv);

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
        newFriednsTvSearch.setOnClickListener(this);
        newFriendsTvSend.setOnClickListener(this);
        newFriednsBack.setOnClickListener(this);
        newFriednsBackIv.setOnClickListener(this);
        setListViewHeightBasedOnChildren(newFriendsLv);
        search.setOnFocusChangeListener(onFocusAutoClearHintListener);
    }

    /**
     * 输入框的焦点监听
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
                if (!newFriendsEtCheckGone.getText().toString().trim().equals("")) {
                    newFriendsRlCheck.setVisibility(View.GONE);
                    SendCheckInfo(newFriendsEtCheckGone.getText().toString());
                    newFriendsEtCheckGone.setText("");

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
                    newFriendsLv.setVisibility(View.INVISIBLE);
                    newFriendsRlCheck.setVisibility(View.INVISIBLE);
                    Toast.makeText(NewFriendActivity.this, "没有此用户", Toast.LENGTH_SHORT).show();
                } else {
                    JsonFriendAdd jp = new JsonFriendAdd();
                    try {
                        jp.JsonParsing(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    id = jp.list.get(0).id + "";
                    newFriendAdapter = new NewFriendAdapter(NewFriendActivity.this, jp.list, newFriendsRlCheck);
                    newFriendsLv.setAdapter(newFriendAdapter);
                    newFriendsLv.setVisibility(View.VISIBLE);

                }
            }
        }, this.getRequestQueue(), null).send();
    }

    /**
     * @param strEdit 输入框的内容
     */
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