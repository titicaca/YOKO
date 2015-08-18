package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.yoko.R;

import java.util.ArrayList;

public class NewLabelGvAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<JsonFriendList> list = new ArrayList<JsonFriendList>();
    private String flag;
    public static int ivDeleteIsVisiable = 0;

    public NewLabelGvAdapter(Context c, ArrayList<JsonFriendList> list, String flag) {
        this.c = c;
        this.list = list;
        this.flag = flag;

        if (flag.equals("0")) {
            ivDeleteIsVisiable = 0;
        } else {
            ivDeleteIsVisiable = 1;
        }

    }

    @Override
    public int getCount() {

        if (list.size() == 0) {
            return 2;
        } else {
            return list.size() + 2;
        }

    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
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
            convertView = inflater.inflate(R.layout.new_label_gv_item, null);
            viewHolder.iv = (ImageView) convertView
                    .findViewById(R.id.new_label_gv_iv);
            viewHolder.ivDelete = (ImageView) convertView
                    .findViewById(R.id.new_label_gv_ivDelete);
            viewHolder.new_label_gv_tv = (TextView) convertView
                    .findViewById(R.id.new_label_gv_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (arg0 < list.size()) {
            viewHolder.new_label_gv_tv.setText(list.get(arg0).nickname);
        } else if (arg0 == list.size()) {
            viewHolder.iv.setImageResource(R.drawable.face_05);
            viewHolder.new_label_gv_tv.setText("添加");
        } else if (arg0 == list.size() + 1) {
            viewHolder.iv.setImageResource(R.drawable.ic);
            viewHolder.new_label_gv_tv.setText("删除");
        }

        if (flag.equals("1")) {
            if (arg0 < list.size()) {
                viewHolder.ivDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivDelete.setVisibility(View.GONE);
            }
        } else {
            viewHolder.ivDelete.setVisibility(View.GONE);
        }
        final int po = arg0;
        viewHolder.ivDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                list.remove(po);
                notifyDataSetChanged();

            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView ivDelete;
        TextView new_label_gv_tv;
        ImageView iv;
    }

}

