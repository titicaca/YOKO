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
import com.fifteentec.item.InvitedBrief;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundMsgInvited extends Fragment {
    private List<InvitedBrief> invitedList = new ArrayList<InvitedBrief>();
    private ListView list_invited;
    private InvitedAdapter invitedAdapter;
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
        createEventList(5,invitedList);
        list_invited = (ListView) parentView.findViewById(R.id.listView_invited);
        invitedAdapter = new InvitedAdapter(getActivity().getLayoutInflater(),invitedList);
        list_invited.setAdapter(invitedAdapter);

        list_invited.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentManager = FoundMsgInvited.this.getParentFragment().getFragmentManager();
                FragmentTransaction mFmTrans= mFragmentManager.beginTransaction();

                if(invitedItem==null){
                    invitedItem = new FoundMsgItem();
                }
                if(mFragmentManager.findFragmentByTag("invitedItem")!=null){
                    mFmTrans.remove(mFragmentManager.findFragmentByTag("invitedItem"));
                }

                Bundle args = new Bundle();
                args.putString("name", invitedList.get(position).getFriendName());
                args.putString("intro", invitedList.get(position).getInvitedContent());
                args.putString("uri", invitedList.get(position).getLogoUri());
                args.putString("time",invitedList.get(position).getTime());

                invitedItem.setArguments(args);

                mFmTrans.add(R.id.id_content, invitedItem, "groupItem");

                mFmTrans.addToBackStack("groupItem");
                mFmTrans.commit();
                mFmTrans.hide(FoundMsgInvited.this.getParentFragment());

            }
        });

    }

    private void createEventList(int invitedNum,List<InvitedBrief> list){
        list.clear();
        for(int i=0;i<invitedNum;i++){
            InvitedBrief newItem = new InvitedBrief();
            newItem.setFriendName("friend");
            newItem.setInvitedContent("party");
            newItem.setTime("2020-8-12 08:00");
            list.add(newItem);
        }
    }

    public List<InvitedBrief> getInvitedList(){
        return invitedList;
    }


}
