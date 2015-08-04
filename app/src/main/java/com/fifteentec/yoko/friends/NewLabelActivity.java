package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.app.Activity;
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

import com.fifteentec.Adapter.commonAdapter.NewLabelGvAdapter;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.R;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewLabelActivity extends Activity implements OnItemClickListener,
        OnClickListener {

    private KeyboardLayout mainView; // 判断软键盘是否隐藏
    private EditText search; // 输入框
    private GridView new_label_gv;
    private NewLabelGvAdapter nlgvadapter;
    private ArrayList<String> list = new ArrayList<String>();
    private Boolean isClickDelelte = false;
    private ArrayList<JsonParsing> jsonData = new ArrayList<JsonParsing>();
    private ArrayList<JsonParsing> jsonTrans = new ArrayList<JsonParsing>();
    public static int ADD_REQUEST = 1;
    public static int ADD_RESULT = 2;
    private TextView new_label_tv;
    public static String labelstr = Environment.getExternalStorageDirectory()
            + File.separator + "label" + File.separator + "json.txt";
    private String labaltrans;
    private Integer labelindex = -1;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_label);
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_label);
        search = (EditText) findViewById(R.id.new_label_et_search);
        new_label_gv = (GridView) findViewById(R.id.new_label_gv);
        new_label_tv = (TextView) findViewById(R.id.new_label_tv);

        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");
        labelindex = intent.getIntExtra("labelindex", -1);
        labaltrans = intent.getStringExtra("isLabelTrans");
        jsonData = (ArrayList<JsonParsing>) intent.getSerializableExtra("personlist");
        if (flag.equals("0")) {
            nlgvadapter = new NewLabelGvAdapter(this, jsonData, "0");
            new_label_gv.setAdapter(nlgvadapter);
        } else {
            jsonTrans = jsonData;
            nlgvadapter = new NewLabelGvAdapter(this, jsonTrans, "0");
            new_label_gv.setAdapter(nlgvadapter);
        }

        new_label_gv.setOnItemClickListener(this);

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
                // if (textView.getText().toString().equals("")) {
                // textView.setGravity(Gravity.CENTER);
                // } else {
                // textView.setGravity(Gravity.LEFT);
                // }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg0.getId()) {
            case R.id.new_label_gv:
                if (jsonTrans.size() == 0) {
                    if (arg2 == list.size()) {
                        Intent in = new Intent();
                        in.setClass(NewLabelActivity.this,
                                NewLabelGvAddActivity.class);

                        in.putExtra("jsonTransModified", (Serializable) jsonTrans);

                        startActivityForResult(in, ADD_REQUEST);

                    }
                } else {

                    if (arg2 < jsonTrans.size()) {
                        if (isClickDelelte) {
                            if (NewLabelGvAdapter.ivDeleteIsVisiable == 0) {
                                Intent intent = new Intent();
                                intent.setClass(NewLabelActivity.this,
                                        FriendDetailsActivity.class);
                                intent.putExtra("position", arg2);
                                // 传递集合数据时，需要将bean文件实现Serializable接口才能正常传递
                                intent.putExtra("json", (Serializable) jsonTrans);
                                startActivity(intent);
                            } else {
                                nlgvadapter = new NewLabelGvAdapter(this,
                                        jsonTrans, "0");
                                new_label_gv.setAdapter(nlgvadapter);
                                Toast.makeText(NewLabelActivity.this, "隐藏控件1",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(NewLabelActivity.this,
                                    FriendDetailsActivity.class);
                            intent.putExtra("position", arg2);
                            // 传递集合数据时，需要将bean文件实现Serializable接口才能正常传递
                            intent.putExtra("json", (Serializable) jsonTrans);
                            startActivity(intent);
                        }

                    } else if (arg2 == jsonTrans.size()) {
                        if (isClickDelelte) {
                            if (NewLabelGvAdapter.ivDeleteIsVisiable == 0) {
                                Intent in = new Intent();
                                in.setClass(NewLabelActivity.this,
                                        NewLabelGvAddActivity.class);
                                in.putExtra("jsonTransModified",
                                        (Serializable) jsonTrans);

                                startActivityForResult(in, ADD_REQUEST);
                            } else {
                                nlgvadapter = new NewLabelGvAdapter(this,
                                        jsonTrans, "0");
                                new_label_gv.setAdapter(nlgvadapter);
                                Toast.makeText(NewLabelActivity.this, "隐藏控件2",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent in = new Intent();
                            in.setClass(NewLabelActivity.this,
                                    NewLabelGvAddActivity.class);

                            in.putExtra("jsonTransModified",
                                    (Serializable) jsonTrans);

                            startActivityForResult(in, ADD_REQUEST);
                        }
                    } else if (arg2 == jsonTrans.size() + 1) {
                        isClickDelelte = true;
                        Toast.makeText(NewLabelActivity.this, "3",
                                Toast.LENGTH_SHORT).show();

                        if (isClickDelelte) {
                            if (NewLabelGvAdapter.ivDeleteIsVisiable == 0) {
                                Toast.makeText(NewLabelActivity.this, "显示控件3",
                                        Toast.LENGTH_SHORT).show();
                                nlgvadapter = new NewLabelGvAdapter(this,
                                        jsonTrans, "1");
                                new_label_gv.setAdapter(nlgvadapter);
                            } else {
                                nlgvadapter = new NewLabelGvAdapter(this,
                                        jsonTrans, "0");
                                new_label_gv.setAdapter(nlgvadapter);
                                Toast.makeText(NewLabelActivity.this, "隐藏控件3",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NewLabelActivity.this, "显示控件3",
                                    Toast.LENGTH_SHORT).show();
                            nlgvadapter = new NewLabelGvAdapter(this, jsonTrans,
                                    "1");
                            new_label_gv.setAdapter(nlgvadapter);
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
                JsonParsing jp = new JsonParsing();
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

            jsonTrans = new ArrayList<JsonParsing>();

            jsonTrans = (ArrayList<JsonParsing>) data
                    .getSerializableExtra("jsonTrans");
            nlgvadapter = new NewLabelGvAdapter(this, jsonTrans, "0");
            new_label_gv.setAdapter(nlgvadapter);
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.new_label_tv:
                if (!search.getText().toString().equals("")) {

                    File file = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "label" + File.separator
                            + "json.txt");// 要输出的文件路径
                    if (!file.getParentFile().exists()) {// 文件不存在
                        file.getParentFile().mkdirs();// 创建文件夹
                    }
                    String json = "";
                    try {
                        json = readSDFile(labelstr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (json.toString().equals("")) {
                        // 直接写入数据，是第一次
                        if (jsonTrans.size() == 0) {
                            JSONObject allData = new JSONObject();// 建立最外面的节点对象
                            JSONArray sing = new JSONArray();// 定义数组
                            JSONArray all = new JSONArray();
                            for (int x = 0; x < 1; x++) {// 将数组内容配置到相应的节点
                                JSONObject temp = new JSONObject();// JSONObject包装数据,而JSONArray包含多个JSONObject
                                try {
                                    // temp.put("labelName",
                                    // search.getText().toString()); //
                                    // JSONObject是按照key:value形式保存
                                    temp.put("name", "");
                                    temp.put("id", "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sing.put(temp);// 保存多个JSONObject
                            }
                            try {
                                for (int i = 0; i < 1; i++) {
                                    allData.put("labelnameAndId", sing);// 把JSONArray用最外面JSONObject包装起来
                                    allData.put("label", search.getText()
                                            .toString());
                                    all.put(allData);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrintWriter out = null;
                            try {
                                out = new PrintWriter(new FileOutputStream(file));
                                out.print(all.toString());// 将数据变为字符串后保存
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } finally {
                                if (out != null) {
                                    out.close();// 关闭输出
                                }
                            }

                        } else {
                            JSONObject allData = new JSONObject();// 建立最外面的节点对象
                            JSONArray sing = new JSONArray();// 定义数组
                            JSONArray all = new JSONArray();
                            for (int x = 0; x < jsonTrans.size(); x++) {// 将数组内容配置到相应的节点
                                JSONObject temp = new JSONObject();// JSONObject包装数据,而JSONArray包含多个JSONObject
                                try {
                                    // temp.put("labelName",
                                    // search.getText().toString()); //
                                    // JSONObject是按照key:value形式保存
//                                    temp.put("name", jsonTrans.get(x).name);
                                    temp.put("id", jsonTrans.get(x).id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                sing.put(temp);// 保存多个JSONObject
                            }
                            try {
                                for (int i = 0; i < 1; i++) {
                                    allData.put("labelnameAndId", sing);// 把JSONArray用最外面JSONObject包装起来
                                    allData.put("label", search.getText()
                                            .toString());
                                    all.put(allData);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            PrintWriter out = null;
                            try {
                                out = new PrintWriter(new FileOutputStream(file));
                                out.print(all.toString());// 将数据变为字符串后保存
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } finally {
                                if (out != null) {
                                    out.close();// 关闭输出
                                }
                            }
                        }

                    } else {
                        String jsonstra = "";
                        ArrayList<JsonParsing> listNameid = new ArrayList<JsonParsing>();
                        ArrayList<String> label = new ArrayList<String>();
                        Map<String, List<JsonParsing>> labellist = new HashMap<String, List<JsonParsing>>();
                        // 先读出数据，再整合，再写入输入
                        // 如果file中有数据，说明是第二次新建，所以需要先读取，再添加
                        if (labaltrans.equals("labalnews")) {
                            try {
                                jsonstra = readSDFile(labelstr);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray jsonArray = new JSONArray(jsonstra);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjs = (JSONObject) jsonArray
                                            .opt(i);
                                    // 自定义json的bean文件

                                    label.add(jsonObjs.optString("label"));

                                    JSONArray jsonArraynameandid = jsonObjs
                                            .getJSONArray("labelnameAndId");

                                    for (int j = 0; j < jsonArraynameandid.length(); j++) {

                                        JSONObject jsonObjs1 = (JSONObject) jsonArraynameandid
                                                .opt(j);

                                        JsonParsing jp = new JsonParsing();
                                        try {
                                            jp.parsingJson(jsonObjs1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        listNameid.add(jp);
                                    }

                                    labellist.put(jsonObjs.optString("label"),
                                            listNameid);
                                    listNameid = new ArrayList<JsonParsing>();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (jsonTrans.size() == 0) {
                                listNameid = new ArrayList<JsonParsing>();
                                JsonParsing jps = new JsonParsing();
//                                jps.id = "";
//                                jps.name = "";
                                listNameid.add(jps);
                                label.add(search.getText().toString());

                                labellist.put(search.getText().toString(),
                                        listNameid);

                            } else {
                                listNameid = new ArrayList<JsonParsing>();
                                for (int i = 0; i < jsonTrans.size(); i++) {
                                    JsonParsing jps = new JsonParsing();
                                    jps.id = jsonTrans.get(i).id;
//                                    jps.name = jsonTrans.get(i).name;
                                    listNameid.add(jps);
                                }
                                Log.e("123", listNameid + "");
                                label.add(search.getText().toString());
                                labellist.put(search.getText().toString(),
                                        listNameid);
                                Log.e("123", labellist + "");
                                Log.e("123", labellist + "");
                            }

                            JSONObject allData = new JSONObject();// 建立最外面的节点对象
                            JSONArray all = new JSONArray();
                            JSONArray sing = new JSONArray();// 定义数组
                            List<JsonParsing> jsonLabelListData = new ArrayList<JsonParsing>();

                            try {
                                for (int i = 0; i < label.size(); i++) {
                                    jsonLabelListData = labellist.get(label.get(i));
                                    for (int x = 0; x < jsonLabelListData.size(); x++) {// 将数组内容配置到相应的节点
                                        JSONObject temp = new JSONObject();// JSONObject包装数据,而JSONArray包含多个JSONObject
                                        try {
                                            // temp.put("labelName",
                                            // search.getText().toString()); //
                                            // JSONObject是按照key:value形式保存

//                                            temp.put("name",
//                                                    jsonLabelListData.get(x).name);
                                            temp.put("id",
                                                    jsonLabelListData.get(x).id);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        sing.put(temp);// 保存多个JSONObject
                                    }

                                    allData.put("labelnameAndId", sing);// 把JSONArray用最外面JSONObject包装起来
                                    allData.put("label", label.get(i));
                                    all.put(allData);
                                    allData = new JSONObject();
                                    sing = new JSONArray();// 定义数组
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            PrintWriter out = null;
                            try {
                                out = new PrintWriter(new FileOutputStream(file));
                                out.print(all.toString());// 将数据变为字符串后保存
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } finally {
                                if (out != null) {
                                    out.close();// 关闭输出
                                }
                            }

                        }
                        // 这是修改数据，需要先传入定位的数据，然后在修改这条数据，并保存在file中

                        else {
                            try {
                                jsonstra = readSDFile(labelstr);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray jsonArray = new JSONArray(jsonstra);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjs = (JSONObject) jsonArray
                                            .opt(i);
                                    // 自定义json的bean文件

                                    label.add(jsonObjs.optString("label"));

                                    JSONArray jsonArraynameandid = jsonObjs
                                            .getJSONArray("labelnameAndId");

                                    for (int j = 0; j < jsonArraynameandid.length(); j++) {

                                        JSONObject jsonObjs1 = (JSONObject) jsonArraynameandid
                                                .opt(j);

                                        JsonParsing jp = new JsonParsing();
                                        try {
                                            jp.parsingJson(jsonObjs1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        listNameid.add(jp);
                                    }

                                    labellist.put(jsonObjs.optString("label"),
                                            listNameid);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            Set keyset = labellist.keySet();
//                            for (Object labename : keyset) {
//                                if (label.get(labelindex).equals(labename)) {
//                                    Toast.makeText(NewLabelActivity.this,"123",Toast.LENGTH_SHORT).show();
//
//                                }
//                            }


//                            for (int i = 0; i < listNameid.size(); i++) {
//                                if (label.get(labelindex).equals(labellist.get(labelname).get(i).name)) {
//
//                                }
//                            }
//                            for (int i = 0; i < ; i++) {
//
//                            }


                        }

                    }
                    finish();
                } else {
                    Toast.makeText(NewLabelActivity.this, "请填写一个标签名~",
                            Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }
}
