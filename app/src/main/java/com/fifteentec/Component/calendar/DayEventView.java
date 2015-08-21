package com.fifteentec.Component.calendar;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.Database.EventRecord;
import com.fifteentec.yoko.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class DayEventView extends ViewGroup {


    private Surface mSurface;
    private TextView mDateView;
    private View mBackgroundView;
    private ImageView mPageView;
    private AllDayView mAllDayView;
    private NormalEvent mNormalEvent;
    private EventManager mEventManager;

    private Context mcontext;
    private GregorianCalendar mDate;

    private int mScreenWidth;
    private int mScreenHeight;
    private DayEventViewListener dayEventViewListener;


    /**
     * 日视图通知外部fragment增加事件
     * 关闭该视图
     * 删除事件
     * 打开编辑页面
     */
    public interface DayEventViewListener {
        void createRecord(int Type,EventRecord eventRecord);
        void closeDayView();
        void deleteRecord(long rid);
        void checkExist(long rid);
    }

    public void setDayEventViewListener(DayEventViewListener dayEventViewListener) {
        this.dayEventViewListener = dayEventViewListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void EventRecordUpdate(long rid,boolean exist) {
        if(!exist) {
            if (mEventManager.addEvent(rid)) mAllDayView.UpdateView();
            else mNormalEvent.UpdateView();
        }else{
            if(mEventManager.isAllDayEventExist(rid)) mAllDayView.UpdateView();
            else mNormalEvent.UpdateView();
        }
    }


    public static DayEventView newInstance(Context context, GregorianCalendar today) {
        DayEventView dayEventView = new DayEventView(context);
        dayEventView.init(today);
        return dayEventView;
    }

    public DayEventView(Context context) {
        this(context, null);
    }

    public DayEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext = context;
    }

    private void init(GregorianCalendar today) {
        mSurface = new Surface();
        mDate = CalUtil.CopyDate(today);
        mEventManager = EventManager.newInstance(((BaseActivity)mcontext).getDBManager().getTableEvent(),EventManager.DAY_VIEW_EVENT_MANAGER,today.getTimeInMillis());

        mBackgroundView = new View(mcontext);
        mBackgroundView.setBackgroundColor(mSurface.BackgoundColor);
        mBackgroundView.setAlpha(mSurface.BackgroundAlpha);
        mBackgroundView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dayEventViewListener.closeDayView();
            }
        });
        addView(mBackgroundView);

        mPageView = new ImageView(mcontext);
        mPageView.setBackgroundColor(mSurface.PageColor);
        addView(mPageView);

        mDateView = new TextView(mcontext);
        String dateShow = mDate.get(Calendar.YEAR) + "年" + (mDate.get(Calendar.MONTH) + 1) + "月" + mDate.get(Calendar.DAY_OF_MONTH) + "日";
        mDateView.setText(dateShow);
        mDateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSurface.TextSize);
        addView(mDateView);

        mAllDayView = new AllDayView(mcontext);
        addView(mAllDayView);

        mNormalEvent = new NormalEvent(mcontext);
        mNormalEvent.setNormalEventListener(new NormalEvent.NormalEventListener() {
            @Override
            public void createRecord(int Type,EventRecord eventRecord) {
                dayEventViewListener.createRecord(Type, eventRecord);
            }

            @Override
            public void deleteRecord(long rid) {
                dayEventViewListener.deleteRecord(rid);
            }

            @Override
            public void checkExist(long i) {
                dayEventViewListener.checkExist(i);
            }

        });
        mNormalEvent.initNormal(mEventManager);
        addView(mNormalEvent);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);


        int SidePadding = (int) (mScreenWidth * mSurface.SidePadding);
        int UpdownPadding = (int) (mScreenHeight * mSurface.UpdownPadding);


        if (mBackgroundView != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.EXACTLY);
            mBackgroundView.measure(widthSpec, heightSpec);
        }

        if (mDateView != null) {
            LayoutParams lp = mDateView.getLayoutParams();
            int widthSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.TextHeight, MeasureSpec.EXACTLY);
            mDateView.measure(widthSpec, heightSpec);
        }


        if (mPageView != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * SidePadding, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mScreenHeight - 2 * UpdownPadding, MeasureSpec.EXACTLY);
            mPageView.measure(widthSpec, heightSpec);
        }

        int AllDayWidth = (int) ((mScreenWidth - 2 * (SidePadding + mSurface.PageSidePadding)) * mSurface.AllDayViewWidth);
        int AllDayHeight = mScreenHeight - 2 * (UpdownPadding + mSurface.PageUpdownPadding) - mSurface.TextHeight - mSurface.AllDayViewPadding;

        if (mAllDayView != null) {

            int widthSpec = MeasureSpec.makeMeasureSpec(AllDayWidth, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(AllDayHeight, MeasureSpec.EXACTLY);
            mAllDayView.measure(widthSpec, heightSpec);
        }

        int NormalEvnentWidth = (int) ((mScreenWidth - 2 * (SidePadding + mSurface.PageSidePadding)) * (1 - mSurface.AllDayViewWidth));
        if (mNormalEvent != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(NormalEvnentWidth, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(AllDayHeight, MeasureSpec.EXACTLY);
            mNormalEvent.measure(widthSpec, heightSpec);
        }
               /*if(!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec();
            int heightSpec = MeasureSpec.makeMeasureSpec();
            .measure(widthSpec,heightSpec);
        }
               /*if(!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec();
            int heightSpec = MeasureSpec.makeMeasureSpec();
            .measure(widthSpec,heightSpec);
        }

         */
        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int SidePadding = (int) (mScreenWidth * mSurface.SidePadding);
        int UpdownPadding = (int) (mScreenHeight * mSurface.UpdownPadding);

        if (mBackgroundView != null) mBackgroundView.layout(l, t, r, b);

        if (mDateView != null) mDateView.layout(
                l + SidePadding + mSurface.PageSidePadding,
                t + UpdownPadding + mSurface.PageUpdownPadding,
                l + SidePadding + mSurface.PageSidePadding + mDateView.getMeasuredWidth(),
                t + UpdownPadding + mSurface.PageUpdownPadding + mDateView.getMeasuredHeight());


        if (mPageView != null) {

            int left = l + SidePadding;
            int top = t + UpdownPadding;
            int right = r - SidePadding;
            int botton = b - UpdownPadding;
            mPageView.layout(left, top, right, botton);
        }

        if (mAllDayView != null) {

            int left = l + SidePadding + mSurface.PageSidePadding;
            int top = t + UpdownPadding + mSurface.PageUpdownPadding * 2 + mSurface.TextHeight;
            int right = left + mAllDayView.getMeasuredWidth();
            int botton = top + mAllDayView.getMeasuredHeight();
            mAllDayView.layout(left, top, right, botton);
        }

        if (mNormalEvent != null) {

            int right = r - SidePadding - mSurface.PageSidePadding;
            int left = right - mNormalEvent.getMeasuredWidth();
            int top = t + UpdownPadding + mSurface.PageUpdownPadding * 2 + mSurface.TextHeight;
            int botton = top + mNormalEvent.getMeasuredHeight();
            mNormalEvent.layout(left, top, right, botton);
        }
                /*
        if( != null) {

            int left =;
            int top =;
            int right =;
            int botton =;
            .layout(left,top,right,botton);
        }
                /*
        if( != null) {

            int left =;
            int top =;
            int right =;
            int botton =;
            .layout(left,top,right,botton);
        }
                /*
        if( != null) {

            int left =;
            int top =;
            int right =;
            int botton =;
            .layout(left,top,right,botton);
        }
        */


    }

    private class Surface {


        float SidePadding = (1 / 20f);
        float UpdownPadding = (1 / 20f);

        int BackgoundColor = Color.BLACK;
        float BackgroundAlpha = 0.5f;

        int TextHeight = 100;
        int TextSize = 25;

        int PageSidePadding = 50;
        int PageUpdownPadding = 50;
        int PageColor = Color.WHITE;

        float AllDayViewWidth = (1 / 4f);
        int AllDayItemHeight = 100;
        int AllDayViewPadding = 50;

        Paint LinePaint;
        int LineColor = Color.BLACK;
        int LineWidth = 3;
        int LinePadding = 80;

        Paint NormalText;
        int NormalEventViewDivid = 8;
        int NormalEvnetLineLength = 100;
        int NormalEvnetTextColor = Color.BLACK;
        int NormalEvnetTextSize = 50;

        Paint tempRectPaint;
        int tempRectColor = Color.BLUE;
        int tempRectWidth = 10;

        Paint ExistRectPaint;
        int ExistRectColor = Color.GREEN;

        Paint NormalEventPaint;
        int NormalEventColor = Color.GRAY;

        public Surface() {
            LinePaint = new Paint();
            LinePaint.setColor(LineColor);
            LinePaint.setStrokeWidth(LineWidth);
            NormalText = new Paint();
            NormalText.setColor(NormalEvnetTextColor);
            NormalText.setTextSize(NormalEvnetTextSize);
            tempRectPaint = new Paint();
            tempRectPaint.setColor(tempRectColor);
            tempRectPaint.setStyle(Paint.Style.FILL);
            NormalEventPaint = new Paint();
            NormalEventPaint.setColor(NormalEventColor);
            ExistRectPaint = new Paint();
            ExistRectPaint.setColor(ExistRectColor);
        }

    }

    private class AllDayView extends ViewGroup implements GestureDetector.OnGestureListener {

        int[] tempRect = new int[5];
        int FullHeight = 0;
        long DownChildId = -1;

        GestureDetector mgestureDetector;

        public AllDayView(Context context) {
            this(context, null);
        }

        public AllDayView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public AllDayView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initAllDay();
        }

        private void initAllDay() {
            mgestureDetector = new GestureDetector(mcontext, this);
            for (int i = 0; i < mEventManager.getAllDayEventCount(); i++) {
                tempRectView tx = new tempRectView(mcontext);
                String item = mEventManager.getAllDayIntroduciton(i);
                tx.setText(item);
                tx.setTag(mEventManager.getAllDayEventRid(i));
                addView(tx);
            }
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            for (int i = 0; i < getChildCount(); i++) {
                View temp = getChildAt(i);
                int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.AllDayItemHeight, MeasureSpec.EXACTLY);
                temp.measure(widthSpec, heightSpec);
                FullHeight += mSurface.AllDayItemHeight;
            }

            setMeasuredDimension(width, height);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View temp = getChildAt(i);
                temp.layout(0, height, temp.getMeasuredWidth(), height + temp.getMeasuredHeight());
                height += temp.getMeasuredHeight();
            }
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) DownChildId = -1;
            return mgestureDetector.onTouchEvent(event);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mEventManager.isAllDayEventExist(DownChildId)) {
                Toast.makeText(mcontext, mEventManager.getIntroduction(DownChildId), Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mEventManager.removeEvent(DownChildId)) {
                dayEventViewListener.deleteRecord(DownChildId);
                UpdateView();
            } else {
                EventRecord eventRecord = new EventRecord();
                eventRecord.timebegin = mEventManager.DayView_Date;
                eventRecord.timeend = mEventManager.DayView_Date;
                dayEventViewListener.createRecord(NewEventView.HAVE_TIME,eventRecord);
            }

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        public void UpdateView() {
            removeAllViews();
            DownChildId = -1;
            for (int i = 0; i < mEventManager.getAllDayEventCount(); i++) {
                tempRectView tx = new tempRectView(mcontext);
                String item = mEventManager.getAllDayIntroduciton(i);
                tx.setText(item);
                tx.setTag(i);
                addView(tx);
            }
        }

        private class tempRectView extends TextView {

            public tempRectView(Context context) {
                this(context, null);
            }

            public tempRectView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
            }

            public tempRectView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
            }


            @Override
            public boolean onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    DownChildId = (long) getTag();
                }
                return false;
            }
        }
    }
}


