package com.fifteentec.FoundAdapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.Database.DBManager;
import com.Database.EventRecord;

import com.fifteentec.Component.FoundItems.EventBrief;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Fragment.FoundFragment.FoundEvent;
import com.fifteentec.Fragment.FoundFragment.FoundEventItem;
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
public class EventAdapter extends BaseAdapter {
    private LayoutInflater listcontaner;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    List<EventBrief> eventList = new ArrayList<EventBrief>();
    long currentTime = 0;
    private BaseActivity activity;
    private DBManager dbManager;
    ListItemView item = null;
    private FragmentManager mFragmentManager;
    private FoundEvent mFoundEvent;
    private FoundEventItem eventItem;


    static class ListItemView {
        public TextView name;
        public TextView groupName;
        public TextView intro;
        public ImageView logo;
        public ImageView event;
        public TextView time;
        public TextView location;
        public ImageButton takepart_btn;
        public EventBrief eventInfo;
    }

    public EventAdapter(LayoutInflater listcontaner, List<EventBrief> eventData,FoundEvent mFoundEvent) {
        this.listcontaner = listcontaner;
        this.eventList = eventData;
        this.mFoundEvent = mFoundEvent;
        this.activity = (BaseActivity) listcontaner.getContext();
        this.dbManager = this.activity.getDBManager();
    }

    @Override
    public int getCount() {
        if (eventList == null) return 0;
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

    public void setList(List<EventBrief> groupData) {
        this.eventList = groupData;
        this.notifyDataSetChanged();
    }

    public void setFragment(FoundEvent mFoundEvent){
        this.mFoundEvent = mFoundEvent;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       if (null == convertView) {
            item = new ListItemView();
            convertView = listcontaner.inflate(R.layout.found_event_item, null);
            item.name = (TextView) convertView.findViewById(R.id.event_name);
            item.intro = (TextView) convertView.findViewById(R.id.event_intro);
            item.time = (TextView) convertView.findViewById(R.id.time);
            item.location = (TextView) convertView.findViewById(R.id.location);
            item.logo = (ImageView) convertView.findViewById(R.id.group_logo3);
            item.event = (ImageView) convertView.findViewById(R.id.event_post);
            item.takepart_btn = (ImageButton) convertView.findViewById(R.id.takepartSBtn);
            item.eventInfo = new EventBrief();
            convertView.setTag(item);
       } else {
           item = (ListItemView) convertView.getTag();
       }

        if(dbManager==null){
            activity = (BaseActivity) listcontaner.getContext();
            dbManager = this.activity.getDBManager();
        }

        MyListener listener = new MyListener(dbManager);

        item.eventInfo.setName(eventList.get(position).getName());
        item.eventInfo.setEventIntro(eventList.get(position).getEventIntro());
        item.eventInfo.setTimeBegin(eventList.get(position).getTimeBegin());
        item.eventInfo.setTimeEnd(eventList.get(position).getTimeEnd());
        item.eventInfo.setID(eventList.get(position).getID());
        item.eventInfo.setPicUri(eventList.get(position).getPicUri());
        item.eventInfo.setEventUri(eventList.get(position).getEventUri());
        item.eventInfo.setLocation(eventList.get(position).getLocation());

        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sft = new SimpleDateFormat("HH:mm");
        item.name.setText(item.eventInfo.getName());
        item.location.setText(item.eventInfo.getLocation());
        currentTime = (long) System.currentTimeMillis();

        if ((currentTime > item.eventInfo.getTimeEnd()) && (item.eventInfo.getTimeEnd() != 0)) {
            if(sf.format(item.eventInfo.getTimeBegin()).contains(sf.format(item.eventInfo.getTimeEnd()))){
                item.time.setText(sf.format(item.eventInfo.getTimeBegin())+" "+sft.format(item.eventInfo.getTimeBegin())+"-"+ sft.format(item.eventInfo.getTimeEnd()));

            }else{
                item.time.setText(sf.format(item.eventInfo.getTimeBegin()) + "-" + sf.format(item.eventInfo.getTimeEnd())+ " "+sft.format(item.eventInfo.getTimeBegin()) + "-" + sft.format(item.eventInfo.getTimeEnd()));
            }
            item.takepart_btn.setBackgroundResource(R.drawable.takepart_s_n);
            item.takepart_btn.setClickable(false);
        } else if (item.eventInfo.getTimeEnd() == 0) {
            item.time.setText(R.string.no_limit);
            if (dbManager.getTableEvent().queryEventById(item.eventInfo.getID()) == null) {
                Log.e("id", Long.toString(item.eventInfo.getID()));
                item.takepart_btn.setBackgroundResource(R.drawable.takepart_s);
                item.takepart_btn.setClickable(true);
            } else {
                item.takepart_btn.setBackgroundResource(R.drawable.takepart_s_j);
                item.takepart_btn.setClickable(false);
            }
        } else {
            if(sf.format(item.eventInfo.getTimeBegin()).contains(sf.format(item.eventInfo.getTimeEnd()))){
                item.time.setText(sf.format(item.eventInfo.getTimeBegin())+" "+sft.format(item.eventInfo.getTimeBegin())+"-"+ sft.format(item.eventInfo.getTimeEnd()));

            }else{
                item.time.setText(sf.format(item.eventInfo.getTimeBegin()) + "-" + sf.format(item.eventInfo.getTimeEnd())+ " "+sft.format(item.eventInfo.getTimeBegin()) + "-" + sft.format(item.eventInfo.getTimeEnd()));
            }
            if (dbManager.getTableEvent().queryEventById(item.eventInfo.getID()) == null) {
                item.takepart_btn.setBackgroundResource(R.drawable.takepart_s);
                item.takepart_btn.setClickable(true);
            } else {
                item.takepart_btn.setBackgroundResource(R.drawable.takepart_s_j);
                item.takepart_btn.setClickable(false);
            }

        }

        item.takepart_btn.setTag(-2);
        item.takepart_btn.setOnClickListener(listener);

        item.name.setTag(-3);
        item.name.setOnClickListener(listener);
        item.event.setTag(-3);
        item.event.setOnClickListener(listener);
        item.time.setTag(-3);
        item.time.setOnClickListener(listener);
        item.intro.setTag(-3);
        item.intro.setOnClickListener(listener);
        item.location.setTag(-3);
        item.location.setOnClickListener(listener);
        item.logo.setTag(-3);
        item.logo.setOnClickListener(listener);

        if (!"".equals(item.eventInfo.getEventIntro()) && (item.eventInfo.getEventIntro().length() > 30)) {
            String str = item.eventInfo.getEventIntro().trim().substring(0, 34);
            item.intro.setText(str);
        } else {
            item.intro.setText(item.eventInfo.getEventIntro());
        }


        if (null != item.eventInfo.getLogoUri()
                && !"".equals(item.eventInfo.getLogoUri()) && !"null".equals(item.eventInfo.getLogoUri())) {
            imageLoader.displayImage(item.eventInfo.getLogoUri(), item.logo);
        } else {
            item.logo.setImageResource(R.drawable.logo_default);
        }

        if (null != item.eventInfo.getEventUri()
                && !"".equals(item.eventInfo.getEventUri()) && !"null".equals(item.eventInfo.getEventUri())) {
            imageLoader.displayImage(item.eventInfo.getEventUri(), item.event);
        } else {
            item.event.setImageResource(R.drawable.event_eg);
        }

        this.notifyDataSetChanged();

        return convertView;

    }

    private void addNewEvent(ListItemView newItem,DBManager mDbManager) {
        EventRecord eventRecord = new EventRecord();

        eventRecord.introduction = newItem.eventInfo.getName();
        eventRecord.id = newItem.eventInfo.getID();
        eventRecord.timeend = newItem.eventInfo.getTimeEnd();
        eventRecord.timebegin = newItem.eventInfo.getTimeBegin();
        if (item.eventInfo.getTimeEnd() == 0) {
            eventRecord.timebegin = currentTime;
            eventRecord.timeend = currentTime;

        }
        eventRecord.type = 0;
        eventRecord.uid = UserServer.getInstance().getUserid();

        if (mDbManager.getTableEvent().addEvent(eventRecord) == -1) {
            Log.e("add", "error in adding event");
        } else {
            Log.e("take part", Long.toString(eventRecord.id));
        }

    }



    public class MyListener implements View.OnClickListener {
        private DBManager localDB;

        public MyListener(DBManager localDB){
            this.localDB = localDB;
        }

        @Override
        public void onClick(View v) {
            if(((Integer)v.getTag())==-2){
                View convertView=(View)v.getParent().getParent();
                Log.e("covertView",convertView.toString());
                ListItemView newItem =(ListItemView) convertView.getTag();
                if(localDB.getTableEvent().queryEventById(newItem.eventInfo.getID()) == null) {
                    addNewEvent(newItem,localDB);
                    v.setBackgroundResource(R.drawable.takepart_s_j);
                    v.setClickable(false);
                }
            }
            else if(((Integer)v.getTag())==-3){
                View convertView=(View)v.getParent();
                Log.e("covertView",convertView.toString());
                ListItemView newItem =(ListItemView) convertView.getTag();
                ListItemView newItem2 =(ListItemView) convertView.getTag();
                mFragmentManager = mFoundEvent.getFragmentManager();
                FragmentTransaction mFmTrans = mFragmentManager.beginTransaction();

                eventItem = new FoundEventItem();
                if (mFragmentManager.findFragmentByTag("eventItem") != null) {
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("eventItem"));
                }
                Bundle args = new Bundle();
                args.putString("name", newItem2.eventInfo.getGroupName());
                args.putString("eventUri", newItem2.eventInfo.getEventUri());
                args.putString("name", newItem2.eventInfo.getName());
                args.putLong("id", newItem2.eventInfo.getID());
                args.putLong("timeBegin", newItem2.eventInfo.getTimeBegin());
                args.putLong("timeEnd", newItem2.eventInfo.getTimeEnd());
                args.putString("location", newItem2.eventInfo.getLocation());
                args.putString("uri", newItem2.eventInfo.getLogoUri());
                args.putString("intro", newItem2.eventInfo.getEventIntro());
                eventItem.setArguments(args);

                Log.e("open item", Long.toString(newItem2.eventInfo.getID()));

                mFmTrans.add(R.id.id_content, eventItem, "eventItem");
                mFmTrans.addToBackStack("eventItem");
                mFmTrans.commit();
                mFmTrans.hide(mFoundEvent);

            }


        }
    }
}