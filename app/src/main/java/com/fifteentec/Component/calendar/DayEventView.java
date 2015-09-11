package com.fifteentec.Component.calendar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.Database.EventRecord;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

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
    private TitleView mTitleBackGroud;

    private Context mcontext;
    private GregorianCalendar mDate;

    private int mScreenWidth;
    private int mScreenHeight;
    private DayEventViewListener dayEventViewListener;

    private boolean firstEntry = true;


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
        GregorianCalendar time = new GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH),0,0);
        mEventManager = EventManager.newInstance(((BaseActivity)mcontext).getDBManager().getTableEvent(),EventManager.DAY_VIEW_EVENT_MANAGER,time.getTimeInMillis());

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

        mTitleBackGroud = new TitleView(mcontext);

        addView(mTitleBackGroud);

        mDateView = new TextView(mcontext);
        String dateShow = mDate.get(Calendar.YEAR) + "年" + (mDate.get(Calendar.MONTH) + 1) + "月" + mDate.get(Calendar.DAY_OF_MONTH) + "日";
        mDateView.setText(dateShow);
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

        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);


        if(firstEntry) {
            mSurface.initSurface();
            firstEntry = false;
            mDateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSurface.TextSize);
            mAllDayView.initAllDay();

        }
        int SidePadding = (int) (mScreenWidth * mSurface.SidePadding);
        int UpdownPadding = (int) (mScreenHeight * mSurface.UpdownPadding);


        if (mBackgroundView != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.EXACTLY);
            mBackgroundView.measure(widthSpec, heightSpec);
        }

        int DateWidth = (int) ((mScreenWidth - 2 * (SidePadding + mSurface.PageSidePadding)));
        if (mDateView != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(DateWidth, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.TextHeight, MeasureSpec.EXACTLY);
            mDateView.measure(widthSpec, heightSpec);
        }


        if (mPageView != null) {
            int widthSpec = MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * SidePadding, MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mScreenHeight - 2 * UpdownPadding, MeasureSpec.EXACTLY);
            mPageView.measure(widthSpec, heightSpec);
        }

        int AllDayWidth = (int) ((mScreenWidth - 2 * (SidePadding + mSurface.PageSidePadding)) * mSurface.AllDayViewWidth);
        int AllDayHeight = mScreenHeight - 2 * (UpdownPadding + mSurface.PageUpdownPadding) - mSurface.TextHeight- mSurface.AllDayViewPadding;

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

        int Title =  ((mScreenWidth - 2 * (SidePadding)) );

        if(mTitleBackGroud!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec(Title,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.TextHeight+mSurface.PageUpdownPadding,MeasureSpec.EXACTLY);
            mTitleBackGroud.measure(widthSpec, heightSpec);
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

        /*
        if (mDateView != null) mDateView.layout(
                l + SidePadding + mSurface.PageSidePadding,
                t + UpdownPadding + mSurface.PageUpdownPadding,
                l + SidePadding + mSurface.PageSidePadding + mDateView.getMeasuredWidth(),
                t + UpdownPadding + mSurface.PageUpdownPadding + mDateView.getMeasuredHeight());

*/
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

        if(mTitleBackGroud != null) {

            int left =l+SidePadding;
            int top =t+UpdownPadding;
            int right =left +mTitleBackGroud.getMeasuredWidth();
            int botton =top+mTitleBackGroud.getMeasuredHeight();
            mTitleBackGroud.layout(left, top, right, botton);
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

    public void deleteView(long rid){
        mEventManager.deleteByRid(rid);
        mNormalEvent.UpdateView();
        mAllDayView.UpdateView();
        invalidate();
    }

    private class Surface {


        float SidePadding = (1 / 20f);
        float UpdownPadding = (1 / 20f);

        int BackgoundColor = Color.BLACK;
        float BackgroundAlpha = 0.5f;

        float TextHeightRatio= 1/12f;
        int TextHeight;
        int TextSize = 25;

        float PageSidePaddingRatio = 1/30f;
        int PageSidePadding ;
        float PageUpdownPaddingRatio = 1/40f;
        int PageUpdownPadding;
        int PageColor = Color.WHITE;

        Paint TitleDatePaint = new Paint();
        float TitleDatePaintRatio = 1/12f;
        Paint TitleSmallPaint = new Paint();
        float TitleSmallPaintRatio = 1/30f;
        float TitleTextPaddingRatio = 1/60f;
        int TitleTextPadding;

        float AllDayViewWidth = (5/ 16f);
        int AllDayItemHeight ;
        float AllDayItemHeightRatio = (1/8f) ;
        int AllDayViewPadding;
        float AllDayViewPaddingRatio = 1/65f;
        float AllDayViewTextSizeBase = 0f;
        float AllDayViewTextSizeRatio = 1/30f;

        String AllDayFirstString = "全天事件";


        void initSurface(){

            PageSidePadding = (int)(mScreenWidth*PageSidePaddingRatio);
            PageUpdownPadding= (int)(mScreenHeight*PageUpdownPaddingRatio);
            AllDayViewPadding = (int)(mScreenWidth*AllDayViewPaddingRatio);
            TextHeight = (int)(mScreenHeight*TextHeightRatio);
            TitleDatePaint.setTextSize(mScreenWidth * TitleDatePaintRatio);
            TitleDatePaint.setAntiAlias(true);
            TitleDatePaint.setColor(Color.WHITE);
            TitleDatePaint.setFakeBoldText(true);
            TitleSmallPaint.setTextSize(mScreenWidth * TitleSmallPaintRatio);
            TitleSmallPaint.setAntiAlias(true);
            TitleSmallPaint.setColor(Color.WHITE);
            TitleTextPadding = (int)(mScreenWidth*TitleTextPaddingRatio);
            AllDayItemHeight = (int)(AllDayItemHeightRatio*mScreenWidth);
        }

    }

    private class AllDayView extends ViewGroup implements GestureDetector.OnGestureListener {

        long DownChildId = -1;
        int ScrollMin = 0;
        int ScreenHeight=0;
        int allHeihgt=0;


        GestureDetector mgestureDetector;

        public AllDayView(Context context) {
            this(context, null);
        }

        public AllDayView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public AllDayView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

        }

        void initAllDay() {
            mgestureDetector = new GestureDetector(mcontext, this);
            UpdateView();
        }

        void addTx(int i){
            tempRectView tx = new tempRectView(mcontext);
            String item = mEventManager.getAllDayIntroduciton(i);
            tx.setText(item);
            tx.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.AllDayViewTextSizeBase + mSurface.AllDayViewTextSizeRatio * mScreenWidth);
            Drawable leftDrawabletx = getResources().getDrawable(R.drawable.dot);
            leftDrawabletx.setBounds(0,0,mSurface.AllDayItemHeight/5,mSurface.AllDayItemHeight/5);
            tx.setCompoundDrawablePadding(mSurface.AllDayViewPadding);
            tx.setCompoundDrawables(leftDrawabletx, null, null, null);
            tx.setPadding(mSurface.AllDayViewPadding, mSurface.AllDayViewPadding, mSurface.AllDayViewPadding, mSurface.AllDayViewPadding);
            int aimColor;
            switch (mEventManager.getAllDayTag(i)){
                case 0:
                    aimColor = R.color.WorkEventColor;
                    break;
                case 1:
                    aimColor = R.color.StudyEventColor;
                    break;
                case 2:
                    aimColor = R.color.EntertainEventColor;
                    break;
                case 3:
                    aimColor = R.color.OtherEventColor;
                    break;
                default:
                    aimColor = R.color.OtherEventColor;
            }
            tx.setBackgroundColor(getResources().getColor(aimColor));
            tx.setGravity(Gravity.CENTER_VERTICAL);
            tx.setTag(mEventManager.getAllDayEventRid(i));
            addView(tx);
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            ScreenHeight =height;
            for (int i = 0; i < getChildCount(); i++) {
                View temp = getChildAt(i);
                int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.AllDayItemHeight, MeasureSpec.EXACTLY);
                temp.measure(widthSpec, heightSpec);
            }

            setMeasuredDimension(width, height);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View temp = getChildAt(i);
                temp.layout(0, height, temp.getMeasuredWidth(), height + temp.getMeasuredHeight());
                height += temp.getMeasuredHeight()+mSurface.AllDayViewPadding;
            }
            allHeihgt = getChildCount()*(mSurface.AllDayViewPadding+mSurface.AllDayItemHeight);
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //if (event.getAction() == MotionEvent.ACTION_UP) DownChildId = -1;
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
                dayEventViewListener.checkExist(DownChildId);
                DownChildId = -1;
            }else if(DownChildId == -2){
                EventRecord eventRecord =  new EventRecord();
                eventRecord.timebegin = mEventManager.DayView_Date;
                eventRecord.timeend = mEventManager.DayView_Date;
                dayEventViewListener.createRecord(NewEventView.HAVE_TIME,eventRecord);

            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            DownChildId = -1;
            Log.d("Test", "getScrollY:" + getScrollY() + " distanceY:" + distanceY + " allHeight:" + this.allHeihgt + " ScreenHeight:" + ScreenHeight);
            if(distanceY<0){
                if(getScrollY()>ScrollMin) {
                    if (getScrollY() - ScrollMin < Math.abs(distanceY)) scrollTo(0, ScrollMin);
                    else scrollBy(0, (int) distanceY);
                }else scrollBy(0,ScrollMin);
            }else {
                if(getScrollY()<this.allHeihgt -ScreenHeight) {
                    if (this.allHeihgt - ScreenHeight - getScrollY() < Math.abs(distanceY))
                        scrollTo(0, allHeihgt - ScreenHeight);
                    else scrollBy(0, (int) distanceY);
                }else {
                    scrollTo(0,this.allHeihgt -ScreenHeight>ScrollMin?this.allHeihgt -ScreenHeight:ScrollMin);
                }
            }
            return true;
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
            DownChildId =-1;

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        public void UpdateView() {
            removeAllViews();
            DownChildId = -1;

            tempRectView txfirst = new tempRectView(mcontext);
            txfirst.setText(mSurface.AllDayFirstString);
            txfirst.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.AllDayViewTextSizeBase + mSurface.AllDayViewTextSizeRatio * mScreenWidth);
            Drawable leftDrawable = getResources().getDrawable(R.drawable.plusbutton);
            leftDrawable.setBounds(0,0,mSurface.AllDayItemHeight/5,mSurface.AllDayItemHeight/5);
            txfirst.setCompoundDrawablePadding(mSurface.AllDayViewPadding);
            txfirst.setCompoundDrawables(leftDrawable, null, null, null);
            txfirst.setTag((long) -2);
            txfirst.setPadding(mSurface.AllDayViewPadding,mSurface.AllDayViewPadding,mSurface.AllDayViewPadding,mSurface.AllDayViewPadding);
            txfirst.setBackgroundColor(getResources().getColor(R.color.AllDayEvent));
            txfirst.setGravity(Gravity.CENTER_VERTICAL);
            addView(txfirst);
            for (int i = 0; i < mEventManager.getAllDayEventCount(); i++) {
                addTx(i);
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

    private class TitleView extends ImageView{

        public TitleView(Context context) {
            this(context, null);
        }

        public TitleView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initTitleView();

        }

        private void initTitleView(){
            this.setImageResource(R.drawable.cat);
            this.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Rect rect = new Rect();
            String date = mDate.get(Calendar.DAY_OF_MONTH)+"";
            mSurface.TitleDatePaint.getTextBounds(date,0,date.length(),rect);
            int height = rect.height();
            canvas.drawText(date, mSurface.TitleTextPadding, mSurface.TitleTextPadding + rect.height(), mSurface.TitleDatePaint);
            String week = CalUtil.WEEK_NAME.get(mDate.get(Calendar.DAY_OF_WEEK));
            mSurface.TitleSmallPaint.getTextBounds(week,0,week.length(),rect);
            canvas.drawText(week,mSurface.TitleTextPadding*3/2,2*mSurface.TitleTextPadding+rect.height()+height,mSurface.TitleSmallPaint);

        }
    }
}


