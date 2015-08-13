package com.fifteentec.FoundAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.item.GroupBrief;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2015/8/11.
 */
public class RecommendedGroupAdapter extends BaseAdapter{
    private LayoutInflater listcontaner;
    private int itemViewResource;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    List<GroupBrief> groupList = new ArrayList<GroupBrief>();


    static class ListItemView {
        public TextView name;
        public TextView intro;
        public ImageView logo;
        public Button join;
    }

    public RecommendedGroupAdapter(LayoutInflater listcontaner, List<GroupBrief> groupData) {
        this.listcontaner = listcontaner;
        groupList = groupData;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView item = null;
        if (null == convertView) {
            item = new ListItemView();
            convertView = listcontaner.inflate(R.layout.found_group_recomended_item, null);
            item.name = (TextView)convertView.findViewById(R.id.group_name2);
            item.intro = (TextView)convertView.findViewById(R.id.group_intro2);
            item.logo = (ImageView)convertView.findViewById(R.id.group_logo2);
            item.join = (Button)convertView.findViewById(R.id.join);
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

        item.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return convertView;

    }
}
