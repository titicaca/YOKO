package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIKey;
import com.API.APIServer;
import com.API.APIUrl;
import com.Database.DBManager;
import com.Database.FriendInfoRecord;
import com.Database.FriendTagRecord;
import com.fifteentec.Adapter.commonAdapter.NewLabelGvAdapter;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.Component.Parser.JsonFriendTagReturn;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Component.calendar.KeyboardLayout;
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

public class NewLabelActivity extends BaseActivity implements OnItemClickListener,
        OnClickListener {
    // 判断软键盘是否隐藏
    private KeyboardLayout mainView;
    // 标签名输入框
    private EditText editNewLabel;
    //好友列表gridview和添加删除好友按钮
    private GridView newLabelGv;
    //好友列表的适配器
    private NewLabelGvAdapter newLabelGvAdapter;
    private Boolean isClickDelelte = false;
    private ArrayList<JsonFriendList> jsonData = new ArrayList<JsonFriendList>();
    private List<FriendTagRecord> jsonTrans;
    private long id;

    public static int ADD_REQUEST = 1;
    public static int ADD_RESULT = 2;
    private TextView new_label_tv;
    public static String labelstr = Environment.getExternalStorageDirectory()
            + File.separator + "label" + File.separator + "json.txt";
    private String labaltrans;
    private Integer labelindex = -1;
    private BaseActivity activity;
    private DBManager dbManager;
    List<Long> list_id = new ArrayList<Long>();
    private String flag;
    private EditText new_label_et_search;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_label);
        this.dbManager = this.getDBManager();
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_label);
        editNewLabel = (EditText) findViewById(R.id.new_label_et_search);
        newLabelGv = (GridView) findViewById(R.id.new_label_gv);
        new_label_tv = (TextView) findViewById(R.id.new_label_tv);
        new_label_et_search = (EditText) findViewById(R.id.new_label_et_search);

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        labelindex = intent.getIntExtra("labelindex", -1);
        labaltrans = intent.getStringExtra("isLabelTrans");
        if (flag.equals("0")) {
            newLabelGvAdapter = new NewLabelGvAdapter(this, jsonData, "0");
            newLabelGv.setAdapter(newLabelGvAdapter);
        } else {
            id = intent.getLongExtra("tagId", 0);
            String tagName = dbManager.getTableFriendTag().queryTagName(UserServer.getInstance().getUserid(), id);
            new_label_et_search.setText(tagName);
            jsonData = new ArrayList<JsonFriendList>();
            jsonTrans = dbManager.getTableFriendTag().queryFriendsByTag(UserServer.getInstance().getUserid(), id);
            for (FriendTagRecord friendTagRecord : jsonTrans) {
                JsonFriendList item = new JsonFriendList();
                item.id = friendTagRecord.fuid;
                FriendInfoRecord friendInfoRecord = dbManager.getTableFriendInfo().queryFriendInfo(UserServer.getInstance().getUserid(), item.id);
                item.nickname = friendInfoRecord.nickname;
                item.picturelink = friendInfoRecord.picturelink;
                jsonData.add(item);
            }
            newLabelGvAdapter = new NewLabelGvAdapter(this, jsonData, "0");
            newLabelGv.setAdapter(newLabelGvAdapter);
        }

        newLabelGv.setOnItemClickListener(this);

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
        editNewLabel.setOnFocusChangeListener(onFocusAutoClearHintListener);
        new_label_tv.setOnClickListener(this);
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
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg0.getId()) {
            case R.id.new_label_gv:
                if (jsonData.size() == 0) {
                    if (arg2 == 0) {
                        Intent in = new Intent();
                        in.setClass(NewLabelActivity.this,
                                NewLabelGvAddActivity.class);

                        in.putExtra("jsonTransModified", (Serializable) jsonData);

                        startActivityForResult(in, ADD_REQUEST);

                    }
                } else {

                    if (arg2 < jsonData.size()) {
                        if (isClickDelelte) {
                            if (NewLabelGvAdapter.ivDeleteIsVisiable == 0) {
                                Intent intent = new Intent();
                                intent.setClass(NewLabelActivity.this,
                                        FriendDetailsActivity.class);
                                intent.putExtra("position", arg2);
                                // 传递集合数据时，需要将bean文件实现Serializable接口才能正常传递
                                intent.putExtra("json", (Serializable) jsonData);
                                startActivity(intent);
                            } else {
                                newLabelGvAdapter = new NewLabelGvAdapter(this,
                                        jsonData, "0");
                                newLabelGv.setAdapter(newLabelGvAdapter);
                                Toast.makeText(NewLabelActivity.this, "隐藏控件1",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(NewLabelActivity.this,
                                    FriendDetailsActivity.class);
                            intent.putExtra("position", arg2);
                            // 传递集合数据时，需要将bean文件实现Serializable接口才能正常传递
                            intent.putExtra("json", (Serializable) jsonData);
                            startActivity(intent);
                        }

                    } else if (arg2 == jsonData.size()) {
                        if (isClickDelelte) {
                            if (NewLabelGvAdapter.ivDeleteIsVisiable == 0) {
                                Intent in = new Intent();
                                in.setClass(NewLabelActivity.this,
                                        NewLabelGvAddActivity.class);
                                in.putExtra("jsonTransModified",
                                        (Serializable) jsonData);
                                startActivityForResult(in, ADD_REQUEST);
                            } else {
                                newLabelGvAdapter = new NewLabelGvAdapter(this,
                                        jsonData, "0");
                                newLabelGv.setAdapter(newLabelGvAdapter);
                                Toast.makeText(NewLabelActivity.this, "隐藏控件2",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent in = new Intent();
                            in.setClass(NewLabelActivity.this,
                                    NewLabelGvAddActivity.class);

                            in.putExtra("jsonTransModified",
                                    (Serializable) jsonData);

                            startActivityForResult(in, ADD_REQUEST);
                        }
                    } else if (arg2 == jsonData.size() + 1) {
                        isClickDelelte = true;
                        Toast.makeText(NewLabelActivity.this, "3",
                                Toast.LENGTH_SHORT).show();

                        if (isClickDelelte) {
                            if (NewLabelGvAdapter.ivDeleteIsVisiable == 0) {
                                Toast.makeText(NewLabelActivity.this, "显示控件3",
                                        Toast.LENGTH_SHORT).show();
                                newLabelGvAdapter = new NewLabelGvAdapter(this,
                                        jsonData, "1");
                                newLabelGv.setAdapter(newLabelGvAdapter);
                            } else {
                                newLabelGvAdapter = new NewLabelGvAdapter(this,
                                        jsonData, "0");
                                newLabelGv.setAdapter(newLabelGvAdapter);
                                Toast.makeText(NewLabelActivity.this, "隐藏控件3",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NewLabelActivity.this, "显示控件3",
                                    Toast.LENGTH_SHORT).show();
                            newLabelGvAdapter = new NewLabelGvAdapter(this, jsonData,
                                    "1");
                            newLabelGv.setAdapter(newLabelGvAdapter);
                        }

                    }
                }
                break;

            default:
                break;
        }
    }

    private void initdatas() {

        String str = Environment.getExternalStorageDirectory() + File.separator
                + "mldndata" + File.separator + "json.txt";
        String json = "";
        try {
            json = readSDFile(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("json", json);
        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray("urldata");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjs = (JSONObject) jsonArray.opt(i);
                // 自定义json的bean文件
                JsonFriendList jp = new JsonFriendList();
                try {
                    jp.parsingJson(jsonObjs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonData.add(jp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST && resultCode == ADD_RESULT) {

            //以后可删掉的循环，测试接口用


            jsonData = (ArrayList<JsonFriendList>) data
                    .getSerializableExtra("jsonTrans");


            newLabelGvAdapter = new NewLabelGvAdapter(this, jsonData, "0");
            newLabelGv.setAdapter(newLabelGvAdapter);
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.new_label_tv:
                if (!editNewLabel.getText().toString().equals("")) {
                    try {
                        PostTagInfor();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(NewLabelActivity.this, "请填写一个标签名~",
                            Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }

    private void PostTagInfor() throws JSONException {
        Map<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String tagName = editNewLabel.getText().toString();
        JSONObject jsono = new JSONObject();
        final JSONArray jsonarray = new JSONArray();
        for (int i = 0; i < jsonData.size(); i++) {
            JSONObject ob = new JSONObject();
            try {
                ob.put("id", jsonData.get(i).id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonarray.put(ob);
        }
        jsono.put("friendlist", jsonarray);
        jsono.put("tagname", tagName);

        if (flag.equals("0")) {

            new APIServer.JsonPost(APIUrl.URL_MYTAG, jsono, headers, new APIJsonCallbackResponse() {
                @Override
                public void run() {
                    JSONObject response = this.getResponse();
                    JsonFriendTagReturn jr = new JsonFriendTagReturn();
                    jr.JsonParsing(response);
                    if (jr.isAdd) {
                        //需给本地数据库
                        list_id = new ArrayList<Long>();

                        for (int i = 0; i < jsonData.size(); i++) {
                            list_id.add(jsonData.get(i).id);
                        }
                        dbManager.getTableFriendTag().addTag(UserServer.getInstance().getUserid(), jr.id, tagName, list_id);
                        finish();
                    } else {
                        Toast.makeText(NewLabelActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }, this.getRequestQueue(), null).send();
        } else {
            new APIServer.JsonPut(APIUrl.URL_MYTAG_UPDATE + id + APIUrl.URL_MYTAG_UPDATE_END, jsono, headers, new APIJsonCallbackResponse() {
                @Override
                public void run() {
                    JSONObject response = this.getResponse();
                    JsonFriendTagReturn jr = new JsonFriendTagReturn();
                    jr.JsonParsing(response);
                    if (jr.isAdd) {
                        //需给本地数据库
                        List<FriendTagRecord> friendTagRecords = new ArrayList<FriendTagRecord>();
                        for (int i = 0; i < jsonData.size(); i++) {
                            FriendTagRecord ft = new FriendTagRecord();
                            ft.fuid = jsonData.get(i).id;
                            ft.tagId = jr.id;
                            friendTagRecords.add(ft);
                        }
                        dbManager.getTableFriendTag().updateTagName(UserServer.getInstance().getUserid(), jr.id, tagName);
                        dbManager.getTableFriendTag().updateFriendsInTag(UserServer.getInstance().getUserid(), jr.id, friendTagRecords);
                        finish();
                    } else {
                        Toast.makeText(NewLabelActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }, this.getRequestQueue(), null).send();
        }
    }
}

