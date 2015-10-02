package com.fifteentec.Fragment.FoundFragment;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIUrl;
import com.API.APIUserServer;
import com.Database.DBManager;
import com.android.volley.RequestQueue;
import com.fifteentec.Component.FoundItems.PullToRefresh;
import com.fifteentec.Component.Parser.FoundDataParser;
import com.fifteentec.FoundAdapter.EventAdapter;
import com.fifteentec.Component.FoundItems.EventBrief;
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
    private List<EventBrief> tempList = new ArrayList<EventBrief>();
    //private ImageView ivDeleteText;
    //private EditText etSearch;
    private ListView events;
    private EventAdapter eventAdapter;
    private FoundEventItem eventItem;
    private int lastItem = 0;
    private int page = 0;
    RequestQueue mRequestQueue;
    BaseActivity baseActivity;
    PullToRefresh refreshableView;


    private FragmentManager mFragmentManager;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.fragment_found_event, null);
            events = (ListView) view.findViewById(R.id.listView_event);
            refreshableView = (PullToRefresh) view.findViewById(R.id.refreshable_view2);
            initDBfunctions();
//            initSearchFrame(view);
            initListView(view);

            refreshableView.setOnRefreshListener(new PullToRefresh.PullToRefreshListener() {
                @Override
                public void onRefresh() {
                    new APIUserServer.JsonGet(APIUrl.URL_EVENTS_GET+"/page/0/4", null, null, new APIJsonCallbackResponse() {
                        @Override
                        public void run() {
                            JSONObject object = this.getResponse();
                            Log.e("object", object.toString());
                            eventList = FoundDataParser.parseEventBriefInfo(object);
                            if (eventList != null) {
                                eventAdapter.setFragment(FoundEvent.this);
                                eventAdapter.setList(eventList);
                                eventAdapter.notifyDataSetChanged();

                            }
                        }
                    }, mRequestQueue, null).send();
                    refreshableView.finishRefreshing();
                }
            }, 0);
            page=0;

            return view;
        }

    private void initDBfunctions() {
        baseActivity = (BaseActivity) this.getActivity();
        mRequestQueue = baseActivity.getRequestQueue();
        new APIUserServer.JsonGet(APIUrl.URL_EVENTS_GET+ "/page/0/4", null, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject object = this.getResponse();
                Log.e("Event object", object.toString());

                eventList = FoundDataParser.parseEventBriefInfo(object);
                if(eventList!=null){
                    eventAdapter = new EventAdapter(getActivity().getLayoutInflater(),eventList,FoundEvent.this);
                    events.setAdapter(eventAdapter);
                    eventAdapter.notifyDataSetChanged();
                }
            }
        }, mRequestQueue, null).send();
    }

    private void initListView(View parentView) {
        Log.e("initial listview", "get into function");
        events.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (lastItem == eventAdapter.getCount()) {
                        page++;
                        new APIUserServer.JsonGet(APIUrl.URL_EVENTS_GET + "/page/" + page + "/4", null, null, new APIJsonCallbackResponse() {
                            @Override
                            public void run() {
                                JSONObject object = this.getResponse();
                                Log.e("object", object.toString());
                                tempList = FoundDataParser.parseEventBriefInfo(object);
                                Log.e("get parsed values", tempList.toString());
                                if (tempList == null || tempList.size() == 0) {
                                    Toast.makeText(getActivity(), "It is the last group", Toast.LENGTH_SHORT);
                                } else {
                                    eventList.addAll(tempList);
                                    eventAdapter.setFragment(FoundEvent.this);
                                    eventAdapter.setList(eventList);
                                    eventAdapter.notifyDataSetChanged();
                                }
                            }
                        }, mRequestQueue, null).send();
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem+visibleItemCount;
                }
            });

        }

}

