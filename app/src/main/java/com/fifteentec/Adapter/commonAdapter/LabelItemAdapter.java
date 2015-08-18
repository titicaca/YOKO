package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Database.FriendTagRecord;
import com.fifteentec.yoko.R;

import java.util.List;

public class LabelItemAdapter extends BaseAdapter {

    private Context c;
    private List<FriendTagRecord> list;

    public LabelItemAdapter(Context c, List<FriendTagRecord> list) {
        this.c = c;
        this.list = list;
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
            convertView = inflater.inflate(R.layout.label_item, null);
            viewHolder.label_item_tv = (TextView) convertView
                    .findViewById(R.id.label_item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.label_item_tv.setText(list.get(arg0).tagName);
        return convertView;
    }

    public class ViewHolder {
        private TextView label_item_tv;
    }

}

