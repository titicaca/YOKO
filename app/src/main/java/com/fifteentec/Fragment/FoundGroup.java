package com.fifteentec.Fragment;


import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fifteentec.FoundAdapter.JoinedGroupAdapter;
import com.fifteentec.FoundAdapter.RecommendedGroupAdapter;
import com.fifteentec.item.GroupBrief;
import com.fifteentec.yoko.R;

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
    private ListView joined,recommeded;
    private JoinedGroupAdapter joinedAdapter;
    private RecommendedGroupAdapter recommendedAdapter;
    private FragmentManager mFragmentManager;
    private FoundGroupItem groupItem;
    private FoundGroupItemR groupItemR;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_found_group, null);
        initSearchFrame(view);
        initListView(view);

        return view;
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
        createGroupList(5,groupList);
        createGroupList(6,groupListR);
        joined = (ListView) parentView.findViewById(R.id.listView1);
        joinedAdapter = new JoinedGroupAdapter(getActivity().getLayoutInflater(),groupList);
        joined.setAdapter(joinedAdapter);

        joined.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        recommeded = (ListView) parentView.findViewById(R.id.listView2);
        recommendedAdapter= new RecommendedGroupAdapter(getActivity().getLayoutInflater(),groupListR);
        recommeded.setAdapter(recommendedAdapter);

        recommeded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFragmentManager = FoundGroup.this.getParentFragment().getFragmentManager();
                FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                if(groupItemR==null){
                    groupItemR = new FoundGroupItemR();
                }
                if(mFragmentManager.findFragmentByTag("groupItemR")!=null){
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("groupItemR"));
                }

                Bundle args = new Bundle();
                args.putString("name", groupListR.get(position).getGroupName());
                args.putString("intro", groupListR.get(position).getGroupIntro());
                args.putString("uri", groupListR.get(position).getLogoUri());
                groupItemR.setArguments(args);

                mFmTrans.add(R.id.id_content, groupItemR, "groupItemR");

                mFmTrans.addToBackStack("groupItemR");
                mFmTrans.commit();
                mFmTrans.hide(FoundGroup.this.getParentFragment());

            }
        });
    }

    private void createGroupList(int groupNum,List<GroupBrief> list){
        list.clear();
        for(int i=0;i<groupNum;i++){
            GroupBrief newItem = new GroupBrief();
            newItem.setGroupIntro("default");
            newItem.setGroupName("default");
            list.add(newItem);
        }
    }

}
