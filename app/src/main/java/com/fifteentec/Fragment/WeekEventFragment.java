package com.fifteentec.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Database.EventRecord;
import com.fifteentec.Component.calendar.WeekEventView;

import java.util.ArrayList;


public class WeekEventFragment extends Fragment {

    private static final String DATE = "Date";

    private ArrayList<Integer> mCurDate = new ArrayList<>();

    private WeekViewFragmentLinstener mWeekViewFragmentLinstener;
    private WeekEventView weekEventView;

    public interface WeekViewFragmentLinstener{
        void CheckExist(long rid);
        void CreateRecord(int TYPE,EventRecord eventRecord);
    }

    public void setmWeekViewFragmentLinstener(WeekViewFragmentLinstener mWeekViewFragmentLinstener) {
        this.mWeekViewFragmentLinstener = mWeekViewFragmentLinstener;
    }

    public static WeekEventFragment newInstance(ArrayList<Integer> date){
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(DATE, date);
        WeekEventFragment weekEventFragment = new WeekEventFragment();
        weekEventFragment.setArguments(bundle);
        return weekEventFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mCurDate = bundle.getIntegerArrayList(DATE);
        }
    }

    public void EventRecordUpdate(long rid,boolean exist){
        weekEventView.UpdateView(rid,exist);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        weekEventView = WeekEventView.newInstance(getActivity(),mCurDate);
        weekEventView.setmWeekViewListener(new WeekEventView.WeekViewListener() {
            @Override
            public void CheckExistItem(long rid) {
                mWeekViewFragmentLinstener.CheckExist(rid);
            }

            @Override
            public void CreatRecord(int Type, EventRecord eventRecord) {
                mWeekViewFragmentLinstener.CreateRecord(Type,eventRecord);
            }
        });
        return weekEventView;
    }
}
