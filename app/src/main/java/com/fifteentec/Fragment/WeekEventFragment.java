package com.fifteentec.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Database.EventRecord;
import com.fifteentec.Component.calendar.CalendarView;
import com.fifteentec.Component.calendar.WeekEventView;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class WeekEventFragment extends Fragment {

    private static final String DATE = "Date";

    private ArrayList<Integer> mCurDate = new ArrayList<>();

    private WeekViewFragmentLinstener mWeekViewFragmentLinstener;
    private WeekEventView weekEventView;
    private CalendarView calendarView;

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

    public void UpdateViewTime(ArrayList<Integer> date){
        mCurDate.clear();
        mCurDate.add(date.get(0));
        mCurDate.add(date.get(1));
        mCurDate.add(date.get(2));
        mCurDate.add(date.get(3));
        weekEventView.UpdateViewByTime(mCurDate);
    }

    public void EventRecordUpdate(long rid,boolean exist){
        weekEventView.UpdateView(rid,exist);

    }

    public void UpdateScale(){
        weekEventView.UpdateScale();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_weekevent,container,false);
        weekEventView = (WeekEventView)view.findViewById(R.id.id_week_event);
        weekEventView.initView(mCurDate);
        weekEventView.setmWeekViewListener(new WeekEventView.WeekViewListener() {
            @Override
            public void CheckExistItem(long rid) {
                mWeekViewFragmentLinstener.CheckExist(rid);
            }

            @Override
            public void CreatRecord(int Type, EventRecord eventRecord) {
                mWeekViewFragmentLinstener.CreateRecord(Type, eventRecord);
            }
        });
        calendarView = (CalendarView)view.findViewById(R.id.id_cal_view);
        calendarView.initView(mCurDate);
        calendarView.setmCalendarListener(new CalendarView.CalendarListener() {
            @Override
            public void UpdateTime(GregorianCalendar time) {
                //mDate.UpdateCur(time);
            }

            @Override
            public void ShowDayDetail(GregorianCalendar time) {
                String text = "Year:" + time.get(Calendar.YEAR) + " Month:" + time.get(Calendar.MONTH) + " Day:" + time.get(Calendar.DAY_OF_MONTH);
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
