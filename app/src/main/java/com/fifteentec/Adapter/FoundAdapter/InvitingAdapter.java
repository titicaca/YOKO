package com.fifteentec.FoundAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Component.FoundItems.InvitingBrief;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2015/8/11.
 */
public class InvitingAdapter extends BaseAdapter{
    private LayoutInflater listcontaner;
    private int itemViewResource;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    List<InvitingBrief> eventList = new ArrayList<InvitingBrief>();


    static class ListItemView {
        public TextView name;
        public TextView content;
        public ImageView logo;
        public TextView time;
        public Button delete;
        public Button check;
    }

    public InvitingAdapter(LayoutInflater listcontaner, List<InvitingBrief> eventData) {
        this.listcontaner = listcontaner;
        eventList = eventData;
    }

    @Override
    public int getCount() {
        return eventList.size();
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
            convertView = listcontaner.inflate(R.layout.found_msg_inviting_item, null);
            item.name = (TextView)convertView.findViewById(R.id.friend_name2);
            item.content = (TextView)convertView.findViewById(R.id.inviting_content);
            item.time = (TextView)convertView.findViewById(R.id.sended_time2);
            item.logo = (ImageView)convertView.findViewById(R.id.friend_logo2);
            item.delete = (Button)convertView.findViewById(R.id.delete);
            item.check = (Button)convertView.findViewById(R.id.check);
            convertView.setTag(item);
        } else {
            item = (ListItemView) convertView.getTag();
        }

        item.name.setText(eventList.get(position).getFriendName());
        item.time.setText(eventList.get(position).getTime());

        if(!"".equals(eventList.get(position).getInvitedContent()) && eventList.get(position).getInvitedContent().length()>30){
            String str=eventList.get(position).getInvitedContent().trim().substring(0, 30);
            item.content.setText(str);
        } else {
            item.content.setText(eventList.get(position).getInvitedContent());
        }


        if (null != eventList.get(position).getLogoUri()
                && !"".equals(eventList.get(position).getLogoUri())&& !"null".equals(eventList.get(position).getLogoUri())) {
            imageLoader.displayImage(eventList.get(position).getLogoUri(), item.logo);
        } else {
            item.logo.setImageResource(R.drawable.logo_default);
        }
        item.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        item.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;

    }
}
