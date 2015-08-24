package com.fifteentec.FoundFragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.API.APIJsonCallbackResponse;
import com.API.APIUrl;
import com.API.APIUserServer;
import com.android.volley.RequestQueue;
import com.fifteentec.Component.Parser.FoundDataParser;
import com.fifteentec.FoundAdapter.GroupAdapter;
import com.fifteentec.FoundItems.GroupBrief;
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
    private List<GroupBrief> groupListR = new ArrayList<GroupBrief>();
    private ListView mListView;
    private GroupAdapter joinedAdapter,recommendedAdapter;
    private FragmentManager mFragmentManager;
    private FoundGroupItem groupItem;
    BaseActivity baseActivity;
    RequestQueue mRequestQueue;
    Button joined_btn;
    Button reco_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_found_group, null);

        initListView(view);
        initDBfunctions();
        initSearchFrame(view);
        baseActivity = (BaseActivity)this.getActivity();
        mRequestQueue = baseActivity.getRequestQueue();

        joined_btn = (Button)view.findViewById(R.id.joined_btn);
        reco_btn = (Button)view.findViewById(R.id.reco_btn);

        joined_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListView.setAdapter(joinedAdapter);

            }
        });

        reco_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.setAdapter(recommendedAdapter);
            }
        });
        return view;
    }
    private void initDBfunctions()
    {
        baseActivity = (BaseActivity)this.getActivity();
        mRequestQueue = baseActivity.getRequestQueue();

        Log.e("initial DB", "get into function");


        new APIUserServer.JsonGet(APIUrl.URL_JOINED_GROUP_GET,null , null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject object=this.getResponse();
                Log.e("object", object.toString());

                groupList = FoundDataParser.parseGroupBriefInfo(object);
                if(groupList!=null){
                    joinedAdapter = new GroupAdapter(getActivity().getLayoutInflater(),groupList,true);
                    mListView.setAdapter(joinedAdapter);
                }
            }
        }, mRequestQueue, null).send();

//        new APIUserServer.JsonGet(APIUrl.URL_GROUP_GET,null , null, new APIJsonCallbackResponse() {
//            @Override
//            public void run() {
//                JSONObject object=this.getResponse();
//                Log.e("object", object.toString());
//
//                groupListR = DataSyncServerParser.parseGroupBriefInfo(object);
//            }
//        }, mRequestQueue, null).send();
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
        mListView = (ListView) parentView.findViewById(R.id.listView1);
 //       joinedAdapter = new GroupAdapter(getActivity().getLayoutInflater(),groupList,true);
 //       recommendedAdapter = new GroupAdapter(getActivity().getLayoutInflater(),groupListR,false);
  //      mListView.setAdapter(joinedAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentManager = FoundGroup.this.getParentFragment().getFragmentManager();
                FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                if(groupItem==null){
                    groupItem = new FoundGroupItem();
                }
                if(mFragmentManager.findFragmentByTag("groupItem")!=null){
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
