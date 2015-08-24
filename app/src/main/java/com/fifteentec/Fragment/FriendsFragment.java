package com.fifteentec.Fragment;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIUrl;
import com.API.APIUserServer;
import com.Database.DBManager;
import com.Database.FriendInfoRecord;
import com.Service.InvitationReceiver;
import com.fifteentec.Adapter.commonAdapter.FriendsAdapter;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.Component.Parser.JsonFriendTagReturn;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;
import com.fifteentec.yoko.friends.FriendDetailsActivity;
import com.fifteentec.yoko.friends.LabelActivity;
import com.fifteentec.yoko.friends.NewFriendActivity;
import com.fifteentec.yoko.friends.NewFriendsListActivity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements OnItemClickListener,
        OnClickListener, OnItemLongClickListener {
    // 好友列表listview
    private ListView friendsfragmentListview;
    // 手机号输入框，暂未使用
    private EditText search;
    // 判断软键盘是否隐藏
    private KeyboardLayout mainView;
    //好友列表数据
    private ArrayList<JsonFriendList> jsonData = new ArrayList<JsonFriendList>();
    //传入一个view，用来判断是否隐藏
    private View v;
    //好友列表适配器
    private FriendsAdapter friendsAdapter;
    private BaseActivity activity;
    private DBManager dbManager;
    //新的朋友按钮
    private RelativeLayout friendsRlnewfriendbutton;
    //标签按钮
    private RelativeLayout friendsRllabelbutton;
    //添加按钮
    private RelativeLayout friendsRladdbutton;
    //好友列表数据list
    private List<FriendInfoRecord> friendInfoRecords = null;
    //动态显示的红点，用来提示消息来了
    private ImageView friendsIvaddnewfriend;
    //用来判断的startactivity的请求码
    public static final int FRIENDINVICODE = 6;


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FRIENDINVICODE) {
            //返回时，更新数据
            LoadFriendsList();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = (BaseActivity) this.getActivity();
        this.dbManager = this.activity.getDBManager();


        //设置系统状态监听过滤器IntentFilter
        IntentFilter mFilter = new IntentFilter();
        //设定监听内容为网络状态改变
        mFilter.addAction(InvitationReceiver.ACTION_NEW_FRIEND_INVITATION);
        //注册绑定BroadcastReceiver监听相应的系统状态
        getActivity().registerReceiver(friendInvitationReceiver, mFilter);


        View view = inflater.inflate(R.layout.friends, container, false);
        friendsRladdbutton = (RelativeLayout) view.findViewById(R.id.friends_rl_add_button);
        friendsIvaddnewfriend = (ImageView) view.findViewById(R.id.friends_iv_add_newfriend);
        friendsRllabelbutton = (RelativeLayout) view.findViewById(R.id.friends_rl_label_button);
        friendsRlnewfriendbutton = (RelativeLayout) view.findViewById(R.id.friends_rl_newfriend_button);
        friendsfragmentListview = (ListView) view.findViewById(R.id.friends_lv2);
        search = (EditText) view.findViewById(R.id.friends_search_et);
        this.activity = (BaseActivity) this.getActivity();
        mainView = (KeyboardLayout) view.findViewById(R.id.keyboardLayout_friends);
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
        friendsfragmentListview.setOnItemClickListener(this);
        friendsfragmentListview.setOnItemLongClickListener(this);
        friendsRladdbutton.setOnClickListener(this);
        friendsRllabelbutton.setOnClickListener(this);
        friendsRlnewfriendbutton.setOnClickListener(this);
        //加载好友列表
        LoadFriendsList();
        search.setOnFocusChangeListener(onFocusAutoClearHintListener);
        return view;
    }

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

    /**
     * 设置hint被选中消失,设置hint和显示文字的位置
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


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg0.getId()) {
            case R.id.friends_lv2:
                Intent intent = new Intent();
                intent.setClass(getActivity(), FriendDetailsActivity.class);
                intent.putExtra("position", arg2);
                // 传递集合数据时，需要将bean文件实现Serializable接口才能正常传递
                intent.putExtra("json", (Serializable) friendInfoRecords);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.friends_rl_add_button:
                Intent in = new Intent();
                in.setClass(getActivity(), NewFriendActivity.class);
                startActivity(in);
                break;
            case R.id.friends_rl_label_button:
                Intent inn = new Intent();
                inn.setClass(getActivity(), LabelActivity.class);
                startActivity(inn);
                break;
            case R.id.friends_rl_newfriend_button:
                friendsIvaddnewfriend.setVisibility(View.GONE);
                Intent ine = new Intent();
                ine.setClass(getActivity(), NewFriendsListActivity.class);
//                startActivity(ine);
                startActivityForResult(ine, FRIENDINVICODE);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        switch (arg0.getId()) {
            case R.id.friends_lv2:

                final int position = arg2;
                AlertDialog.Builder normalDia = new AlertDialog.Builder(
                        getActivity());
                normalDia.setIcon(R.drawable.ic_launcher);
                normalDia.setTitle("1111");
                normalDia.setMessage("是否删除当前选中项");

                normalDia.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除固定位置的好友
                                deleteFriends(position);
                            }
                        });
                normalDia.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                normalDia.create().show();
                break;

            default:
                break;
        }
        return false;
    }

    /**
     * 加载好友数据
     */
    private void LoadFriendsList() {
        friendInfoRecords = this.dbManager.getTableFriendInfo().queryFriendsInfo(UserServer.getInstance().getUserid());
        friendsAdapter = new FriendsAdapter(activity, friendInfoRecords, "0", v);
        friendsfragmentListview.setAdapter(friendsAdapter);
        setListViewHeightBasedOnChildren(friendsfragmentListview);
    }

    /**
     * 删除position位置的好友
     *
     * @param position
     */
    private void deleteFriends(final int position) {
        new APIUserServer.JsonDel(APIUrl.URL_DELETE_FRIENDS + friendInfoRecords.get(position).fuid, null, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject response = this.getResponse();
                JsonFriendTagReturn jr = new JsonFriendTagReturn();
                jr.JsonParsing(response);
                //返回成功时，执行删除操作
                if (jr.isAdd) {
                    dbManager.getTableFriendInfo().deleteFriendInfo(UserServer.getInstance().getUserid(), friendInfoRecords.get(position).fuid);
                    friendInfoRecords.remove(position);
                    friendsAdapter.notifyDataSetChanged();
                    // 因为之前listview已经固定高度，所以删除后，需重新定高
                    setListViewHeightBasedOnChildren(friendsfragmentListview);
                } else {
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, this.activity.getRequestQueue(), null).send();
    }

    /**
     * 推送来时，响应广播的接收器
     */
    private InvitationReceiver friendInvitationReceiver = new InvitationReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("Invitation", "action: " + action);
            String msg = intent.getExtras().getString(InvitationReceiver.ACTION_KEY_MSG);
            Log.v("Invitation", "msg: " + msg);
            int action_code = intent.getExtras().getInt(InvitationReceiver.ACTION_KEY_ACTION_CODE);
            Log.v("Invitation", "code: " + action_code);
            friendsIvaddnewfriend.setVisibility(View.VISIBLE);
        }
    };

}

