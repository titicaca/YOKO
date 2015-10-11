package com.fifteentec.Fragment.CalendarFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Database.EventRecord;
import com.fifteentec.Component.calendar.CalendarView;
import com.fifteentec.Component.calendar.EventManager;
import com.fifteentec.Component.calendar.WeekEventView;
import com.fifteentec.yoko.BaseActivity;
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
        void UpdateTime(ArrayList<Integer> Date);
        void ShowDetailView(GregorianCalendar date);
    }

    public void UpdateWeekView(){
        weekEventView.UpdateEventArray();
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


    public void EventRecordUpdate(){

        weekEventView.UpdateView();

    }

    private void UpdateFragmentTime(GregorianCalendar gregorianCalendar){
        mCurDate.clear();
        mCurDate.add(gregorianCalendar.get(Calendar.YEAR));
        mCurDate.add(gregorianCalendar.get(Calendar.MONTH));
        mCurDate.add(gregorianCalendar.get(Calendar.DAY_OF_MONTH));
        mCurDate.add(gregorianCalendar.get(Calendar.DAY_OF_WEEK));
        mWeekViewFragmentLinstener.UpdateTime(mCurDate);
        weekEventView.UpdateViewByTime(mCurDate);

    }

    public void deleteRecord(long rid){
        if(weekEventView!=null){
            weekEventView.UpdateView(rid);
        }
    }

    public void UpdateToView(GregorianCalendar time){
        if(weekEventView!=null&&calendarView !=null) {
            UpdateFragmentTime(time);
            calendarView.initView(mCurDate);
        }
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

            @Override
            public void CalEnable(boolean enable) {
                calendarView.TouchEnable(enable);
            }
        });
        calendarView = (CalendarView)view.findViewById(R.id.id_cal_view);
        calendarView.initView(mCurDate);
        calendarView.setmCalendarListener(new CalendarView.CalendarListener() {
            @Override
            public void UpdateTime(GregorianCalendar time) {
                UpdateFragmentTime(time);
            }

            @Override
            public void ShowDayDetail(GregorianCalendar time) {
                mWeekViewFragmentLinstener.ShowDetailView(time);
            }

            @Override
            public void ModeSwitch(boolean isWeek) {
                weekEventView.setViewEnable(isWeek);
            }
        });

        return view;
    }
}
