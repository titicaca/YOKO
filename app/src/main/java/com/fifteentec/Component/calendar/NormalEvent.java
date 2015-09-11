package com.fifteentec.Component.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Database.EventRecord;
import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 日视图与周视图公用的一天的事件列表,实现以下手势操作:
 * 1,在空白格子上单击,出现临时选框,用户可以再次点击该选框添加新事件.
 * 2,在已经存在的事件上长按,可以选取该事件(获取事件的RID),可以对其进行拖曳,缩放操作.
 * 3,在事件选框上单击,在该位置再次固定该事件.
 */
public class NormalEvent extends ViewGroup implements GestureDetector.OnGestureListener{

    public int CellHeight;
    private int EndY;
    private int ScrollEndY;
    private int ScrollStartY;
    private int ViewWidth;
    private int ViewHeight;
    private boolean firstEntry = true;

    private EventItem tempRect = new EventItem(false);
    private EventItem existRect = new EventItem(false);

    private GestureDetector gestureDetector;
    private tempRectView mtempRectView;

    private int pressOffset = 0;



    private final int TEMPRECT =0x00;
    private final int SCREEN =0x01;
    private final int NONE = 0x02;
    private final int EXIST = 0x03;
    private final int CONSUME_EXIST = 0x04;
    private final int EXIST_TOP = 0x05;
    private final int EXIST_DOWN = 0x06;

    private final int AUTO_SCROLL_DOWN = 0x05;
    private final int AUTO_SCROLL_UP = 0x06;
    private final int AUTO_SCROLL_TOPUP = 0x07;
    private final int AUTO_SCROLL_TOPDOWN = 0x08;
    private final int AUTO_SCROLL_BOTTONUP = 0x09;
    private final int AUTO_SCROLL_BOTTONDOWN = 0x10;
    private int OPERATIONMODE =NONE;

    private ArrayList<EventItem> NormalEvnetPosition = new ArrayList<>();

    private NormalEventListener normalEventListener;

    private int AutoScrollEnd =0;
    private int AutoScrollMove =100;

    private int StrechMinDistance = 30;
    private int StrechMinHeight = 100;

    private Surface mSurface;

    private Context mcontext;

    private EventManager mEventManager;
    Handler handler = new Handler(){
        public void handleMessage(Message ms){
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

    /**
     * 提供给外部调用,若数据库的数据发生变化则通过该函数通知视图更新.
     */
    public void UpdateView() {
        ClearExistRect();
        CleartempRect();
        UpdateNormalPosition();
        invalidate();
    }

    /**
     * 显示通知视图内部管理画图位置的NormalPosition与数据库的数据同步.
     */
    private void UpdateNormalPosition() {
        NormalEvnetPosition.clear();
        for (int i = 0; i < mEventManager.getNormalDayEventCount(); i++) {
            EventItem eventItem = new EventItem();
            eventItem.rid = mEventManager.getNormalEventByIndex(i).rid;
            eventItem.left = mSurface.LinePadding+mSurface.LineRighePadding;
            eventItem.right = ViewWidth;
            eventItem.top = (int)(EndY * mEventManager.getPositionRatioByTime(mEventManager.getNormalEventByIndex(i).timebegin));
            eventItem.botton= (int)(EndY * mEventManager.getPositionRatioByTime(mEventManager.getNormalEventByIndex(i).timeend));
            eventItem.starttime = mEventManager.getEventRecordByRid(eventItem.rid).timebegin;
            eventItem.endtime = mEventManager.getEventRecordByRid(eventItem.rid).timeend;
            addNewEvent(eventItem);
        }
    }

    public interface NormalEventListener{
        void createRecord(int Type,EventRecord eventRecord);
        void deleteRecord(long rid);

        void checkExist(long i);
    }

    public void setNormalEventListener(NormalEventListener normalEventListener) {
        this.normalEventListener = normalEventListener;
    }

    public NormalEvent(Context context) {
        this(context, null);
    }

    public NormalEvent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalEvent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext =context;
    }

    public void initNormal(EventManager eventManager) {
        setWillNotDraw(false);
        mtempRectView = new tempRectView(mcontext);
        addView(mtempRectView);
        gestureDetector = new GestureDetector(getContext(),this);
        mSurface =new Surface();
        mEventManager =eventManager;


    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height =MeasureSpec.getSize(heightMeasureSpec);

        if(firstEntry){
            ViewHeight =(int)(height*(1-mSurface.CircleOneRadiusRatio));
            ViewWidth =width;
            mSurface.initSurface();

            CellHeight = ViewHeight/mSurface.NormalEventViewDivid;

            EndY = CellHeight*24;
            ScrollStartY = -(int)(mSurface.CircleOneRadiusRatio*ViewHeight);
            ScrollEndY =EndY-ViewHeight;
            mtempRectView.initView();
            UpdateNormalPosition();

        }



        if(mtempRectView !=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(EndY,MeasureSpec.EXACTLY);
            mtempRectView.measure(widthSpec, heightSpec);
        }
        setMeasuredDimension(width, height);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        int tempheight = 0;

        for (int i = 0; i < 13; i++) {
            canvas.drawLine(0, tempheight, ViewWidth,tempheight,mSurface.HorizonLinePaint);
            tempheight+=2*CellHeight;
        }

        canvas.drawLine(mSurface.LinePadding, 0, mSurface.LinePadding, EndY, mSurface.LinePaint);
        drawCircle(canvas, this.getScrollY()+ViewHeight*mSurface.CircleOneRadiusRatio,findTimeByY(getScrollY()+(int)(ViewHeight*mSurface.CircleOneRadiusRatio),true),false,true );
        drawCircle(canvas, this.getScrollY() + ViewHeight, findTimeByY(getScrollY() + ViewHeight, true), false, true);
        for(int i = 0;i<mEventManager.getNormalDayEventCount();i++){
            EventItem eventItem = NormalEvnetPosition.get(i);
            int getEventColor = 3;
            if( mEventManager.getEventRecordByRid(eventItem.rid).type ==0 ) getEventColor = mSurface.WorkEventPaint;
            else if( mEventManager.getEventRecordByRid(eventItem.rid).type ==1 ) getEventColor = mSurface.StudyEventPaint;
            else if( mEventManager.getEventRecordByRid(eventItem.rid).type ==2 ) getEventColor = mSurface.EntertainmentEventPaint;
            else if( mEventManager.getEventRecordByRid(eventItem.rid).type ==3 ) getEventColor = mSurface.OtherEventPaint;
            canvas.drawRect(eventItem.left, eventItem.top, eventItem.right - mSurface.EventTextPadding, eventItem.botton-mSurface.EventTextPadding, mSurface.pencase(getEventColor));
            long timebeigin = mEventManager.getEventRecordByRid(eventItem.rid).timebegin;
            drawCircle(canvas, eventItem.top,findTimeByY(timebeigin),true,true);
            long timeend = mEventManager.getEventRecordByRid(eventItem.rid).timeend;
            drawCircle(canvas, eventItem.botton, findTimeByY(timeend), true, false);


            int width = (eventItem.right-eventItem.left)-3*mSurface.EventTextPadding;
            int height = (eventItem.botton-eventItem.top)-2*mSurface.EventTextPadding;
            ArrayList<String> text = EventText(mEventManager.getNormalIntroduction(i),width,height);
            Rect rect = new Rect();
            mSurface.NormalText.getTextBounds("啊", 0, 1, rect);
            int allheight = text.size()*rect.height()*3/2;
            int startheight = (eventItem.top + eventItem.botton)/ 2 + allheight/2;
            for (int j = 0; j < text.size(); j++) {
                canvas.drawText(text.get(j), eventItem.left+mSurface.EventTextPadding, startheight, mSurface.NormalText);
                startheight -=rect.height()*3/2;
            }

        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mtempRectView !=null) mtempRectView.layout(0,0,mtempRectView.getMeasuredWidth(),mtempRectView.getMeasuredHeight());
    }

    private ArrayList<String> EventText(String s,int width,int height){

        /*
        int lenth = s.length();
        mSurface.NormalText.getTextBounds("啊",0,1,rect);
        int hsize = height/(rect.height()*3/2);
        int wsize =width/rect.width();
        int TextHeightSize;
        if(wsize<=0) TextHeightSize = 0;
        else {
            if (lenth / wsize > hsize) TextHeightSize = hsize;
            else {
                TextHeightSize = lenth / wsize;
                if (lenth % wsize != 0&&TextHeightSize != hsize)
                    TextHeightSize++;
            }
        }
*/
        Rect rect = new Rect();

        boolean isFull;
        ArrayList<String> result = new ArrayList<>();
        int start = 0;
        int end = 0;
        int Allheight = 0;
        do{
            isFull = false;
            do {
                if(end == s.length()) break;
                end ++;
                mSurface.NormalText.getTextBounds(s.substring(start, end),0,s.substring(start, end).length(),rect);
            }while(rect.width()<width);
            if(s.length() !=end || rect.width()>width) end --;
            result.add(0,s.substring(start, end));
            Allheight +=(rect.height()*3/2);
            if(s.length() == end) break;
            isFull =true;
            start = end;
        }while(Allheight<height);

        if(isFull) result.remove(result.size()-1);

        return result;

    }
    private String findTimeByY(int Y,boolean near){
        double finalY = Y;
        String result = new String();

        if(near) {
            double dividePart = (double)CellHeight /mSurface.CellUnit;
            double divide = Y % dividePart;
            if (divide < dividePart / 2d) finalY -= divide;
            else finalY += (dividePart - divide);

        }

        double offset = (finalY / (double) EndY) * mEventManager.MillsInOneDay;
        long Time = mEventManager.DayView_Date + Math.round(offset);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(Time);
        int Hour = gregorianCalendar.get(Calendar.HOUR);
        if(gregorianCalendar.get(Calendar.AM_PM)==Calendar.PM) Hour +=12;
        if(Hour<10){
            result += "0"+Hour+":";
        }else{
            result += ""+Hour+":";
        }
        if (gregorianCalendar.get(Calendar.MINUTE)<10) {
            result += "0"+gregorianCalendar.get(Calendar.MINUTE);
        }else {
            result += ""+gregorianCalendar.get(Calendar.MINUTE);
        }
        return result;
    }

    private String findTimeByY(long begin){

        GregorianCalendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTimeInMillis(begin);
        String timeText = new String();
        int Hour = tempCalendar.get(Calendar.HOUR);
        if(tempCalendar.get(Calendar.AM_PM)==Calendar.PM) Hour +=12;
        if(Hour<10){
            timeText += "0"+Hour+":";
        }else{
            timeText += ""+Hour+":";
        }
        if (tempCalendar.get(Calendar.MINUTE)<10) {
            timeText += "0"+tempCalendar.get(Calendar.MINUTE);
        }else {
            timeText += ""+tempCalendar.get(Calendar.MINUTE);
        }

        return timeText;
    }

    private void drawCircle(Canvas canvas,float y,String text,boolean line,boolean circle){
        if(circle) {
            canvas.drawCircle(mSurface.LinePadding, y, mSurface.CircleOneRadiusRatio * ViewWidth, mSurface.pencase(mSurface.CircleOnePaint));
            canvas.drawCircle(mSurface.LinePadding, y, mSurface.CircleOneRadiusRatio * ViewWidth, mSurface.pencase(mSurface.CircleTwoPaint));
            canvas.drawCircle(mSurface.LinePadding, y, mSurface.CircleTwoRadiusRatio * ViewWidth, mSurface.pencase(mSurface.CircleThreePaint));
        }
        Rect rect = new Rect();
        mSurface.pencase(mSurface.CircleRightText).getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text,mSurface.LinePadding+mSurface.CirclePadding,y+rect.height()/2,mSurface.pencase(mSurface.CircleRightText));
        if(line) {

            canvas.drawLine(mSurface.LinePadding+mSurface.CirclePadding*4/3+rect.width(),y,mSurface.LineRighePadding+mSurface.LinePadding-mSurface.CirclePadding,y,mSurface.LinePaint);
        }

    }


    /**
     * 增加一个新的事件,通过算法计算新的事件所需要作画的位置.
     * @param eventItem 新的事件
     */
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
        if(index == -1) {
            NormalEvnetPosition.add(a);
            ChangeList.add(NormalEvnetPosition.size() - 1);
        }else{
            NormalEvnetPosition.add(index, a);
            ChangeList.add(index);
        }

        boolean isChanged ;
        int Head = a.botton;
        int Tail = a.top;
        do {
            int lengthNew = Head-Tail;
            isChanged = false;
            for (int j = 0; j < NormalEvnetPosition.size(); j++) {
                EventItem temp = NormalEvnetPosition.get(j);
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


        RerangeList(ChangeList);
        OPERATIONMODE = NONE;
        invalidate();

    }

    /**
     * 根据一个rid删除特定事件,然后通过计算重新排列事件的视图
     * @param rid 需要删除事件的RID
     * @return 是否正确删除
     */
    private int deleteEvent(long rid){
        EventItem aim = null;
        int index=-1;
        for (int i = 0; i <NormalEvnetPosition.size(); i++) {
            long a = NormalEvnetPosition.get(i).rid;
            if(a == rid){
                aim = NormalEvnetPosition.get(i);
                NormalEvnetPosition.remove(i);
                index = i;
                break;
            }
        }

        mEventManager.deleteByRid(rid);
        if(aim != null) {
            List<Integer> ChangeList = new ArrayList<>();
            boolean isChanged;
            int Head = aim.botton;
            int Tail = aim.top;
            do {
                int lengthNew = Head - Tail;
                isChanged = false;
                for (int j = 0; j < NormalEvnetPosition.size(); j++) {
                    EventItem temp = NormalEvnetPosition.get(j);
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


            RerangeList(ChangeList);
            invalidate();
            return index;
        }else return index;

    }

    private void RerangeList(List<Integer> ChangeList){
        ArrayList<Integer> HandleList = new ArrayList<>();
        ArrayList<Integer> ModifList = new ArrayList<>();
        int divide = 0;

        while(ChangeList.size()!=0){
            int getIndex =0;
            EventItem hand = NormalEvnetPosition.get(ChangeList.get(0));
            for(int i =0;i<ChangeList.size();i++){
                EventItem temp = NormalEvnetPosition.get(ChangeList.get(i));
                if(temp.top<hand.top){
                    hand =temp;
                    getIndex =i;
                }
            }

            int behind =-1;
            int lengthhand = hand.botton-hand.top;

            for(int i =0;i<HandleList.size();i++){
                EventItem temp = NormalEvnetPosition.get(HandleList.get(i));
                int lengthtemp = temp.botton-temp.top;
                if(Math.max(Math.abs(temp.botton - hand.top), Math.abs(hand.botton-temp.top))>(lengthtemp+lengthhand)-1){
                    behind = i;
                    break;
                }
            }

            if(behind == -1){
                divide ++;
                int dividePart = (ViewWidth-mSurface.LinePadding-mSurface.LineRighePadding)/divide;
                for(int i =0;i<ModifList.size()/2;i++){
                    EventItem temp = NormalEvnetPosition.get(ModifList.get(i*2));
                    temp.left = mSurface.LineRighePadding+mSurface.LinePadding+dividePart*(ModifList.get(i*2+1)-1);
                    temp.right = temp.left +dividePart;
                }

                hand.left = mSurface.LineRighePadding+mSurface.LinePadding+dividePart*(divide-1);
                hand.right = hand.left +dividePart;

                ModifList.add(ChangeList.get(getIndex));
                ModifList.add(divide);

                HandleList.add(ChangeList.get(getIndex));
            }else{
                EventItem temp = NormalEvnetPosition.get(HandleList.get(behind));
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

    public void CleartempRect() {
        tempRect.left = 0;
        tempRect.right = 0;
        tempRect.top = 0;
        tempRect.botton = 0;
        tempRect.exist = false;
        mtempRectView.invalidate();
    }

    private void tempRectUpdate(float y) {
        int head = findIndex(y);
        tempRect.left = mSurface.LineRighePadding+mSurface.LinePadding;
        tempRect.top = head*CellHeight;
        tempRect.right = ViewWidth;
        tempRect.botton = (head+1)*CellHeight;
        tempRect.exist =true;
        mtempRectView.invalidate();

    }

    private void tempRectUpdate(int start,int end){
        tempRect.left =  mSurface.LineRighePadding+mSurface.LinePadding;
        tempRect.top = start;
        tempRect.right = ViewWidth;
        tempRect.botton = end;
        tempRect.exist =true;
        mtempRectView.invalidate();
    }

    private void ExistRectUpdate(int start,int end){
        existRect.left = mSurface.LineRighePadding+mSurface.LinePadding;
        existRect.top = start;
        existRect.right = ViewWidth;
        existRect.botton = end;
        existRect.exist = true;
        mtempRectView.invalidate();
    }

    private void ExistRectUpdate(int start,int end,long rid){
        existRect.rid =rid;
        ExistRectUpdate(start, end);
    }
    private void ClearExistRect(){
        existRect.left = 0;
        existRect.botton =0;
        existRect.right= 0;
        existRect.top = 0;
        existRect.exist =false;
        mtempRectView.invalidate();
    }
    private int findIndex(float y) {
        int Index=0;
        while (Index*CellHeight < y){
            Index++;
        }
        Index -- ;
        return Index;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        int positionY =(int)e.getY()+getScrollY();

        if(OPERATIONMODE == TEMPRECT){
            gestureDetector.setIsLongpressEnabled(true);
            return true;
        }else if(OPERATIONMODE == EXIST){
            if(Math.abs(positionY - existRect.top)<StrechMinDistance){
                OPERATIONMODE = EXIST_TOP;
                return true;
            }else if(Math.abs(positionY - existRect.botton)<StrechMinDistance){
                OPERATIONMODE = EXIST_DOWN;
                return true;
            }else if((positionY > existRect.top&&(positionY<existRect.botton)))return true;
            UpdateTimeByMove();


        }else if(OPERATIONMODE ==CONSUME_EXIST){
            return true;
        }
        OPERATIONMODE = SCREEN;
        ClearExistRect();
        CleartempRect();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        int positionX = (int) e.getX() - mSurface.LinePadding;
        int positionY = (int) (getScrollY() + e.getY());
        if(OPERATIONMODE ==SCREEN) {
            long i = isExist(positionY, positionX);
            if (i == -1) {
                //tempRectUpdate(positionY);
                //OPERATIONMODE=TEMPRECT;
            } else {
                normalEventListener.checkExist(i);
            }
            return true;
        }else if(OPERATIONMODE==TEMPRECT){
            /*
            int positionTop = tempRect.top;
            int positionBotton = tempRect.botton;
            long offsetTop = Math.round((positionTop / (double) (EndY)) * mEventManager.MillsInOneDay);
            long offsetBotton = Math.round((positionBotton / ((double) EndY)) * mEventManager.MillsInOneDay);
            EventRecord eventRecord = new EventRecord();
            eventRecord.timebegin = mEventManager.DayView_Date+offsetTop;
            eventRecord.timeend = mEventManager.DayView_Date+offsetBotton;

            normalEventListener.createRecord(NewEventView.HAVE_TIME,eventRecord);
            */
            return true;
        }else if(OPERATIONMODE == EXIST|| OPERATIONMODE == EXIST_DOWN || OPERATIONMODE == EXIST_TOP){
            UpdateTimeByMove();

            return true;
        }
        return false;
    }

    private void UpdateTimeByMove() {
        addNewEvent(existRect);
        mEventManager.UpdateEvent(existRect.starttime, existRect.endtime,existRect.rid);
        mEventManager.addEvent(existRect.rid);
        UpdateView();
        ClearExistRect();
    }

    private long isExist(int positionY,int positionX) {
        for(int i = 0;i<NormalEvnetPosition.size();i++){
            EventItem temp = NormalEvnetPosition.get(i);
            if((temp.top<positionY)&&
                    (temp.botton>positionY)&&
                    (temp.left<positionX)&&
                    (temp.right>positionX))
                return NormalEvnetPosition.get(i).rid;
        }
        return -1;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        final int endy = (int) e2.getY();
        int scrollY = endy+getScrollY();
        switch (OPERATIONMODE) {
            case SCREEN:
                if (distanceY > 0) {
                    if (getScrollY() < ScrollEndY) {
                        if (ScrollEndY - getScrollY() < Math.abs(distanceY)) scrollTo(0, ScrollEndY);
                        else scrollBy(0, (int) distanceY);
                    } else scrollTo(0, ScrollEndY);
                } else {
                    if (getScrollY() > ScrollStartY) {
                        if (getScrollY()-ScrollStartY < Math.abs(distanceY)) scrollTo(0, ScrollStartY);
                        else scrollBy(0, (int) distanceY);
                    } else scrollTo(0, ScrollStartY);
                }
                return true;
            case CONSUME_EXIST:
                OPERATIONMODE = EXIST;
                gestureDetector.setIsLongpressEnabled(true);
            case EXIST:
                int TimeHeight = existRect.botton-existRect.top;
                if(scrollY+pressOffset-TimeHeight/2<0) ExistRectUpdate(0,TimeHeight);
                else if(scrollY+pressOffset+TimeHeight/2 >EndY) ExistRectUpdate(EndY-TimeHeight,EndY);
                else {
                    ExistRectUpdate(scrollY+pressOffset - TimeHeight / 2, scrollY +pressOffset+ TimeHeight / 2);
                    if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                    if ((endy < 100 && getScrollY() > ScrollStartY) || (endy > ViewHeight - 100 && getScrollY() <ScrollEndY)) {
                        if (AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -1;
                            if (endy > ViewHeight - 100) AutoScrollEnd = 1;
                            AutoScrollThread mthread = new AutoScrollThread();
                            mthread.start();
                        }
                    }
                }

                return true;
            case EXIST_TOP:
                if((existRect.botton-existRect.top>StrechMinHeight||distanceY>0)&& endy+getScrollY()<existRect.botton-StrechMinHeight) {
                    ExistRectUpdate(existRect.top - (int) distanceY, existRect.botton);
                    if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                    if ((endy < 100 && getScrollY() > ScrollStartY) || (endy > ViewHeight - 100 && getScrollY() < ScrollEndY)) {
                        if (AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -2;
                            if (endy > ViewHeight - 100) AutoScrollEnd = 2;
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
                    if ((endy < 100 && getScrollY() > ScrollStartY) || (endy > ViewHeight - 100 && getScrollY() < ScrollEndY)) {
                        if (AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -3;
                            if (endy > ViewHeight - 100) AutoScrollEnd = 3;
                            AutoScrollThread mthread = new AutoScrollThread();
                            mthread.start();
                        }
                    }
                }
                return true;
            case TEMPRECT:
                int TimeHeightTemp = tempRect.botton-tempRect.top;
                if(scrollY-TimeHeightTemp/2<0) tempRectUpdate(0, TimeHeightTemp);
                else if(scrollY+TimeHeightTemp/2 >EndY) tempRectUpdate(EndY - TimeHeightTemp, EndY);
                else {
                    tempRectUpdate(scrollY - TimeHeightTemp / 2, scrollY + TimeHeightTemp / 2);
                }

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
                    if (getScrollY() < ScrollEndY) {
                        if (ScrollEndY - getScrollY() > AutoScrollMove) {
                            scrollBy(0, AutoScrollMove);
                            if(AutoScrollEnd == 2) handler.sendEmptyMessage(AUTO_SCROLL_TOPDOWN);
                            else if(AutoScrollEnd == 3) handler.sendEmptyMessage(AUTO_SCROLL_BOTTONDOWN);
                            else handler.sendEmptyMessage(AUTO_SCROLL_DOWN);
                        } else {
                            scrollTo(0, ScrollEndY);
                            break;
                        }
                    } else {
                        scrollTo(0, ScrollEndY);
                        break;
                    }
                } else {
                    if (getScrollY() > ScrollStartY) {
                        if (getScrollY() > AutoScrollMove) {
                            scrollBy(0, -AutoScrollMove);
                            if(AutoScrollEnd == -2) handler.sendEmptyMessage(AUTO_SCROLL_TOPUP);
                            else if(AutoScrollEnd == -3) handler.sendEmptyMessage(AUTO_SCROLL_BOTTONUP);
                            else handler.sendEmptyMessage(AUTO_SCROLL_UP);
                        } else {
                            scrollTo(0, ScrollStartY);
                            break;
                        }
                    } else {
                        scrollTo(0, ScrollStartY);
                        break;
                    }
                }
                synchronized (this) {
                    try {
                        wait(50000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        int positionX = (int)e.getX();
        int positionY = (int)e.getY()+getScrollY();
        long i =isExist(positionY,positionX);
        if(OPERATIONMODE == SCREEN) {
            if (!(i == -1)) {
                EventItem temp = null;
                for (int j = 0; j < NormalEvnetPosition.size(); j++) {
                    temp = NormalEvnetPosition.get(j);
                    if(temp.rid == i)  break;
                }
                if(temp == null) return;
                ExistRectUpdate(temp.top, temp.botton, temp.rid);
                deleteEvent(existRect.rid);
                OPERATIONMODE = CONSUME_EXIST;
                pressOffset = (existRect.botton+existRect.top)/2 - positionY;
                gestureDetector.setIsLongpressEnabled(false);
                MotionEvent ev = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(), e.getY(), 0);
                dispatchTouchEvent(ev);
            }else{
                int startPosition = 0;
                int endPosition = 0;
                if(positionY-CellHeight/2<0){
                    startPosition = 0;
                    endPosition = CellHeight;
                }else if(positionY +CellHeight/2>EndY){
                    startPosition = EndY-CellHeight;
                    endPosition = EndY;
                }else{
                    startPosition = positionY -CellHeight/2;
                    endPosition = positionY +CellHeight/2;
                }
                tempRectUpdate(startPosition, endPosition);
                OPERATIONMODE=TEMPRECT;

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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        /*if(OPERATIONMODE == EXIST || OPERATIONMODE == CONSUME_EXIST ||OPERATIONMODE ==EXIST_DOWN ||OPERATIONMODE ==EXIST_TOP){
            if(Math.abs(velocityX)>3000 ||Math.abs(velocityY)>3000 ){
                deleteEvent(existRect.rid);
                mEventManager.removeEvent(existRect.rid);
                normalEventListener.deleteRecord(existRect.rid);
                ClearExistRect();
            }
        }*/
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(OPERATIONMODE == CONSUME_EXIST) {
                gestureDetector.setIsLongpressEnabled(true);
                double unit = (double)CellHeight/mSurface.CellUnit;
                double newTop;
                if(existRect.top%unit<unit/2){
                    newTop = existRect.top-existRect.top%unit;
                }else{
                    newTop = existRect.top-existRect.top%unit+unit;
                }

                double newEnd;
                if(existRect.botton%unit<unit/2){
                    newEnd = existRect.botton-existRect.botton%unit;
                }else{
                    newEnd = existRect.botton-existRect.botton%unit+unit;
                }
                existRect.starttime = mEventManager.DayView_Date+Math.round((newTop/(double)EndY)*mEventManager.MillsInOneDay);
                existRect.endtime =mEventManager.DayView_Date+Math.round((newEnd/(double)EndY)*mEventManager.MillsInOneDay);

                ExistRectUpdate((int) Math.round(newTop), (int) Math.round(newEnd));
                pressOffset = 0;

                OPERATIONMODE = EXIST;
                return true;
            }else if(OPERATIONMODE == EXIST_TOP|| OPERATIONMODE == EXIST_DOWN) OPERATIONMODE = EXIST;
            else if(OPERATIONMODE == TEMPRECT){
                int positionTop = tempRect.top;
                int positionBotton = tempRect.botton;
                long offsetTop = Math.round((positionTop / (double) (EndY)) * mEventManager.MillsInOneDay);
                long offsetBotton = Math.round((positionBotton / ((double) EndY)) * mEventManager.MillsInOneDay);
                EventRecord eventRecord = new EventRecord();
                eventRecord.timebegin = mEventManager.DayView_Date+offsetTop;
                eventRecord.timeend = mEventManager.DayView_Date+offsetBotton;
                normalEventListener.createRecord(NewEventView.HAVE_TIME,eventRecord);
                CleartempRect();
                OPERATIONMODE= SCREEN;
            }else if(OPERATIONMODE == EXIST){
                double unit = (double)CellHeight/mSurface.CellUnit;
                double newTop;
                if(existRect.top%unit<unit/2){
                    newTop = existRect.top-existRect.top%unit;
                }else{
                    newTop = existRect.top-existRect.top%unit+unit;
                }

                double newEnd;
                if(existRect.botton%unit<unit/2){
                    newEnd = existRect.botton-existRect.botton%unit;
                }else{
                    newEnd = existRect.botton-existRect.botton%unit+unit;
                }
                existRect.starttime = mEventManager.DayView_Date+Math.round((newTop/(double)EndY)*mEventManager.MillsInOneDay);
                existRect.endtime =mEventManager.DayView_Date+Math.round((newEnd/(double)EndY)*mEventManager.MillsInOneDay);

                ExistRectUpdate((int) Math.round(newTop), (int) Math.round(newEnd));
                pressOffset = 0;
            }
            AutoScrollEnd = 0;
        }
        return gestureDetector.onTouchEvent(event);
    }



    private class tempRectView extends View {

        public tempRectView(Context context) {
            this(context,null);
        }

        public tempRectView(Context context, AttributeSet attrs) {
            this(context, attrs,0);
        }

        public tempRectView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        void initView(){        this.setAlpha(mSurface.templeAlpha);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if(tempRect.exist){
                canvas.drawRect(tempRect.left,tempRect.top,tempRect.right,tempRect.botton,mSurface.tempRectPaint);
            }
            if(existRect.exist){

                int getEventColor = 3;
                if( mEventManager.getEventRecordByRid(existRect.rid).type ==0 ) getEventColor = mSurface.WorkEventPaint;
                else if( mEventManager.getEventRecordByRid(existRect.rid).type ==1 ) getEventColor = mSurface.StudyEventPaint;
                else if( mEventManager.getEventRecordByRid(existRect.rid).type ==2 ) getEventColor = mSurface.EntertainmentEventPaint;
                else if( mEventManager.getEventRecordByRid(existRect.rid).type ==3 ) getEventColor = mSurface.OtherEventPaint;
                canvas.drawRect(existRect.left,existRect.top,existRect.right,existRect.botton,mSurface.pencase(getEventColor));

                int width = (existRect.right-existRect.left)-3*mSurface.EventTextPadding;
                int height = (existRect.botton-existRect.top)-2*mSurface.EventTextPadding;
                ArrayList<String> text = EventText(mEventManager.getEventRecordByRid(existRect.rid).introduction,width,height);
                Rect rect = new Rect();
                mSurface.NormalText.getTextBounds("啊", 0, 1, rect);
                int allheight = text.size()*rect.height()*3/2;
                int startheight = (existRect.top + existRect.botton)/ 2 + allheight/2;
                for (int j = 0; j < text.size(); j++) {
                    canvas.drawText(text.get(j), existRect.left+mSurface.EventTextPadding, startheight, mSurface.NormalText);
                    startheight -=rect.height()*3/2;
                }

                drawCircle(canvas, existRect.top,findTimeByY(existRect.top,true),true,true);
                drawCircle(canvas, existRect.botton, findTimeByY(existRect.botton,true), true, false);


            }
        }
    }

    private class Surface {

        Paint LinePaint;
        int LineColor = Color.BLACK;
        int LineWidth;
        float LineWidthRatio = 1/120f;
        float LinePaddingRatio = 1/20f;
        int LinePadding;

        float templeAlpha = 6/8f;

        int LineRighePadding;
        float LineRighePaddingRatio = 1/4f;

        Paint NormalText;
        int NormalEventViewDivid = 12;
        int NormalEvnetTextColor = Color.BLACK;
        int NormalEvnetTextSize ;
        float NormalEventTextSizeRatio = 1/15f;
        float EventTextPaddingRatio = 1/100f;
        int EventTextPadding ;

        Paint tempRectPaint;
        int tempRectColor = Color.BLUE;
        int tempRectWidth = 10;

        Paint ExistRectPaint;
        int ExistRectColor = Color.GREEN;

        Paint HorizonLinePaint = new Paint();
        int HorizonLinePaintColor = Color.parseColor("#F3F3F3");

        float CirclePadding;

        float CircleOneRadiusRatio = 1/50f;
        float CircleTwoRadiusRatio = 1/100f;
        float CircleWidth  = 1/200f;

        float CircleRightTextSize = 1/20f;

        final int CircleOnePaint = 0x00;
        final int CircleTwoPaint = 0x01;
        final int CircleThreePaint = 0x02;
        final int CircleRightText = 0x03;

        final int WorkEventPaint = 0x04;
        final int StudyEventPaint = 0x05;
        final int EntertainmentEventPaint = 0x06;
        final int OtherEventPaint = 0x07;

        final int DashLinePaint = 0x08;

        double CellUnit = 12d;

        void initSurface(){
            LinePaint = new Paint();
            LinePaint.setColor(LineColor);
            LineWidth = (int)(ViewWidth*LineWidthRatio);
            LinePaint.setStrokeWidth(LineWidth);
            NormalText = new Paint();
            NormalText.setAntiAlias(true);
            NormalText.setColor(NormalEvnetTextColor);
            NormalText.setTextSize(NormalEvnetTextSize);
            tempRectPaint = new Paint();
            tempRectPaint.setColor(tempRectColor);
            tempRectPaint.setStyle(Paint.Style.FILL);
            ExistRectPaint = new Paint();
            ExistRectPaint.setColor(ExistRectColor);
            LinePadding= (int)(ViewWidth*LinePaddingRatio);
            LineRighePadding =(int)(ViewWidth*LineRighePaddingRatio);
            HorizonLinePaint.setColor(HorizonLinePaintColor);
            HorizonLinePaint.setAntiAlias(true);
            HorizonLinePaint.setStrokeWidth(LineWidth);
            CirclePadding = CircleOneRadiusRatio*ViewWidth*3/2;
            EventTextPadding = (int)(ViewWidth*EventTextPaddingRatio);
            NormalText.setTextSize(ViewWidth*NormalEventTextSizeRatio);

        }

        Paint pencase(int Type){
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            switch (Type){
                case CircleOnePaint:
                    paint.setColor(Color.WHITE);
                    break;
                case CircleTwoPaint:
                    paint.setColor(Color.BLACK);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(ViewWidth*CircleWidth);
                    break;
                case CircleThreePaint:
                    paint.setColor(Color.BLACK);
                    break;
                case CircleRightText:
                    paint.setTextSize(ViewWidth * CircleRightTextSize);
                    paint.setColor(Color.BLACK);
                    break;
                case WorkEventPaint:
                    paint.setColor(getResources().getColor(R.color.WorkEventColor));
                    break;
                case StudyEventPaint:
                    paint.setColor(getResources().getColor(R.color.StudyEventColor));
                    break;
                case EntertainmentEventPaint:
                    paint.setColor(getResources().getColor(R.color.EntertainEventColor));
                    break;
                case OtherEventPaint:
                    paint.setColor(getResources().getColor(R.color.OtherEventColor));
                    break;
                case DashLinePaint:
                    break;

            }
            return paint;
        }

    }

    private class EventItem{

        EventItem(){

        };
        EventItem(boolean exist){
            this.exist = exist;
        }
        int left=0;
        int right=0;
        int top=0;
        int botton=0;
        long starttime = 0;
        long endtime = 0;
        long rid=0;
        boolean exist=true;
    }
}