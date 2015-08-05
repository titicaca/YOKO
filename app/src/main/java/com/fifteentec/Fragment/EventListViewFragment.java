package com.fifteentec.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.fifteentec.Adapter.commonAdapter.ViewHolder;
import com.fifteentec.Adapter.commonAdapter.commonAdapter;
import com.fifteentec.Component.calendar.EventListView;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EventListViewFragment extends Fragment{

    private EventListView mListView;

    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext = getActivity();
        EventListView my =  new EventListView(mContext);
        my.init(new ArrayList<>(Arrays.asList(2015,7,5,4)));

        return my;
    }

}
