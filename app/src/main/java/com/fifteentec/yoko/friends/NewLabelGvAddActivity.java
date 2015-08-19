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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Database.DBManager;
import com.Database.FriendInfoRecord;
import com.Database.FriendTagRecord;
import com.fifteentec.Adapter.commonAdapter.NewLabelGvAddAdapter;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewLabelGvAddActivity extends BaseActivity implements OnClickListener {

    private ListView lv;
    private KeyboardLayout mainView; // 判断软键盘是否隐藏
    private EditText search; // 输入框
    private NewLabelGvAddAdapter nlgaadapter;
    // private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<JsonFriendList> jsonData = new ArrayList<JsonFriendList>();
    private ArrayList<JsonFriendList> jsonTrans = new ArrayList<JsonFriendList>();
    private ArrayList<JsonFriendList> jsonTransModified = new ArrayList<JsonFriendList>();
    private TextView new_label_gvadd_tv_sure;
    private BaseActivity activity;
    private DBManager dbManager;
    private com.fifteentec.Component.calendar.PredicateLayout new_label_gvadd_pop_linearlayout;
    private List<FriendTagRecord> listTag;
    private boolean isAddView = true;
    private ArrayList<JsonFriendList> listData = new ArrayList<JsonFriendList>();
    private List<FriendTagRecord> listTrans;
    private List<FriendInfoRecord> friendInfoRecords;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_label_gvadd);
        this.activity = (BaseActivity) this;
        this.dbManager = this.activity.getDBManager();

        listTag = this.dbManager.getTableFriendTag().queryTag(0);


        new_label_gvadd_pop_linearlayout = (com.fifteentec.Component.calendar.PredicateLayout) findViewById(R.id.new_label_gvadd_pop_linearlayout);
        lv = (ListView) findViewById(R.id.new_label_gvadd_lv);
        mainView = (KeyboardLayout) findViewById(R.id.keyboardLayout_new_label_gvadd);
        search = (EditText) findViewById(R.id.new_label_gvadd_et_search);
        new_label_gvadd_tv_sure = (TextView) findViewById(R.id.new_label_gvadd_tv_sure);
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
        friendInfoRecords = this.dbManager.getTableFriendInfo().queryFriendsInfo(0);
        nlgaadapter = new NewLabelGvAddAdapter(this, friendInfoRecords, jsonTrans,
                jsonTransModified);
        lv.setAdapter(nlgaadapter);
        // setListViewHeightBasedOnChildren(lv);
        new_label_gvadd_tv_sure.setOnClickListener(this);
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

                        new_label_gvadd_pop_linearlayout.addView(tv);
                        tv.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(NewLabelGvAddActivity.this, v.getTag() + "", Toast.LENGTH_SHORT).show();
                                long id = listTag.get((Integer) v.getTag()).tagId;
                                listData = new ArrayList<JsonFriendList>();
                                listTrans = dbManager.getTableFriendTag().queryFriendsByTag(0, id);
                                for (FriendTagRecord friendTagRecord : listTrans) {
                                    JsonFriendList item = new JsonFriendList();
                                    item.id = friendTagRecord.fuid;
                                    FriendInfoRecord friendInfoRecord = dbManager.getTableFriendInfo().queryFriendInfo(0, item.id);
                                    item.nickname = friendInfoRecord.nickname;
                                    item.picturelink = friendInfoRecord.picturelink;
                                    listData.add(item);
                                }
                                jsonTransModified = listData;
                                jsonTrans = listData;
                                nlgaadapter = new NewLabelGvAddAdapter(NewLabelGvAddActivity.this, friendInfoRecords, jsonTrans,
                                        jsonTransModified);
                                lv.setAdapter(nlgaadapter);
                                new_label_gvadd_pop_linearlayout.setVisibility(View.GONE);
                            }
                        });
                    }
                    isAddView = false;
                }
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                new_label_gvadd_pop_linearlayout.measure(w, h);
                int height = new_label_gvadd_pop_linearlayout.getMeasuredHeight();
                int width = new_label_gvadd_pop_linearlayout.getMeasuredWidth();
                new_label_gvadd_pop_linearlayout.setVisibility(View.VISIBLE);
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
                new_label_gvadd_pop_linearlayout.setVisibility(View.GONE);
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

    // private void setDatas() {
    // list = new ArrayList<String>();
    // list.add("1");
    // list.add("2");
    // list.add("3");
    // list.add("4");
    // list.add("5");
    // list.add("6");
    // list.add("7");
    // list.add("8");
    // list.add("11");
    // list.add("21");
    // list.add("31");
    // list.add("41");
    // list.add("51");
    // list.add("61");
    // list.add("71");
    // list.add("81");
    // }

    private void readDatas() {
//        String str = Environment.getExternalStorageDirectory() + File.separator
//                + "mldndata" + File.separator + "json.txt";
//        String json = "";
//        try {
//            json = readSDFile(str);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.e("json", json);
//        try {
//            JSONArray jsonArray = new JSONObject(json).getJSONArray("urldata");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObjs = (JSONObject) jsonArray.opt(i);
//                // 自定义json的bean文件
//                JsonFriendList jp = new JsonFriendList();
//                try {
//                    jp.parsingJson(jsonObjs);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                jsonData.add(jp);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        for (int i = 0; i < 12; i++) {
            JsonFriendList j = new JsonFriendList();
            j.id = i;
            jsonData.add(j);
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

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.new_label_gvadd_tv_sure:
                Intent intent = new Intent();
                intent.setClass(NewLabelGvAddActivity.this, NewLabelActivity.class);
                // intent.putExtra("position", arg2);
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
