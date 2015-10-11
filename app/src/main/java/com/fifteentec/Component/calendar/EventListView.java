package com.fifteentec.Component.calendar;


import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Database.EventRecord;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


public class EventListView extends ViewGroup implements GestureDetector.OnGestureListener{
    private Context mcontext ;
    private EventController mEvent;
    private float mRatio =1/3f;
    private int ScreenWidth;
    private int ScreenHeight;
    private LayoutInflater mInflater;
    private GestureDetector mGesture;
    private Surface mSurface;



    /**
     * 通过滚动EventList来改变当前事件,通知CalViewFragment改变时间.
     */



    public EventListView(Context context) {
        super(context, null);
        mcontext = context;
        mGesture = new GestureDetector(mcontext,this);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }





    public void init(ArrayList<Integer> date){
        setBackgroundColor(Color.WHITE);
        removeAllViews();
        mSurface = new Surface();
        ScreenWidth =(getResources().getDisplayMetrics().widthPixels);
        ScreenHeight = (getResources().getDisplayMetrics().heightPixels);
        GregorianCalendar temp = new GregorianCalendar(date.get(0),date.get(1),date.get(2),0,0,0);
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

    private class Surface{
        float DayTextSize = 1/10f;
        float WeekTextSize = 1/30f;
        float MonthTextSize = 1/40f;
    }


    /**
     * 单个事件的List,当天的所有事件组成一个View.
     */
    private class EventList extends View{

        private ArrayList<EventRecord> mNormalEvent;
        private ArrayList<EventRecord> mAllDayEvent;
        private GregorianCalendar mDate;
        private float ItemHeightRatio = 1/8f;
        private int ItemHeight;
        private int ItemWidth;
        private int mInterval;
        private float PaddingLeftRatio = 1/4f;
        private int mPaddingLeft;
        private int mPaddingRectRight = 40;
        private Paint mRectPaint;
        private Paint mTextPaint;
        public int ViewHeight; // View高度
        private EventManager mEventManager;
        private Paint DashPaint= new Paint();

        private Paint mSmallTextPaint= new Paint();


        public EventList(Context context) {
            this(context, null);
        }

        public EventList(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public EventList(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public boolean initView(GregorianCalendar date,int ParentWidth){

            ItemHeight = (int)(ParentWidth*ItemHeightRatio);
            mInterval = (int)(ParentWidth*ItemHeightRatio/2);

            this.mDate = date;
            float TextRatio = 1/13f;
            mPaddingLeft = (int)(ParentWidth*PaddingLeftRatio);
            this.ItemWidth = ParentWidth;
            mRectPaint = new Paint();
            mRectPaint.setColor(Color.parseColor("#920510"));
            mRectPaint.setAntiAlias(true);
            mTextPaint = new Paint();
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(ParentWidth * TextRatio);
            mNormalEvent = new ArrayList<>();
            mAllDayEvent = new ArrayList<>();
            mSmallTextPaint.setAntiAlias(true);
            mSmallTextPaint.setTextSize(ParentWidth * TextRatio / 2);
            mSmallTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
            DashPaint.setAntiAlias(true);
            DashPaint.setPathEffect(new DashPathEffect(new float[]{10, 2}, 1));
            DashPaint.setStrokeWidth(2);

            mEventManager = EventManager.newInstance(((BaseActivity)mcontext).getDBManager().getTableEvent(),EventManager.DAY_VIEW_EVENT_MANAGER,mDate.getTimeInMillis());
            for (int i = 0; i < mEventManager.getAllDayEventCount(); i++) {
                mAllDayEvent.add(mEventManager.getAllDayEventRecord(i));
            }
            for (int i = 0; i < mEventManager.getNormalDayEventCount(); i++) {
                mNormalEvent.add(mEventManager.getNormalEventByIndex(i));
            }
            ViewHeight = (ItemHeight+mInterval)*mNormalEvent.size()+(ItemHeight+mInterval)*mAllDayEvent.size()+mInterval;

            return mNormalEvent.size()==0&&mAllDayEvent.size()==0;
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
                height = (ItemHeight+mInterval)*mNormalEvent.size()+(ItemHeight+mInterval)*mAllDayEvent.size()+mInterval;
            }
            setMeasuredDimension(width,height);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            int topItem = 0;
            for(int i = 0; i <mNormalEvent.size();i++) {
                Rect mRect = new Rect(0,topItem,ItemWidth-mPaddingRectRight,topItem+ItemHeight);
                //canvas.drawRect(mRect, mRectPaint);
                Paint.FontMetricsInt font = mTextPaint.getFontMetricsInt();
                int baseline = (int)(mRect.top + (mRect.bottom - mRect.top - font.bottom + font.top) / 2 - font.top);
                canvas.drawText(mNormalEvent.get(i).introduction, mPaddingLeft, baseline, mTextPaint);
                canvas.drawRect(0, topItem, mPaddingLeft / 10, topItem+mPaddingLeft / 6, mRectPaint);
                String startTime = TimeInMillsToString(mNormalEvent.get(i).timebegin);
                Rect bound = new Rect();
                mSmallTextPaint.getTextBounds(startTime,0,startTime.length(),bound);
                canvas.drawText(TimeInMillsToString(mNormalEvent.get(i).timebegin), mPaddingLeft / 4, topItem+bound.height(), mSmallTextPaint);
                canvas.drawText(TimeInMillsToString(mNormalEvent.get(i).timeend),mPaddingLeft/4,topItem+bound.height()*3,mSmallTextPaint);
                topItem += (ItemHeight+mInterval);
            }

            Bitmap gou = BitmapFactory.decodeResource(getResources(),R.drawable.ok);
            Rect rect;

            for(int i = 0; i <mAllDayEvent.size();i++) {
                Rect mRect = new Rect(0,topItem,ItemWidth-mPaddingRectRight,topItem+ItemHeight);
                //canvas.drawRect(mRect, mRectPaint);
                Paint.FontMetricsInt font = mTextPaint.getFontMetricsInt();
                int baseline = (int)(mRect.top + (mRect.bottom - mRect.top - font.bottom + font.top) / 2 - font.top);
                String introduction = mAllDayEvent.get(i).introduction;
                Rect TextBound =  new Rect();
                mTextPaint.getTextBounds(introduction,0,introduction.length(),TextBound);
                canvas.drawText(introduction,mPaddingLeft,baseline,mTextPaint);
                rect= new Rect(mPaddingLeft/4,topItem,mPaddingLeft/3*2,topItem+mPaddingLeft/3);
                canvas.drawBitmap(gou, null, rect, null);
                canvas.drawLine(mPaddingLeft,topItem+mRect.height(),mPaddingLeft+TextBound.width(),topItem+mRect.height(),DashPaint);
                topItem += (ItemHeight+mInterval);
            }
        }

        private String TimeInMillsToString(long mills){
            GregorianCalendar time = new GregorianCalendar();
            time.setTimeInMillis(mills);
            String resule = new String();
            if( time.get(Calendar.HOUR_OF_DAY)<10) resule = "0"+time.get(Calendar.HOUR_OF_DAY)+":";
            else resule = time.get(Calendar.HOUR_OF_DAY)+":";

            if(time.get(Calendar.MINUTE)<10) resule += "0"+time.get(Calendar.MINUTE);
            else resule += time.get(Calendar.MINUTE)+"";
            return resule;
        }
    }


    /**
     * 控制整个View的排布与管理,动态加载上\下\当前View三个总共3*ScreenHeight的视图,提供预加载的能力.
     */
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
            addViewList();
            do {
                count += addViewList();
            }while(count<mShowHeight);


            return result;

        }

        /**
         * 加载上页面的View
         * @return 本次加载过程中增加的Height
         */
        public int  addHideViewList(){
            mBottonIndex--;
            GregorianCalendar tempDate = new GregorianCalendar(mCurDate.get(0),mCurDate.get(1),mCurDate.get(2),0,0,0);
            tempDate.add(Calendar.DAY_OF_MONTH, mBottonIndex);

            EventList mEL = new EventList(mcontext);
            if(mEL.initView(tempDate, (int) (ScreenWidth * (1 - mRatio)))){
                addView(addViewText(tempDate,true), 0);
            }else{
                addView(addViewText(tempDate,false), 0);
            }
            addView(mEL, 1);
            return mEL.ViewHeight;


        }


        /**
         * 加载下视图的Viewa
         * @return 本次加载增加的Height
         */
        public int addViewList(){
            mTopIndex++;
            GregorianCalendar tempDate = new GregorianCalendar(mCurDate.get(0),mCurDate.get(1),mCurDate.get(2));
            tempDate.add(Calendar.DAY_OF_MONTH, mTopIndex);
            EventList mEL = new EventList(mcontext);
            if(mEL.initView(tempDate, (int) (ScreenWidth * (1 - mRatio)))){
                addView(addViewText(tempDate,true));
            }else{
                addView(addViewText(tempDate,false));
            }
            addView(mEL);
            return mEL.ViewHeight;
        }


        private View addViewText(GregorianCalendar tempDate,boolean isEmpty){
            View temp = mInflater.inflate(R.layout.view_event_list_text_layout,null);
            TextView day = (TextView) temp.findViewById(R.id.id_event_list_day_text);
            int dayDate =tempDate.get(Calendar.DAY_OF_MONTH);
            if(dayDate <10){
                day.setText("0" + tempDate.get(Calendar.DAY_OF_MONTH));
            }else{
                day.setText("" + tempDate.get(Calendar.DAY_OF_MONTH));
            }
            day.setTypeface(Typeface.DEFAULT_BOLD);

            if(isEmpty){
                day.setTextSize(TypedValue.COMPLEX_UNIT_PX,ScreenWidth*mSurface.MonthTextSize);
                day.setTextColor(getResources().getColor(R.color.InvaildGrayColor));
            }
            else {
                day.setTextSize(TypedValue.COMPLEX_UNIT_PX,ScreenWidth*mSurface.DayTextSize);
                day.setTextColor(Color.BLACK);
            }

            TextView Week = (TextView) temp.findViewById(R.id.id_event_list_week_text);
            if(isEmpty) {
                Week.setText("");
                Week.setTextSize(0);
            }
            else {
                Week.setText(CalUtil.WEEK_NAME.get(tempDate.get(Calendar.DAY_OF_WEEK)));
                Week.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenWidth * mSurface.WeekTextSize);

            }

            TextView Month = (TextView) temp.findViewById(R.id.id_event_list_month_text);
            int NowMonth = tempDate.get(Calendar.MONTH) +1;
            String month=new String();
            if(NowMonth<10){
                month = "0"+NowMonth;
            }else{
                month = ""+NowMonth;
            }
            Month.setText(tempDate.get(Calendar.YEAR)+"."+month);
            Month.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenWidth * mSurface.MonthTextSize);
            if(isEmpty){
                Month.setTextColor(getResources().getColor(R.color.InvaildGrayColor));
            }else {
                Month.setTextColor(Color.BLACK);
            }

            return temp;
        }


        /**
         * 删除上页面的View
         * @return 本次删除删去的height
         */
        private int removeViewList(){
            mTopIndex--;
            EventList view = ((EventList)(getChildAt(getChildCount()-1)));
            int result = view.ViewHeight;
            removeView(view);
            removeViewAt(getChildCount()-1);
            return result;
        }

        /**
         * 自己看
         * @return
         */
        private int removeHideViewList(){
            mBottonIndex++;
            EventList view = ((EventList)(getChildAt(1)));
            int result = view.ViewHeight;
            removeViewAt(0);
            removeViewAt(0);
            return result;
        }


        /**
         * 滚动offset单位的index之后的视图
         * @param offset
         */
        public void changeDate(int offset ){
            GregorianCalendar temp = new GregorianCalendar(mCurDate.get(0),mCurDate.get(1),mCurDate.get(2));
            temp.add(Calendar.DAY_OF_MONTH, offset);
            mCurDate = CalUtil.CalToArray(temp);

            mBottonIndex -=offset;
            mTopIndex -=offset;
            //mEventListListener.DateChange(mCurDate);

        }

        private EventController(ArrayList<Integer> date){
            mTodayDate = new GregorianCalendar(date.get(0),date.get(1),date.get(2),0,0,0);
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
