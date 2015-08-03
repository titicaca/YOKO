package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by Administrator on 2015/8/3.
 */

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.yoko.R;
import com.fifteentec.yoko.friends.JsonParsing;

public class FriendsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<JsonParsing> list = new ArrayList<JsonParsing>();
    private String flag;// 用于区分是否显示添加按钮
    private View v;

    public FriendsAdapter(Context c, ArrayList<JsonParsing> list, String flag,
                          View v) {
        context = c;
        this.list = list;
        this.flag = flag;
        this.v = v;
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
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.friends_adapter, null);
            viewHolder.friends_adapter_tv_name = (TextView) convertView
                    .findViewById(R.id.friends_adapter_tv_name);
            viewHolder.friends_adapter_tv_add = (TextView) convertView
                    .findViewById(R.id.friends_adapter_tv_add);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (flag.equals("1")) {
            viewHolder.friends_adapter_tv_add.setVisibility(View.VISIBLE);
        }
        viewHolder.friends_adapter_tv_name.setText(list.get(arg0).name);
        viewHolder.friends_adapter_tv_add
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        v.setVisibility(View.VISIBLE);
                    }
                });
        return convertView;

    }

    public class ViewHolder {
        public TextView friends_adapter_tv_name;
        public ImageView iv;
        public TextView friends_adapter_tv_add;
    }

}
