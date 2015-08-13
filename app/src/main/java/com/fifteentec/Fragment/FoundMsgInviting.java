package com.fifteentec.Fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fifteentec.FoundAdapter.InvitedAdapter;
import com.fifteentec.FoundAdapter.InvitingAdapter;
import com.fifteentec.item.InvitingBrief;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundMsgInviting extends Fragment {
    private List<InvitingBrief> invitingList = new ArrayList<InvitingBrief>();
    private ListView list_inviting;
    private InvitingAdapter invitingAdapter;
    private FragmentManager mFragmentManager;
    private FoundMsgItem invitedItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.found_msg_invited, null);
        initListView(view);

        return view;
    }
    private void initListView(View parentView){
        createEventList(5,invitingList);
        list_inviting = (ListView) parentView.findViewById(R.id.listView_invited);
        invitingAdapter = new InvitingAdapter(getActivity().getLayoutInflater(),invitingList);
        list_inviting.setAdapter(invitingAdapter);

        list_inviting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentManager = FoundMsgInviting.this.getParentFragment().getFragmentManager();
                FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                if(invitedItem==null){
                    invitedItem = new FoundMsgItem();
                }
                if(mFragmentManager.findFragmentByTag("invitedItem")!=null){
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("invitedItem"));
                }

                Bundle args = new Bundle();
                args.putString("name", invitingList.get(position).getFriendName());
                args.putString("intro", invitingList.get(position).getInvitedContent());
                args.putString("uri", invitingList.get(position).getLogoUri());
                args.putString("time",invitingList.get(position).getTime());
                args.putBoolean("invited",false);

                invitedItem.setArguments(args);

                mFmTrans.add(R.id.id_content, invitedItem, "groupItem");

                mFmTrans.addToBackStack("groupItem");
                mFmTrans.commit();
                mFmTrans.hide(FoundMsgInviting.this.getParentFragment());

            }
        });

    }

    private void createEventList(int invitedNum,List<InvitingBrief> list){
        list.clear();
        for(int i=0;i<invitedNum;i++){
            InvitingBrief newItem = new InvitingBrief();
            newItem.setFriendName("friend");
            newItem.setInvitedContent("party");
            newItem.setTime("2020-8-12 08:00");
            list.add(newItem);
        }
    }

    public List<InvitingBrief> getInvitingList(){
        return invitingList;
    }



}
