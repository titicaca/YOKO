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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIUrl;
import com.API.APIUserServer;
import com.android.volley.RequestQueue;
import com.fifteentec.Component.FoundItems.PullToRefresh;
import com.fifteentec.Component.Parser.FoundDataParser;
import com.fifteentec.FoundAdapter.GroupAdapter;
import com.fifteentec.Component.FoundItems.GroupBrief;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundGroup extends Fragment {
    private ImageView ivDeleteText;
    private EditText etSearch;
    private List<GroupBrief> groupList = new ArrayList<GroupBrief>();
    private List<GroupBrief> tempList = new ArrayList<GroupBrief>();
    private ListView mListView;
    private GroupAdapter joinedAdapter;
    private FragmentManager mFragmentManager;
    private FoundGroupItem groupItem;
    private int lastItem = 0;
    private int page = 0;
    BaseActivity baseActivity;
    RequestQueue mRequestQueue;
    PullToRefresh refreshableView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_found_group, null);
        mListView = (ListView) view.findViewById(R.id.listView1);

        initListView(view);
        initDBfunctions();
        initSearchFrame(view);
        baseActivity = (BaseActivity)this.getActivity();
        mRequestQueue = baseActivity.getRequestQueue();

        refreshableView = (PullToRefresh) view.findViewById(R.id.refreshable_view);


        refreshableView.setOnRefreshListener(new PullToRefresh.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListView.getAdapter() == joinedAdapter) {
                    new APIUserServer.JsonGet(APIUrl.URL_JOINED_GROUP_GET+ "/page/0/4",null , null, new APIJsonCallbackResponse() {
                        @Override
                        public void run() {
                            JSONObject object=this.getResponse();
                            Log.e("object", object.toString());
                            groupList = FoundDataParser.parseGroupBriefInfo(object);
                            if(groupList!=null) {
                                joinedAdapter.setList(groupList);
                                joinedAdapter.notifyDataSetChanged();
                            }
                        }
                    }, mRequestQueue, null).send();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);
        return view;
    }
    private void initDBfunctions()
    {
        baseActivity = (BaseActivity)this.getActivity();
        mRequestQueue = baseActivity.getRequestQueue();

        Log.e("initial DB", "get into function");
        new APIUserServer.JsonGet(APIUrl.URL_JOINED_GROUP_GET+ "/page/0/4",null , null, new APIJsonCallbackResponse() {
        //new APIUserServer.JsonGet(APIUrl.URL_JOINED_GROUP_GET+ "/page/0/2",null , null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject object=this.getResponse();
                Log.e("object", object.toString());
                groupList = FoundDataParser.parseGroupBriefInfo(object);
                if(groupList!=null) {
                    joinedAdapter = new GroupAdapter(getActivity().getLayoutInflater(), groupList, true);
                    mListView.setAdapter(joinedAdapter);
                }
            }
        }, mRequestQueue, null).send();
    }



    private void initSearchFrame(View parentView){
        ivDeleteText = (ImageView) parentView.findViewById(R.id.ivDeleteText);
        etSearch = (EditText) parentView.findViewById(R.id.etSearch);

        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initListView(View parentView){
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    if(lastItem+1 == joinedAdapter.getCount()){
                        page++;
                        new APIUserServer.JsonGet(APIUrl.URL_JOINED_GROUP_GET+"/page/"+page+"/4",null , null, new APIJsonCallbackResponse() {
                            @Override
                            public void run() {
                                JSONObject object=this.getResponse();
                                Log.e("object", object.toString());
                                tempList = FoundDataParser.parseGroupBriefInfo(object);
                                if(groupList == null||groupList.size()==0||page<0){
                                    Toast.makeText(getActivity(),"It is the last group",Toast.LENGTH_SHORT);
                                }
                                else {
                                    groupList.addAll(tempList);
                                    joinedAdapter.setList(groupList);
                                    joinedAdapter.notifyDataSetChanged();
                                }
                            }
                        }, mRequestQueue, null).send();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = mListView.getLastVisiblePosition();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentManager = FoundGroup.this.getParentFragment().getFragmentManager();
                FragmentTransaction mFmTrans = mFragmentManager.beginTransaction();

                groupItem = new FoundGroupItem();

                if (mFragmentManager.findFragmentByTag("groupItem") != null) {
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("groupItem"));
                }

                Bundle args = new Bundle();
                args.putString("name", groupList.get(position).getGroupName());
                args.putString("intro", groupList.get(position).getGroupIntro());
                args.putString("uri", groupList.get(position).getLogoUri());
                groupItem.setArguments(args);

                mFmTrans.add(R.id.id_content, groupItem, "groupItem");

                mFmTrans.addToBackStack("groupItem");
                mFmTrans.commit();
                mFmTrans.hide(FoundGroup.this.getParentFragment());
            }
        });
    }

}
