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

import com.Database.DBManager;
import com.Database.FriendTagRecord;
import com.fifteentec.Adapter.commonAdapter.LabelItemAdapter;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabelActivity extends BaseActivity implements OnItemLongClickListener,
        OnClickListener, OnItemClickListener {

    private ListView lv;
    private LabelItemAdapter liadapter;
    private TextView label_create;
    public static int LABEL_REQUEST = 3;
    private String json = "";
    ArrayList<JsonFriendList> listNameid = new ArrayList<JsonFriendList>();
    ArrayList<String> label = new ArrayList<String>();
    Map<String, List<JsonFriendList>> labellist = new HashMap<String, List<JsonFriendList>>();
    private BaseActivity activity;
    private DBManager dbManager;
    private List<FriendTagRecord> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label);
        this.activity = (BaseActivity) this;
        this.dbManager = this.activity.getDBManager();
        lv = (ListView) findViewById(R.id.label_lv);
        label_create = (TextView) findViewById(R.id.label_create);

        list = this.dbManager.getTableFriendTag().queryTag(0);

        liadapter = new LabelItemAdapter(this, list);
        lv.setAdapter(liadapter);
        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);
        label_create.setOnClickListener(this);


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
                                list.remove(position);
                                liadapter.notifyDataSetChanged();
                            }
                        });
                normalDia.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showClickMessage("取消");
                            }
                        });
                normalDia.create().show();
                break;

            default:
                break;
        }
        return false;
    }

    public void addlist() {
        label = new ArrayList<String>();
        try {
            json = readSDFile(NewLabelActivity.labelstr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjs = (JSONObject) jsonArray.opt(i);
                // 自定义json的bean文件

                label.add(jsonObjs.optString("label"));

                JSONArray jsonArraynameandid = jsonObjs
                        .getJSONArray("labelnameAndId");

                for (int j = 0; j < jsonArraynameandid.length(); j++) {

                    JSONObject jsonObjs1 = (JSONObject) jsonArraynameandid
                            .opt(j);

                    JsonFriendList jp = new JsonFriendList();
                    try {
                        jp.parsingJson(jsonObjs1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listNameid.add(jp);
                }

                labellist.put(jsonObjs.optString("label"), listNameid);
                listNameid = new ArrayList<JsonFriendList>();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /* 显示点击的内容 */
    private void showClickMessage(String message) {
        Toast.makeText(LabelActivity.this, "你选择的是: " + message,
                Toast.LENGTH_SHORT).show();
    }

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
        in.putExtra("personlist", (Serializable) labellist.get(label.get(arg2)));
        in.putExtra("isLabelTrans", "labaltrans");
        in.putExtra("tagId", list.get(arg2).tagId);
        startActivityForResult(in, LABEL_REQUEST);
    }

    // 读文件
    public String readSDFile(String fileName) throws IOException {

        String res = "";

        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file);

        int length = fis.available();

        byte[] buffer = new byte[length];
        fis.read(buffer);

        res = EncodingUtils.getString(buffer, "UTF-8");

        fis.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        list = dbManager.getTableFriendTag().queryTag(0);
        liadapter = new LabelItemAdapter(this, list);
        lv.setAdapter(liadapter);
    }
}
