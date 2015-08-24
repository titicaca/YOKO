package com.fifteentec.FoundAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Component.FoundItems.EventBrief;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cj on 2015/8/11.
 */
public class EventAdapter extends BaseAdapter{
    private LayoutInflater listcontaner;
    private int itemViewResource;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    List<EventBrief> eventList = new ArrayList<EventBrief>();


    static class ListItemView {
        public TextView name;
        public TextView intro;
        public ImageView logo;
        public ImageView event;
        public TextView time;
        public TextView location;
    }

    public EventAdapter(LayoutInflater listcontaner, List<EventBrief> eventData) {
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
            convertView = listcontaner.inflate(R.layout.found_event_item, null);
            item.name = (TextView)convertView.findViewById(R.id.group_event_name);
            item.intro = (TextView)convertView.findViewById(R.id.event_intro);
            item.time = (TextView)convertView.findViewById(R.id.time);
            item.location=(TextView)convertView.findViewById(R.id.location);
            item.logo = (ImageView)convertView.findViewById(R.id.group_logo3);
            item.event = (ImageView)convertView.findViewById(R.id.event_post);
            convertView.setTag(item);
        } else {
            item = (ListItemView) convertView.getTag();
        }

        item.name.setText(eventList.get(position).getGroupName());
        item.time.setText(eventList.get(position).getTime());
        item.location.setText(eventList.get(position).getLocation());

        if(!"".equals(eventList.get(position).getEventIntro()) && eventList.get(position).getEventIntro().length()>30){
            String str=eventList.get(position).getEventIntro().trim().substring(0, 30);
            item.intro.setText(str);
        } else {
            item.intro.setText(eventList.get(position).getEventIntro());
        }


        if (null != eventList.get(position).getLogoUri()
                && !"".equals(eventList.get(position).getLogoUri())&&!"null".equals(eventList.get(position).getLogoUri())) {
            imageLoader.displayImage(eventList.get(position).getLogoUri(), item.logo);
        } else {
            item.logo.setImageResource(R.drawable.logo_default);
        }

        if (null != eventList.get(position).getEventUri()
                && !"".equals(eventList.get(position).getEventUri())&& !"null".equals(eventList.get(position).getEventUri())) {
            imageLoader.displayImage(eventList.get(position).getEventUri(),item.event);
        } else {
            item.event.setImageResource(R.drawable.event_eg);
        }

        return convertView;

    }
}
