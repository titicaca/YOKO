package com.fifteentec.Component.calendar;


import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


public class EventListView extends ViewGroup implements GestureDetector.OnGestureListener{
    private Context mcontext ;
    private EventController mEvent;
    private float mRatio =1/7f;
    private int ScreenWidth;
    private int ScreenHeight;
    private LayoutInflater mInflater;
    private GestureDetector mGesture;
    private EventListListener mEventListListener;

    public interface EventListListener{
        public void DateChange(ArrayList<Integer> list);
    }

    public void setEvnetListDateChangeListener(EventListListener listener){
        this.mEventListListener = listener;
    }

    public EventListView(Context context) {
        super(context, null);
        mcontext = context;
        mGesture = new GestureDetector(mcontext,this);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }





    public void init(ArrayList<Integer> date){
        removeAllViews();
        ScreenWidth =(getResources().getDisplayMetrics().widthPixels);
        ScreenHeight = (getResources().getDisplayMetrics().heightPixels);
        GregorianCalendar temp = new GregorianCalendar(date.get(0),date.get(1),date.get(2));
        mEvent = new EventController(temp);
        scrollTo(0, mEvent.initEventList());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode ==MeasureSpec.UNSPECIFIED|| heightMode == MeasureSpec.UNSPECIFIED){
            throw new IllegalArgumentException("Wrong Argument");
        }

        int count = getChildCount();
        for(int i = 0 ;i<count;i++){
            View ChildView = getChildAt(i);
            LayoutParams lp = ChildView.getLayoutParams();
            int childWidthMeasureSpec =MeasureSpec.makeMeasureSpec(lp.width,MeasureSpec.AT_MOST);
            int childHeightMeasureSpec =MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.AT_MOST);
            ChildView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int CurHeight=0;
        int mExistItem = getChildCount();
        for(int i =0; i < mExistItem/2;i++){
            View v =getChildAt(i*2+1);

            View v1 =getChildAt(i * 2);
            if(v !=null && v1!=null) {
                int ItemHeight = v.getMeasuredHeight();
                v.layout((int) (ScreenWidth * mRatio), CurHeight, r, CurHeight + ItemHeight);
                if(i ==-mEvent.mBottonIndex) {
                    if(CurHeight + ItemHeight <v1.getMeasuredHeight()+getScrollY())
                        v1.layout(l,  CurHeight + ItemHeight-v1.getMeasuredHeight() , (int) (ScreenWidth * mRatio + l),  CurHeight + ItemHeight);

                    else v1.layout(l, getScrollY(), (int) (ScreenWidth * mRatio + l), getScrollY() + v1.getMeasuredHeight());

                }else {
                    v1.layout(l, CurHeight, (int) (ScreenWidth * mRatio + l), CurHeight + v1.getMeasuredHeight());
                }
                CurHeight += ItemHeight;
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGesture.onTouchEvent(event);
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

        scrollBy(0, (int) distanceY);

        int ScrollY=getScrollY();
        mEvent.DateScroll(ScrollY);

        scrollBy(0,mEvent.ViewScroll(ScrollY));
        requestLayout();

        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    private class EventList extends View{

        private ArrayList<String> mDate;
        private int ItemHeight = 130;
        private int ItemWidth;
        private int mInterval = 30;
        private int mPaddingTop = 10;
        private int mPaddingLeft =60;
        private int mRoundness = 8;
        private int mPaddingRectRight = 40;
        private Paint mRectPaint;
        private Paint mTextPaint;
        public int ViewHeight;
        public EventList(Context context) {
            this(context, null);
        }

        public EventList(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public EventList(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void initView(ArrayList<String> date,int ParentWidth){

            this.mDate = date;
            this.ItemWidth = ParentWidth;
            mRectPaint = new Paint();
            mRectPaint.setColor(Color.parseColor("#920510"));
            mRectPaint.setAntiAlias(true);
            mTextPaint = new Paint();
            mTextPaint.setColor(Color.WHITE);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(50);
            ViewHeight = (ItemHeight+mInterval)*mDate.size()+mInterval;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int width;
            int height;


            if(widthMode == MeasureSpec.EXACTLY){
                width = widthSize;
            }else {
                width = ItemWidth;
            }
            if(heightMode == MeasureSpec.EXACTLY){
                height = heightSize;

            }else{
                height = (ItemHeight+mInterval)*mDate.size()+mInterval;
            }
            setMeasuredDimension(width,height);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            int topItem = mInterval;
            for(int i = 0; i <mDate.size();i++) {
                RectF mRect = new RectF(0,topItem,ItemWidth-mPaddingRectRight,topItem+ItemHeight);
                canvas.drawRoundRect(mRect, mRoundness, mRoundness, mRectPaint);
                Paint.FontMetricsInt font = mTextPaint.getFontMetricsInt();
                int baseline = (int)(mRect.top + (mRect.bottom - mRect.top - font.bottom + font.top) / 2 - font.top);
                canvas.drawText(mDate.get(i),mPaddingLeft,baseline,mTextPaint);
                topItem += (ItemHeight+mInterval);

            }
        }
    }


    private class EventController{


        private final int mShowHeight = ScreenHeight*2;

        private GregorianCalendar mTodayDate;
        private ArrayList<Integer> mCurDate;
        private GregorianCalendar mLastDate;

        public int mTopIndex;

        public int mBottonIndex;

        public int initEventList(){
            int count = 0;
            int result = 0;
            mBottonIndex = 0;
            mTopIndex=-1;
            do{
                result += addHideViewList();

            }while (result<mShowHeight);
            count =0;
            addViewList();
            do {
                count += addViewList();
            }while(count<mShowHeight);

            return result;

        }
        public int  addHideViewList(){
            mBottonIndex--;
            GregorianCalendar tempDate = new GregorianCalendar(mCurDate.get(0),mCurDate.get(1),mCurDate.get(2));
            tempDate.add(Calendar.DAY_OF_MONTH, mBottonIndex);
            View temp = mInflater.inflate(R.layout.view_event_list_text_layout,null);
            TextView day = (TextView) temp.findViewById(R.id.id_event_list_day_text);
            day.setText("" + tempDate.get(Calendar.DAY_OF_MONTH));
            TextView Week = (TextView) temp.findViewById(R.id.id_event_list_week_text);
            Week.setText(CalUtil.WEEK_NAME.get(tempDate.get(Calendar.DAY_OF_WEEK)));
            addView(temp, 0);
            EventList mEL = new EventList(mcontext);
            mEL.initView(mEvent.getEvenet(CalUtil.CalToArray(tempDate)), (int) (ScreenWidth * (1 - mRatio)));
            addView(mEL, 1);
            return mEL.ViewHeight;


        }
        public int addViewList(){
            mTopIndex++;
            GregorianCalendar tempDate = new GregorianCalendar(mCurDate.get(0),mCurDate.get(1),mCurDate.get(2));
            tempDate.add(Calendar.DAY_OF_MONTH, mTopIndex);
            View temp = mInflater.inflate(R.layout.view_event_list_text_layout,null);
            TextView day = (TextView) temp.findViewById(R.id.id_event_list_day_text);
            day.setText("" + tempDate.get(Calendar.DAY_OF_MONTH));
            TextView Week = (TextView) temp.findViewById(R.id.id_event_list_week_text);
            Week.setText(CalUtil.WEEK_NAME.get(tempDate.get(Calendar.DAY_OF_WEEK)));
            addView(temp);

            EventList mEL = new EventList(mcontext);
            mEL.initView(mEvent.getEvenet(CalUtil.CalToArray(tempDate)), (int) (ScreenWidth * (1 - mRatio)));
            addView(mEL);
            return mEL.ViewHeight;
        }

        private int removeViewList(){
            mTopIndex--;
            EventList view = ((EventList)(getChildAt(getChildCount()-1)));
            int result = view.ViewHeight;
            removeView(view);
            removeViewAt(getChildCount()-1);
            return result;
        }

        private int removeHideViewList(){
            mBottonIndex++;
            EventList view = ((EventList)(getChildAt(1)));
            int result = view.ViewHeight;
            removeViewAt(0);
            removeViewAt(0);
            return result;
        }


        public void changeDate(int offset ){
            GregorianCalendar temp = new GregorianCalendar(mCurDate.get(0),mCurDate.get(1),mCurDate.get(2));
            temp.add(Calendar.DAY_OF_MONTH, offset);
            mCurDate = CalUtil.CalToArray(temp);

            mBottonIndex -=offset;
            mTopIndex -=offset;
            mEventListListener.DateChange(mCurDate);

        }

        private EventController(ArrayList<Integer> date){
            mTodayDate = new GregorianCalendar(date.get(0),date.get(1),date.get(2));
            mCurDate = date;
        }

        private EventController(GregorianCalendar date){
            mTodayDate = date;
            mCurDate = new ArrayList<>();
            mCurDate.add(date.get(Calendar.YEAR));
            mCurDate.add(date.get(Calendar.MONTH));
            mCurDate.add(date.get(Calendar.DAY_OF_MONTH));
            mCurDate.add(date.get(Calendar.DAY_OF_WEEK));

        }

        public ArrayList<String> getEvenet(ArrayList<Integer> date){
            String a = new String();
            a= "year" +date.get(0) +"+month" +date.get(1)+ "+day"+date.get(2)+"+week"+date.get(3);
            int b = (int)(Math.random()*10);
            ArrayList<String> temp = new ArrayList<>();
            temp.add(a);
            for(int i = 0;i<b/3;i++){
                temp.add("test:"+i);
            }
            return temp;
        }

        public int getColor() {
            Random random =new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            return Color.rgb(r,g,b);
        }

        public int ViewScroll(int ScrollY) {
            if(ScrollY < ScreenHeight/2){
                int result=0;

                do{
                    result += addHideViewList();
                }while (result < mShowHeight);
                int count=0;
                do{
                    count += removeViewList();
                }while (count < mShowHeight);

                return result;
            }else if(ScrollY >ScreenHeight*5/2){
                int resule =0;
                int count=0;
                do{
                    count +=addViewList();
                }while (count < mShowHeight);

                do{
                    resule += removeHideViewList();
                }while (resule < mShowHeight);

                return -resule;

            }

            return 0;
        }


        public void DateScroll(int Scroll) {
            int temp=0;
            int i =-1;
            while(temp < Scroll){
                i+=2;
                temp +=((EventList)(getChildAt(i))).ViewHeight;
            }
            int offset = i/2 + mBottonIndex;
            changeDate(offset);
        }
    };



}
