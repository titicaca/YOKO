package com.fifteentec.Component.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.Database.TableEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarView extends ViewGroup {


    private Surface mSurface;

    private int ViewHeight;
    private int ViewWidth;
    private TableEvent tableEvent;
    private ArrayList<Integer> today;
    private Context mContext;
    private ViewController mViewController;
    private boolean firstEnter =true;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void initView(ArrayList<Integer> date,TableEvent tableEvent) {
        today = new ArrayList<>(Arrays.asList(date.get(0),date.get(1),date.get(2),date.get(3)));
        this.tableEvent = tableEvent;
        mSurface = new Surface();
        mViewController =new ViewController(mContext);
        mViewController.initCalView();
        addView(mViewController);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        ViewWidth = MeasureSpec.getSize(widthMeasureSpec);

        if(firstEnter) {
            mSurface.initSurface();
            firstEnter = false;
        }

        if(mViewController != null){
            int widthSpec = MeasureSpec.makeMeasureSpec(ViewWidth,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(ViewHeight,MeasureSpec.EXACTLY);
            mViewController.measure(widthSpec,heightSpec);
        }

        setMeasuredDimension(ViewWidth,mSurface.ViewMeasureHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mViewController != null){
            mViewController.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
        }
    }

    private class Surface{

        final int HORIZON = 0x00;
        final int VERTICAL = 0x01;
        int SCROLL_MODE = VERTICAL;

        int ViewMeasureHeight;
        float ViewMeasureHeightRatio = (1/2f);
        int ViewCellWidth;
        int ViewCellHeight;
        float CellWidthHeightRatio = (3/4f);

        int ViewLeftPaddingWidth;
        float ViewLeftPaddingWidthRatio = (1/8f);

        float MonthGapRatio = (1/4f);
        int MonthGap;

        int MonthTextSize;
        float MonthTextSizeRatio = 1/3f;
        Paint MonthText = new Paint();
        int MonthNormalTextColor = Color.BLACK;

        void initSurface(){
            ViewMeasureHeight = (int)(ViewHeight*ViewMeasureHeightRatio);
            ViewLeftPaddingWidth =(int)(ViewWidth*ViewLeftPaddingWidthRatio);
            MonthGap = (int)(ViewHeight*MonthGapRatio);
            //ViewCellWidth = (ViewWidth-ViewLeftPaddingWidth)/7;
            ViewCellWidth = (ViewWidth)/7;
            ViewCellHeight =(int)(ViewCellWidth*(1/CellWidthHeightRatio));
            MonthTextSize = (int)(ViewCellWidth*MonthTextSizeRatio);
            MonthText.setColor(MonthNormalTextColor);
            MonthText.setAntiAlias(true);
            MonthText.setTextSize(MonthTextSize);
        }
    }

    private class ViewController extends ViewGroup implements GestureDetector.OnGestureListener{

        ArrayList<MonthView> monthViews=new ArrayList<>();
        int HideSpace = ViewHeight;
        int ExcessSpace = ViewHeight;
        int HideIndex=0;
        int ExcessIndex=0;
        GestureDetector mGestureDetector;
        int MonthViewHeight;

        public ViewController(Context context) {
            this(context, null);
        }

        public ViewController(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ViewController(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        void initCalView(){
            mGestureDetector = new GestureDetector(mContext,this);
            int temp = 0;
            do {
                HideIndex --;
                MonthView monthView = new MonthView(mContext);
                monthView.initMonthView(HideIndex);
                monthViews.add(monthView);
                this.addView(monthView,0);
                temp += monthView.MonthHeight;
            }while (temp < HideSpace);
            this.scrollTo(0, temp);

            MonthView monthView = new MonthView(mContext);
            monthView.initMonthView(0);
            monthViews.add(monthView);
            this.addView(monthView);
            MonthViewHeight = 0;

            temp = 0;
            do {
                ExcessIndex ++;
                MonthView monthView1 = new MonthView(mContext);
                monthView1.initMonthView(ExcessIndex);
                monthViews.add(monthView1);
                this.addView(monthView1);
                temp += monthView1.MonthHeight;
            }while (temp < HideSpace);
            this.scrollTo(0,temp);

            for(MonthView item:monthViews){
                MonthViewHeight +=item.MonthHeight;
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                int WithSpec = MeasureSpec.makeMeasureSpec(ViewWidth,MeasureSpec.EXACTLY);
                int HeightSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewMeasureHeight,MeasureSpec.UNSPECIFIED);
                view.measure(WithSpec,HeightSpec);

            }

            setMeasuredDimension(ViewWidth,MonthViewHeight);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int temp = 0;
            for (int i = 0; i < getChildCount(); i++) {
                MonthView monthView = (MonthView)getChildAt(i);
                monthView.layout(0,temp,monthView.getMeasuredWidth(),temp+monthView.MonthHeight);
                temp += monthView.MonthHeight;
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return mGestureDetector.onTouchEvent(event);
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
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            this.scrollBy(0, (int) distanceY);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private class MonthView extends View{

        GregorianCalendar month;
        int index;
        ArrayList<Integer> mMonthItem;
        EventManager mEventManager;
        int MonthHeight;
        private boolean firstEntry = true;


        public MonthView(Context context) {
            this(context, null);
        }

        public MonthView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        void initMonthView(int index) {
            month = new GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),0,0,0);
            month.add(Calendar.MONTH,index);
            mMonthItem  = CalUtil.GetDayInMonth(month);
            this.index = index;
            mEventManager = EventManager.newInstance(tableEvent, EventManager.MONTH_VIEW_EVENT_MANAGER,month.getTimeInMillis());
            setTag("Month");
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int line = (mMonthItem.size()-1)/7+1;
            int height = line*mSurface.ViewCellHeight;
            if(firstEntry) {
                MonthHeight = height;
                firstEntry = false;
            }
            int width = MeasureSpec.getSize(widthMeasureSpec);
            height += mSurface.MonthGap;
            setMeasuredDimension(width,height);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            int width = 0;
            int height = 0;
            int index = 0;
            while (mMonthItem.get(index)==0) {
                index++;
                width += mSurface.ViewCellWidth;
            }
            Rect rect = new Rect();
            for (int i = index; i <mMonthItem.size(); i++) {
                String text = mMonthItem.get(i)+"";
                mSurface.MonthText.getTextBounds(text, 0, text.length(), rect);
                canvas.drawText(text,width+mSurface.ViewCellWidth/2-rect.width()/2,mSurface.MonthGap+height+mSurface.ViewCellHeight-rect.height()/2,mSurface.MonthText);
                if(index%7 == 6){
                    height += mSurface.ViewCellHeight;
                    width =0;
                }else{
                    width += mSurface.ViewCellWidth;
                }
                index ++;
                //canvas.drawCircle();
            }

        }
    }


}

