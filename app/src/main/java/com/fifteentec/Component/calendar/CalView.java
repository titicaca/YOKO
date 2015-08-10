package com.fifteentec.Component.calendar;

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

import java.util.ArrayList;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalView extends View implements GestureDetector.OnGestureListener {


    private int MinDayOfFirstWeek;
    private ArrayList<Integer> mCurDateList;
    private GregorianCalendar mTodayDate;
    private mSurface mSurface;
    private final int YEAR_RANGE = 1;

    private final int COLUM_NUM = CalUtil.LENTH_OF_WEEK;
    private final int MONTH_OF_YEAR = 12;
    private final int ROW_NUM = 6;
    private final int DAY_OF_MONTH = COLUM_NUM * ROW_NUM;
    private GestureDetector mGestureDetector;
    private boolean mMode = true;
    private CalViewListener mCalViewListener;
    private boolean firstEnter = true;
    private final int DAY = 0x00;
    private final int WEEK = 0x01;
    private final int MONTH = 0x02;
    private final int YEAR = 0x03;

    public interface CalViewListener {
        public void MonthChange(int month);

        public void YearChange(int year);

        public void DayChange(int year);
    }

    public void setCalViewListner(CalViewListener a) {
        mCalViewListener = a;
    }

    public CalView(Context context) {
        this(context, null);
    }

    public CalView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public void SwitchMode() {
        ViewGroup.LayoutParams Params = getLayoutParams();
        if (mMode) {
            GregorianCalendar mCurDate = new GregorianCalendar(mTodayDate.get(Calendar.YEAR) + (mCurDateList.get(0) - (YEAR_RANGE - 1) / 2), mCurDateList.get(1), 1);
            int Fisrtday = mCurDate.get(Calendar.DAY_OF_WEEK) - 1;
            int day = mCurDateList.get(3) + mCurDateList.get(2) * COLUM_NUM - Fisrtday;
            int pos = findWeekPosition(mCurDateList.get(0), mCurDateList.get(1), day);
            Params.height = mSurface.mCellHeight;
            setLayoutParams(Params);
            scrollTo(pos, 0);
            mMode = false;
        } else {
            Params.height = mSurface.mViewHeight;
            setLayoutParams(Params);
            float CurY = findPositionByIndex(mCurDateList.get(0), mCurDateList.get(1), mCurDateList.get(2), mCurDateList.get(3));
            scrollTo(0, (int) (CurY - mSurface.mViewHeight / 2 + mSurface.mCellHeight / 2 + mSurface.mScaleHeight * (mCurDateList.get(3) - (COLUM_NUM - 1) / 2)));
            mMode = true;
        }

    }

    private int findWeekPosition(int year, int month, int day) {
        int horizen = 0;
        for (int a = 0; a < year; a++) {
            int yearForReal = mTodayDate.get(Calendar.YEAR) + (a - (YEAR_RANGE - 1) / 2);
            if (CalUtil.isLeapYear(yearForReal)) {
                horizen += 366;
            } else {
                horizen += 365;
            }
        }
        int yearForReal = mTodayDate.get(Calendar.YEAR) + (year - (YEAR_RANGE - 1) / 2);
        boolean isLeap = CalUtil.isLeapYear(yearForReal);
        for (int b = 0; b < month; b++) {
            horizen += CalUtil.LENTH_OF_MONTH.get(b);
            if (isLeap && (b == 1)) horizen += 1;
        }
        for (int temp = 0; temp < day; temp++) {
            horizen += 1;
        }
        horizen += MinDayOfFirstWeek;
        return (horizen / COLUM_NUM) * mSurface.mViewWidth;
    }

    public CalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(GregorianCalendar date) {

        mMode = true;
        mTodayDate = CalUtil.CopyDate(date);
        GregorianCalendar tempdate = new GregorianCalendar(date.get(Calendar.YEAR) - (YEAR_RANGE - 1) / 2, 0, 1);
        MinDayOfFirstWeek = tempdate.get(Calendar.DAY_OF_WEEK) - 1;
        mSurface = new mSurface();
        mGestureDetector = new GestureDetector(getContext(), this);
        mSurface.mdensity = getResources().getDisplayMetrics().density;
        setBackgroundColor(mSurface.mBgColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mSurface.mViewHeight = mSurface.mViewWidth = getResources().getDisplayMetrics().widthPixels;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (firstEnter) {
            mSurface.init();
            ViewGroup.LayoutParams Params = getLayoutParams();
            Params.height = mSurface.mViewHeight;
            Params.width = mSurface.mViewWidth;
            setLayoutParams(Params);
            firstEnter = false;
            int fisrtDay = CalUtil.FindFirstDayofMonthInWeek(mTodayDate) - 1;
            int week = (mTodayDate.get(Calendar.DAY_OF_MONTH) + fisrtDay - 1) / COLUM_NUM;
            int day = (mTodayDate.get(Calendar.DAY_OF_MONTH) + fisrtDay - 1) % COLUM_NUM;
            mCurDateList = new ArrayList<Integer>();
            mCurDateList.add((YEAR_RANGE - 1) / 2);
            mCurDateList.add(mTodayDate.get(Calendar.MONTH));
            mCurDateList.add(week);
            mCurDateList.add(day);
            ScrollToSelect(mCurDateList);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        List<Integer> mDate = new ArrayList<Integer>();
        Rect mBound = new Rect();
        float drawx = 0, drawy = 0;
        if (mMode) {
            drawAutoCircle(canvas);
            ArrayList<Integer> mSelectedDate = findIndexByPosition(findAutoSelectPosition().get(0), findAutoSelectPosition().get(1));
            for (int year = 0; year < YEAR_RANGE; year++) {
                for (int month = 0; month < MONTH_OF_YEAR; month++) {
                    mDate = CalculateDate(year, month);
                    for (int week = 0; week < ROW_NUM; week++) {
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
                }
            }
        } else {
            int WidthCount = MinDayOfFirstWeek;
            int weekCount = 0;
            for (int year = 0; year < YEAR_RANGE; year++) {
                for (int month = 0; month < MONTH_OF_YEAR; month++) {
                    mDate = CalculateDate(year, month);
                    GregorianCalendar temp = new GregorianCalendar(mTodayDate.get(Calendar.YEAR) + (year - (YEAR_RANGE - 1) / 2), month, 1);
                    int FirstDay = CalUtil.FindFirstDayofMonthInWeek(temp);
                    for (int day = 0; day < mDate.size(); day++) {

                        if (mDate.get(day) != 0) {
                            mSurface.mDatePaint.getTextBounds(String.valueOf(mDate.get(day)), 0, String.valueOf(mDate.get(day)).length(), mBound);
                            if (mCurDateList.get(0) == year && mCurDateList.get(1) == month && mCurDateList.get(2) * COLUM_NUM + mCurDateList.get(3) == day) {
                                canvas.drawCircle((weekCount * mSurface.mViewWidth) + (WidthCount % COLUM_NUM) * mSurface.mCellWidth + mSurface.mCellWidth / 2, mSurface.mCellHeight / 2, mSurface.mCellWidth / 2.5f, mSurface.mCirclePaint);
                                canvas.drawText(mDate.get(day) + "",
                                        (weekCount * mSurface.mViewWidth) + (WidthCount % COLUM_NUM) * mSurface.mCellWidth + mSurface.mCellWidth / 2 - mBound.width() / 2,
                                        mSurface.mCellHeight / 2 + mBound.height() / 2,
                                        mSurface.mSelectedPaint);
                            } else {

                                canvas.drawText(mDate.get(day) + "",
                                        (weekCount * mSurface.mViewWidth) + (WidthCount % COLUM_NUM) * mSurface.mCellWidth + mSurface.mCellWidth / 2 - mBound.width() / 2,
                                        mSurface.mCellHeight / 2 + mBound.height() / 2,
                                        mSurface.mDatePaint);
                            }
                            WidthCount++;
                            if (WidthCount % COLUM_NUM == 0) {
                                weekCount += 1;
                            }
                        }
                    }
                }
            }

        }
        super.onDraw(canvas);

    }

    private ArrayList<Integer> findIndexByPosition(float x, float y) {
        int heightIndex = (int) (y / mSurface.mCellHeight);
        int widthIndex = (int) (x / mSurface.mCellWidth);
        int week = heightIndex % ROW_NUM;
        int month = ((heightIndex - week) / ROW_NUM) % MONTH_OF_YEAR;
        int year = ((heightIndex - week) / ROW_NUM) / MONTH_OF_YEAR;
        GregorianCalendar tempDate = new GregorianCalendar(mTodayDate.get(Calendar.YEAR) + year - (YEAR_RANGE - 1) / 2, month, 1);
        int firstday = CalUtil.FindFirstDayofMonthInWeek(tempDate) - 1;
        int length0fmonth = CalUtil.getMaxDayOfMonth(tempDate) - 1;
        int index = week * COLUM_NUM + widthIndex;
        if (index < firstday) {
            widthIndex = firstday;
        }
        if (index > length0fmonth + firstday) {
            week = (length0fmonth + firstday) / COLUM_NUM;
            widthIndex = (length0fmonth + firstday) % COLUM_NUM;
        }
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(year);
        temp.add(month);
        temp.add(week);
        temp.add(widthIndex);
        return temp;
    }

    private ArrayList<Float> findAutoSelectPosition() {

        float CurY = getScrollY() + mSurface.mViewHeight / 2f;
        float ex_rest = CurY % mSurface.mCellHeight;
        float scale_height = mSurface.mScaleHeight;
        int x = (int) (ex_rest / scale_height);
        float drawy = CurY - ex_rest;
        float drawx = x * mSurface.mCellWidth;
        ArrayList<Integer> tempDate = findIndexByPosition(drawx, drawy);
        drawy = ((tempDate.get(0) * MONTH_OF_YEAR + tempDate.get(1)) * ROW_NUM + tempDate.get(2)) * mSurface.mCellHeight;
        drawx = tempDate.get(3) * mSurface.mCellWidth;
        ArrayList<Float> temp = new ArrayList<Float>();
        temp.add(drawx);
        temp.add(drawy);
        return temp;
    }

    private void drawAutoCircle(Canvas canvas) {

        float drawy = findAutoSelectPosition().get(1);
        float drawx = findAutoSelectPosition().get(0);
        canvas.drawRect(0, drawy, mSurface.mViewWidth, drawy + mSurface.mCellHeight, mSurface.mRecPaint);
        canvas.drawCircle(drawx + mSurface.mCellWidth / 2, drawy + mSurface.mCellHeight / 2f, mSurface.mCellWidth / 2.5f, mSurface.mCirclePaint);

    }

    private List<Integer> CalculateDate(int yearoffset, int month) {

        List<Integer> mDate = new ArrayList<Integer>();
        int year = mTodayDate.get(Calendar.YEAR) + (yearoffset - (YEAR_RANGE - 1) / 2);
        GregorianCalendar temp = new GregorianCalendar(year, month, 1);
        mDate = CalUtil.GetDayInMonth(temp);
        for (int i = 0; i < DAY_OF_MONTH - (CalUtil.getMaxDayOfMonth(temp) + (CalUtil.FindFirstDayofMonthInWeek(temp) - 1)); i++) {
            mDate.add(0);
        }

        return mDate;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d("Test", "Up");
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    /**
     * @param year
     * @param month
     * @param week
     * @param day
     * @return the position of the item in given index
     */
    private float findPositionByIndex(int year, int month, int week, int day) {

        float y = (((year * MONTH_OF_YEAR + month) * ROW_NUM + week) * mSurface.mCellHeight);
        return y;
    }

    private void ScrollToSelect(ArrayList<Integer> List) {

        float y = findPositionByIndex(List.get(0), List.get(1), List.get(2), List.get(3));
        int targetY = (int) (y - mSurface.mViewHeight / 2 + (List.get(3)) * mSurface.mScaleHeight);

        scrollTo(0, targetY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Test", "Show");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (mMode) {
            float posX = e.getX();
            float posY = e.getY();
            float CurY = getScrollY() + posY;
            mCurDateList = findIndexByPosition(posX, CurY);
            ScrollToSelect(mCurDateList);

        }
        return true;

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mMode) {
            if ((distanceY < 0 && getScrollY() > 0) || (distanceY > 0 && getScrollY() < mSurface.mMaxHeight)) {
                scrollBy(0, (int) distanceY);
            }
            mCurDateList = findIndexByPosition(findAutoSelectPosition().get(0), findAutoSelectPosition().get(1));

        } else {
            if ((distanceX < 0 && getScaleX() > 0) || (distanceX > 0 && getScaleX() < mSurface.mMaxWidth)) {
                scrollBy((int) distanceX, 0);
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Test", "LongPress");
    }


    private void DateChange(int timefield, int value) {
        GregorianCalendar temp = new GregorianCalendar(mCurDateList.get(0) - (YEAR_RANGE - 1) / 2 + mTodayDate.get(Calendar.YEAR), mCurDateList.get(1), 1);
        GregorianCalendar tempDate = new GregorianCalendar(mCurDateList.get(0) - (YEAR_RANGE - 1) / 2 + mTodayDate.get(Calendar.YEAR), mCurDateList.get(1), 1);
        int first = CalUtil.FindFirstDayofMonthInWeek(temp) - 1;
        int day = mCurDateList.get(2) * COLUM_NUM + mCurDateList.get(3) - first;
        temp.add(Calendar.DAY_OF_MONTH, day - 1);
        switch (timefield) {

            case DAY:
                temp.add(Calendar.DAY_OF_MONTH, value);
                break;
            case WEEK:
                temp.add(Calendar.WEEK_OF_YEAR, value);
                break;
            case MONTH:
                temp.add(Calendar.MONTH, value);
                break;
            case YEAR:
                temp.add(Calendar.YEAR, value);
                break;
            default:
                break;
        }
        mCurDateList.clear();
        mCurDateList.add((tempDate.get(Calendar.YEAR) - temp.get(Calendar.YEAR)) + (YEAR_RANGE - 1) / 2);
        mCurDateList.add(temp.get(Calendar.MONTH));
        first = CalUtil.FindFirstDayofMonthInWeek(temp) - 1;
        int week = (temp.get(Calendar.DAY_OF_MONTH) + first) / COLUM_NUM;
        mCurDateList.add(week);
        day = (temp.get(Calendar.DAY_OF_MONTH) + first) % COLUM_NUM;
        mCurDateList.add(day);

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (!mMode) {
            float pos = e1.getX() + getScrollX();
            float ex_horizen = pos % mSurface.mViewWidth;
            pos -= ex_horizen;
            if ((Math.abs(e1.getX() - e2.getX()) > mMotion.mMinFlingDistance) && ((Math.abs(velocityX) > mMotion.mMinFlingVelocity))) {
                if (velocityX < 0) {
                    DateChange(WEEK, 1);
                    scrollTo((int) pos + mSurface.mViewWidth, 0);

                } else {
                    DateChange(WEEK, -1);
                    scrollTo((int) pos - mSurface.mViewWidth, 0);

                }
            }
        }
        return true;
    }

    private static class mMotion {
        public static int mMinFlingDistance = 250;
        public static int mMinFlingVelocity = 2500;
        public static boolean ScrollX = false;
    }

    private class mSurface {
        public float mdensity;
        public int mViewHeight;
        public int mViewWidth;
        public int mCellWidth;
        public int mCellHeight;
        public float mMonthHeight;
        public float mMonthWidth;
        public int mBgColor = Color.parseColor("#FFFFFF");
        public int mCirColor = Color.parseColor("#888888");
        public int mRecColor = Color.parseColor("#EEEEEE");
        public int mTextColor = Color.BLACK;
        public int mCellDownColoe = Color.parseColor("#CCFFFF");
        public int mCellSelectedColoe = Color.parseColor("#FFFFFF");
        public int mMonthColor = Color.BLUE;
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

            mCellHeight = mCellWidth = (int) (mViewWidth / 7f);
            mMaxHeight = (YEAR_RANGE * MONTH_OF_YEAR - 1) * mCellHeight * ROW_NUM;
            mMaxWidth = (YEAR_RANGE * 365) * mCellWidth;

            mMonthHeight = 2 * mCellHeight;
            mScaleHeight = mCellHeight / COLUM_NUM;
            mMonthWidth = mViewWidth;
            mDatePaint = new Paint();
            mDatePaint.setColor(mTextColor);
            mDatePaint.setAntiAlias(true);
            mDatePaint.setTextSize(mCellHeight * 0.5f);
            mSelectedPaint = new Paint();
            mSelectedPaint.setColor(mCellSelectedColoe);
            mSelectedPaint.setAntiAlias(true);
            mSelectedPaint.setTextSize(mCellHeight * 0.5f);
            mTodayPaint = new Paint();
            mTodayPaint.setColor(mTodayColor);
            mTodayPaint.setAntiAlias(true);
            mTodayPaint.setTextSize(mCellHeight * 0.7f);
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

