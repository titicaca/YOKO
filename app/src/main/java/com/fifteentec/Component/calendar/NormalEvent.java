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

import java.util.ArrayList;
import java.util.List;

/**
 * 日视图与周视图公用的一天的事件列表,实现以下手势操作:
 * 1,在空白格子上单击,出现临时选框,用户可以再次点击该选框添加新事件.
 * 2,在已经存在的事件上长按,可以选取该事件(获取事件的RID),可以对其进行拖曳,缩放操作.
 * 3,在事件选框上单击,在该位置再次固定该事件.
 */
public class NormalEvent extends ViewGroup implements GestureDetector.OnGestureListener{

    private int CellHeight;
    private int EndY;
    private int ScrollEndY;
    private int ViewWidth;
    private int ViewHeight;
    private boolean firstEntry = true;

    private EventItem tempRect = new EventItem(false);
    private EventItem existRect = new EventItem(false);

    private GestureDetector gestureDetector;
    private tempRectView mtempRectView;



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
            eventItem.left = mSurface.LinePadding;
            eventItem.right = ViewWidth;
            eventItem.top = (int)(3*ViewHeight*mEventManager.getPositionRatioByTime(mEventManager.getNormalEventByIndex(i).timebegin));
            eventItem.botton= (int)(3*ViewHeight*mEventManager.getPositionRatioByTime(mEventManager.getNormalEventByIndex(i).timeend));
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

        CellHeight = height/mSurface.NormalEventViewDivid;
        ViewWidth =width;
        EndY = height*3;
        ScrollEndY =height*2;
        ViewHeight =height;

        if(mtempRectView !=null){
            int widthSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(EndY,MeasureSpec.EXACTLY);
            mtempRectView.measure(widthSpec,heightSpec);
        }
        if(firstEntry) UpdateNormalPosition();
        setMeasuredDimension(width, height);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawLine(mSurface.LinePadding, 0, mSurface.LinePadding, EndY, mSurface.LinePaint);
        for(int i = 0;i<mEventManager.getNormalDayEventCount();i++){
            EventItem eventItem = NormalEvnetPosition.get(i);
            canvas.drawRect(eventItem.left, eventItem.top, eventItem.right, eventItem.botton, mSurface.NormalEventPaint);
            canvas.drawText(mEventManager.getNormalIntroduction(i), (eventItem.left+ eventItem.right) / 2, (eventItem.top + eventItem.botton) / 2, mSurface.NormalText);

        }
        int tempheight = CellHeight;
        Rect TextRect =new Rect();
        mSurface.NormalText.getTextBounds("8", 0, 1, TextRect);
        for (int i = 0; i < 24; i++) {
            canvas.drawLine(mSurface.LinePadding, tempheight, mSurface.LinePadding + mSurface.NormalEvnetLineLength,tempheight,mSurface.LinePaint);
            String a = i+"";
            canvas.drawText(a,mSurface.LinePadding-a.length()*TextRect.width()-15,tempheight,mSurface.NormalText);
            tempheight +=CellHeight;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mtempRectView !=null) mtempRectView.layout(0,0,mtempRectView.getMeasuredWidth(),mtempRectView.getMeasuredHeight());
    }


    /**
     * 增加一个新的事件,通过算法计算新的事件所需要作画的位置.
     * @param eventItem 新的事件
     */
    public void addNewEvent(EventItem eventItem) {
        List<Integer> ChangeList = new ArrayList<>();

        EventItem a =new EventItem();
        a.rid =eventItem.rid;
        a.left = eventItem.left;
        a.right =eventItem.right;
        a.top = eventItem.top;
        a.botton =eventItem.botton;
        NormalEvnetPosition.add(a);
        ChangeList.add(NormalEvnetPosition.size() - 1);
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
    private boolean deleteEvent(long rid){
        EventItem aim = null;
        for (int i = 0; i <NormalEvnetPosition.size(); i++) {
            long a = NormalEvnetPosition.get(i).rid;
            if(a == rid){
                aim = NormalEvnetPosition.get(i);
                NormalEvnetPosition.remove(i);

                break;
            }
        }
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
            return true;
        }else return false;

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
                int dividePart = (ViewWidth-mSurface.LinePadding)/divide;
                for(int i =0;i<ModifList.size()/2;i++){
                    EventItem temp = NormalEvnetPosition.get(ModifList.get(i*2));
                    temp.left = mSurface.LinePadding+dividePart*(ModifList.get(i*2+1)-1);
                    temp.right = temp.left +dividePart;
                }

                hand.left = mSurface.LinePadding+dividePart*(divide-1);
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
        tempRect.left = mSurface.LinePadding;
        tempRect.top = head*CellHeight;
        tempRect.right = ViewWidth;
        tempRect.botton = (head+1)*CellHeight;
        tempRect.exist =true;
        mtempRectView.invalidate();

    }

    private void tempRectUpdate(int start,int end){
        tempRect.left = mSurface.LinePadding;
        tempRect.top = start;
        tempRect.right = ViewWidth;
        tempRect.botton = end;
        tempRect.exist =true;
        mtempRectView.invalidate();
    }

    private void ExistRectUpdate(int start,int end){
        existRect.left = mSurface.LinePadding;
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
            if((positionY > tempRect.top) && (positionY < tempRect.botton)) return true;
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
                tempRectUpdate(positionY);
                OPERATIONMODE=TEMPRECT;
            } else {
                normalEventListener.checkExist(i);
            }
            return true;
        }else if(OPERATIONMODE==TEMPRECT){
            int positionTop = tempRect.top;
            int positionBotton = tempRect.botton;
            long offsetTop = (long)((positionTop/(float)(ViewHeight*3))*mEventManager.MillsInOneDay);
            long offsetBotton = (long)((positionBotton/((float)ViewHeight*3))*mEventManager.MillsInOneDay);
            EventRecord eventRecord = new EventRecord();
            eventRecord.timebegin = mEventManager.DayView_Date+offsetTop;
            eventRecord.timeend = mEventManager.DayView_Date+offsetBotton;

            normalEventListener.createRecord(NewEventView.HAVE_TIME,eventRecord);
            return true;
        }else if(OPERATIONMODE == EXIST|| OPERATIONMODE == EXIST_DOWN || OPERATIONMODE == EXIST_TOP){
            UpdateTimeByMove();

            return true;
        }
        return false;
    }

    private void UpdateTimeByMove() {
        deleteEvent(existRect.rid);
        addNewEvent(existRect);
        long timebegin =(long)(mEventManager.DayView_Date+(existRect.top/(float)(3*ViewHeight))*mEventManager.MillsInOneDay);
        long timeend = (long)(mEventManager.DayView_Date+(existRect.botton/(float)(3*ViewHeight))*mEventManager.MillsInOneDay);
        mEventManager.UpdateEvent(timebegin,timeend,existRect.rid);
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
        switch (OPERATIONMODE) {
            case SCREEN:
                if (distanceY > 0) {
                    if (getScrollY() < ScrollEndY) {
                        if (ScrollEndY - getScaleY() < Math.abs(distanceY)) scrollTo(0, ScrollEndY);
                        else scrollBy(0, (int) distanceY);
                    } else scrollTo(0, ScrollEndY);
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
                if((((existRect.top - (int) distanceY)>0)||distanceY<0)&&(((existRect.botton - (int) distanceY)<EndY)||distanceY>0)) {
                    ExistRectUpdate(existRect.top - (int) distanceY, existRect.botton - (int) distanceY);
                    if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                    if ((endy < 100 && getScrollY() > 0) || (endy > ViewHeight - 100 && getScrollY() < ScrollEndY)) {
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
                    if ((endy < 100 && getScrollY() > 0) || (endy > ViewHeight - 100 && getScrollY() < ScrollEndY)) {
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
                    if ((endy < 100 && getScrollY() > 0) || (endy > ViewHeight - 100 && getScrollY() < ScrollEndY)) {
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
                    if (getScrollY() < ScrollEndY) {
                        if (ScrollEndY - getScaleY() > AutoScrollMove) {
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
                    if (getScrollY() > 0) {
                        if (getScaleY() < AutoScrollMove) {
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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(OPERATIONMODE == EXIST || OPERATIONMODE == CONSUME_EXIST ||OPERATIONMODE ==EXIST_DOWN ||OPERATIONMODE ==EXIST_TOP){
            if(Math.abs(velocityX)>3000 ||Math.abs(velocityY)>3000 ){
                deleteEvent(existRect.rid);
                mEventManager.removeEvent(existRect.rid);
                normalEventListener.deleteRecord(existRect.rid);
                ClearExistRect();
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(OPERATIONMODE == CONSUME_EXIST) {
                gestureDetector.setIsLongpressEnabled(true);
                OPERATIONMODE = EXIST;
                return true;
            }else if(OPERATIONMODE == EXIST_TOP|| OPERATIONMODE == EXIST_DOWN) OPERATIONMODE = EXIST;
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

        @Override
        protected void onDraw(Canvas canvas) {
            if(tempRect.exist){
                canvas.drawRect(tempRect.left,tempRect.top,tempRect.right,tempRect.botton,mSurface.tempRectPaint);
            }
            if(existRect.exist){
                canvas.drawRect(existRect.left,existRect.top,existRect.right,existRect.botton,mSurface.ExistRectPaint);
            }
        }
    }

    private class Surface {

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
        long rid=0;
        boolean exist=true;
    }
}