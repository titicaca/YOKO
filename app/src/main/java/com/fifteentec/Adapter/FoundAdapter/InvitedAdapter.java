package com.fifteentec.FoundAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Component.FoundItems.InvitedBrief;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2015/8/11.
 */
public class InvitedAdapter extends BaseAdapter{
    private LayoutInflater listcontaner;
    private int itemViewResource;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    List<InvitedBrief> eventList = new ArrayList<InvitedBrief>();


    static class ListItemView {
        public TextView name;
        public TextView content;
        public ImageView logo;
        public TextView time;
        public Button refuse;
        public Button accept;
        public Button argue;
    }

    public InvitedAdapter(LayoutInflater listcontaner, List<InvitedBrief> eventData) {
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
            convertView = listcontaner.inflate(R.layout.found_msg_invited_item, null);
            item.name = (TextView)convertView.findViewById(R.id.friend_name);
            item.content = (TextView)convertView.findViewById(R.id.invited_content);
            item.time = (TextView)convertView.findViewById(R.id.sended_time);
            item.logo = (ImageView)convertView.findViewById(R.id.friend_logo);
            item.refuse = (Button)convertView.findViewById(R.id.refuse);
            item.accept = (Button)convertView.findViewById(R.id.accept);
            item.argue = (Button)convertView.findViewById(R.id.argue);
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
        item.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        item.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        item.argue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;

    }
}
