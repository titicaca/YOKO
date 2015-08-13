package com.fifteentec.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fifteentec.FoundAdapter.EventAdapter;
import com.fifteentec.FoundAdapter.InvitedAdapter;
import com.fifteentec.item.EventBrief;
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
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
