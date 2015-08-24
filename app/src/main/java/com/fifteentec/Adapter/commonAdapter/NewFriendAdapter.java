package com.fifteentec.Adapter.commonAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.yoko.R;
import com.fifteentec.Component.Parser.JsonFriendAdd;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/14.
 */
public class NewFriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<JsonFriendAdd> list = new ArrayList<JsonFriendAdd>();
    private View v;

    public NewFriendAdapter(Context c, ArrayList<JsonFriendAdd> list,
                            View v) {
        context = c;
        this.list = list;
        this.v = v;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.newfriend_add_item, null);

            viewHolder.newfriend_add_iv_icon = (ImageView) convertView.findViewById(R.id.newfriend_add_iv_icon);
            viewHolder.newfriend_add_tv_name = (TextView) convertView.findViewById(R.id.newfriend_add_tv_name);
            viewHolder.newfriend_add_tv_location = (TextView) convertView.findViewById(R.id.newfriend_add_tv_location);
            viewHolder.newfriend_add_tv_addbutton = (TextView) convertView.findViewById(R.id.newfriend_add_tv_addbutton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(list.get(position).picturelink, viewHolder.newfriend_add_iv_icon);
        viewHolder.newfriend_add_tv_name.setText(list.get(position).nickname);
        viewHolder.newfriend_add_tv_location.setText(list.get(position).location);
        viewHolder.newfriend_add_tv_addbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                v.setVisibility(View.VISIBLE);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView newfriend_add_tv_name;
        public ImageView newfriend_add_iv_icon;
        public TextView newfriend_add_tv_location;
        public TextView newfriend_add_tv_addbutton;
    }
}
