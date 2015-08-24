package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIUrl;
import com.API.APIUserServer;
import com.Database.DBManager;
import com.Database.FriendTagRecord;
import com.fifteentec.Adapter.commonAdapter.LabelItemAdapter;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.Component.Parser.JsonFriendTagReturn;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LabelActivity extends BaseActivity implements OnItemLongClickListener,
        OnClickListener, OnItemClickListener {

    //标签列表listview
    private ListView lv;
    //标签列表适配器
    private LabelItemAdapter liadapter;
    //新建标签的按钮
    private TextView labelCreate;
    //新建标签请求码
    public static int LABEL_REQUEST = 3;
    private BaseActivity activity;
    private DBManager dbManager;
    //getTableFriendTag
    private List<FriendTagRecord> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label);
        this.activity = (BaseActivity) this;
        this.dbManager = this.activity.getDBManager();
        lv = (ListView) findViewById(R.id.label_lv);
        labelCreate = (TextView) findViewById(R.id.label_create);
        list = this.dbManager.getTableFriendTag().queryTag(UserServer.getInstance().getUserid());
        liadapter = new LabelItemAdapter(this, list);
        lv.setAdapter(liadapter);
        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);
        labelCreate.setOnClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        switch (arg0.getId()) {
            case R.id.label_lv:
                final int position = arg2;
                AlertDialog.Builder normalDia = new AlertDialog.Builder(
                        LabelActivity.this);
                normalDia.setIcon(R.drawable.ic_launcher);
                normalDia.setTitle("1111");
                normalDia.setMessage("是否删除当前选中项");

                normalDia.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // showClickMessage("确定");
                                deleteTagList(position);
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


    /* 显示点击的内容 */
    private void showClickMessage(String message) {
        Toast.makeText(LabelActivity.this, "你选择的是: " + message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 单击事件
     *
     * @param arg0
     */
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.label_create:
                Intent in = new Intent();
                in.setClass(LabelActivity.this, NewLabelActivity.class);
                in.putExtra("flag", "0");
                // startActivity(in);
                in.putExtra("isLabelTrans", "labalnews");
                in.putExtra("labelindex", -1);
                in.putExtra("personlist", (Serializable) new ArrayList<JsonFriendList>());
                startActivityForResult(in, LABEL_REQUEST);

                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent in = new Intent();
        in.setClass(LabelActivity.this, NewLabelActivity.class);
        in.putExtra("flag", "3");
        in.putExtra("labelindex", arg2);
        // startActivity(in);
//        in.putExtra("personlist", (Serializable) labellist.get(label.get(arg2)));
        in.putExtra("isLabelTrans", "labaltrans");
        in.putExtra("tagId", list.get(arg2).tagId);
        startActivityForResult(in, LABEL_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //getTableFriendTag
        list = dbManager.getTableFriendTag().queryTag(UserServer.getInstance().getUserid());
        //加载适配器
        liadapter = new LabelItemAdapter(this, list);
        lv.setAdapter(liadapter);
    }

    private void deleteTagList(final int position) {
        new APIUserServer.JsonDel(APIUrl.URL_DELETE_TAG + list.get(position).tagId, null, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject response = this.getResponse();
                JsonFriendTagReturn jr = new JsonFriendTagReturn();
                jr.JsonParsing(response);
                //判断是否成功响应
                if (jr.isAdd) {
                    dbManager.getTableFriendTag().deleteTag(UserServer.getInstance().getUserid(), list.get(position).tagId);
                    list.remove(position);
                    liadapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LabelActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, this.getRequestQueue(), null).send();
    }
}
