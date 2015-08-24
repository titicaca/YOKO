package com.fifteentec.Component.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by kylin on 15/8/24.
 */
public class CalendarView extends ViewGroup {


    private Surface mSurface;

    private int ViewHeight;
    private int ViewWidth;

    private ArrayList<Integer> today;

    public static CalendarView newInstance(Context context,ArrayList<Integer> date){
        CalendarView calendarView = new CalendarView(context);
        calendarView.today = new ArrayList<>(Arrays.asList(date.get(0),date.get(1),date.get(2),date.get(3)));
        return calendarView;
    }
    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mSurface = new Surface();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        ViewWidth = MeasureSpec.getSize(widthMeasureSpec);

        mSurface.initSurface();

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private class Surface{

        final int HORIZON = 0x00;
        final int VERTICAL = 0x01;
        int SCROLL_MODE = VERTICAL;

        int ViewMeasureHeight;
        float ViewMeasureHeightRatio = (1/2f);
        int ViewCellWidth;

        int ViewLeftPaddingWidth;
        float ViewLeftPaddingWidthRatio = (1/8f);
        void initSurface(){
            ViewMeasureHeight = (int)(ViewHeight*ViewMeasureHeightRatio);
            ViewLeftPaddingWidth =(int)(ViewWidth*ViewLeftPaddingWidthRatio);
        }
    }

    private class MonthView extends View{

        GregorianCalendar month;
        int index;
        ArrayList<Integer> mMonthItem;

        public MonthView newInstance(Context context,GregorianCalendar date,int index){
            MonthView monthView = new MonthView(context);
            monthView.month = new GregorianCalendar(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH));
            monthView.index = index;
            monthView.initMonthView();
            return monthView;
        }
        public MonthView(Context context) {
            this(context, null);
        }

        public MonthView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        void initMonthView() {

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            CalUtil.GetDayInMonth(month);
        }
    }


}

