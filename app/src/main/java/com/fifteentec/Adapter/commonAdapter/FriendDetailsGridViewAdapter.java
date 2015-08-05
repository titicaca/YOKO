package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fifteentec.yoko.R;

import java.util.ArrayList;

public class FriendDetailsGridViewAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<Integer> list = new ArrayList<Integer>();

    public FriendDetailsGridViewAdapter(Context c) {
        list = new ArrayList<Integer>();
        this.c = c;
    }

    @Override
    public int getCount() {
        return 4;
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
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.fdgridview_adapter, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView iv;
    }

}
