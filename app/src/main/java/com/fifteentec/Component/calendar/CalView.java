package com.fifteentec.Component.calendar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Rect;

import android.util.AttributeSet;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import android.view.View;

import android.view.ViewGroup;

import com.fifteentec.Fragment.CalViewFragment;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalView extends View implements GestureDetector.OnGestureListener{


    private int MinDayOfFirstWeek;
    private ArrayList<Integer> mCurDateList;
    private GregorianCalendar mTodayDate;
    private mSurface mSurface;
    private final int YEAR_RANGE = 31;

    private final int COLUM_NUM =CalUtil.LENTH_OF_WEEK;
    private final int MONTH_OF_YEAR=12;
    private final int ROW_NUM = 7; //  周视图显示的行数
    private final int DAY_OF_MONTH = COLUM_NUM*ROW_NUM;
    private GestureDetector mGestureDetector;
    private boolean mMode =true; //true为月模式,flase为周模式
    private CalViewListener mCalViewListener;
    private boolean firstEnter = true;
    private final int DAY=0x00;
    private final int WEEK = 0x01;
    private final int MONTH = 0x02;
    private final int YEAR = 0x03;

    public interface CalViewListener
    {
        public void DateChange(GregorianCalendar arry);

        void ShowDayDetail(GregorianCalendar date);
    }

    public void setCalViewListner (CalViewListener a)
    {
        mCalViewListener = a;
    }

    public CalView(Context context) {
        this(context, null);
    }

    public CalView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public void SwitchMode()
    {
        ViewGroup.LayoutParams Params = getLayoutParams();
        if(mMode){
            GregorianCalendar mCurDate= new GregorianCalendar(mTodayDate.get(Calendar.YEAR)+(mCurDateList.get(0)-(YEAR_RANGE-1)/2), mCurDateList.get(1), 1);
            int Fisrtday = mCurDate.get(Calendar.DAY_OF_WEEK) - 1;
            int day = mCurDateList.get(3) + mCurDateList.get(2) *COLUM_NUM-Fisrtday;
            int pos = findWeekPosition(mCurDateList.get(0),mCurDateList.get(1),day);
            Params.height = mSurface.mCellHeight;
            setLayoutParams(Params);
            scrollTo(pos,0);
            mMode = false;
        }else
        {
            Params.height =  mSurface.mViewHeight;
            setLayoutParams(Params);
            float CurY = findPositionByIndex(mCurDateList.get(0),mCurDateList.get(1),mCurDateList.get(2),mCurDateList.get(3));
            scrollTo(0, (int) (CurY - mSurface.mViewHeight / 2 + mSurface.mCellHeight / 2+mSurface.mScaleHeight*(mCurDateList.get(3)-(COLUM_NUM-1)/2)));
            mMode = true;
        }

    }

    private int findWeekPosition(int year,int month,int day){
        int horizen=0;
        for(int a = 0; a < year;a++){
            int yearForReal = mTodayDate.get(Calendar.YEAR)+(a-(YEAR_RANGE-1)/2);
            if(CalUtil.isLeapYear(yearForReal))
            {
                horizen += 366;
            }else{
                horizen += 365;
            }
        }
        int yearForReal = mTodayDate.get(Calendar.YEAR)+(year-(YEAR_RANGE-1)/2);
        boolean isLeap = CalUtil.isLeapYear(yearForReal);
        for(int b =0; b < month ;b++){
            horizen += CalUtil.LENTH_OF_MONTH.get(b);
            if(isLeap &&(b == 1)) horizen +=1;
        }
        for (int temp =0;temp < day ;temp ++) {
            horizen +=1;
        }
        horizen += MinDayOfFirstWeek;
        return (horizen/COLUM_NUM)*mSurface.mViewWidth;
    }

    public CalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(GregorianCalendar date) {

        mMode = true;
        mTodayDate =CalUtil.CopyDate(date);
        GregorianCalendar tempdate = new GregorianCalendar(date.get(Calendar.YEAR)-(YEAR_RANGE-1)/2,0,1);
        MinDayOfFirstWeek = tempdate.get(Calendar.DAY_OF_WEEK)-1;
        mSurface = new mSurface();
        mGestureDetector = new GestureDetector(getContext(),this);
        mSurface.mdensity = getResources().getDisplayMetrics().density;
        setBackgroundColor(mSurface.mBgColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {



        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(firstEnter) {
            mSurface.mViewWidth = getMeasuredWidth();
            mSurface.mViewHeight = (int)(getMeasuredWidth()*0.8f);
            mSurface.init();
            ViewGroup.LayoutParams Params = getLayoutParams();
            Params.height = mSurface.mViewHeight;
            Params.width = mSurface.mViewWidth;
            setLayoutParams(Params);
            firstEnter = false;
            int fisrtDay = CalUtil.FindFirstDayofMonthInWeek(mTodayDate)-1;
            int week =( mTodayDate.get(Calendar.DAY_OF_MONTH)+fisrtDay-1 )/COLUM_NUM;
            int day = ( mTodayDate.get(Calendar.DAY_OF_MONTH)+fisrtDay-1) %COLUM_NUM;
            mCurDateList = new ArrayList<Integer>();
            mCurDateList.add((YEAR_RANGE-1)/2);
            mCurDateList.add(mTodayDate.get(Calendar.MONTH));
            mCurDateList.add(week);
            mCurDateList.add(day);
            ScrollToSelect(mCurDateList);
            UpdateCurTime();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        List<Integer> mDate =new ArrayList<Integer>();
        Rect mBound = new Rect();
        float drawx =0,drawy =0;
        if(mMode) {
            float SelectedX = findAutoSelectPosition().get(0);
            float SelectedY = findAutoSelectPosition().get(1);
            ArrayList<Integer> mSelectedDate = findIndexByPosition(SelectedX, SelectedY);
            for (int year = 0; year < YEAR_RANGE; year++) {
                if(Math.abs(year - mCurDateList.get(0))<2) {
                    for (int month = 0; month < MONTH_OF_YEAR; month++) {

                        if(Math.abs((month + (year - mCurDateList.get(0)) * MONTH_OF_YEAR)-mCurDateList.get(1))<3) {

                            mDate = CalculateDate(year, month);
                            for (int week = 0; week < ROW_NUM; week++) {
                                if((year == mSelectedDate.get(0)) && (month == mSelectedDate.get(1)) && (week == mSelectedDate.get(2))){
                                    canvas.drawRect(0,SelectedY, mSurface.mViewWidth, SelectedY+ mSurface.mCellHeight, mSurface.mRecPaint);
                                    canvas.drawCircle(SelectedX + mSurface.mCellWidth / 2,SelectedY + mSurface.mCellHeight / 2f, mSurface.mCellWidth / 2.5f, mSurface.mCirclePaint);
                                }
                                for (int day = 0; day < COLUM_NUM; day++) {
                                    int index = week * COLUM_NUM + day;

                                    if (mDate.get(index) == 0) {
                                        drawx += mSurface.mCellWidth;
                                    } else {

                                        if ((year == (YEAR_RANGE - 1) / 2) && (month == mTodayDate.get(Calendar.MONTH)) && (index - CalUtil.FindFirstDayofMonthInWeek(mTodayDate) + 1 == mTodayDate.get(Calendar.DAY_OF_MONTH) - 1)) {

                                            mSurface.mTodayPaint.getTextBounds(String.valueOf(mDate.get(index)), 0, String.valueOf(mDate.get(index)).length(), mBound);

                                            canvas.drawText(mDate.get(index) + "",
                                                    drawx + mSurface.mCellWidth / 2 - mBound.width() / 2,
                                                    drawy + mSurface.mCellHeight / 2 + mBound.height() / 2,
                                                    mSurface.mTodayPaint);
                                            drawx += mSurface.mCellWidth;
                                        } else {
                                            if ((year == mSelectedDate.get(0)) && (month == mSelectedDate.get(1)) && (week == mSelectedDate.get(2)) && (day == mSelectedDate.get(3))) {

                                                mSurface.mDatePaint.getTextBounds(String.valueOf(mDate.get(index)), 0, String.valueOf(mDate.get(index)).length(), mBound);
                                                canvas.drawText(mDate.get(index) + "",
                                                        drawx + mSurface.mCellWidth / 2 - mBound.width() / 2,
                                                        drawy + mSurface.mCellHeight / 2 + mBound.height() / 2,
                                                        mSurface.mSelectedPaint);

                                            } else {
                                                mSurface.mDatePaint.getTextBounds(String.valueOf(mDate.get(index)), 0, String.valueOf(mDate.get(index)).length(), mBound);
                                                canvas.drawText(mDate.get(index) + "",
                                                        drawx + mSurface.mCellWidth / 2 - mBound.width() / 2,
                                                        drawy + mSurface.mCellHeight / 2 + mBound.height() / 2,
                                                        mSurface.mDatePaint);
                                            }

                                            drawx += mSurface.mCellWidth;
                                        }
                                    }

                                }
                                drawx = 0;
                                drawy += mSurface.mCellHeight;
                            }
                        }else{
                            drawy +=mSurface.mCellHeight*ROW_NUM;
                        }
                    }
                }else {
                    drawy += mSurface.mCellHeight*ROW_NUM*MONTH_OF_YEAR;
                }
            }
        }else{
            int WidthCount = MinDayOfFirstWeek;
            GregorianCalendar temp = new GregorianCalendar(mTodayDate.get(Calendar.YEAR)  - (YEAR_RANGE - 1) / 2, 1, 1);
            for(int year = 0;year < YEAR_RANGE;year++){

                if(Math.abs(year-mCurDateList.get(0))<2) {

                    for (int month = 0; month < MONTH_OF_YEAR; month++) {
                        if(Math.abs((month + (year - mCurDateList.get(0)) * MONTH_OF_YEAR - mCurDateList.get(1)))<2) {

                            mDate = CalculateDate(year, month);

                            for (int day = 0; day < mDate.size(); day++) {

                                if (mDate.get(day) != 0) {
                                    mSurface.mDatePaint.getTextBounds(String.valueOf(mDate.get(day)), 0, String.valueOf(mDate.get(day)).length(), mBound);
                                    if (mCurDateList.get(0) == year && mCurDateList.get(1) == month && mCurDateList.get(2) * COLUM_NUM + mCurDateList.get(3) == day) {
                                        canvas.drawCircle(((WidthCount/COLUM_NUM) * mSurface.mViewWidth) + (WidthCount % COLUM_NUM) * mSurface.mCellWidth + mSurface.mCellWidth / 2, mSurface.mCellHeight / 2, mSurface.mCellWidth / 2.5f, mSurface.mCirclePaint);
                                        canvas.drawText(mDate.get(day) + "",
                                                ((WidthCount/COLUM_NUM) * mSurface.mViewWidth) + (WidthCount % COLUM_NUM) * mSurface.mCellWidth + mSurface.mCellWidth / 2 - mBound.width() / 2,
                                                mSurface.mCellHeight / 2 + mBound.height() / 2,
                                                mSurface.mSelectedPaint);
                                    } else {

                                        canvas.drawText(mDate.get(day) + "",
                                                ((WidthCount/COLUM_NUM)* mSurface.mViewWidth) + (WidthCount % COLUM_NUM) * mSurface.mCellWidth + mSurface.mCellWidth / 2 - mBound.width() / 2,
                                                mSurface.mCellHeight / 2 + mBound.height() / 2,
                                                mSurface.mDatePaint);
                                    }
                                    WidthCount++;
                                }
                            }
                        }else{
                            WidthCount += CalUtil.LENTH_OF_MONTH.get(month);
                            if(month==1 && temp.isLeapYear(temp.get(Calendar.YEAR)))
                            {
                                WidthCount ++;
                            }

                        }
                    }
                }else{
                    if(temp.isLeapYear(temp.get(Calendar.YEAR))){
                        WidthCount+=366;

                    }else
                    {
                        WidthCount+=365;
                    }

                }
                temp.add(Calendar.YEAR,1);
            }

        }
        super.onDraw(canvas);

    }

    private void ScrollToSelectWeek(ArrayList<Integer> date){
        int DayCount = MinDayOfFirstWeek;
        for(int i =0;i<date.get(0);i++){
            GregorianCalendar temp = new GregorianCalendar(mTodayDate.get(Calendar.YEAR)-(YEAR_RANGE-1)/2+i,1,1);
            if(temp.isLeapYear(temp.get(Calendar.YEAR))){
                DayCount +=366;
            }else {
                DayCount +=365;
            }
        }
        GregorianCalendar temp = new GregorianCalendar(mTodayDate.get(Calendar.YEAR)-(YEAR_RANGE-1)/2+date.get(0),1,1);
        for(int i =0;i<date.get(1);i++){
            DayCount += CalUtil.LENTH_OF_MONTH.get(i);
            if(i==1&&temp.isLeapYear(temp.get(Calendar.YEAR))){
                DayCount+=1;
            }
        }
        for(int i=0;i<date.get(2);i++) DayCount +=COLUM_NUM;
        scrollTo((DayCount/7)*mSurface.mViewWidth,0);
    }

    private ArrayList<Integer> findIndexByPosition(float x,float y)
    {
        int heightIndex = (int) (y/mSurface.mCellHeight );
        int widthIndex = (int) (x/mSurface.mCellWidth );
        int week = heightIndex%ROW_NUM;
        int month = ((heightIndex -week)/ROW_NUM)%MONTH_OF_YEAR;
        int year = ((heightIndex -week)/ROW_NUM)/MONTH_OF_YEAR;
        GregorianCalendar tempDate = new GregorianCalendar(mTodayDate.get(Calendar.YEAR)+year-(YEAR_RANGE-1)/2,month,1);
        int firstday =  CalUtil.FindFirstDayofMonthInWeek(tempDate)-1;
        int length0fmonth = CalUtil.getMaxDayOfMonth(tempDate)-1;
        int index = week*COLUM_NUM +widthIndex;
        if(index < firstday)
        {
            widthIndex = firstday;
        }
        if(index > length0fmonth+firstday)
        {
            week =( length0fmonth +firstday)/COLUM_NUM;
            widthIndex = ( length0fmonth +firstday)%COLUM_NUM;
        }
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(year);
        temp.add(month);
        temp.add(week);
        temp.add(widthIndex);
        return temp;
    }

    private ArrayList<Float> findAutoSelectPosition()
    {

        float CurY = getScrollY() + mSurface.mViewHeight / 2f;
        float ex_rest = CurY % mSurface.mCellHeight;
        float scale_height = mSurface.mScaleHeight;
        int x = (int) (ex_rest  / scale_height);
        float drawy = CurY - ex_rest ;
        float drawx = x * mSurface.mCellWidth;
        ArrayList<Integer> tempDate = findIndexByPosition(drawx,drawy);
        drawy =(( tempDate.get(0)*MONTH_OF_YEAR + tempDate.get(1))*ROW_NUM+tempDate.get(2))*mSurface.mCellHeight;
        drawx = tempDate.get(3)*mSurface.mCellWidth;
        ArrayList<Float> temp = new ArrayList<Float>();
        temp.add(drawx);
        temp.add(drawy);
        return temp;
    }


    private List<Integer> CalculateDate(int yearoffset,int month) {

        List<Integer> mDate =new ArrayList<Integer>();
        int year =mTodayDate.get(Calendar.YEAR)+(yearoffset- (YEAR_RANGE-1)/2);
        GregorianCalendar temp =new GregorianCalendar(year,month,1);
        mDate = CalUtil.GetDayInMonth(temp);
        for(int i = 0;i<DAY_OF_MONTH-(CalUtil.getMaxDayOfMonth(temp)+(CalUtil.FindFirstDayofMonthInWeek(temp)-1));i++){
            mDate.add(0);
        }

        return mDate;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (mMotion.ACTION_STATE) {

            case mMotion.SCROLL:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(mMode){

                    }else {
                        float ex_posX = mMotion.mDownPosX % mSurface.mViewWidth;
                        if ((Math.abs(getScrollX() - mMotion.mDownPosX) > mSurface.mViewWidth * mMotion.mScreenDistanceRatio)) {
                            if (getScrollX() > mMotion.mDownPosX) {
                                DateChange(WEEK, 1);
                                scrollTo((int) (mMotion.mDownPosX - ex_posX + mSurface.mViewWidth), 0);
                            } else {
                                DateChange(WEEK, -1);
                                scrollTo((int) (mMotion.mDownPosX - ex_posX - mSurface.mViewWidth), 0);
                            }
                        }else{
                            scrollTo((int)(mMotion.mDownPosX-ex_posX),0);
                        }
                        UpdateCurTime();
                    }
                }
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mMotion.mDownPosX = getScrollX();
        mMotion.mDownPosY = getScrollY();

        return true;
    }

    /**
     *
     * @param year
     * @param month
     * @param week
     * @param day
     * @return the position of the item in given index
     */
    private float findPositionByIndex (int year,int month,int week,int day)
    {

        float y =(((year*MONTH_OF_YEAR+month)*ROW_NUM+week)*mSurface.mCellHeight);
        return y;
    }

    public void ScrollToSelect(ArrayList<Integer> List) {

            float y = findPositionByIndex(List.get(0), List.get(1), List.get(2), List.get(3));
            int targetY = (int) (y - mSurface.mViewHeight / 2 + (List.get(3)) * mSurface.mScaleHeight);

            scrollTo(0, targetY);
    }

    public void UpdateTime(GregorianCalendar temp){
        mCurDateList.clear();
        int fisrtDay = CalUtil.FindFirstDayofMonthInWeek(temp)-1;
        mCurDateList.add(-mTodayDate.get(Calendar.YEAR)+temp.get(Calendar.YEAR)+(YEAR_RANGE-1)/2);
        mCurDateList.add(temp.get(Calendar.MONTH));
        mCurDateList.add((temp.get(Calendar.DAY_OF_MONTH) + fisrtDay - 1) / COLUM_NUM);
        mCurDateList.add((temp.get(Calendar.DAY_OF_MONTH) + fisrtDay - 1) % COLUM_NUM);
        invalidate();
        if(mMode){
            ScrollToSelect(mCurDateList);
        }else {
            ScrollToSelectWeek(mCurDateList);
        }
    }


    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        float posX = e.getX();
        float posY = e.getY();
        if(mMode) {

            float CurY = getScrollY() + posY;
            mCurDateList = findIndexByPosition(posX, CurY);
            ScrollToSelect(mCurDateList);

        }else{
            int num = (int) posX/mSurface.mCellWidth;
            int nowDate = mCurDateList.get(3);
            DateChange(DAY,num-nowDate);
            GregorianCalendar date = new GregorianCalendar((mTodayDate.get(Calendar.YEAR)+mCurDateList.get(0)-(YEAR_RANGE-1)/2),mCurDateList.get(1),1);
            int firstday = CalUtil.FindFirstDayofMonthInWeek(date)-1;
            int day = mCurDateList.get(2)*COLUM_NUM+mCurDateList.get(3)-firstday;
            date.add(Calendar.DAY_OF_MONTH, day);
            mCalViewListener.ShowDayDetail(date);
            invalidate();
        }
        UpdateCurTime();
        return  true;

    }

    private void UpdateCurTime (){
        GregorianCalendar date = new GregorianCalendar((mTodayDate.get(Calendar.YEAR)+mCurDateList.get(0)-(YEAR_RANGE-1)/2),mCurDateList.get(1),1);
        int firstday = CalUtil.FindFirstDayofMonthInWeek(date)-1;
        int day = mCurDateList.get(2)*COLUM_NUM+mCurDateList.get(3)-firstday;
        date.add(Calendar.DAY_OF_MONTH, day);
        mCalViewListener.DateChange(date);
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mMotion.ACTION_STATE = mMotion.SCROLL;
        if(mMode) {
            if ((distanceY < 0 && getScrollY() > 0) || (distanceY > 0 && getScrollY() < mSurface.mMaxHeight)) {
                scrollBy(0, (int) distanceY);
            }
            mCurDateList =findIndexByPosition(findAutoSelectPosition().get(0),findAutoSelectPosition().get(1));
            UpdateCurTime();

        }else{
            if((distanceX <0 && getScaleX()>0)||(distanceX >0 && getScaleX() < mSurface.mMaxWidth)){
                scrollBy((int)distanceX,0);
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {}


    private void DateChange(int timefield,int value){
        GregorianCalendar temp = new GregorianCalendar(mCurDateList.get(0)-(YEAR_RANGE-1)/2+mTodayDate.get(Calendar.YEAR),mCurDateList.get(1),1);
        int first = CalUtil.FindFirstDayofMonthInWeek(temp) -1;
        int day = mCurDateList.get(2)*COLUM_NUM+mCurDateList.get(3)-first;
        temp.add(Calendar.DAY_OF_MONTH, day);
        switch (timefield){

            case DAY:
                temp.add(Calendar.DAY_OF_MONTH,value);
                break;
            case WEEK:
                temp.add(Calendar.WEEK_OF_YEAR,value);
                break;
            case MONTH:
                temp.add(Calendar.MONTH,value);
                break;
            case YEAR:
                temp.add(Calendar.YEAR,value);
                break;
            default:
                break;
        }
        mCurDateList.clear();
        mCurDateList.add(temp.get(Calendar.YEAR)-mTodayDate.get(Calendar.YEAR)+ (YEAR_RANGE - 1) / 2);
        mCurDateList.add(temp.get(Calendar.MONTH));
        first = CalUtil.FindFirstDayofMonthInWeek(temp)-1;
        int week =( temp.get(Calendar.DAY_OF_MONTH)-1+first)/COLUM_NUM;
        mCurDateList.add(week);
        day = (temp.get(Calendar.DAY_OF_MONTH)-1+first)%COLUM_NUM;
        mCurDateList.add(day);

    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return false;
    }

    private static class mMotion{
        public static int mMinFlingDistance= 100;
        public static int mMinFlingVelocity=1500;
        public static float mDownPosX;
        public static float mDownPosY;
        public static final int SCROLL=0x01;
        public static final int WAIT=0x00;
        public static int ACTION_STATE = WAIT;
        public static float mScreenDistanceRatio = 0.3f;
    }
    private class mSurface {
        public float mdensity;
        public int mViewHeight;
        public int mViewWidth;
        public int mCellWidth;
        public int mCellHeight;
        public float mMonthHeight;
        public float mMonthWidth;
        public int mBgColor=Color.parseColor("#FFFFFF");
        public int mCirColor = Color.parseColor("#888888");
        public int mRecColor = Color.parseColor("#EEEEEE");
        public int mTextColor = Color.BLACK;
        public int mCellDownColoe=Color.parseColor("#CCFFFF");
        public int mCellSelectedColoe=Color.parseColor("#FFFFFF");
        public int mMonthColor=Color.BLUE;
        public int mTodayColor = Color.RED;
        public Paint mDatePaint;
        public Paint mMonthPaint;
        public Paint mSelectedPaint;
        public Paint mCirclePaint;
        public Paint mRecPaint;
        public Paint mTodayPaint;
        public int mMaxHeight;
        public int mMaxWidth;
        float mScaleHeight;

        public void init() {

            mCellHeight=mCellWidth =(int) (mViewWidth/7f);
            mMaxHeight = (YEAR_RANGE*MONTH_OF_YEAR-1)*mCellHeight*ROW_NUM;
            mMaxWidth = (YEAR_RANGE*365)*mCellWidth;

            mMonthHeight = 2*mCellHeight;
            mScaleHeight = mCellHeight/COLUM_NUM;
            mMonthWidth = mViewWidth;
            mDatePaint = new Paint();
            mDatePaint.setColor(mTextColor);
            mDatePaint.setAntiAlias(true);
            mDatePaint.setTextSize(mCellHeight * 0.3f);
            mSelectedPaint = new Paint();
            mSelectedPaint.setColor(mCellSelectedColoe);
            mSelectedPaint.setAntiAlias(true);
            mSelectedPaint.setTextSize(mCellHeight * 0.3f);
            mTodayPaint = new Paint();
            mTodayPaint.setColor(mTodayColor);
            mTodayPaint.setAntiAlias(true);
            mTodayPaint.setTextSize(mCellHeight * 0.5f);
            mMonthPaint = new Paint();
            mMonthPaint.setColor(mMonthColor);
            mMonthPaint.setAntiAlias(true);
            mCirclePaint = new Paint();
            mCirclePaint.setColor(mCirColor);
            mCirclePaint.setAntiAlias(true);
            mRecPaint = new Paint();
            mRecPaint.setColor(mRecColor);
            mRecPaint.setAntiAlias(true);

        }


    }

}

