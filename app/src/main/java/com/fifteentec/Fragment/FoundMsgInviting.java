package com.fifteentec.Fragment;


import android.app.Fragment;
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
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
