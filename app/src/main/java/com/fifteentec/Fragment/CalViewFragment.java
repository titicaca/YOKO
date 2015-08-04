package com.fifteentec.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Test","in0");
        View view = inflater.inflate(R.layout.fragment_calendar_main_layout,container,false);
        Log.d("Test","in1");
        mCalView =  (CalView) view.findViewById(R.id.id_cal_view);
        Log.d("Test","in2");
        mCalView.init(mDate.getNowCalendar());
        Log.d("Test", "in3");
        mMonthText = (TextView) view.findViewById(R.id.id_cal_view_month);
        Log.d("Test","in4");
        mMonthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalView.SwitchMode();
            }
        });
        Log.d("Test", "in5");
        return view;
    }
}
