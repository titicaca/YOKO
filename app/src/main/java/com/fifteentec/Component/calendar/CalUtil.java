package com.fifteentec.Component.calendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class CalUtil {

    public static final List<Integer> LENTH_OF_MONTH = new ArrayList<>(Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31));
    public static final long TimeOfDayInMills = 115740000;
    public static final List<String> WEEK_NAME = new ArrayList<>(Arrays.asList("Non","Sun.","Mon.","Tue.","Wed.","Thr.","Fri.","Sat."));
    public static boolean isLeapYear (int year){
        GregorianCalendar temp = new GregorianCalendar(year,1,1);

        return temp.isLeapYear(temp.get(Calendar.YEAR));
    }
    public static final int LENTH_OF_WEEK = 7;

    public static int GetDayOfWeekFromDayOfMonth(int year,int month,int day)
    {
        GregorianCalendar token = new GregorianCalendar(year,month,day);
        return token.get(Calendar.DAY_OF_WEEK);
    }

    public static ArrayList<Integer> CalToArray(GregorianCalendar date)
    {
        ArrayList<Integer>  token  = new ArrayList<>();
        token.add(date.get(Calendar.YEAR));
        token.add(date.get(Calendar.MONTH));
        token.add(date.get(Calendar.DAY_OF_MONTH));
        token.add(date.get(Calendar.DAY_OF_WEEK));

        return token;
    }


    public static ArrayList<Integer> GetDayInMonth(GregorianCalendar date)
    {
        GregorianCalendar DrawDate = new GregorianCalendar(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        DrawDate.set(year,month,1);
        int firstdayofweek = DrawDate.get(Calendar.DAY_OF_WEEK);
        GregorianCalendar token_date = new GregorianCalendar(year,month+1,1);

        ArrayList<Integer> mCurMonthList = new ArrayList<Integer>();
        for (int i =0;i<firstdayofweek-1;i++)
        {
            mCurMonthList.add(0);
        }

        do {
            mCurMonthList.add(DrawDate.get(Calendar.DAY_OF_MONTH));
            DrawDate.add(Calendar.DAY_OF_MONTH,1);

        }while (DrawDate.get(Calendar.MONTH) != token_date.get(Calendar.MONTH));


        return mCurMonthList;
    }



    public static int FindFirstDayofMonthInWeek(GregorianCalendar date)
    {
        GregorianCalendar TokenDate = new GregorianCalendar(date.get(Calendar.YEAR),date.get(Calendar.MONTH),1);
        return TokenDate.get(Calendar.DAY_OF_WEEK);
    }

    public static ArrayList<Integer> GetDayInMonthOfWeek(GregorianCalendar date)
    {
        GregorianCalendar DrawDate = new GregorianCalendar(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int dayofWeek = date.get(Calendar.DAY_OF_WEEK);
        DrawDate.add(Calendar.DAY_OF_MONTH,-dayofWeek+1);

        ArrayList<Integer> mCurWeekList = new ArrayList<Integer>();
        for(int i = 0;i< LENTH_OF_WEEK ;i++)
        {
            mCurWeekList.add(DrawDate.get(Calendar.DAY_OF_MONTH));
            DrawDate.add(Calendar.DAY_OF_YEAR,1);
        }

        return mCurWeekList;
    }

    public static ArrayList<Long> GetWeekBeginTimeInMills(long time){
        GregorianCalendar day = new GregorianCalendar();
        day.setTimeInMillis(time);
        int week = day.get(Calendar.DAY_OF_WEEK)-1;
        day.add(Calendar.DAY_OF_MONTH, -week);
        int month = day.get(Calendar.MONTH);
        int year = day.get(Calendar.YEAR);
        int date = day.get(Calendar.DAY_OF_MONTH);
        day.set(year,month,date,0,0);
        ArrayList<Long> result = new ArrayList<>();
        for (int i = 0; i < LENTH_OF_WEEK; i++) {
            result.add(day.getTimeInMillis());
            day.add(Calendar.DAY_OF_MONTH,1);
        }

        return result;

    }

    public static ArrayList<GregorianCalendar> GetThreeDayInWeekFromeDayOfMonth(GregorianCalendar date)
    {
        GregorianCalendar PreDate = new GregorianCalendar(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
        GregorianCalendar NextDate = new GregorianCalendar(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
        PreDate.add(Calendar.WEEK_OF_YEAR,-1);
        NextDate.add(Calendar.WEEK_OF_YEAR,1);

        ArrayList<GregorianCalendar> mWeekList = new ArrayList<GregorianCalendar>();
        mWeekList.add(PreDate);
        mWeekList.add(date);
        mWeekList.add(NextDate);

        return mWeekList;
    }

    public static GregorianCalendar CopyDate(GregorianCalendar date)
    {
        return new GregorianCalendar(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
    }



    public static int getMaxDayOfMonth(GregorianCalendar mCurDate) {
        GregorianCalendar token = new GregorianCalendar(mCurDate.get(Calendar.YEAR),mCurDate.get(Calendar.MONTH)+1,1);
        token.add(Calendar.DAY_OF_MONTH,-1);
        return token.get(Calendar.DAY_OF_MONTH);

    }
}

