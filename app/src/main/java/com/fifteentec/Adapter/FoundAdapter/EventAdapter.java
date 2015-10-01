package com.fifteentec.FoundAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.Database.DBManager;
import com.Database.EventRecord;

import com.fifteentec.Component.FoundItems.EventBrief;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.yoko.BaseActivity;
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
    private BaseActivity activity;
    private DBManager dbManager;
    private EventBrief eventInfo;


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

        this.activity = (BaseActivity)listcontaner.getContext();
        this.dbManager = this.activity.getDBManager();

        editItemContent(position);


        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        item.name.setText(eventInfo.getName());
        item.location.setText(eventInfo.getEventIntro());
        currentTime = (long)System.currentTimeMillis();

        if((currentTime>eventInfo.getTimeEnd())&&(eventInfo.getTimeEnd()!=0)){
            item.time.setText(sf.format(eventInfo.getTimeBegin())+"-"+sf.format(eventInfo.getTimeEnd()));
            item.takepart_btn.setImageResource(R.drawable.takepart_s_n);
            item.takepart_btn.setClickable(false);
        }
        else if(eventInfo.getTimeEnd()==0) {
            item.time.setText(R.string.no_limit);
        }
        else{
            if(dbManager.getTableEvent().queryEventById(eventInfo.getID())==null){
                Log.e("id", Long.toString(eventInfo.getID()));
                item.takepart_btn.setBackgroundResource(R.drawable.takepart_s);
                item.takepart_btn.setClickable(true);
            }
            else{
                item.takepart_btn.setBackgroundResource(R.drawable.takepart_s_j);
                item.takepart_btn.setClickable(false);
            }

        }

        final com.fifteentec.FoundAdapter.EventAdapter.ListItemView finalItem = item;
        item.takepart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbManager.getTableEvent().queryEventById(eventInfo.getID()) == null) {
                    addNewEvent();
                    finalItem.takepart_btn.setBackgroundResource(R.drawable.takepart_s_j);
                    finalItem.takepart_btn.setClickable(false);
                }
            }
        });


        if(!"".equals(eventInfo.getEventIntro()) && (eventInfo.getEventIntro().length()>30)){
            String str=eventInfo.getEventIntro().trim().substring(0, 30);
            item.intro.setText(str);
        } else {
            item.intro.setText(eventInfo.getEventIntro());
        }


        if (null != eventInfo.getLogoUri()
                && !"".equals(eventInfo.getLogoUri())&&!"null".equals(eventInfo.getLogoUri())) {
            imageLoader.displayImage(eventInfo.getLogoUri(), item.logo);
        } else {
            item.logo.setImageResource(R.drawable.logo_default);
        }

        if (null != eventInfo.getEventUri()
                && !"".equals(eventInfo.getEventUri())&& !"null".equals(eventInfo.getEventUri())) {
            imageLoader.displayImage(eventInfo.getEventUri(),item.event);
        } else {
            item.event.setImageResource(R.drawable.event_eg);
        }

        return convertView;

    }

    private void addNewEvent(){
        EventRecord eventRecord = new EventRecord();

        eventRecord.introduction = eventInfo.getEventIntro();
        eventRecord.id = eventInfo.getID();
        eventRecord.timeend = eventInfo.getTimeEnd();
        eventRecord.timebegin = eventInfo.getTimeBegin();
        if(eventInfo.getTimeEnd()==0) {
            eventRecord.timebegin = currentTime;
            eventRecord.timeend = currentTime;

        }
        eventRecord.type = 0;
        eventRecord.uid = UserServer.getInstance().getUserid();

       if(dbManager.getTableEvent().addEvent(eventRecord)==-1){
           Log.e("add","error in adding event");
       }
        else {
           Log.e("add",Long.toString(eventRecord.id));
       }

    }


    private void editItemContent(int position){
        if(eventInfo==null)eventInfo = new EventBrief();
        eventInfo.setName(eventList.get(position).getName());
        eventInfo.setEventIntro(eventList.get(position).getEventIntro());
        eventInfo.setTimeBegin(eventList.get(position).getTimeBegin());
        eventInfo.setTimeEnd(eventList.get(position).getTimeEnd());
        eventInfo.setID(eventList.get(position).getID());
        eventInfo.setPicUri(eventList.get(position).getPicUri());
        eventInfo.setEventUri(eventList.get(position).getEventUri());
    }


}
