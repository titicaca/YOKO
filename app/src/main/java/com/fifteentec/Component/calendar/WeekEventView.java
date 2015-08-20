package com.fifteentec.Component.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.GregorianCalendar;
public class WeekEventView extends ViewGroup{

    private GregorianCalendar StartDate;
    public WeekEventView(Context context) {
        this(context,null);
    }

    public WeekEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(ArrayList<Integer> date){
        StartDate = new GregorianCalendar(date.get(0),date.get(1),date.get(2),0,0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
