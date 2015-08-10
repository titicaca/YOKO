package com.fifteentec.Fragment;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.API.APIJsonCallbackResponse;
import com.API.APIServer;
import com.API.APIUrl;
import com.fifteentec.Adapter.commonAdapter.FriendsAdapter;
import com.fifteentec.Adapter.commonAdapter.FriendsTwoAdapter;
import com.fifteentec.Component.calendar.KeyboardLayout;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;
import com.fifteentec.yoko.friends.FriendDetailsActivity;
import com.fifteentec.yoko.friends.JsonParsing;
import com.fifteentec.yoko.friends.LabelActivity;
import com.fifteentec.yoko.friends.NewFriendActivity;
import com.fifteentec.yoko.friends.NewFriendsListActivity;

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

public class FriendsFragment extends Fragment implements OnItemClickListener,
        OnClickListener, OnItemLongClickListener {
    private ListView lv1; // 好友和标签;
    private ListView lv2; // 好友列表
    private EditText search; // 输入框
    private KeyboardLayout mainView; // 判断软键盘是否隐藏
    private ArrayList<JsonParsing> jsonData = new ArrayList<JsonParsing>();
    private View v;
    private TextView friends_tv_addfriends;
    private FriendsAdapter fAdapter;
    private BaseActivity activity;
    private ArrayList<JsonParsing> list = new ArrayList<JsonParsing>();
    private JsonParsing jp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends, container, false);
        lv1 = (ListView) view.findViewById(R.id.friends_lv1);
        lv2 = (ListView) view.findViewById(R.id.friends_lv2);
        search = (EditText) view.findViewById(R.id.friends_search_et);
        friends_tv_addfriends = (TextView) view
                .findViewById(R.id.friends_tv_addfriends);

        this.activity = (BaseActivity) this.getActivity();
        jp = new JsonParsing();


        // 判断软键盘弹出收起
        // friends_search_ll.getViewTreeObserver().addOnGlobalLayoutListener(
        // new OnGlobalLayoutListener() {
        // @Override
        // public void onGlobalLayout() {
        //
        // int heightDiff = friends_search_ll.getRootView()
        // .getHeight() - friends_search_ll.getHeight();
        //
        // }
        // });
//        initDatas();
        mainView = (KeyboardLayout) view.findViewById(R.id.keyboardLayout1);
        mainView.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {

            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    // 软键盘隐藏
                    case KeyboardLayout.KEYBOARD_STATE_HIDE:

                        mainView.setFocusable(true);
                        mainView.setFocusableInTouchMode(true);
                        mainView.requestFocus();

                        // 点击空白处软键盘关闭
                        // InputMethodManager imm = (InputMethodManager)
                        // getActivity()
                        // .getSystemService(Context.INPUT_METHOD_SERVICE);
                        // if (getActivity().getCurrentFocus() != null) {
                        // if (getActivity().getCurrentFocus().getWindowToken() !=
                        // null) {
                        // imm.hideSoftInputFromWindow(getActivity()
                        // .getCurrentFocus().getWindowToken(),
                        // InputMethodManager.HIDE_NOT_ALWAYS);
                        // }
                        // }

                        break;
                    // 软键盘弹起
                    case KeyboardLayout.KEYBOARD_STATE_SHOW:
                        break;
                }
            }
        });
        // 好友标签适配器
        FriendsTwoAdapter ftapterAdapter = new FriendsTwoAdapter(getActivity());
        lv1.setAdapter(ftapterAdapter);

        // 设置listview定高
        setListViewHeightBasedOnChildren(lv1);

        friends_tv_addfriends.setOnClickListener(this);
        search.setOnFocusChangeListener(onFocusAutoClearHintListener);
        // lv1.setOnItemClickListener(this);
        lv2.setOnItemClickListener(this);
        lv1.setOnItemClickListener(this);
        lv2.setOnItemLongClickListener(this);
        //加载好友列表
        LoadFriendsList();
        // lv2.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        // long arg3) {
        //
        // }
        // });
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
                textView.setGravity(Gravity.LEFT);
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
                if (textView.getText().toString().equals("")) {
                    textView.setGravity(Gravity.CENTER);
                } else {
                    textView.setGravity(Gravity.LEFT);
                }
            }
        }
    };

    /**
     * 假数据json，写入file中
     */
    private void initDatas() {
        String id[] = new String[]{"1", "2", "3", "4", "5", "6", "7"};
        String name[] = new String[]{"大白", "二白", "三白", "四白", "5白", "6白", "7白"};// 要输出的数据
        JSONObject allData = new JSONObject();// 建立最外面的节点对象
        JSONArray sing = new JSONArray();// 定义数组
        for (int x = 0; x < id.length; x++) {// 将数组内容配置到相应的节点
            JSONObject temp = new JSONObject();// JSONObject包装数据,而JSONArray包含多个JSONObject
            try {
                temp.put("id", id[x]); // JSONObject是按照key:value形式保存
                temp.put("name", name[x]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sing.put(temp);// 保存多个JSONObject
        }
        try {
            allData.put("urldata", sing);// 把JSONArray用最外面JSONObject包装起来
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// SD卡不存在则不操作
            return;// 返回到程序的被调用处
        }
        String str = Environment.getExternalStorageDirectory() + File.separator
                + "mldndata" + File.separator + "json.txt";
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "mldndata" + File.separator + "json.txt");// 要输出的文件路径
        if (!file.getParentFile().exists()) {// 文件不存在
            file.getParentFile().mkdirs();// 创建文件夹
        }
        // PrintStream 和 PrintWriter
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(file));
            out.print(allData.toString());// 将数据变为字符串后保存
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();// 关闭输出
            }
        }
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

    // 写文件
    public void writeSDFile(String fileName, String write_str)
            throws IOException {

        File file = new File(fileName);

        FileOutputStream fos = new FileOutputStream(file);

        byte[] bytes = write_str.getBytes();

        fos.write(bytes);

        fos.close();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg0.getId()) {
            case R.id.friends_lv1:
                if (arg2 == 1) {
                    Intent in = new Intent();
                    in.setClass(getActivity(), LabelActivity.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent();
                    in.setClass(getActivity(), NewFriendsListActivity.class);
                    startActivity(in);
                }
                break;
            case R.id.friends_lv2:
                Intent intent = new Intent();
                intent.setClass(getActivity(), FriendDetailsActivity.class);
                intent.putExtra("position", arg2);
                // 传递集合数据时，需要将bean文件实现Serializable接口才能正常传递
                intent.putExtra("json", (Serializable) list);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.friends_tv_addfriends:
                Intent in = new Intent();
                in.setClass(getActivity(), NewFriendActivity.class);
                startActivity(in);
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
                                // showClickMessage("确定");
                                list.remove(position);
                                fAdapter.notifyDataSetChanged();
                                // 因为之前listview已经固定高度，所以删除后，需重新定高
                                setListViewHeightBasedOnChildren(lv2);
                            }
                        });
                normalDia.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // showClickMessage("取消");
                            }
                        });
                normalDia.create().show();
                break;

            default:
                break;
        }
        return false;
    }

    private void LoadFriendsList() {
        new APIServer.JsonGet(
                APIUrl.URL_FRIENDLIST,
                null,
                null,
                new APIJsonCallbackResponse() {

                    @Override
                    public void run() {
                        JSONObject response = this.getResponse();

                        try {
                            jp.parsingJson(response);
                            // 好友列表适配器
                            list = jp.list;
                            fAdapter = new FriendsAdapter(getActivity(), list, "0", v);
                            lv2.setAdapter(fAdapter);
                            setListViewHeightBasedOnChildren(lv2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                activity.getRequestQueue(),
                null).send();
    }
}

