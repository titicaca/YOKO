package com.fifteentec.Component.calendar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.Database.TableEvent;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarView extends ViewGroup implements GestureDetector.OnGestureListener {


    private Surface mSurface;

    private int ViewHeight;
    private int ViewWidth;
    private ArrayList<Integer> today; // 初始化时确定的今天时间,无法手动改变.
    private GregorianCalendar CurrentWeekBegin;  // 当前选择器选择的时间,为某个星期的周一开始的时间(0点)
    private Context mContext;
    private ViewController mViewController; //  管理布局MonthView的容器
    private boolean firstEnter =true;
    private GestureDetector gestureDetector;
    private AnimatorController mAnimatorController; //管理动画的容器

    int MinDistance =150;

    private TextView textViewSun;
    private TextView textViewMon;
    private TextView textViewTues;
    private TextView textViewWed;
    private TextView textViewThr;
    private TextView textViewFri;
    private TextView textViewSat;
    //以上为周一到周日的Text
    private ShadowView backGound;
    //展开的背景颜色
    private ImageView puller;
    //展开后的下拉拉手
    private int PullDistance= -1;
    //点击拉手时句距离


    private final int MONTH=0x00;
    private final int WEEK =0x01;
    private final int WEEK_HORIZON=0x02;
    private final int WEEK_VERTICAL= 0x03;
    private int OPERATION= WEEK;
    //操作状态,在不同的状态提供不同的手势控制

    private boolean ViewEnable =true;

    private CalendarListener mCalendarListener;

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

    public interface CalendarListener{
        void UpdateTime(GregorianCalendar time);
        void ShowDayDetail(GregorianCalendar time);
        void ModeSwitch(boolean isWeek);
    }

    public void setmCalendarListener(CalendarListener mCalendarListener) {
        this.mCalendarListener = mCalendarListener;
    }

    public void TouchEnable(boolean Enable){
        ViewEnable = Enable;
    }

    public void initView(ArrayList<Integer> date) {
        mAnimatorController=new AnimatorController();
        gestureDetector = new GestureDetector(mContext,this);
        gestureDetector.setIsLongpressEnabled(false);
        today = new ArrayList<>(Arrays.asList(date.get(0), date.get(1), date.get(2), date.get(3)));
        CurrentWeekBegin = new GregorianCalendar(today.get(0),today.get(1),today.get(2),0,0);
        CurrentWeekBegin.add(Calendar.DAY_OF_MONTH, 1 - today.get(3));


        mSurface = new Surface();
        backGound = new ShadowView(mContext);

        addView(backGound, 0);
        mViewController =new ViewController(mContext);
        textViewSun = new TextView(mContext);
        textViewSun.setText("日");
        textViewSun.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textViewSun);
        textViewMon = new TextView(mContext);
        textViewMon.setText("一");
        textViewMon.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textViewMon);
        textViewTues = new TextView(mContext);
        textViewTues.setText("二");
        textViewTues.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textViewTues);
        textViewWed = new TextView(mContext);
        textViewWed.setText("三");
        textViewWed.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textViewWed);
        textViewFri = new TextView(mContext);
        textViewFri.setText("五");
        textViewFri.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textViewFri);
        textViewSat = new TextView(mContext);
        textViewSat.setText("六");
        textViewSat.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textViewSat);
        textViewThr = new TextView(mContext);
        textViewThr.setText("四");
        textViewThr.setGravity(Gravity.CENTER_HORIZONTAL);
        addView(textViewThr);
        puller = new ImageView(mContext);
        puller.setImageResource(R.drawable.puller);
        addView(mViewController);

        addView(puller,1);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        ViewWidth = MeasureSpec.getSize(widthMeasureSpec);

        if(firstEnter) {
            mSurface.initSurface();
            mViewController.initCalView();
            firstEnter = false;
        }



        if(mViewController != null){
            int widthSpec = MeasureSpec.makeMeasureSpec(ViewWidth,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(ViewHeight,MeasureSpec.EXACTLY);
            mViewController.measure(widthSpec,heightSpec);
        }

        int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.TextHeight,MeasureSpec.EXACTLY);

        if(textViewSun !=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewCellWidth,MeasureSpec.EXACTLY);
            textViewSun.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.WeekTextSize);
            textViewSun.measure(widthSpec, heightSpec);
        }
        if( textViewMon !=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewCellWidth,MeasureSpec.EXACTLY);
            textViewMon.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.WeekTextSize);
            textViewMon.measure(widthSpec, heightSpec);
        }
        if(textViewTues !=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewCellWidth,MeasureSpec.EXACTLY);
            textViewTues.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.WeekTextSize);
            textViewTues.measure(widthSpec, heightSpec);
        }
        if( textViewWed!=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewCellWidth,MeasureSpec.EXACTLY);
            textViewWed.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.WeekTextSize);
            textViewWed.measure(widthSpec, heightSpec);
        }
        if( textViewThr!=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewCellWidth,MeasureSpec.EXACTLY);
            textViewThr.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.WeekTextSize);
            textViewThr.measure(widthSpec, heightSpec);
        }
        if( textViewFri!=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewCellWidth,MeasureSpec.EXACTLY);
            textViewFri.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.WeekTextSize);
            textViewFri.measure(widthSpec, heightSpec);
        }
        if( textViewSat!=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewCellWidth,MeasureSpec.EXACTLY);
            textViewSat.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.WeekTextSize);
            textViewSat.measure(widthSpec, heightSpec);
        }

        if(backGound != null){
            int mheightSpec = MeasureSpec.makeMeasureSpec(ViewHeight,MeasureSpec.EXACTLY);
            int widthSpec = MeasureSpec.makeMeasureSpec(ViewWidth,MeasureSpec.EXACTLY);
            backGound.measure(widthSpec,mheightSpec);
        }

        if(puller != null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.pullerSize,MeasureSpec.EXACTLY);
            int mheightSpec = MeasureSpec.makeMeasureSpec(mSurface.pullerSize,MeasureSpec.EXACTLY);
            puller.measure(widthSpec, mheightSpec);
        }

        setMeasuredDimension(ViewWidth, ViewHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if(backGound != null) backGound.layout(0,0,backGound.getMeasuredWidth(),backGound.getMeasuredHeight());
        if(mViewController != null&&textViewSun != null){
            mViewController.layout(0, textViewSun.getMeasuredHeight(), mViewController.getMeasuredWidth(), textViewSun.getMeasuredHeight() + mViewController.getMeasuredHeight());
        }

        if(textViewSun!=null) textViewSun.layout(mSurface.MonthIconWidth,0,mSurface.MonthIconWidth+textViewSun.getMeasuredWidth(),textViewSun.getMeasuredHeight());
        if(textViewMon!=null) textViewMon.layout(mSurface.MonthIconWidth+mSurface.ViewCellWidth,0,mSurface.MonthIconWidth+mSurface.ViewCellWidth+textViewMon.getMeasuredWidth(),textViewMon.getMeasuredHeight());

        if(textViewTues!=null)textViewTues.layout(mSurface.MonthIconWidth+mSurface.ViewCellWidth*2,0,mSurface.MonthIconWidth+mSurface.ViewCellWidth*2+textViewTues.getMeasuredWidth(),textViewTues.getMeasuredHeight());
        if(textViewWed!=null) textViewWed.layout(mSurface.MonthIconWidth+mSurface.ViewCellWidth*3,0,mSurface.MonthIconWidth+mSurface.ViewCellWidth*3+textViewWed.getMeasuredWidth(),textViewWed.getMeasuredHeight());

        if(textViewThr!=null) textViewThr.layout(mSurface.MonthIconWidth+mSurface.ViewCellWidth*4,0,mSurface.MonthIconWidth+mSurface.ViewCellWidth*4+textViewThr.getMeasuredWidth(),textViewThr.getMeasuredHeight());

        if(textViewFri!=null) textViewFri.layout(mSurface.MonthIconWidth+mSurface.ViewCellWidth*5,0,mSurface.MonthIconWidth+mSurface.ViewCellWidth*5+textViewFri.getMeasuredWidth(),textViewFri.getMeasuredHeight());
        if(textViewSat!=null) textViewSat.layout(mSurface.MonthIconWidth+mSurface.ViewCellWidth*6,0,mSurface.MonthIconWidth+mSurface.ViewCellWidth*6+textViewSat.getMeasuredWidth(),textViewSat.getMeasuredHeight());
        if(puller != null && mViewController != null){
            puller.layout((ViewWidth - puller.getMeasuredWidth()) / 2,
                    mViewController.getMeasuredHeight() + textViewSun.getMeasuredHeight()-(int)(puller.getMeasuredHeight()*(mSurface.ScreenStrinkRatio)),
                    (ViewWidth + puller.getMeasuredWidth()) / 2,
                    mViewController.getMeasuredHeight() + textViewSun.getMeasuredHeight()+puller.getMeasuredHeight()-(int)(puller.getMeasuredHeight()*(mSurface.ScreenStrinkRatio)));
        }


    }

    private boolean ClickDistancce(int x,int y,int targetX,int targetY){
        int xoffset = Math.abs(targetX - x);
        int yoffset = Math.abs(targetY-y);
        double distance = Math.sqrt(xoffset*xoffset+yoffset*yoffset);
        return distance < MinDistance;
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(PullDistance>0){
                PullDistance = -1;
                if(mSurface.ViewMeasureHeight-mSurface.fullViewHeight>mSurface.ScrollMinDistanceTrager){
                    CurrentWeekBegin.add(Calendar.DAY_OF_MONTH, -CurrentWeekBegin.get(Calendar.DAY_OF_WEEK) + 1);
                    mAnimatorController.ScrollToWeekAnimation(mSurface.ViewCellHeight);
                    mCalendarListener.ModeSwitch(true);
                    OPERATION = WEEK;


                }
                else mAnimatorController.ScrollToWeekAnimation(mSurface.fullViewHeight);
            }
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (OPERATION == MONTH && ClickDistancce((int) e.getX(), (int)e.getY(),(int) puller.getX(),(int) puller.getY())){
            PullDistance = (int)(e.getY()-puller.getY());
            return true;
        }
        return false;
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
        if(PullDistance>0){
            mSurface.ScreenStrinkRatioUpdate(e2.getY() - PullDistance,false);

            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private class Surface{


        int ScrollMinDistanceTrager;
        int ViewMeasureHeight;
        int fullViewHeight;
        float ViewMeasureHeightRatio = (28/40f);
        float TextHeightRatio = (1/35f);
        int TextHeight;
        int ViewCellWidth;
        int ViewCellHeight;
        float CellHeightRatio = (1/9f);
        float ViewWidthRightPadding = 1/30f;

        float MonthGapRatio = (1/20f);
        int MonthGap;

        Paint whitePaint = new Paint();

        Paint MonthShadowPaint = new Paint();
        int ShadowColor =getResources().getColor(R.color.ShadowColor);
        float ShadowSize = 1/50f;

        float WeekTextSizeRatio= 1/4f;
        float WeekTextSize;
        int MonthTextSize;
        float MonthTextSizeRatio = 1/3f;
        Paint MonthText = new Paint();
        int MonthNormalTextColor = Color.BLACK;
        Paint MonthChineseText = new Paint();
        int MonthChineseTextInt;
        Paint MonthTodayText = new Paint();
        int MonthTodayTextColor = Color.WHITE;
        Paint MonthTodayChineseText = new Paint();
        Paint MonthTodayCircle = new Paint();
        int MonthTodayCircleColor= getResources().getColor(R.color.appMainColor);

        int MonthIconWidth;
        float MonthIconWidthRatio = 1/7f;
        float MonthIconPaddingRatio=1/5f;
        int MonthIconPadding;

        Paint MonthIconMonthTextPaint = new Paint();
        int MonthIconMonthTextSize;
        int MonthIconMonthTextColor = getResources().getColor(R.color.appMainColor);
        Paint MonthIconYearTextPaint = new Paint();
        int MonthIconYearTextSize;
        int MonthIconYearTextColor = Color.parseColor("#555555");

        Paint MonthCurrentWeek = new Paint();
        int MonthCurrentWeekColor = Color.parseColor("#ffffff");


        float pullerSizeRatio = 1/10f;
        int pullerSize ;

        float ScreenStrinkRatio =1f;

        void initSurface(){

            whitePaint.setColor(Color.WHITE);
            MonthGap = (int)(ViewHeight*MonthGapRatio*(1-ScreenStrinkRatio));
            MonthIconWidth = (int)(ViewWidth*MonthIconWidthRatio);
            ViewCellWidth = (int)((1-ViewWidthRightPadding)*ViewWidth-MonthIconWidth)/7;
            ViewCellHeight =(int)(ViewHeight*(CellHeightRatio));
            ViewMeasureHeight = (int)(ViewHeight*ViewMeasureHeightRatio)-(int)(((ViewHeight*ViewMeasureHeightRatio)-ViewCellHeight)*ScreenStrinkRatio);
            TextHeight = (int)(ViewHeight*TextHeightRatio);
            fullViewHeight = (int)(ViewHeight*ViewMeasureHeightRatio);
            ScrollMinDistanceTrager = (ViewHeight-fullViewHeight)/3;
            pullerSize = (int)(ViewWidth*pullerSizeRatio);

            WeekTextSize = ViewCellWidth*WeekTextSizeRatio;

            MonthIconPadding = (int)(ViewCellHeight*MonthIconPaddingRatio);
            MonthTextSize = (int) (ViewCellWidth * MonthTextSizeRatio);
            MonthText.setColor(MonthNormalTextColor);
            MonthText.setAntiAlias(true);
            MonthText.setTextSize(MonthTextSize);

            MonthIconMonthTextSize = MonthIconWidth/2;
            MonthIconMonthTextPaint.setAntiAlias(true);
            MonthIconMonthTextPaint.setFakeBoldText(true);
            MonthIconMonthTextPaint.setColor(MonthIconMonthTextColor);
            MonthIconMonthTextPaint.setTextSize(MonthIconMonthTextSize);
            MonthIconYearTextSize = MonthIconMonthTextSize/2;
            MonthIconYearTextPaint.setTextSize(MonthIconYearTextSize);
            MonthIconYearTextPaint.setColor(MonthIconYearTextColor);
            MonthIconYearTextPaint.setAntiAlias(true);

            MonthShadowPaint.setAntiAlias(true);
            MonthShadowPaint.setShadowLayer(ViewMeasureHeight * ShadowSize,ViewMeasureHeight*ShadowSize,ViewMeasureHeight*ShadowSize,ShadowColor );

            MonthChineseTextInt =MonthTextSize/2;
            MonthChineseText.setTextSize(MonthChineseTextInt);
            MonthChineseText.setAntiAlias(true);
            MonthChineseText.setColor(MonthNormalTextColor);

            MonthTodayCircle.setColor(MonthTodayCircleColor);
            MonthTodayCircle.setAntiAlias(true);

            MonthTodayText.setColor(MonthTodayTextColor);
            MonthTodayText.setAntiAlias(true);
            MonthTodayText.setTextSize(MonthTextSize);
            MonthTodayText.setFakeBoldText(true);
            MonthTodayChineseText.setTextSize(MonthChineseTextInt);
            MonthTodayChineseText.setAntiAlias(true);
            MonthTodayChineseText.setColor(MonthTodayTextColor);

            MonthCurrentWeek.setColor(MonthCurrentWeekColor);
            MonthCurrentWeek.setAntiAlias(true);

        }

        public void ScreenStrinkRatioUpdate(float v,boolean animaition) {
            int answer;
            if(!animaition) {

                if (v > fullViewHeight)
                    answer = fullViewHeight + (int) ((v - fullViewHeight) * (1 / 2f));
                else answer = fullViewHeight;
            }else {
                answer = (int)v;
            }
            if(((fullViewHeight-(float)answer)/(fullViewHeight-ViewCellHeight))<=1) {
                ScreenStrinkRatio = ((fullViewHeight - (float) answer) / (fullViewHeight - ViewCellHeight));
                ViewMeasureHeight = answer;
            }else{
                ScreenStrinkRatio = 1;
                ViewMeasureHeight = mSurface.ViewCellHeight;

            }
            MonthGap = (int) (ViewHeight * MonthGapRatio * (1 - ScreenStrinkRatio));
            mViewController.requestLayout();
            backGound.invalidate();


        }
    }

    private class AnimatorController{

        AnimatorSet set = null;
        ObjectAnimator Scroller = null;

        private void CircleAnimation(final MonthView monthView){
            if(set==null) {
                set = new AnimatorSet();
                int Time=5;

                for (int i = 0; i < Time; i++) {
                    final int index = monthView.addHolder();
                    int start = 220;
                    int interval = (255-start)/Time;
                    int color = Color.rgb(255 - (Time -1- i) * interval, 255 - (Time -1- i) * interval, 255 - (Time -1- i) * interval);
                    monthView.circleAnimaitionHolders.get(index).initHolder(color);

                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(monthView, "HJQ", 0, 12);
                    objectAnimator.setDuration(200);
                    objectAnimator.setInterpolator(new TimeInterpolator() {

                         @Override
                           public float getInterpolation(float input) {
                             input -= 1;
                             return 1 - input * input * input * input;
                       }
                     });
                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float Value = (float) animation.getAnimatedValue();
                            monthView.circleAnimaitionHolders.get(index).FrameChange(Value);
                            monthView.invalidate();
                        }
                    });
                    set.play(objectAnimator).after(i * 60);
                }

                MonthView Current = null;
                for (int i = 0; i < mViewController.getChildCount() / 2; i++) {
                    MonthView monthView1 = (MonthView)mViewController.getChildAt(i*2);
                    if(monthView1.month.get(Calendar.YEAR)*12+monthView1.month.get(Calendar.MONTH)==CurrentWeekBegin.get(Calendar.YEAR)*12+CurrentWeekBegin.get(Calendar.MONTH)){
                        Current = monthView1;
                    }
                }
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mSurface, "HJQ", 255, 0);
                objectAnimator.setDuration(300);
                final MonthView finalCurrent = Current;
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int Value = (int) animation.getAnimatedValue();
                        if(finalCurrent != null){
                            mSurface.MonthCurrentWeek.setAlpha(Value);
                            finalCurrent.invalidate();
                        }
                    }
                });

                set.play(objectAnimator).after(10);

                set.start();
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        monthView.circleAnimaitionHolders.clear();
                        mViewController.UpdateDrawCurrent();
                        mSurface.MonthCurrentWeek.setAlpha(255);
                        set = null;
                        CurrentWeekBegin.add(Calendar.DAY_OF_MONTH, -CurrentWeekBegin.get(Calendar.DAY_OF_WEEK) + 1);
                        mAnimatorController.ScrollToWeekAnimation(mSurface.ViewCellHeight);
                        mCalendarListener.ModeSwitch(true);
                        OPERATION = WEEK;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        monthView.circleAnimaitionHolders.clear();
                        mViewController.UpdateDrawCurrent();
                        mSurface.MonthCurrentWeek.setAlpha(255);
                        set = null;
                        for (int i = 0; i < mViewController.getChildCount(); i++) {
                            View view = mViewController.getChildAt(i);
                            view.invalidate();
                        }
                        CurrentWeekBegin.add(Calendar.DAY_OF_MONTH, -CurrentWeekBegin.get(Calendar.DAY_OF_WEEK) + 1);
                        mAnimatorController.ScrollToWeekAnimation(mSurface.ViewCellHeight);
                        mCalendarListener.ModeSwitch(true);
                        OPERATION = WEEK;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            } else {
                set.cancel();
                CircleAnimation(monthView);
            }
        }

        private void ScrollToWeekAnimation(final float end){
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mSurface, "HJQ", mSurface.ViewMeasureHeight,end);
            final int startheight =  mSurface.ViewMeasureHeight;
            objectAnimator.setDuration(300);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.setInterpolator(new TimeInterpolator() {
                private static final float s = 1.70158f * 1.525f;

                @Override
                public float getInterpolation(float input) {
                    if ((input*=2) < 1.0f) {
                        return 0.5f * (input*input*((s + 1)*input - s));
                    }

                    input -= 2;
                    return 0.5f*(input*input*((s + 1)*input + s) + 2);
                }
            });
            objectAnimator.start();
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float Value = (float) animation.getAnimatedValue();

                    mSurface.ScreenStrinkRatioUpdate(Value, true);
                }
            });
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if ((mSurface.ViewMeasureHeight < mSurface.fullViewHeight&&startheight>=mSurface.fullViewHeight)) {
                        mViewController.GoToCurrent(CurrentWeekBegin.get(Calendar.YEAR) * 12 + CurrentWeekBegin.get(Calendar.MONTH) - (today.get(0) * 12 + today.get(1)));
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        private void ScrollToLeftRightAnimation(float end){
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mViewController, "HJQ", mViewController.ScrollXToken, end);
            objectAnimator.setDuration(200);
            objectAnimator.setInterpolator(new TimeInterpolator() {
                @Override
                public float getInterpolation(float input) {
                    input -= 1;
                    return 1 - input * input * input * input;
                }
            });
            objectAnimator.start();
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float Value = (float) animation.getAnimatedValue();
                    mViewController.ScrollXToken = Value;
                    mViewController.requestLayout();
                }
            });
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(mViewController.ScrollXToken<0){
                        mViewController.scrollBy(0, -mSurface.ViewCellHeight);
                        CurrentWeekBegin.add(Calendar.WEEK_OF_YEAR, -1);
                        mViewController.UpdateDrawCurrent();
                        mCalendarListener.UpdateTime(CurrentWeekBegin);
                    }else if(mViewController.ScrollXToken>0) {
                        mViewController.scrollBy(0, mSurface.ViewCellHeight);
                        CurrentWeekBegin.add(Calendar.WEEK_OF_YEAR, 1);
                        mViewController.UpdateDrawCurrent();
                        mCalendarListener.UpdateTime(CurrentWeekBegin);

                    }
                    mViewController.UpdateMonth();
                    mViewController.ScrollXToken=0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    if(mViewController.ScrollXToken<0){
                        mViewController.scrollBy(0, -mSurface.ViewCellHeight);
                        CurrentWeekBegin.add(Calendar.WEEK_OF_YEAR, -1);
                        mViewController.UpdateDrawCurrent();
                        mCalendarListener.UpdateTime(CurrentWeekBegin);
                    }else if(mViewController.ScrollXToken>0) {
                        mViewController.scrollBy(0, mSurface.ViewCellHeight);

                        CurrentWeekBegin.add(Calendar.WEEK_OF_YEAR, 1);
                        mViewController.UpdateDrawCurrent();
                        mCalendarListener.UpdateTime(CurrentWeekBegin);

                    }
                    mViewController.UpdateMonth();
                    mViewController.ScrollXToken=0;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        private void ScrollToCurrenAnimation(final int end, final int addTime, final boolean little){
            if(Scroller == null) {
                Scroller = ObjectAnimator.ofInt(mSurface, "HJQ", mViewController.getScrollY(), end);
                Scroller.setDuration(200);
                Scroller.start();
                Scroller.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int Value = (int) animation.getAnimatedValue();
                        mViewController.scrollTo(0, Value);
                        mViewController.requestLayout();
                    }
                });
                Scroller.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Scroller =null;
                        if(!little) {
                            if (addTime < 0) {
                                for (int i = 0; i < -addTime; i++) {
                                    mViewController.deleteExcess();
                                }
                            } else if (addTime > 0) {
                                for (int i = 0; i < addTime; i++) {
                                    Log.d("Test", "delete");
                                    mViewController.scrollBy(0, -mViewController.deleteHide());
                                }
                            }
                            int ScrollY = mViewController.getScrollY();
                            int height = ScrollY;
                            for (int i = 0; i < mViewController.getChildCount() / 2; i++) {
                                MonthView monthView = (MonthView) mViewController.getChildAt(i * 2);
                                if (height - monthView.getMonthHeight() < 0) {
                                    break;
                                }
                                height -= monthView.getMonthHeight();
                            }
                            if (height - mSurface.MonthGap < 0) {
                                ScrollToCurrenAnimation(ScrollY - height, 0, true);
                            } else {
                                height -= mSurface.MonthGap;
                                int offset = height % mSurface.ViewCellHeight;
                                if (offset < mSurface.ViewCellHeight / 2)
                                    ScrollToCurrenAnimation(ScrollY - offset, 0, true);//mViewController.scrollBy(0, -offset);
                                else
                                    ScrollToCurrenAnimation(ScrollY +mSurface.ViewCellHeight-offset, 0, true);
                            }
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mViewController.scrollTo(0, end);
                        mViewController.requestLayout();
                        Scroller = null;
                        if (addTime < 0) {
                            for (int i = 0; i < -addTime; i++) {
                                mViewController.deleteExcess();
                            }
                        } else if (addTime > 0) {
                            for (int i = 0; i < addTime; i++) {
                                mViewController.scrollBy(0, -mViewController.deleteHide());
                            }
                        }
                        int ScrollY = mViewController.getScrollY();
                        int height = ScrollY;
                        for (int i = 0; i < mViewController.getChildCount() / 2; i++) {
                            MonthView monthView = (MonthView) mViewController.getChildAt(i * 2);
                            if (height - monthView.getMonthHeight() < 0) {
                                break;
                            }
                            height -= monthView.getMonthHeight();
                        }
                        if (height - mSurface.MonthGap < 0) {
                            ScrollToCurrenAnimation(ScrollY -height, 0, true);

                        } else {
                            height -= mSurface.MonthGap;
                            int offset = height % mSurface.ViewCellHeight;
                            if (offset < mSurface.ViewCellHeight / 3)
                                ScrollToCurrenAnimation(ScrollY - offset, 0, true);//mViewController.scrollBy(0, -offset);
                            else
                                ScrollToCurrenAnimation(ScrollY +mSurface.ViewCellHeight- offset, 0, true);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }
    }

    private class ViewController extends ViewGroup implements GestureDetector.OnGestureListener{

        ArrayList<MonthView> monthViews=new ArrayList<>();
        ArrayList<MonthIco> monthIcos = new ArrayList<>();
        int HideSpace;
        int ExcessSpace;
        int HideItemCount=0;
        int ExcessItemCount =0;
        int HideIndex=0;
        int ExcessIndex=-1;
        GestureDetector mGestureDetector;
        int ScrollBuffer = 50;
        int MonthViewHeight;
        float ScrollXToken=0;

        boolean LongEnough=false;
        boolean isScrolled = false;
        float VelocityTime = 4/10000f;

        MonthView  SelectedMonthView=null;

        GregorianCalendar SelectedTime;
        GregorianCalendar DrawCureent;

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
            UpdateDrawCurrent();
            SelectedTime = null;
            mGestureDetector = new GestureDetector(mContext,this);
            mGestureDetector.setIsLongpressEnabled(false);
            HideSpace = ViewHeight*2;
            ExcessSpace = ViewHeight*2;
            int ScrollTo=0;
            int temp = 0;
            do {
                temp += addHide();
                HideItemCount++;
                MonthView monthView = (MonthView)getChildAt(getChildCount()-2);
                if(monthView.gap) ScrollTo -= mSurface.ViewCellHeight;
            }while (temp < HideSpace);
            addExcess();
            MonthView monthView = (MonthView)getChildAt(getChildCount()-2);
            ScrollTo += temp;
            if(!monthView.gap) ScrollTo += mSurface.ViewCellHeight;

            temp = 0;
            do {
                temp += addExcess();
                ExcessItemCount++;
            }while (temp < ExcessSpace);

            MonthViewHeight = 0;
            for(MonthView item:monthViews){
                MonthViewHeight +=item.getMonthHeight();
            }
            int index = ((CurrentWeekBegin.get(Calendar.DAY_OF_MONTH)+CalUtil.FindFirstDayofMonthInWeek(CurrentWeekBegin))/7-2)*mSurface.ViewCellHeight;

            ScrollTo += index + mSurface.MonthGap;

            //this.scrollTo(0, ScrollTo);
            mViewController.GoToCurrent(CurrentWeekBegin.get(Calendar.YEAR) * 12 + CurrentWeekBegin.get(Calendar.MONTH) - (today.get(0) * 12 + today.get(1)));
            setBackgroundColor(Color.rgb(245,245,245));

        }

        public void UpdateDrawCurrent(){
            DrawCureent = CalUtil.CopyDate(CurrentWeekBegin);
            DrawCureent.add(Calendar.DAY_OF_MONTH, -DrawCureent.get(Calendar.DAY_OF_WEEK) + 1);
        }

        public void GoToCurrent(int offset){
            int addTime=0;
            if(offset<HideIndex){
                int finish = HideIndex;
                for (int i = 0; i <finish-offset+HideItemCount; i++) {
                    this.scrollBy(0, addHide());
                    addTime--;
                }
            }else if(offset>ExcessIndex){
                int finish = ExcessIndex;
                for (int i = 0; i < offset - finish+ExcessItemCount; i++) {
                    addExcess();
                    addTime++;
                }
            }
            int AimScroll=0;
            int begin = CalUtil.FindFirstDayofMonthInWeek(CurrentWeekBegin)-1;
            int height = ((begin+CurrentWeekBegin.get(Calendar.DAY_OF_MONTH)-1)/7)*mSurface.ViewCellHeight+mSurface.MonthGap;

            for (int i = 0; i <this.getChildCount()/2; i++) {
                MonthView monthView = (MonthView)this.getChildAt(i*2);
                if(monthView.index == offset){
                    AimScroll += height;
                    if(monthView.gap){
                        AimScroll -=mSurface.ViewCellHeight*mSurface.ScreenStrinkRatio;
                    }
                    break;
                }
                AimScroll+=((monthView.mMonthItem.size()-1)/7+1)*mSurface.ViewCellHeight;
                if(monthView.gap){
                    AimScroll -= mSurface.ViewCellHeight*mSurface.ScreenStrinkRatio;
                }
            }
            mAnimatorController.ScrollToCurrenAnimation(AimScroll, addTime, false);


        }



        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


            for (int i = 0; i < getChildCount()/2; i++) {
                View view = getChildAt(i * 2);
                if(view != null) {
                    int WithSpec = MeasureSpec.makeMeasureSpec(ViewWidth, MeasureSpec.EXACTLY);
                    int HeightSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewMeasureHeight, MeasureSpec.UNSPECIFIED);
                    view.measure(WithSpec, HeightSpec);
                }

                View view1= getChildAt(i * 2 + 1);
                if(view1 != null) {
                    int WithSpec1 = MeasureSpec.makeMeasureSpec(mSurface.MonthIconWidth, MeasureSpec.EXACTLY);
                    int HeightSpec = MeasureSpec.makeMeasureSpec(mSurface.ViewMeasureHeight, MeasureSpec.UNSPECIFIED);
                    view1.measure(WithSpec1, HeightSpec);
                }

            }

            setMeasuredDimension(ViewWidth, mSurface.ViewMeasureHeight);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int temp = 0;

            for (int i = 0; i < getChildCount()/2; i++) {

                int scrollTemp=0;
                MonthView monthView = (MonthView)getChildAt(i*2);
                if(monthView != null) {
                    if(monthView.gap){

                        scrollTemp =(int)(mSurface.ViewCellHeight*mSurface.ScreenStrinkRatio);

                    }
                    temp -=scrollTemp;
                    monthView.layout((int)(-monthView.getMeasuredWidth()/3-ScrollXToken), temp, (int)(monthView.getMeasuredWidth()*2/3-ScrollXToken), temp + monthView.getMeasuredHeight());
                    temp += monthView.getMeasuredHeight();
                }
                if(monthView.mMonthItem.size()%7==0){
                    scrollTemp=0;
                }else{
                    scrollTemp=(int)(mSurface.ViewCellHeight*mSurface.ScreenStrinkRatio);
                }
                MonthIco monthIco = (MonthIco)getChildAt(i*2+1);
                if(monthIco != null) {
                    if (getScrollY() > temp)
                        monthIco.layout(0, temp - monthIco.getMeasuredHeight()-scrollTemp, monthIco.getMeasuredWidth(), temp-scrollTemp);
                    else if (getScrollY() + monthView.getMeasuredHeight() > temp) {
                        if(getScrollY()+monthIco.getMeasuredHeight()>temp-scrollTemp)
                            monthIco.layout(0,temp-monthIco.getMeasuredHeight()-scrollTemp,monthIco.getMeasuredWidth(),temp-scrollTemp);
                        else if(getScrollY()>temp-monthView.getMeasuredHeight()+mSurface.MonthGap)
                            monthIco.layout(0, getScrollY(), monthIco.getMeasuredWidth(), getScrollY() + monthIco.getMeasuredHeight());
                        else
                            monthIco.layout(0, temp-monthView.getMeasuredHeight()+mSurface.MonthGap, monthIco.getMeasuredWidth(), temp-monthView.getMeasuredHeight()+mSurface.MonthGap + monthIco.getMeasuredHeight());

                    }
                    else
                        monthIco.layout(0, temp-monthView.getMeasuredHeight()+mSurface.MonthGap, monthIco.getMeasuredWidth(), temp-monthView.getMeasuredHeight() + monthIco.getMeasuredHeight()+mSurface.MonthGap);
                }



            }
        }

        public int  addHide(){
            HideIndex --;
            MonthView monthView = new MonthView(mContext);
            monthView.initMonthView(HideIndex);
            monthViews.add(0, monthView);
            int scrollTemp=0;
            if(monthView.gap){
                scrollTemp =(int)(mSurface.ViewCellHeight*mSurface.ScreenStrinkRatio);
            }
            this.addView(monthView, 0);
            MonthIco monthIco = new MonthIco(mContext);
            monthIco.initView(HideIndex);
            monthIcos.add(0, monthIco);
            this.addView(monthIco, 1);
            return monthView.getMonthHeight()-scrollTemp;

        }

        public int deleteHide(){

            int result;
            MonthView monthView = monthViews.get(0);
            result = monthView.getMonthHeight();
            int scrollTemp=0;
            if(monthView.gap){

                scrollTemp =(int)(mSurface.ViewCellHeight*mSurface.ScreenStrinkRatio);

            }
            this.removeView(monthView);
            monthViews.remove(0);
            MonthIco monthIco = monthIcos.get(0);
            this.removeView(monthIco);
            monthIcos.remove(0);
            HideIndex ++;
            return result-scrollTemp;
        }

        public int deleteExcess(){

            int result;
            MonthView monthView = monthViews.get(monthViews.size() - 1);
            result = monthView.getMonthHeight();
            this.removeView(monthView);
            monthViews.remove(monthViews.size() - 1);
            MonthIco monthIco = monthIcos.get(monthIcos.size()-1);
            this.removeView(monthIco);
            monthIcos.remove(monthIcos.size() - 1);
            ExcessIndex --;
            return result;
        }
        public int addExcess(){
            ExcessIndex ++;
            MonthView monthView = new MonthView(mContext);
            monthView.initMonthView(ExcessIndex);
            monthViews.add(monthView);
            this.addView(monthView);
            MonthIco monthIco = new MonthIco(mContext);
            monthIco.initView(ExcessIndex);
            monthIcos.add(monthIco);
            this.addView(monthIco);
            return monthView.getMonthHeight();
        }



        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if(!ViewEnable) return false;
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                LongEnough = false;
                isScrolled = false;
            }
            if(event.getAction()==MotionEvent.ACTION_UP){
                switch (OPERATION){
                    case WEEK_HORIZON:
                        if(ScrollXToken<-getMeasuredWidth()/3){
                            mAnimatorController.ScrollToLeftRightAnimation(-getMeasuredWidth());
                        }else if(ScrollXToken>getMeasuredWidth()/3){
                            mAnimatorController.ScrollToLeftRightAnimation(getMeasuredWidth());
                        }else{
                            mAnimatorController.ScrollToLeftRightAnimation(0);
                        }
                        this.requestLayout();
                        OPERATION=WEEK;
                        return true;
                    case WEEK_VERTICAL:
                        if(mSurface.ViewMeasureHeight>(mSurface.fullViewHeight+mSurface.ViewCellHeight)/2){
                            mAnimatorController.ScrollToWeekAnimation(mSurface.fullViewHeight);
                            mCalendarListener.ModeSwitch(false);
                            OPERATION=MONTH;
                            return true;
                        }else{
                            mAnimatorController.ScrollToWeekAnimation(mSurface.ViewCellHeight);
                            OPERATION=WEEK;
                            return true;
                        }
                    case MONTH:
                        if(isScrolled) {
                            int ScrollY = mViewController.getScrollY();
                            int height = ScrollY;
                            for (int i = 0; i < mViewController.getChildCount() / 2; i++) {
                                MonthView monthView = (MonthView) mViewController.getChildAt(i * 2);
                                if (height - monthView.getMonthHeight() < 0) {
                                    break;
                                }
                                height -= monthView.getMonthHeight();
                            }
                            if (height - mSurface.MonthGap < 0) {
                                mAnimatorController.ScrollToCurrenAnimation(ScrollY -height, 0, true);

                            } else {
                                height -= mSurface.MonthGap;
                                int offset = height % mSurface.ViewCellHeight;
                                if (offset < mSurface.ViewCellHeight / 2)
                                    mAnimatorController.ScrollToCurrenAnimation(ScrollY - offset, 0, true);
                                else
                                    mAnimatorController.ScrollToCurrenAnimation(ScrollY + mSurface.ViewCellHeight-offset, 0, true);
                            }
                        }
                        break;
                }
            }
            return mGestureDetector.onTouchEvent(event);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            LongEnough = true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(SelectedTime!=null&&!LongEnough&&OPERATION==MONTH){
                mAnimatorController.CircleAnimation(SelectedMonthView);
                CurrentWeekBegin.set(SelectedTime.get(Calendar.YEAR), SelectedTime.get(Calendar.MONTH), SelectedTime.get(Calendar.DAY_OF_MONTH), 0, 0);
                mCalendarListener.UpdateTime(CurrentWeekBegin);
            }else if(SelectedTime != null && !LongEnough && OPERATION == WEEK) {
                CurrentWeekBegin.set(SelectedTime.get(Calendar.YEAR), SelectedTime.get(Calendar.MONTH), SelectedTime.get(Calendar.DAY_OF_MONTH), 0, 0);
                mCalendarListener.ShowDayDetail(SelectedTime);

            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            switch (OPERATION){
                case MONTH:
                    this.scrollBy(0, (int) distanceY);
                    UpdateMonth();
                    isScrolled = true;
                    return true;
                case WEEK:
                    if(Math.abs(distanceX)>Math.abs(distanceY)&&Math.abs(distanceX)>10){
                        ScrollXToken+=(int)distanceX;
                        this.requestLayout();
                        OPERATION=WEEK_HORIZON;
                    }else if(Math.abs(distanceX)<Math.abs(distanceY)&&Math.abs(distanceY)>10&&Math.abs(e2.getY()-mSurface.ViewCellHeight)<50){
                        mSurface.ScreenStrinkRatioUpdate(e2.getY(),true);
                        OPERATION =WEEK_VERTICAL;
                    }
                    return true;
                case WEEK_HORIZON:
                    ScrollXToken+=(int)distanceX;
                    this.requestLayout();
                    return true;
                case WEEK_VERTICAL:
                    mSurface.ScreenStrinkRatioUpdate(e2.getY(), true);

                    return true;

            }
            return false;
        }

        private void UpdateMonth(){
            if (this.getScrollY() < ViewHeight + ScrollBuffer) {
                int scroll = addHide();
                this.scrollBy(0, scroll);
                deleteExcess();
            } else if (this.getScrollY() > 2 * ViewHeight - ScrollBuffer) {
                int scroll = -deleteHide();
                this.scrollBy(0, scroll);
                addExcess();
            }
            requestLayout();
        }
        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("Test", "Long Press");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(Math.abs(velocityY )>5000&&OPERATION == MONTH) {
                int offset = -(int) (velocityY * VelocityTime);
                GoToCurrent(getMidIndex()+offset);
            }

            return true;
        }

        private int getMidIndex(){
            int temp=0;
            for (int i = 0; i < getChildCount() / 2; i++) {
                MonthView monthView = (MonthView)getChildAt(i*2);
                temp += monthView.getHeight();
                if(temp > HideSpace){
                    return monthView.index;
                }
            }
            return 0;
        }

    }

    private class MonthView extends View {

        GregorianCalendar month;
        int index;
        ArrayList<Integer> mMonthItem;
        EventManager mEventManager;
        boolean gap =false;

        ArrayList<CircleAnimaitonHolder> circleAnimaitionHolders = new ArrayList<>();



        float DrawCircleX;
        float DrawCircleY;


        int getMonthHeight(){
            return ((mMonthItem.size()-1)/7+1)*mSurface.ViewCellHeight+mSurface.MonthGap;
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

        void initMonthView(int index) {
            month = new GregorianCalendar(today.get(0),today.get(1),1,0,0);
            month.add(Calendar.MONTH, index);
            mMonthItem  = CalUtil.GetDayInMonth(month);
            if(mMonthItem.get(0)==0) gap = true;
            this.index = index;
            mEventManager = EventManager.newInstance(((BaseActivity) mContext).getDBManager().getTableEvent(), EventManager.MONTH_VIEW_EVENT_MANAGER,month.getTimeInMillis());
            setTag("Month");

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int MonthHeight = ((mMonthItem.size()-1)/7+1)*mSurface.ViewCellHeight+mSurface.MonthGap;
            setMeasuredDimension(3 * width, MonthHeight);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            int MonthTextRatioInt= mSurface.ViewCellHeight *2/4;
            int ChineseTextRatioInt= mSurface.ViewCellHeight*4/6;
            int CircleY=mSurface.ViewCellHeight * 2 / 5;
            int width;
            int height;
            int index;
            Rect rect = new Rect();

            Paint usePaint ;
            Paint useChinesePaint;

            for(CircleAnimaitonHolder item:circleAnimaitionHolders){
                canvas.save();
                float Offset = (item.drawY-mSurface.MonthGap)%mSurface.ViewCellHeight;
                canvas.clipRect(0,item.drawY-Offset,getMeasuredWidth(),item.drawY-Offset+mSurface.ViewCellHeight);
                canvas.drawCircle(item.drawX, item.drawY, item.Radio, item.paint);
                canvas.restore();
            }


            int offset = (month.get(Calendar.YEAR)*12+month.get(Calendar.MONTH)-mViewController.DrawCureent.get(Calendar.YEAR)*12-mViewController.DrawCureent.get(Calendar.MONTH));
            if(offset==1||offset==0) {
                if((OPERATION == WEEK||OPERATION==WEEK_VERTICAL||OPERATION==WEEK_HORIZON)) {
                    int firstDay = CalUtil.FindFirstDayofMonthInWeek(mViewController.DrawCureent) - 1;
                    int CurIndexBegin=(mViewController.DrawCureent.get(Calendar.DAY_OF_MONTH)+firstDay)-1;
                    int drawHeight =((CurIndexBegin/7)*mSurface.ViewCellHeight+mSurface.MonthGap);
                    canvas.drawRect(0, drawHeight, this.getMeasuredWidth(), drawHeight + mSurface.ViewCellHeight, mSurface.MonthCurrentWeek);
                    GregorianCalendar gregorianCalendar = CalUtil.CopyDate(mViewController.DrawCureent);
                    gregorianCalendar.add(Calendar.WEEK_OF_YEAR, -1);
                    height = (CurIndexBegin / 7) * mSurface.ViewCellHeight;
                    for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
                        width = mSurface.MonthIconWidth + i * mSurface.ViewCellWidth;
                        if (gregorianCalendar.get(Calendar.DAY_OF_MONTH) == today.get(2)&&(today.get(1)==gregorianCalendar.get(Calendar.MONTH))) {
                            canvas.drawCircle(width + mSurface.ViewCellWidth / 2, mSurface.MonthGap + height + CircleY, mSurface.ViewCellWidth * 2 / 5, mSurface.MonthTodayCircle);
                            usePaint = mSurface.MonthTodayText;
                            useChinesePaint = mSurface.MonthTodayChineseText;
                        } else {
                            usePaint = mSurface.MonthText;
                            useChinesePaint = mSurface.MonthChineseText;
                        }
                        String text = gregorianCalendar.get(Calendar.DAY_OF_MONTH) + "";
                        mSurface.MonthText.getTextBounds(text, 0, text.length(), rect);
                        canvas.drawText(text, width + mSurface.ViewCellWidth / 2 - rect.width() / 2, mSurface.MonthGap + height + MonthTextRatioInt - rect.height() / 2, usePaint);
                        text = "哎呀";
                        mSurface.MonthChineseText.getTextBounds(text, 0, text.length(), rect);
                        canvas.drawText(text, width + mSurface.ViewCellWidth / 2 - rect.width() / 2, mSurface.MonthGap + height + ChineseTextRatioInt - rect.height() / 2, useChinesePaint);
                        gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    gregorianCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                    for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
                        width = mSurface.MonthIconWidth + i * mSurface.ViewCellWidth + getMeasuredWidth() * 2 / 3;
                        if (gregorianCalendar.get(Calendar.DAY_OF_MONTH) == today.get(2)&&(today.get(1)==gregorianCalendar.get(Calendar.MONTH))) {
                            canvas.drawCircle(width + mSurface.ViewCellWidth / 2, mSurface.MonthGap + height + CircleY, mSurface.ViewCellWidth * 2 / 5, mSurface.MonthTodayCircle);
                            usePaint = mSurface.MonthTodayText;
                            useChinesePaint = mSurface.MonthTodayChineseText;
                        } else {
                            usePaint = mSurface.MonthText;
                            useChinesePaint = mSurface.MonthChineseText;
                        }
                        String text = gregorianCalendar.get(Calendar.DAY_OF_MONTH) + "";
                        mSurface.MonthText.getTextBounds(text, 0, text.length(), rect);
                        canvas.drawText(text, width + mSurface.ViewCellWidth / 2 - rect.width() / 2, mSurface.MonthGap + height + MonthTextRatioInt - rect.height() / 2, usePaint);
                        text = "哎呀";
                        mSurface.MonthChineseText.getTextBounds(text, 0, text.length(), rect);
                        canvas.drawText(text, width + mSurface.ViewCellWidth / 2 - rect.width() / 2, mSurface.MonthGap + height + ChineseTextRatioInt - rect.height() / 2, useChinesePaint);
                        gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }else {
                    int drawHeight =0;
                    if(CurrentWeekBegin.get(Calendar.MONTH)==mViewController.DrawCureent.get(Calendar.MONTH)&&offset==0) {
                        int firstDay = CalUtil.FindFirstDayofMonthInWeek(mViewController.DrawCureent) - 1;
                        int CurIndexBegin = (mViewController.DrawCureent.get(Calendar.DAY_OF_MONTH) + firstDay) - 1;
                        drawHeight = ((CurIndexBegin / 7) * mSurface.ViewCellHeight + mSurface.MonthGap);
                        canvas.drawRect(0, drawHeight, this.getMeasuredWidth(), drawHeight + mSurface.ViewCellHeight, mSurface.MonthCurrentWeek);
                    }else if(CurrentWeekBegin.get(Calendar.MONTH)!=mViewController.DrawCureent.get(Calendar.MONTH)&&offset==1){
                        drawHeight = mSurface.MonthGap;
                        canvas.drawRect(0, drawHeight, this.getMeasuredWidth(), drawHeight + mSurface.ViewCellHeight, mSurface.MonthCurrentWeek);
                    }

                }
            }
            height = 0;
            index = 0;
            width = mSurface.MonthIconWidth+getMeasuredWidth()/3;
            while (mMonthItem.get(index)==0) {
                index++;
                width += mSurface.ViewCellWidth;

            }
            for (int i = index; i <mMonthItem.size(); i++) {

                if(this.index == 0 && mMonthItem.get(i)==today.get(2)) {
                    canvas.drawCircle(width + mSurface.ViewCellWidth / 2, mSurface.MonthGap + height + CircleY, mSurface.ViewCellWidth * 2 / 5, mSurface.MonthTodayCircle);
                    usePaint = mSurface.MonthTodayText;
                    useChinesePaint = mSurface.MonthTodayChineseText;
                }else {
                    usePaint = mSurface.MonthText;
                    useChinesePaint = mSurface.MonthChineseText;
                }
                String text = mMonthItem.get(i)+"";
                mSurface.MonthText.getTextBounds(text, 0, text.length(), rect);
                canvas.drawText(text, width + mSurface.ViewCellWidth / 2 - rect.width() / 2, mSurface.MonthGap + height + MonthTextRatioInt - rect.height() / 2, usePaint);
                text = "哎呀";
                mSurface.MonthChineseText.getTextBounds(text, 0, text.length(), rect);
                canvas.drawText(text, width + mSurface.ViewCellWidth / 2 - rect.width() / 2, mSurface.MonthGap + height + ChineseTextRatioInt - rect.height() / 2,useChinesePaint);
/*
                if(mEventManager.drawTags.get(i-index).event.size()!=0){
                    if(mEventManager.drawTags.get(i-index).event.size()==1){

                    }
                }
*/
                if(index%7 == 6){
                    height += mSurface.ViewCellHeight;
                    width =mSurface.MonthIconWidth+getMeasuredWidth()/3;
                }else{
                    width += mSurface.ViewCellWidth;
                }
                index ++;
            }

        }



        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                if(OPERATION == MONTH){
                    mViewController.SelectedTime = null;
                    mViewController.SelectedMonthView = null;
                }
                int index = 40;
                int day = 0;
                int PositionY = (int) event.getY() - mSurface.MonthGap;
                DrawCircleX = event.getX();
                DrawCircleY = event.getY();
                int PositionX = (int) event.getX() - mSurface.MonthIconWidth - this.getMeasuredWidth() / 3;
                if (PositionY > 0&&PositionX>0) {
                    index = (PositionY / mSurface.ViewCellHeight) * 7 + PositionX / mSurface.ViewCellWidth;
                }
                if (index < mMonthItem.size()) {
                    day = mMonthItem.get(index);

                }
                if (day > 0) {
                    mViewController.SelectedTime = new GregorianCalendar(month.get(Calendar.YEAR), month.get(Calendar.MONTH), day, 0, 0);
                    mViewController.SelectedMonthView = this;
                }

            }
            return false;
        }

        public int addHolder(){
            CircleAnimaitonHolder circleAnimaitonHolder = new CircleAnimaitonHolder(DrawCircleX,DrawCircleY);
            circleAnimaitionHolders.add(circleAnimaitonHolder);
            return circleAnimaitionHolders.size()-1;
        }

        public class CircleAnimaitonHolder{

            CircleAnimaitonHolder(float x,float y){
                this.drawX =x;
                this.drawY =y;
            }
            Paint paint = new Paint();
            float frame = 0;
            float drawX;
            float drawY;
            float Radio = 0;

            void initHolder(int Color){
                initHolder(Color,null,0);
            }

            void initHolder(int Color,Paint.Style style,int WidthSize){
                if(style !=null){
                    paint.setStyle(style);
                    paint.setStrokeWidth(WidthSize);
                }
                paint.setAntiAlias(true);
                paint.setColor(Color);
            }

            void FrameChange(float Nextframe){
                frame = Nextframe;
                Radio = (ViewWidth*7/4)*(Nextframe/24);
            }
        }

    }

    private class MonthIco extends View{

        GregorianCalendar month;
        int Month;
        int Year;
        int index;
        public MonthIco(Context context) {
            this(context,null);
        }

        public MonthIco(Context context, AttributeSet attrs) {
            this(context, attrs,0);
        }

        public MonthIco(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void initView(int index){
            month = new GregorianCalendar(today.get(0),today.get(1),1,0,0);
            month.add(Calendar.MONTH,index);
            this.Month=month.get(Calendar.MONTH)+1;
            this.Year =month.get(Calendar.YEAR);
            this.index=index;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, mSurface.MonthIconMonthTextSize + mSurface.MonthIconYearTextSize + mSurface.MonthIconPadding);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            String monthText;
            if(Month<10) monthText = "0"+Month;
            else monthText = Month +"";
            String yearText = Year+"";
            Rect rect = new Rect();
            mSurface.MonthIconMonthTextPaint.getTextBounds(monthText, 0, monthText.length(), rect);
            canvas.drawText(monthText, getMeasuredWidth() / 2 - rect.width() / 2, getMeasuredHeight()/2, mSurface.MonthIconMonthTextPaint);
            mSurface.MonthIconYearTextPaint.getTextBounds(yearText, 0, yearText.length(), rect);
            canvas.drawText(yearText, getMeasuredWidth() / 2 - rect.width() / 2, getMeasuredHeight() * 5 / 6, mSurface.MonthIconYearTextPaint);
        }
    }


    private class ShadowView extends View{
        public ShadowView(Context context) {
            this(context, null);
        }

        public ShadowView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRect(0,0,getMeasuredWidth(),mSurface.TextHeight,mSurface.whitePaint);
            if(OPERATION == MONTH){
                canvas.drawRect(0,mSurface.TextHeight,getMeasuredWidth(),mSurface.ViewMeasureHeight+mSurface.TextHeight,mSurface.MonthShadowPaint);
            }
        }
    }
}

