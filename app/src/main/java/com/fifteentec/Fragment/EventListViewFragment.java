package com.fifteentec.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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



public class EventListViewFragment extends Fragment{

    private EventListView mListView;
    private ArrayList<Integer> mCurDate;

    private Context mContext;
    private static final String DATE = "DATE";
    private static final String HEIGHT = "HEIGHT";
    private int mViewHeight;
    private EventListFragmentListener mEvnetFragmentListener;

    private EventListView mEvnetView;

    public interface EventListFragmentListener{
        public void ListDateChange(ArrayList<Integer> list);
    }

    public void setEventFragmentListener(EventListFragmentListener listener){
        mEvnetFragmentListener = listener;
    }

    public static EventListViewFragment newInstance(ArrayList<Integer> date){

        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(DATE, date);
        EventListViewFragment eventListViewFragment = new EventListViewFragment();
        eventListViewFragment.setArguments(bundle);
        return eventListViewFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCurDate = bundle.getIntegerArrayList(DATE);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mEvnetView =  new EventListView(mContext);
        mEvnetView.init(new ArrayList<>(mCurDate));
        mEvnetView.setEvnetListDateChangeListener(new EventListView.EventListListener() {
            @Override
            public void DateChange(ArrayList<Integer> list) {
                mCurDate.clear();
                mCurDate.add(list.get(0));
                mCurDate.add(list.get(1));
                mCurDate.add(list.get(2));
                mCurDate.add(list.get(3));
                mEvnetFragmentListener.ListDateChange(list);
            }
        });


        return mEvnetView;
    }


    public void UpdateTime(ArrayList<Integer> time){
        mCurDate.clear();
        mCurDate.add(time.get(0));
        mCurDate.add(time.get(1));
        mCurDate.add(time.get(2));
        mCurDate.add(time.get(3));
        mEvnetView.init(new ArrayList<>(mCurDate));
    }

}
