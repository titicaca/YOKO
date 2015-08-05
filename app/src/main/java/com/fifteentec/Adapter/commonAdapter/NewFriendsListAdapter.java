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
import android.widget.TextView;
import android.widget.Toast;

import com.fifteentec.yoko.R;

import java.util.ArrayList;

public class NewFriendsListAdapter extends BaseAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context c;

    public NewFriendsListAdapter(Context c) {
        list = new ArrayList<String>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        this.c = c;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = inflater.inflate(R.layout.newfriends_list_item, null);
            viewHolder.newfriends_item_tv_add = (TextView) convertView
                    .findViewById(R.id.newfriends_item_tv_add);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TextView tv_add = viewHolder.newfriends_item_tv_add;
        final int po = arg0;
        viewHolder.newfriends_item_tv_add
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (tv_add.getText().toString().equals("添加")) {
                            tv_add.setText("已添加");
                            Toast.makeText(c, "添加成功 " + po, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(c, "已经添加过啦" + po, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

        return convertView;
    }

    public class ViewHolder {
        TextView newfriends_item_tv_add;
    }

}