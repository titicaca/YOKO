package com.fifteentec.FoundAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Component.FoundItems.GroupBrief;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2015/8/11.
 */
public class GroupAdapter extends BaseAdapter{
    private LayoutInflater listcontaner;
    private int itemViewResource;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private Boolean joined=true;
    List<GroupBrief> groupList = new ArrayList<GroupBrief>();


    static class ListItemView {
        public TextView name;
        public TextView intro;
        public ImageView logo;
        public ImageView bigPic;
        public ImageView smallPicLeft;
        public ImageView smallPicMid;
        public ImageView smallPicRight;
        public Button join;
    }

    public GroupAdapter(LayoutInflater listcontaner, List<GroupBrief> groupData,Boolean joined) {
        this.listcontaner = listcontaner;
        this.groupList = groupData;
        this.joined = joined;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setList(List<GroupBrief> groupData){
        this.groupList = groupData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView item = null;
        if (null == convertView) {
            item = new ListItemView();
            convertView = listcontaner.inflate(R.layout.found_group_item, null);
            item.name = (TextView)convertView.findViewById(R.id.group_name);
            item.intro = (TextView)convertView.findViewById(R.id.group_intro);
            item.logo = (ImageView)convertView.findViewById(R.id.group_logo);
            item.join = (Button)convertView.findViewById(R.id.join);
            item.bigPic = (ImageView)convertView.findViewById(R.id.bigPic);
            item.smallPicLeft = (ImageView)convertView.findViewById(R.id.leftPic);
            item.smallPicMid = (ImageView)convertView.findViewById(R.id.midPic);
            item.smallPicRight = (ImageView)convertView.findViewById(R.id.rightPic);
            convertView.setTag(item);
        } else {
            item = (ListItemView) convertView.getTag();
        }



        item.name.setText(groupList.get(position).getGroupName());
        if(!"".equals(groupList.get(position).getGroupIntro()) && groupList.get(position).getGroupIntro().length()>26){
            String str=groupList.get(position).getGroupIntro().trim().substring(0, 24);
            item.intro.setText(str);
        } else {
            item.intro.setText(groupList.get(position).getGroupIntro());
        }

        if (null != groupList.get(position).getLogoUri()
                && !"".equals(groupList.get(position).getLogoUri())) {
            item.logo.setVisibility(View.VISIBLE);
            imageLoader.displayImage(groupList.get(position).getLogoUri(), item.logo);
        } else {
            item.logo.setImageResource(R.drawable.logo_default);
        }

        if ((groupList.get(position).getBigPicUri()!=null)
                && (!groupList.get(position).getBigPicUri().equals(""))&&!"null".equals(groupList.get(position).getBigPicUri())) {
            Log.e("Pic",groupList.get(position).getBigPicUri());
            imageLoader.displayImage(groupList.get(position).getBigPicUri(), item.bigPic);
        } else {
            item.bigPic.setImageResource(R.drawable.sjtubig);
        }

        if (null != groupList.get(position).getSmallPicUriLeft()
                && !"".equals(groupList.get(position).getSmallPicUriLeft())&&!"null".equals(groupList.get(position).getSmallPicUriLeft())) {
            imageLoader.displayImage(groupList.get(position).getSmallPicUriLeft(), item.smallPicLeft);
        } else {
            item.smallPicLeft.setImageResource(R.drawable.sjtu1);
        }

        if (null != groupList.get(position).getSmallPicUriMid()
                && !"".equals(groupList.get(position).getSmallPicUriMid())&&!"null".equals(groupList.get(position).getSmallPicUriMid())) {
            imageLoader.displayImage(groupList.get(position).getSmallPicUriMid(), item.smallPicMid);
        } else {
            item.smallPicMid.setImageResource(R.drawable.sjtu2);
        }

        if (null != groupList.get(position).getSmallPicUriRight()
                && !"".equals(groupList.get(position).getSmallPicUriRight())&&!"null".equals(groupList.get(position).getSmallPicUriRight())) {
            imageLoader.displayImage(groupList.get(position).getSmallPicUriRight(), item.smallPicRight);
        } else {
            item.smallPicRight.setImageResource(R.drawable.sjtu3);
        }

        if(joined) item.join.setText(R.string.cancel);
        else item.join.setText(R.string.join);

        item.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return convertView;

    }
}
