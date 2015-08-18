package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.yoko.R;

import java.util.ArrayList;

public class FriendDetailsAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    private FriendDetailsGridViewAdapter fdgvadapter;
    private ArrayList<JsonFriendList> jsonData = new ArrayList<JsonFriendList>();

    public FriendDetailsAdapter(Context context, ArrayList<JsonFriendList> jsonData) {
        this.context = context;
        this.jsonData = jsonData;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public int getCount() {
        return jsonData != null ? jsonData.size() : 0;
    }

    @SuppressWarnings("unused")
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        // if (view == null) {
        // LayoutInflater inflater = LayoutInflater.from(context);
        // convertView = inflater.inflate(R.layout.friends_adapter, null);
        // convertView.setTag(viewHolder);
        //
        // inflater = LayoutInflater.from(context);
        // // 自定义的view
        // View userLayout = inflater.inflate(R.layout.hlistview_adapter,
        // view, false);
        // TextView tvName = (TextView) userLayout
        // .findViewById(R.id.hlist_tv_label);
        // // 填充数据
        // tvName.setText(list.get(position));
        //
        // } else {
        // viewHolder = (ViewHolder) convertView.getTag();
        // }
        ViewHolder viewHolder = new ViewHolder();
        inflater = LayoutInflater.from(context);
        // 自定义的view
        View userLayout = null;

        if (userLayout == null) {
            userLayout = inflater.inflate(R.layout.hlistview_adapter, view,
                    false);
            viewHolder.tvName = (TextView) userLayout
                    .findViewById(R.id.hlist_tv_label);
            viewHolder.hlist_rL_information_tv_address = (TextView) userLayout.findViewById(R.id.hlist_rL_information_tv_address);
            viewHolder.gv = (GridView) userLayout
                    .findViewById(R.id.hlist_gv_images);
            viewHolder.hlist_rL_information_tv_name = (TextView) userLayout
                    .findViewById(R.id.hlist_rL_information_tv_name);
            userLayout.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) userLayout.getTag();
        }

        // userLayout = inflater.inflate(R.layout.hlistview_adapter, view,
        // false);
        // TextView tvName = (TextView) userLayout
        // .findViewById(R.id.hlist_tv_label);
        // 填充数据
        // viewHolder.tvName.setText(position + 1 + "");
        viewHolder.hlist_rL_information_tv_name
                .setText(jsonData.get(position).nickname);
        viewHolder.hlist_rL_information_tv_address.setText(jsonData.get(position).location);
        fdgvadapter = new FriendDetailsGridViewAdapter(context);
        viewHolder.gv.setAdapter(fdgvadapter);
        ((ViewPager) view).addView(userLayout, 0);
        return userLayout;
    }

    public class ViewHolder {
        public TextView tvName;
        public GridView gv;
        public TextView hlist_rL_information_tv_name;
        public TextView hlist_rL_information_tv_address;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View container) {
    }

}