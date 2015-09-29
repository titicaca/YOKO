package com.fifteentec.FoundAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.Component.FoundItems.EventBrief;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
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
    long currentTime = 0;


    static class ListItemView {
        public TextView name;
        public TextView groupName;
        public TextView intro;
        public ImageView logo;
        public ImageView event;
        public TextView time;
        public TextView location;
        public ImageButton takepart_btn;
    }

    public EventAdapter(LayoutInflater listcontaner, List<EventBrief> eventData) {
        this.listcontaner = listcontaner;
        eventList = eventData;
    }

    @Override
    public int getCount() {
        if(eventList==null) return 0;
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<EventBrief> groupData){
        this.eventList = groupData;
        this.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView item = null;
        if (null == convertView) {
            item = new ListItemView();
            convertView = listcontaner.inflate(R.layout.found_event_item, null);
            item.name = (TextView)convertView.findViewById(R.id.event_name);
            item.intro = (TextView)convertView.findViewById(R.id.event_intro);
            item.time = (TextView)convertView.findViewById(R.id.time);
            item.location=(TextView)convertView.findViewById(R.id.location);
            item.logo = (ImageView)convertView.findViewById(R.id.group_logo3);
            item.event = (ImageView)convertView.findViewById(R.id.event_post);
            item.takepart_btn = (ImageButton)convertView.findViewById(R.id.takepartSBtn);
            convertView.setTag(item);
        } else {
            item = (ListItemView) convertView.getTag();
        }
        final SharedPreferences sharedPreferences;
        sharedPreferences = listcontaner.getContext().getSharedPreferences("event_take_part_info", Context.MODE_PRIVATE);

        final int id;
        id = eventList.get(position).getID();
        final com.fifteentec.FoundAdapter.EventAdapter.ListItemView finalItem = item;
        Boolean joined = sharedPreferences.getBoolean("id" + id, false);
        if(!joined)item.takepart_btn.setBackgroundResource(R.drawable.takepart_s);
        else item.takepart_btn.setBackgroundResource(R.drawable.takepart_s_j);
        item.takepart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean joined = sharedPreferences.getBoolean("id" + id, false);
                if (!joined) {
                    finalItem.takepart_btn.setBackgroundResource(R.drawable.takepart_s_j);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("id" + id, true);
                    editor.commit();
                }
            }
        });
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        item.name.setText(eventList.get(position).getGroupName());
        item.location.setText(eventList.get(position).getLocation());
        currentTime = (long)System.currentTimeMillis();
        if((currentTime>eventList.get(position).getTimeEnd())&&(eventList.get(position).getTimeEnd()!=0)){
            item.takepart_btn.setImageResource(R.drawable.takepart_s_n);
            item.takepart_btn.setClickable(false);
        }
        if(eventList.get(position).getTimeEnd()!=0) item.time.setText(sf.format(eventList.get(position).getTimeBegin())+"-"+sf.format(eventList.get(position).getTimeEnd()));
        else item.time.setText(R.string.no_limit);


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
