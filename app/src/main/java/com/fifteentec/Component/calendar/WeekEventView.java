package com.fifteentec.Component.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.Database.EventRecord;
import com.fifteentec.yoko.BaseActivity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.LogRecord;

public class WeekEventView extends ViewGroup implements GestureDetector.OnGestureListener{

    private GregorianCalendar StartDate;
    private Surface mSurface;
    private int ScreenHeight;
    private int ScreenWidth;

    private int CellHeight;
    private int CellWidth;
    private Context mContext;
    private boolean firstEntry = true;
    private EventManager mEventManager;

    private long TodayTimeInMills;

    private EventItem tempRect =new EventItem(-1);
    private EventItem existRect = new EventItem(-1);

    private int OPERATIONMODE = NONE;
    private final static int NONE = 0x00;
    private final static int TEMPRECT = 0x01;
    private final static int CONSUME_EXIST = 0x02;
    private final static int EXIST = 0x03;
    private final static int EXIST_TOP = 0X04;
    private final static int EXIST_DOWN = 0x05;
    private final static int SCREEN = 0x06;

    private final static int AUTO_SCROLL_DOWN = 0x00;
    private final static int AUTO_SCROLL_UP = 0x01;
    private final static int AUTO_SCROLL_BOTTONDOWN = 0X02;
    private final static int AUTO_SCROLL_TOPUP = 0x03;
    private final static int AUTO_SCROLL_TOPDOWN = 0x04;
    private final static int AUTO_SCROLL_BOTTONUP = 0x05;

    private int AutoScrollMove = 50;
    private int StrechMinDistance = 30;
    private int StrechMinHeight = 100;
    private tempRectCanvas mTempRectCanvas;

    private GestureDetector gestureDetector;

    private int AutoScrollEnd = 0;

    private ArrayList<ArrayList<EventItem>> EventArray = new ArrayList<>();

    private WeekViewListener mWeekViewListener;

    public interface WeekViewListener{
        void CheckExistItem(long rid);
        void CreatRecord(int Type,EventRecord eventRecord);
    }

    public void setmWeekViewListener(WeekViewListener mWeekViewListener) {
        this.mWeekViewListener = mWeekViewListener;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message ms) {
            switch (ms.what){
                case AUTO_SCROLL_DOWN:
                    ExistRectUpdate(existRect.top+AutoScrollMove,existRect.botton+AutoScrollMove);
                    break;
                case AUTO_SCROLL_UP:
                    ExistRectUpdate(existRect.top - AutoScrollMove, existRect.botton - AutoScrollMove);
                    break;
                case AUTO_SCROLL_BOTTONDOWN:
                    ExistRectUpdate(existRect.top, existRect.botton + AutoScrollMove);
                    break;
                case AUTO_SCROLL_TOPDOWN:
                    ExistRectUpdate(existRect.top + AutoScrollMove, existRect.botton);
                    break;
                case AUTO_SCROLL_TOPUP:
                    ExistRectUpdate(existRect.top - AutoScrollMove,existRect.botton);
                    break;
                case AUTO_SCROLL_BOTTONUP:
                    ExistRectUpdate(existRect.top,existRect.botton- AutoScrollMove);
                    break;
            }
            super.handleMessage(ms);
        }
    };

    public WeekEventView(Context context) {
        this(context, null);
    }

    public WeekEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public static WeekEventView newInstance(Context context,ArrayList<Integer> date){
        WeekEventView weekEventView = new WeekEventView(context);
        weekEventView.initView(date);
        return weekEventView;
    }

    public void UpdateView(long rid,boolean exist){
        if(!exist) mEventManager.addEvent(rid);
        UpdateEventArray();
        CleartempRect();
        ClearExistRect();
        invalidate();
    }
    public void initView(ArrayList<Integer> date){
        StartDate = new GregorianCalendar(date.get(0),date.get(1),date.get(2));
        setWillNotDraw(false);
        mSurface = new Surface();
        TodayTimeInMills = StartDate.getTimeInMillis();
        mEventManager = EventManager.newInstance(((BaseActivity) mContext).getDBManager().getTableEvent(), EventManager.WEEK_VIEW_ENENT_MANAGER, TodayTimeInMills);
        mTempRectCanvas = new tempRectCanvas(mContext);
        addView(mTempRectCanvas);
        gestureDetector  = new GestureDetector(mContext,this);

    }

    public void UpdateEventArray(){
        EventArray.clear();
        for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
            ArrayList<EventItem>  arrayList = new ArrayList<>();
            EventArray.add(arrayList);
            for (int j = 0; j < mEventManager.getEventMangerInDayOfWeek(i).getNormalDayEventCount(); j++) {
                EventItem eventItem = new EventItem();
                eventItem.rid = mEventManager.getEventMangerInDayOfWeek(i).getNormalEventByIndex(j).rid;
                eventItem.left = 0;
                eventItem.right = CellWidth;
                eventItem.top = (int)((24/mSurface.dividePart)*ScreenHeight*mEventManager.getEventMangerInDayOfWeek(i).getPositionRatioByTime(mEventManager.getEventMangerInDayOfWeek(i).getNormalEventByIndex(j).timebegin));
                eventItem.botton= (int)((24/mSurface.dividePart)*ScreenHeight*mEventManager.getEventMangerInDayOfWeek(i).getPositionRatioByTime(mEventManager.getEventMangerInDayOfWeek(i).getNormalEventByIndex(j).timeend));
                eventItem.colum = i;
                addNewEvent(eventItem);
            }
        }
    }

    public void addNewEvent(EventItem eventItem){
        addNewEvent(eventItem,-1);
    }
    public void addNewEvent(EventItem eventItem,int index) {
        List<Integer> ChangeList = new ArrayList<>();

        EventItem a =new EventItem();
        a.rid =eventItem.rid;
        a.left = eventItem.left;
        a.right =eventItem.right;
        a.top = eventItem.top;
        a.botton =eventItem.botton;
        a.colum = eventItem.colum;
        if(index == -1) {
            EventArray.get(a.colum).add(a);
            ChangeList.add(EventArray.get(a.colum).size() - 1);
        }else{
            EventArray.get(a.colum).add(index, a);
            ChangeList.add(index);
        }


        boolean isChanged ;
        int Head = a.botton;
        int Tail = a.top;
        do {
            int lengthNew = Head-Tail;
            isChanged = false;
            for (int j = 0; j < EventArray.get(a.colum).size(); j++) {
                EventItem temp = EventArray.get(a.colum).get(j);
                int lengthOld = temp.botton - temp.top;
                if (Math.max(Math.abs(temp.botton - Tail), Math.abs(Head - temp.top)) < (lengthNew + lengthOld) - 1) {
                    boolean isSame = false;
                    for(int i =0;i<ChangeList.size();i++) {
                        if(ChangeList.get(i)==j) {
                            isSame = true;
                            break;
                        }
                    }
                    if(!isSame) {
                        if(temp.botton>Head) Head = temp.botton;
                        if(temp.top<Tail) Tail = temp.top;
                        ChangeList.add(j);
                        isChanged = true;
                    }
                }
            }

        }while (isChanged);


        RerangeList(ChangeList,a.colum);
        OPERATIONMODE = NONE;
        invalidate();


    }


    private void deleteEvent(long rid){
        EventItem aim = null;
        ArrayList<EventItem> arrayList=null;
        int Before_colum=-1;
        for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {

            arrayList = EventArray.get(i);
            for (int j= 0; j <arrayList.size(); j++) {
                long a = arrayList.get(j).rid;
                if(a == rid){
                    aim = arrayList.get(j);
                    arrayList.remove(j);
                    Before_colum = i;
                    break;
                }
            }
            if(aim != null) break;
        }

        if(aim != null) {
            List<Integer> ChangeList = new ArrayList<>();
            boolean isChanged;
            int Head = aim.botton;
            int Tail = aim.top;
            do {
                int lengthNew = Head - Tail;
                isChanged = false;
                for (int j = 0; j < arrayList.size(); j++) {
                    EventItem temp = arrayList.get(j);
                    int lengthOld = temp.botton - temp.top;
                    if (Math.max(Math.abs(temp.botton - Tail), Math.abs(Head - temp.top)) < (lengthNew + lengthOld) - 1) {
                        boolean isSame = false;
                        for (int i = 0; i < ChangeList.size(); i++) {
                            if (ChangeList.get(i) == j) {
                                isSame = true;
                                break;
                            }
                        }
                        if (!isSame) {
                            if (temp.botton > Head) Head = temp.botton;
                            if (temp.top < Tail) Tail = temp.top;
                            ChangeList.add(j);
                            isChanged = true;
                        }
                    }
                }
            } while (isChanged);


            Log.d("Test","Size "+ChangeList.size()+" Colum "+Before_colum);
            RerangeList(ChangeList, Before_colum);
            invalidate();
        }

    }


    private void RerangeList(List<Integer> ChangeList,int colom){
        ArrayList<Integer> HandleList = new ArrayList<>();
        ArrayList<Integer> ModifList = new ArrayList<>();
        int divide = 0;

        while(ChangeList.size()!=0){
            int getIndex =0;
            EventItem hand = EventArray.get(colom).get(ChangeList.get(0));
            for(int i =0;i<ChangeList.size();i++){
                EventItem temp = EventArray.get(colom).get(ChangeList.get(i));
                if(temp.top<hand.top){
                    hand =temp;
                    getIndex =i;
                }
            }

            int behind =-1;
            int lengthhand = hand.botton-hand.top;

            for(int i =0;i<HandleList.size();i++){
                EventItem temp = EventArray.get(colom).get(HandleList.get(i));
                int lengthtemp = temp.botton-temp.top;
                if(Math.max(Math.abs(temp.botton - hand.top), Math.abs(hand.botton-temp.top))>(lengthtemp+lengthhand)-1){
                    behind = i;
                    break;
                }
            }

            if(behind == -1){
                divide ++;
                int dividePart = CellWidth/divide;
                for(int i =0;i<ModifList.size()/2;i++){
                    EventItem temp = EventArray.get(colom).get(ModifList.get(i * 2));
                    temp.left =dividePart*(ModifList.get(i*2+1)-1);
                    temp.right = temp.left +dividePart;
                }

                hand.left =dividePart*(divide-1);
                hand.right = hand.left +dividePart;

                ModifList.add(ChangeList.get(getIndex));
                ModifList.add(divide);

                HandleList.add(ChangeList.get(getIndex));
            }else{
                EventItem temp = EventArray.get(colom).get(HandleList.get(behind));
                hand.left =temp.left;
                hand.right =temp.right;
                ModifList.add(ChangeList.get(getIndex));
                ModifList.add(behind + 1);
                HandleList.remove(behind);
                HandleList.add(behind, ChangeList.get(getIndex));

            }


            ChangeList.remove(getIndex);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if(mTempRectCanvas != null){
            int WidthSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
            int HeightSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
            mTempRectCanvas.measure(WidthSpec,HeightSpec);
        }
        if (firstEntry) {
            ScreenHeight = height;
            ScreenWidth =width;
            CellHeight = height/mSurface.dividePart;
            mSurface.initSurface();
            CellWidth = mSurface.ViewWidth/CalUtil.LENTH_OF_WEEK;
            firstEntry = false;
            UpdateEventArray();
        }
        setMeasuredDimension(width, height);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mTempRectCanvas != null) mTempRectCanvas.layout(mSurface.TimeTextWidthInt,0,ScreenWidth,mSurface.EndHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = 0;
        for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
            canvas.drawLine(mSurface.TimeTextWidthInt+width,0,mSurface.TimeTextWidthInt+width,mSurface.EndHeight,mSurface.mLinePaint);
            width+=CellWidth;
            EventManager eventManager = mEventManager.getEventMangerInDayOfWeek(i);
            for(int j = 0;j<eventManager.getNormalDayEventCount();j++){
                EventItem eventItem = EventArray.get(i).get(j);
                canvas.drawRect(i * CellWidth + mSurface.TimeTextWidthInt + eventItem.left, eventItem.top, i * CellWidth + mSurface.TimeTextWidthInt + eventItem.right, eventItem.botton, mSurface.mNormalEventPaint);
                canvas.drawText(eventManager.getNormalIntroduction(j).substring(0,3), i*CellWidth+mSurface.TimeTextWidthInt+(eventItem.left+ eventItem.right) / 2, (eventItem.top + eventItem.botton) / 2, mSurface.mTextPaint);

            }
        }

        canvas.drawLine(mSurface.TimeTextWidthInt, 0, mSurface.TimeTextWidthInt, mSurface.EndHeight, mSurface.mLinePaint);
        Rect rect = new Rect();
        int height = CellHeight;
        for (int i = 0; i < 24; i++) {
            String a = i+"";
            mSurface.mTextPaint.getTextBounds(a, 0, a.length(), rect);
            canvas.drawText(i + "", mSurface.TimeTextWidthInt / 2 - rect.width() / 2, height-CellHeight/2+ rect.height() / 2, mSurface.mTextPaint);

            canvas.drawLine(mSurface.TimeTextWidthInt, height,ScreenWidth, height, mSurface.mLinePaint);
            height +=CellHeight;
        }

    }

    public void CleartempRect() {
        tempRect.left = 0;
        tempRect.right = 0;
        tempRect.top = 0;
        tempRect.botton = 0;
        tempRect.exist = false;
        tempRect.colum = -1;
        mTempRectCanvas.invalidate();
    }

    private void tempRectUpdate(float y,float x,boolean tf) {
        if(tf) {
            int[] head = findIndex(x,y);
            tempRect.left =0;
            tempRect.top = head[1]*CellHeight;
            tempRect.right = CellWidth;
            tempRect.botton = (head[1] + 1) * CellHeight;
            tempRect.exist = true;
            tempRect.colum = head[0];
            mTempRectCanvas.invalidate();
        }
    }

    private void tempRectUpdate(int start,int end){
        tempRect.left =0;
        tempRect.top = start;
        tempRect.right =CellWidth;
        tempRect.botton = end;
        tempRect.exist =true;
        mTempRectCanvas.invalidate();
    }

    private void ExistRectUpdate(int start,int end){
        existRect.left =0;
        existRect.top = start;
        existRect.right = CellWidth;
        existRect.botton = end;
        existRect.exist = true;
        mTempRectCanvas.invalidate();
    }

    private void ExistRectUpdate(int start,int end,long rid,int colum){
        existRect.rid =rid;
        existRect.colum = colum;
        ExistRectUpdate(start, end);
    }
    private void ClearExistRect(){
        existRect.left = 0;
        existRect.botton =0;
        existRect.right= 0;
        existRect.top = 0;
        existRect.exist =false;
        existRect.colum = -1;
        mTempRectCanvas.invalidate();
    }

    private int[] findIndex(float x,float y) {
        int Index=0;
        while (Index*CellHeight < y){
            Index++;
        }
        Index -- ;
        int IndexX = 0;
        while (IndexX*CellWidth <x){
            IndexX++;
        }
        IndexX--;
        int[] result = new int[2];
        result[0] = IndexX;
        result[1] = Index;
        return result;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_UP){
            switch (OPERATIONMODE){
                case CONSUME_EXIST:
                    gestureDetector.setIsLongpressEnabled(true);
                    OPERATIONMODE = EXIST;
                    return true;
                case EXIST_DOWN:
                case EXIST_TOP:
                    OPERATIONMODE = EXIST;

            }
            AutoScrollEnd = 0;
        }
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        int positionY =(int)e.getY()+getScrollY();
        int positionX =(int)e.getX()-mSurface.TimeTextWidthInt;
        if(OPERATIONMODE == CONSUME_EXIST) return true;
        else if(OPERATIONMODE ==TEMPRECT){
            if((positionY > tempRect.top) && (positionY < tempRect.botton)&&(positionX>tempRect.left+tempRect.colum*CellWidth)&&(positionX<tempRect.right+(tempRect.colum+1)*CellWidth)) return true;
        }else if(OPERATIONMODE == EXIST){
            if((positionX<existRect.right+existRect.colum*CellWidth)&&(positionX>existRect.left+existRect.colum*CellWidth)) {
                if (Math.abs(positionY - existRect.top) < StrechMinDistance) {
                    OPERATIONMODE = EXIST_TOP;
                    return true;
                } else if (Math.abs(positionY - existRect.botton) < StrechMinDistance) {
                    OPERATIONMODE = EXIST_DOWN;
                    return true;
                } else if ((positionY > existRect.top && (positionY < existRect.botton)))
                    return true;
                UpdateTimeByMove();
            }

        }

        OPERATIONMODE = SCREEN;
        ClearExistRect();
        CleartempRect();
        return true;
    }

    private void UpdateTimeByMove() {
        int i= 0;
        deleteEvent(existRect.rid);
        mEventManager.deleteByRid(existRect.rid);
        long timebegin = (long) (mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+(existRect.top/(float)(mSurface.EndHeight))*mEventManager.MillsInOneDay);
        long timeend = (long)(mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+(existRect.botton/(float)(mSurface.EndHeight))*mEventManager.MillsInOneDay);
        mEventManager.UpdateEvent(timebegin, timeend, existRect.rid);
        addNewEvent(existRect);
        mEventManager.addEvent(existRect.rid);

        ClearExistRect();
        invalidate();
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int positionX = (int) e.getX()-mSurface.TimeTextWidthInt;
        int positionY = (int) (getScrollY() + e.getY());
        if(OPERATIONMODE ==SCREEN) {
            long i = isExist(positionY, positionX);
            if (i == -1) {
                tempRectUpdate(positionY,positionX,true);
                OPERATIONMODE=TEMPRECT;
            } else {
                mWeekViewListener.CheckExistItem(i);
            }
            return true;
        }else if(OPERATIONMODE==TEMPRECT){
            int positionTop = tempRect.top;
            int positionBotton = tempRect.botton;
            long offsetTop = (long)((positionTop/(float)((24/mSurface.dividePart)*ScreenHeight)*mEventManager.MillsInOneDay));
            long offsetBotton = (long)((positionBotton/(float)((24/mSurface.dividePart)*ScreenHeight)*mEventManager.MillsInOneDay));
            EventRecord eventRecord = new EventRecord();
            eventRecord.timebegin = mEventManager.getEventMangerInDayOfWeek(tempRect.colum).DayView_Date+offsetTop;
            eventRecord.timeend = mEventManager.getEventMangerInDayOfWeek(tempRect.colum).DayView_Date+offsetBotton;

            mWeekViewListener.CreatRecord(NewEventView.HAVE_TIME,eventRecord);
            return true;
        }else if(OPERATIONMODE == EXIST|| OPERATIONMODE == EXIST_DOWN || OPERATIONMODE == EXIST_TOP){
            UpdateTimeByMove();

            return true;
        }

        return false;

    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        final int endy = (int) e2.getY();
        int scrollY = endy+getScrollY();
        int endx = (int)e2.getX()-mSurface.TimeTextWidthInt;
        switch (OPERATIONMODE) {
            case SCREEN:
                if (distanceY > 0) {
                    if (getScrollY() < mSurface.ScrollEndY) {
                        if ( mSurface.ScrollEndY - getScrollY() < Math.abs(distanceY)) scrollTo(0,  mSurface.ScrollEndY);
                        else scrollBy(0, (int) distanceY);
                    } else scrollTo(0,  mSurface.ScrollEndY);
                } else {
                    if (getScrollY() > 0) {
                        if (getScrollY() < Math.abs(distanceY)) scrollTo(0, 0);
                        else scrollBy(0, (int) distanceY);
                    } else scrollTo(0, 0);
                }
                return true;
            case CONSUME_EXIST:
                OPERATIONMODE = EXIST;
                gestureDetector.setIsLongpressEnabled(true);
            case EXIST:
                int TimeHeight = existRect.botton-existRect.top;
                if(endx>existRect.colum*CellWidth+existRect.left&&endx<existRect.colum*CellWidth+existRect.right) {

                    if(scrollY-TimeHeight/2<0) ExistRectUpdate(0,TimeHeight);
                    else if(scrollY+TimeHeight/2 >mSurface.EndHeight) ExistRectUpdate(mSurface.EndHeight-TimeHeight,mSurface.EndHeight);
                    else {
                        ExistRectUpdate(scrollY - TimeHeight / 2, scrollY + TimeHeight / 2);
                        if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                        if ((endy < 100 && getScrollY() > 0) || (endy > ScreenHeight - 100 && getScrollY() < mSurface.ScrollEndY)) {
                            if (AutoScrollEnd == 0) {
                                if (endy < 100) AutoScrollEnd = -1;
                                if (endy > ScreenHeight - 100) AutoScrollEnd = 1;
                                AutoScrollThread mthread = new AutoScrollThread();
                                mthread.start();
                            }
                        }
                    }
                }else{
                    int index[] = findIndex(endx,endy);
                    if(scrollY-TimeHeight/2<0) ExistRectUpdate(0,TimeHeight,existRect.rid,index[0]);
                    else if(scrollY+TimeHeight/2 >mSurface.EndHeight) ExistRectUpdate(mSurface.EndHeight-TimeHeight,mSurface.EndHeight,existRect.rid,index[0]);
                    else ExistRectUpdate(scrollY-TimeHeight/2,scrollY+TimeHeight/2,existRect.rid,index[0]);
                }
                return true;
            case EXIST_TOP:
                if((existRect.botton-existRect.top>StrechMinHeight||distanceY>0)&& scrollY<existRect.botton-StrechMinHeight) {
                    ExistRectUpdate(existRect.top - (int) distanceY, existRect.botton);
                    if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                    if ((endy < 100 && getScrollY() > 0) || (endy > ScreenHeight - 100 && getScrollY() < mSurface.ScrollEndY)) {
                        if (AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -2;
                            if (endy > ScreenHeight - 100) AutoScrollEnd = 2;
                            AutoScrollThread mthread = new AutoScrollThread();
                            mthread.start();
                        }
                    }
                }
                return true;
            case EXIST_DOWN:
                if((existRect.botton-existRect.top>StrechMinHeight||distanceY<0)&&endy+getScrollY()>existRect.top+StrechMinHeight) {
                    ExistRectUpdate(existRect.top, existRect.botton - (int) distanceY);
                    if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                    if ((endy < 100 && getScrollY() > 0) || (endy >ScreenHeight - 100 && getScrollY() <  mSurface.ScrollEndY)) {
                        if (AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -3;
                            if (endy > ScreenHeight - 100) AutoScrollEnd = 3;
                            AutoScrollThread mthread = new AutoScrollThread();
                            mthread.start();
                        }
                    }
                }
                return true;
            case TEMPRECT:
                OPERATIONMODE = SCREEN;
                CleartempRect();
                return true;
            default:
                return false;
        }

    }


    private class AutoScrollThread extends Thread{

        @Override
        public void run() {
            while (AutoScrollEnd != 0) {
                if (AutoScrollEnd > 0) {
                    if (getScrollY() < mSurface.ScrollEndY) {
                        if (mSurface.ScrollEndY > AutoScrollMove+getScrollY()) {
                            scrollBy(0, AutoScrollMove);
                            if(AutoScrollEnd == 2) handler.sendEmptyMessage(AUTO_SCROLL_TOPDOWN);
                            else if(AutoScrollEnd == 3) handler.sendEmptyMessage(AUTO_SCROLL_BOTTONDOWN);
                            else handler.sendEmptyMessage(AUTO_SCROLL_DOWN);
                        } else {
                            scrollTo(0, mSurface.ScrollEndY);
                            break;
                        }
                    } else {
                        scrollTo(0, mSurface.ScrollEndY);
                        break;
                    }
                } else {
                    if (getScrollY() > 0) {
                        if (getScrollY() > AutoScrollMove) {
                            scrollBy(0, -AutoScrollMove);
                            if(AutoScrollEnd == -2) handler.sendEmptyMessage(AUTO_SCROLL_TOPUP);
                            else if(AutoScrollEnd == -3) handler.sendEmptyMessage(AUTO_SCROLL_BOTTONUP);
                            else handler.sendEmptyMessage(AUTO_SCROLL_UP);
                        } else {
                            scrollTo(0, 0);
                            break;
                        }
                    } else {
                        scrollTo(0, 0);
                        break;
                    }
                }
                synchronized (this) {
                    try {
                        wait(500000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        int positionX = (int)e.getX()-mSurface.TimeTextWidthInt;
        int positionY = (int)e.getY()+getScrollY();
        long i =isExist(positionY,positionX);

        if(OPERATIONMODE == SCREEN) {
            if (!(i == -1)) {
                EventItem temp = null;
                for (int j = 0; j < CalUtil.LENTH_OF_WEEK; j++) {
                    ArrayList<EventItem> arrayList = EventArray.get(j);
                    for (int k = 0; k <arrayList.size();k++) {
                        temp =arrayList.get(k);
                        if(temp.rid == i)  break;
                    }
                    if(temp != null){
                        if(temp.rid ==i) break;
                    }
                }
                if(temp == null) return;
                ExistRectUpdate(temp.top, temp.botton, temp.rid,temp.colum);
                OPERATIONMODE = CONSUME_EXIST;
                gestureDetector.setIsLongpressEnabled(false);
                MotionEvent ev = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(), e.getY(), 0);
                dispatchTouchEvent(ev);

            }

        }else if(OPERATIONMODE == EXIST||OPERATIONMODE == EXIST_TOP || OPERATIONMODE == EXIST_DOWN){
            OPERATIONMODE = CONSUME_EXIST;
            gestureDetector.setIsLongpressEnabled(false);
            MotionEvent ev = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(), e.getY(), 0);
            dispatchTouchEvent(ev);
        }

    }

    private long isExist(int positionY,int positionX) {
        for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
            ArrayList<EventItem> arrayList = EventArray.get(i);
            for(int j = 0;j<arrayList.size();j++){
                EventItem temp = arrayList.get(j);
                if((temp.top<positionY)&&
                        (temp.botton>positionY)&&
                        (temp.left+temp.colum*CellWidth<positionX)&&
                        (temp.right+temp.colum*CellWidth>positionX))
                    return arrayList.get(j).rid;
            }

        }
        return -1;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    private class Surface{
        float TimeTextWidth = 1/10f;
        int TimeTextWidthInt;
        int EndHeight ;
        int ViewWidth;
        int dividePart = 12;
        int ScrollEndY;


        Paint mLinePaint;
        int mLineColor = Color.BLACK;


        Paint mTextPaint;
        int mTextColor = Color.RED;
        int mTextSize =50;
        Paint tempRectPaint;
        int mtempRectColor = Color.BLUE;
        Paint ExistRectPaint;
        int mExistRectPaint = Color.YELLOW;

        Paint mNormalEventPaint;
        int mNormalEvnentColor = Color.parseColor("#303030");

        void initSurface(){
            TimeTextWidthInt = (int)(TimeTextWidth*ScreenWidth);
            ViewWidth = ScreenWidth - TimeTextWidthInt;
            ScrollEndY = CellHeight*24-(int)(ScreenHeight);
            EndHeight = CellHeight*24;
            mLinePaint = new Paint();
            mLinePaint.setColor(mLineColor);
            mLinePaint.setAntiAlias(true);
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(mTextSize);
            tempRectPaint = new Paint();
            tempRectPaint.setColor(mtempRectColor);
            tempRectPaint.setAntiAlias(true);
            tempRectPaint.setStyle(Paint.Style.FILL);
            ExistRectPaint = new Paint();
            ExistRectPaint.setAntiAlias(true);
            ExistRectPaint.setColor(mExistRectPaint);
            ExistRectPaint.setStyle(Paint.Style.FILL);
            mNormalEventPaint = new Paint();
            mNormalEventPaint.setColor(mNormalEvnentColor);
            mNormalEventPaint.setAntiAlias(true);
            mNormalEventPaint.setStyle(Paint.Style.FILL);
        }
    }

    private class tempRectCanvas extends View{

        public tempRectCanvas(Context context) {
            this(context, null);
        }

        public tempRectCanvas(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public tempRectCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            if(tempRect.exist){
                canvas.drawRect(tempRect.colum * CellWidth  +tempRect.left,tempRect.top,tempRect.colum * CellWidth +tempRect.right,tempRect.botton,mSurface.tempRectPaint);
            }
            if(existRect.exist){
                canvas.drawRect(existRect.colum * CellWidth+existRect.left,existRect.top,existRect.colum * CellWidth +existRect.right,existRect.botton,mSurface.ExistRectPaint);
            }
        }
    }

    private class EventItem{

        EventItem(){

        };

        EventItem(int i){
            colum = i;
            if(i == -1){
                exist = false;
            }else exist = true;
        }

        int left=0;
        int right=0;
        int top=0;
        int botton=0;
        long rid=0;
        boolean exist=true;
        int colum=-1;
    }
}
