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
import android.widget.ImageView;
import android.widget.TextView;

import com.Database.FriendInfoRecord;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class FriendDetailsAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    private FriendDetailsGridViewAdapter fdgvadapter;
    private ArrayList<FriendInfoRecord> jsonData = new ArrayList<FriendInfoRecord>();

    public FriendDetailsAdapter(Context context, ArrayList<FriendInfoRecord> jsonData) {
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
        ViewHolder viewHolder = new ViewHolder();
        inflater = LayoutInflater.from(context);
        // 自定义的view
        View userLayout = null;

        if (userLayout == null) {
            userLayout = inflater.inflate(R.layout.hlistview_adapter, view,
                    false);
            viewHolder.hlist_rL_information_iv_address = (ImageView) userLayout.findViewById(R.id.hlist_rL_information_iv_address);
            viewHolder.tvName = (TextView) userLayout
                    .findViewById(R.id.hlist_rL_information_tv_name);
            viewHolder.hlist_rL_information_tv_address = (TextView) userLayout.findViewById(R.id.hlist_rL_information_tv_address);
            viewHolder.gv = (GridView) userLayout
                    .findViewById(R.id.hlist_gv_images);
            viewHolder.hlist_rL_information_tv_name = (TextView) userLayout
                    .findViewById(R.id.hlist_rL_information_tv_name);
            userLayout.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) userLayout.getTag();
        }
        ImageLoader.getInstance().displayImage(jsonData.get(position).picturelink, viewHolder.hlist_rL_information_iv_address);
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
        public ImageView hlist_rL_information_iv_address;
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