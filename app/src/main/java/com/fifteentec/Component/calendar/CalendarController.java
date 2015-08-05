package com.fifteentec.Component.calendar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarController {

    private GregorianCalendar mTimeCur;
    public final ArrayList<String> MONTH_NAME = new ArrayList<>(Arrays.asList("Jan.","Feb.","Mar.","Apr.","May","Jun.","Jul.","Aug.","Sept.","Oct.","Nov.","Dec."));
    private   ArrayList<Integer> mCurDate;
    private int mCurDayOfMonth;
    private int mCurDayOfWeek;
    private int mCurWeekOfMonth;
    private int mCurWeekOfYear;
    private int mCurYear;
    private int mCurMonth;


    private GregorianCalendar mTimeNow;

    private ArrayList<Integer> mNowDate;
    private int mNowDayOfMonth;
    private int mNowDayOfWeek;
    private int mNowWeekOfMonth;
    private int mNowWeekOfYear;
    private int mNowYear;
    private int mNowMonth;

    public CalendarController()
    {
        mTimeNow = new GregorianCalendar();
        mNowDayOfMonth = mTimeNow.get(Calendar.DAY_OF_MONTH);
        mNowDayOfWeek = mTimeNow.get(Calendar.DAY_OF_WEEK);
        mNowWeekOfMonth = mTimeNow.get(Calendar.WEEK_OF_MONTH);
        mNowWeekOfYear = mTimeNow.get(Calendar.WEEK_OF_YEAR);
        mNowYear =mTimeNow.get(Calendar.YEAR);
        mNowMonth = mTimeNow.get(Calendar.MONTH);

        mNowDate = new ArrayList<Integer>(4);

        mNowDate.add(mNowYear);
        mNowDate.add(mNowMonth);
        mNowDate.add(mNowDayOfMonth);
        mNowDate.add(mNowDayOfWeek);

        mCurDate = new ArrayList<>(4);

        UpdateCur(mTimeNow);
    }

    public void UpdateCur(GregorianCalendar mTime) {

        this.mCurDate.clear();
        this.mCurDate.add(mTime.get(Calendar.YEAR));
        this.mCurDate.add(mTime.get(Calendar.MONTH));
        this.mCurDate.add(mTime.get(Calendar.DAY_OF_MONTH));
        this.mCurDate.add(mTime.get(Calendar.DAY_OF_WEEK));
        this.mCurYear = mTime.get(Calendar.YEAR);
        this.mCurMonth = mTime.get(Calendar.MONTH);
        this.mCurDayOfMonth = mTime.get(Calendar.DAY_OF_MONTH);
        this.mCurDayOfWeek = mTime.get(Calendar.DAY_OF_WEEK);

    }

    public void UpdateCur(ArrayList<Integer> date) {
        if(date.size() == 4) {
            this.mCurDate.clear();
            this.mCurDate.add(date.get(0));
            this.mCurDate.add(date.get(1));
            this.mCurDate.add(date.get(2));
            this.mCurDate.add(date.get(3));
            this.mCurYear = date.get(0);
            this.mCurMonth = date.get(1);
            this.mCurDayOfMonth = date.get(2);
            this.mCurDayOfWeek = date.get(3);
        }
    }

    public int getCurDayOfMonth()
    {
        return this.mCurDayOfMonth;
    }

    public int getCurDayOfWeek()
    {
        return this.mCurDayOfWeek;
    }

    public int getCurWeekOfMonth()
    {
        return this.mCurWeekOfMonth;
    }

    public int getCurYear()
    {
        return this.mCurYear;
    }

    public int getCurMonth()
    {
        return this.mCurMonth;
    }

    public int getCurWeekOfYear()
    {
        return mCurWeekOfYear;
    }


    public ArrayList<Integer> getCurArray() {
        return this.mCurDate;
    }
    public GregorianCalendar getCurCalendar() {return mTimeCur;};
    public GregorianCalendar getNowCalendar() {return mTimeNow;};

    public ArrayList<Integer> getNowArray() {
        return this.mNowDate;
    }
}

