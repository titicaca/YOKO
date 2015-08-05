package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fifteentec.yoko.R;
import com.fifteentec.yoko.friends.JsonParsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NewLabelGvAddAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<JsonParsing> jsonData = new ArrayList<JsonParsing>();
    private ArrayList<JsonParsing> jsonTrans = new ArrayList<JsonParsing>();
    // 用来判断checkbox选中状态
    private ArrayList<JsonParsing> jsonTransModified = new ArrayList<JsonParsing>();
    private static HashMap<Integer, Boolean> isSelected;

    @SuppressLint("UseSparseArrays")
    public NewLabelGvAddAdapter(Context c, ArrayList<JsonParsing> jsonData,
                                ArrayList<JsonParsing> jsonTrans,
                                ArrayList<JsonParsing> jsonTransModified) {
        this.c = c;
        this.jsonData = jsonData;
        isSelected = new HashMap<Integer, Boolean>();
        this.jsonTrans = jsonTrans;
        this.jsonTransModified = jsonTransModified;
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {

        if (jsonTransModified.size() != 0) {
            for (int i = 0; i < jsonData.size(); i++) {
                getIsSelected().put(i, false);
            }

            for (int i = 0; i < jsonTransModified.size(); i++) {
                for (int j = 0; j < jsonData.size(); j++) {
//                    if (jsonTransModified.get(i).id.equals(jsonData.get(j).id)) {
//                        isSelected.put(j, true);
//                        setIsSelected(isSelected);
//                        break;
//                    }
                }

            }

        } else {

            for (int i = 0; i < jsonData.size(); i++) {
                getIsSelected().put(i, false);
            }
        }
    }

    @Override
    public int getCount() {
        return jsonData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return jsonData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        // 列表中 没有数据
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.new_label_gvadd_item, null);
            viewHolder.cb = (CheckBox) convertView
                    .findViewById(R.id.new_label_gvadd_item_cb);
            viewHolder.new_label_gvadd_item_tv = (TextView) convertView
                    .findViewById(R.id.new_label_gvadd_item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int position = arg0;
        // if (jsonTransModified.size() != 0) {
        // for (int i = 0; i < jsonTransModified.size(); i++) {
        // for (int j = 0; j < jsonData.size(); j++) {
        // if (jsonTransModified.get(i).id.equals(jsonData.get(j).id)) {
        // isSelected.put(j, true);
        // setIsSelected(isSelected);
        // } else {
        // isSelected.put(j, false);
        // setIsSelected(isSelected);
        // }
        // }
        //
        // }
        // }
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (isSelected.get(position)) {
                    // 删除
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                    @SuppressWarnings("rawtypes")
                    Iterator it = jsonTrans.iterator();
                    while (it.hasNext()) {
                        JsonParsing jp = (JsonParsing) it.next();
//                        if (jp.name.equals(jsonData.get(position).name)) {
//                            it.remove();
//                        }
                    }

                    // jsonTrans.removeAll(jp);

                } else {
                    // 选中
                    isSelected.put(position, true);
                    setIsSelected(isSelected);

                    JsonParsing jp = new JsonParsing();
//                    jp.name = jsonData.get(position).name;
                    jp.id = jsonData.get(position).id;

                    jsonTrans.add(jp);

                }

            }
        });

        // 根据isSelected来设置checkbox的选中状况
        viewHolder.cb.setChecked(getIsSelected().get(position));
//        viewHolder.new_label_gvadd_item_tv.setText(jsonData.get(position).name);
        // viewHolder.cb.setOnCheckedChangeListener(new
        // OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        // Toast.makeText(c, "zhen or jia  " + arg1 + "weizhi  " + po,
        // 2222).show();
        // if (arg1) {
        // isAdd.add(list.get(po));
        // } else {
        // isAdd.remove(list.get(po));
        // }
        // }
        // });

        return convertView;
    }

    public class ViewHolder {
        CheckBox cb;
        TextView new_label_gvadd_item_tv;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        NewLabelGvAddAdapter.isSelected = isSelected;
    }

}
