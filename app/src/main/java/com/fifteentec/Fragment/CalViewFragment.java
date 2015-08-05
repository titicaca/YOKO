package com.fifteentec.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fifteentec.Component.calendar.CalView;
import com.fifteentec.Component.calendar.CalendarController;
import com.fifteentec.Component.calendar.EventListView;
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
    private TextView mYearText;
    private FragmentManager mFragmentManager;
    private EventListViewFragment mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(mDate == null)
        {
            mDate = new CalendarController();
        }
        mFragmentManager = getFragmentManager();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_main_layout,container,false);
        mMonthText = (TextView) view.findViewById(R.id.id_cal_view_month);
        mMonthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalView.SwitchMode();
            }
        });
        mYearText = (TextView) view.findViewById(R.id.id_cal_view_year);
        mCalView =  (CalView) view.findViewById(R.id.id_cal_view);
        mCalView.init(mDate.getNowCalendar());
        mCalView.setCalViewListner(new CalView.CalViewListener() {
            @Override
            public void DateChange(GregorianCalendar arry) {
                mDate.UpdateCur(arry);
                mMonthText.setText(mDate.MONTH_NAME.get(mDate.getCurMonth()));
                mYearText.setText(mDate.getCurYear() + "");
            }
        });
        FragmentTransaction mTrans = mFragmentManager.beginTransaction();
        mListView = new EventListViewFragment();
        mTrans.add(R.id.id_event_content,mListView).commit();

        return view;
    }
}
