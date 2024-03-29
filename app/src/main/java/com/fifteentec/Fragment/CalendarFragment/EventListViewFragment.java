package com.fifteentec.Fragment.CalendarFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fifteentec.Component.calendar.EventListView;

import java.util.ArrayList;



public class EventListViewFragment extends Fragment{


    private ArrayList<Integer> mCurDate;

    private Context mContext;
    private static final String DATE = "DATE";
    private static final String HEIGHT = "HEIGHT";
    private EventListFragmentListener mEvnetFragmentListener;

    private EventListView mEvnetView;

    public interface EventListFragmentListener{
        void ListDateChange(ArrayList<Integer> list);
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


        return mEvnetView;
    }

    public void UpdateView(){
        mEvnetView.init(mCurDate);
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
