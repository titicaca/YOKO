package com.fifteentec.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fifteentec.FoundAdapter.EventAdapter;;
import com.fifteentec.item.EventBrief;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundEvent extends Fragment {
        private List<EventBrief> eventList = new ArrayList<EventBrief>();
        private ListView events;
        private EventAdapter eventAdapter;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.fragment_found_event, null);
            initListView(view);

            return view;
        }
        private void initListView(View parentView){
            createEventList(5,eventList);
            events = (ListView) parentView.findViewById(R.id.listView_event);
            eventAdapter = new EventAdapter(getActivity().getLayoutInflater(),eventList);
            events.setAdapter(eventAdapter);

            events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

