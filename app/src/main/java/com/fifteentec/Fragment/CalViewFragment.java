package com.fifteentec.Fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fifteentec.Component.calendar.CalView;
import com.fifteentec.Component.calendar.CalendarController;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class CalViewFragment extends Fragment {

    private CalView mCalView;

    private CalendarController mDate;

    private TextView mMonthText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(mDate == null)
        {
            mDate = new CalendarController();
        }
        super.onCreate(savedInstanceState);

    }

    /*public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle mbundle = getArguments();

            if(mbundle !=null)
            {
                ArrayList<Integer> mDateArray;
                mDateArray = mbundle.getIntegerArrayList("Date");
                myear = mDateArray.get(0);
                mmonth = mDateArray.get(1);
                mdayofweek = mDateArray.get(3);
                mdayofmonth = mDateArray.get(2);
                mCurDate = new GregorianCalendar(myear,mmonth,mdayofmonth);
            }
        }

        public static CalViewFragment newInstance(ArrayList<Integer> date)
        {
            Bundle mbundle = new Bundle();
            mbundle.putIntegerArrayList("Date", date);
            CalViewFragment mCalViewFragment = new CalViewFragment();
            mCalViewFragment.setArguments(mbundle);
            return mCalViewFragment;
        }
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_main_layout,container,false);
        mCalView =  (CalView) view.findViewById(R.id.id_cal_view);
        mCalView.init(mDate.getNowCalendar());
        mMonthText = (TextView) view.findViewById(R.id.id_cal_view_month);
        mMonthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalView.SwitchMode();
            }
        });

        return view;
    }
}
