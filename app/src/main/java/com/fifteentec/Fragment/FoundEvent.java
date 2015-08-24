package com.fifteentec.Fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.API.APIJsonCallbackResponse;
import com.API.APIUrl;
import com.API.APIUserServer;
import com.android.volley.RequestQueue;
import com.fifteentec.Component.Parser.DataSyncServerParser;
import com.fifteentec.FoundAdapter.EventAdapter;;
import com.fifteentec.FoundAdapter.GroupAdapter;
import com.fifteentec.item.EventBrief;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundEvent extends Fragment {
    private List<EventBrief> eventList = new ArrayList<EventBrief>();
    private ListView events;
    private EventAdapter eventAdapter;
    private FoundEventItem eventItem;
    RequestQueue mRequestQueue;
    BaseActivity baseActivity;

    private FragmentManager mFragmentManager;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.fragment_found_event, null);
            initDBfunctions();
            initListView(view);

            return view;
        }

    private void initDBfunctions() {
        baseActivity = (BaseActivity) this.getActivity();
        mRequestQueue = baseActivity.getRequestQueue();

        Log.e("initial DB", "get into function");


        new APIUserServer.JsonGet(APIUrl.URL_EVENTS_GET, null, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject object = this.getResponse();
                Log.e("Event object", object.toString());

                eventList = DataSyncServerParser.parseEventBriefInfo(object);
                eventAdapter = new EventAdapter(getActivity().getLayoutInflater(),eventList);
                events.setAdapter(eventAdapter);
//                if(groupList!=null){
//                    joinedAdapter = new GroupAdapter(getActivity().getLayoutInflater(),groupList,true);
//                    mListView.setAdapter(joinedAdapter);
//                }
            }
        }, mRequestQueue, null).send();
    }

        private void initListView(View parentView){
            //createEventList(5,eventList);
            events = (ListView) parentView.findViewById(R.id.listView_event);
            //eventAdapter = new EventAdapter(getActivity().getLayoutInflater(),eventList);
            //events.setAdapter(eventAdapter);

            events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mFragmentManager = FoundEvent.this.getParentFragment().getFragmentManager();
                    FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                    if(eventItem==null){
                        eventItem = new FoundEventItem();
                    }
                    if(mFragmentManager.findFragmentByTag("eventItem")!=null){
                        mFmTrans.remove(mFragmentManager.findFragmentByTag("eventItem"));
                    }

                    Bundle args = new Bundle();
                    args.putString("name", eventList.get(position).getGroupName());
                    args.putString("intro", eventList.get(position).getEventIntro());
                    args.putString("uri", eventList.get(position).getLogoUri());
                    args.putString("time", eventList.get(position).getTime());
                    args.putString("location", eventList.get(position).getLocation());
                    args.putString("tags", eventList.get(position).getTags());
                    eventItem.setArguments(args);

                    mFmTrans.add(R.id.id_content, eventItem, "eventItem");

                    mFmTrans.addToBackStack("eventItem");
                    mFmTrans.commit();
                    mFmTrans.hide(FoundEvent.this.getParentFragment());

                }
            });

        }

        private void createEventList(int eventNum,List<EventBrief> list){
            list.clear();
            for(int i=0;i<eventNum;i++){
                EventBrief newItem = new EventBrief();
                newItem.setEventIntro("default");
                newItem.setGroupName("default");
                newItem.setTime("2020-8-12 08:00");
                newItem.setLocation("SJTU");
                list.add(newItem);
            }
        }



}

