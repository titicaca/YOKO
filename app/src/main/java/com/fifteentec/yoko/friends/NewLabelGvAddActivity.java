package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Database.DBManager;
import com.Database.FriendInfoRecord;
import com.Database.FriendTagRecord;
import com.fifteentec.Adapter.commonAdapter.NewLabelGvAddAdapter;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewLabelGvAddActivity extends BaseActivity implements OnClickListener {

    //标签添加朋友的选择列表
    private ListView lvNewLabelGvAdd;
    // 判断软键盘是否隐藏
    private KeyboardLayout mainView;
    // 输入框  暂未使用
    private EditText search;
    //选择列表的适配器
    private NewLabelGvAddAdapter newLabelGvAddAdapter;
    //标签添加朋友的选择列表的好友数据
    private ArrayList<JsonFriendList> jsonData = new ArrayList<JsonFriendList>();
    //用来传输的数据，随后将其返回到newlabel的页面
    private ArrayList<JsonFriendList> jsonTrans = new ArrayList<JsonFriendList>();
    //从newlabel传入的好友数据，用来比对是否需要选中用的
    private ArrayList<JsonFriendList> jsonTransModified = new ArrayList<JsonFriendList>();
    //确定添加的按钮
    private TextView newLabelGvaddTvSure;
    private BaseActivity activity;
    private DBManager dbManager;
    //添加view的viewgroup，用来控制宽度，不够时会换行到控件高度的下一行
    private com.fifteentec.Component.calendar.PredicateLayout newLabelGvaddPopLinearlayout;
    //getTableFriendTag list数据
    private List<FriendTagRecord> listTag;
    //用来判断输入框pop的view只添加一次数据
    private boolean isAddView = true;
    //用来显示列表的好友数据
    private ArrayList<JsonFriendList> listData = new ArrayList<JsonFriendList>();
    //用来接收newlabe页面传来的listtrans，用来返回到newlabel来显示好友组
    private List<FriendTagRecord> listTrans;
    //修改标签内容时，本地查询的好友数据list
    private List<FriendInfoRecord> friendInfoRecords;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_label_gvadd);
        this.activity = (BaseActivity) this;
        this.dbManager = this.activity.getDBManager();
        listTag = this.dbManager.getTableFriendTag().queryTag(UserServer.getInstance().getUserid());
        newLabelGvaddPopLinearlayout = (com.fifteentec.Component.calendar.PredicateLayout) findViewById(R.id.new_label_gvadd_pop_linearlayout);
        lvNewLabelGvAdd = (ListView) findViewById(R.id.new_label_gvadd_lv);
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_label_gvadd);
        search = (EditText) findViewById(R.id.new_label_gvadd_et_search);
        newLabelGvaddTvSure = (TextView) findViewById(R.id.new_label_gvadd_tv_sure);
        Intent in = getIntent();

        jsonTransModified = (ArrayList<JsonFriendList>) in
                .getSerializableExtra("jsonTransModified");

        if (jsonTransModified.size() != 0) {
            jsonTrans = jsonTransModified;
        }

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
        //   getTableFriendInfo    getTableFriendInfo
        friendInfoRecords = this.dbManager.getTableFriendInfo().queryFriendsInfo(UserServer.getInstance().getUserid());
        newLabelGvAddAdapter = new NewLabelGvAddAdapter(this, friendInfoRecords, jsonTrans,
                jsonTransModified);
        lvNewLabelGvAdd.setAdapter(newLabelGvAddAdapter);
        newLabelGvaddTvSure.setOnClickListener(this);
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
                if (isAddView) {
                    //动态添加label中的list数据，并且设置了5种背景色
                    for (int i = 0; i < listTag.size(); i++) {
                        TextView tv = new TextView(NewLabelGvAddActivity.this);
                        tv.setTag(i);
                        tv.setTextColor(Color.WHITE);
                        tv.setText(listTag.get(i).tagName);
                        tv.setPadding(20, 5, 20, 5);
                        int colorCount = i % 5;
                        switch (colorCount) {
                            case 0:
                                tv.setBackgroundResource(R.drawable.search_label_background_color1);
                                break;
                            case 1:
                                tv.setBackgroundResource(R.drawable.search_label_background_color2);
                                break;
                            case 2:
                                tv.setBackgroundResource(R.drawable.search_label_background_color3);
                                break;
                            case 3:
                                tv.setBackgroundResource(R.drawable.search_label_background_color4);
                                break;
                            case 4:
                                tv.setBackgroundResource(R.drawable.search_label_background_color5);
                                break;
                        }

                        newLabelGvaddPopLinearlayout.addView(tv);
                        tv.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(NewLabelGvAddActivity.this, v.getTag() + "", Toast.LENGTH_SHORT).show();
                                long id = listTag.get((Integer) v.getTag()).tagId;
                                listData = new ArrayList<JsonFriendList>();
                                //查询本地数据，并且添加到list中
                                listTrans = dbManager.getTableFriendTag().queryFriendsByTag(UserServer.getInstance().getUserid(), id);
                                for (FriendTagRecord friendTagRecord : listTrans) {
                                    JsonFriendList item = new JsonFriendList();
                                    item.id = friendTagRecord.fuid;
                                    FriendInfoRecord friendInfoRecord = dbManager.getTableFriendInfo().queryFriendInfo(UserServer.getInstance().getUserid(), item.id);
                                    item.nickname = friendInfoRecord.nickname;
                                    item.picturelink = friendInfoRecord.picturelink;
                                    listData.add(item);
                                }
                                jsonTransModified = listData;
                                jsonTrans = listData;
                                newLabelGvAddAdapter = new NewLabelGvAddAdapter(NewLabelGvAddActivity.this, friendInfoRecords, jsonTrans,
                                        jsonTransModified);
                                lvNewLabelGvAdd.setAdapter(newLabelGvAddAdapter);
                                //点击pop中的view时，将整个popview都隐藏
                                newLabelGvaddPopLinearlayout.setVisibility(View.GONE);
                            }
                        });
                    }
                    isAddView = false;
                }
                newLabelGvaddPopLinearlayout.setVisibility(View.VISIBLE);
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
                newLabelGvaddPopLinearlayout.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.new_label_gvadd_tv_sure:
                Intent intent = new Intent();
                intent.setClass(NewLabelGvAddActivity.this, NewLabelActivity.class);
                // 传递集合数据时，需要将bean文件实现Serializable接口才能正常传递
                intent.putExtra("jsonTrans", (Serializable) jsonTrans);
                intent.putExtra("flag", "1");
                setResult(NewLabelActivity.ADD_RESULT, intent);
                finish();
                break;

            default:
                break;
        }
    }
}
