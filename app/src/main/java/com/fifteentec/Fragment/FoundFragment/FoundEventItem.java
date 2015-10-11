package com.fifteentec.Fragment.FoundFragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Database.DBManager;
import com.Database.EventRecord;
import com.fifteentec.Component.FoundItems.CircularImage;
import com.fifteentec.Component.FoundItems.EventBrief;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.ImageLoader;

;import java.text.SimpleDateFormat;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundEventItem extends Fragment {
    private ImageView logo;
    private TextView name;
    private TextView groupName;
    private int id;
    private TextView detail;
    private ImageButton add;
    private ImageButton back;
    private TextView time;
    private CircularImage head;
    private TextView location;
    private FragmentManager mFragmentManager;
    private BaseActivity activity;
    private DBManager dbManager;
    private EventBrief item;


    protected ImageLoader imageLoader = ImageLoader.getInstance();
    long currentTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.found_event_item_detail, null);
        logo = (ImageView)view.findViewById(R.id.group_detail_event_logo);
        groupName = (TextView)view.findViewById(R.id.group_detail_name);
        name = (TextView)view.findViewById(R.id.event_detail_name);
        add = (ImageButton)view.findViewById(R.id.add);
        back = (ImageButton)view.findViewById(R.id.back_arrow);
        detail = (TextView)view.findViewById(R.id.web);
        time = (TextView)view.findViewById(R.id.time_item);
        location = (TextView)view.findViewById(R.id.location_item);
        head = (CircularImage)view.findViewById(R.id.cover_group_detail_event_logo);
        head.setBackgroundResource(R.color.gray);

        mFragmentManager = FoundEventItem.this.getFragmentManager();
        this.activity = (BaseActivity)this.getActivity();
        this.dbManager = this.activity.getDBManager();
        editItemContent();

        location.setText(getArguments().getString("location"));

        SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sft = new SimpleDateFormat("HH:mm");
        currentTime = (long)System.currentTimeMillis();
        if((currentTime>item.getTimeEnd())&&(item.getTimeEnd()!=0)){
            if(sf.format(item.getTimeBegin()).contains(sf.format(item.getTimeEnd()))){
                time.setText(sf.format(item.getTimeBegin())+" "+sft.format(item.getTimeBegin())+"-"+ sft.format(item.getTimeEnd()));

            }else{
                time.setText(sf.format(item.getTimeBegin()) + "-" + sf.format(item.getTimeEnd())+ " "+sft.format(item.getTimeBegin()) + "-" + sft.format(item.getTimeEnd()));
            }
            add.setBackgroundResource(R.drawable.takepart_n);
            add.setClickable(false);
        }
        else if(item.getTimeEnd()==0){
            time.setText(R.string.no_limit);
            add.setBackgroundResource(R.drawable.takepart);
            add.setClickable(true);
            if(dbManager.getTableEvent().queryEventById(item.getID())==null){
                add.setBackgroundResource(R.drawable.takepart);
                add.setClickable(true);
            }
            else{
                Log.e("DB id",Long.toString(dbManager.getTableEvent().queryEventById(item.getID()).id));
                add.setBackgroundResource(R.drawable.takepart_j);
                add.setClickable(false);
            }
        }
        else{
            if(sf.format(item.getTimeBegin()).contains(sf.format(item.getTimeEnd()))){
                time.setText(sf.format(item.getTimeBegin())+" "+sft.format(item.getTimeBegin())+"-"+ sft.format(item.getTimeEnd()));

            }else{
                time.setText(sf.format(item.getTimeBegin()) + "-" + sf.format(item.getTimeEnd())+ " "+sft.format(item.getTimeBegin()) + "-" + sft.format(item.getTimeEnd()));
            }
            if(dbManager.getTableEvent().queryEventById(item.getID())==null){
                add.setBackgroundResource(R.drawable.takepart);
                add.setClickable(true);
            }
            else{
                Log.e("DB id",Long.toString(dbManager.getTableEvent().queryEventById(item.getID()).id));
                add.setBackgroundResource(R.drawable.takepart_j);
                add.setClickable(false);
            }
        }





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbManager.getTableEvent().queryEventById(item.getID()) == null) {
                    addNewEvent();
                    v.setBackgroundResource(R.drawable.takepart_j);
                    v.setClickable(false);
                }
                ;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragmentManager.getBackStackEntryCount()>0){
                    mFragmentManager.popBackStack();

                }

            }
        });

        location.setText(getArguments().getString("location"));
        name.setText(getArguments().getString("name"));
        groupName.setText(getArguments().getString("groupName"));



        if(null!=getArguments().getString("uri")&&!"".equals(getArguments().getString("uri"))){
            imageLoader.displayImage(getArguments().getString("uri"),logo);
        }
//        else{
//            logo.setImageResource(R.drawable.logo_default);
//        }

        if(null!=getArguments().getString("intro")&&!"".equals(getArguments().getString("intro"))){
            detail.setText(getArguments().getString("intro"));
        }
        else{
            detail.setText("");
        }


        return view;
    }

    private void editItemContent(){
        if(item==null)item = new EventBrief();
        item.setName(getArguments().getString("name"));
        item.setEventIntro(getArguments().getString("intro"));
        item.setTimeBegin(getArguments().getLong("timeBegin"));
        item.setTimeEnd(getArguments().getLong("timeEnd"));
        item.setID(getArguments().getLong("id"));
    //    Log.e("item id",Long.toString(item.getID()));
    }

    private void addNewEvent(){
        EventRecord eventRecord = new EventRecord();

        eventRecord.introduction = item.getName();
        eventRecord.id = item.getID();
        eventRecord.timeend = item.getTimeEnd();
        eventRecord.timebegin = item.getTimeBegin();
        if(item.getTimeEnd()==0) {
            eventRecord.timebegin = currentTime;
            eventRecord.timeend = currentTime;

        }
        eventRecord.type = 0;
        eventRecord.uid = UserServer.getInstance().getUserid();

//        if(dbManager.getTableEvent().addEvent(eventRecord)==-1){
//            Log.e("add","error in adding event");
//        }
//        else {
//            Log.e("add",Long.toString(eventRecord.id));
//        }

    }

}

