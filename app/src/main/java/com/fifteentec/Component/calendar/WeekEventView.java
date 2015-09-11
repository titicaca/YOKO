package com.fifteentec.Component.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.Database.EventRecord;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
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

    private int AutoScrollMove = 100;
    private int StrechMinDistance = 30;
    private int StrechMinHeight = CellHeight/2;
    private tempRectCanvas mTempRectCanvas;

    private GestureDetector gestureDetector;

    private int AutoScrollEnd = 0;

    private ArrayList<ArrayList<EventItem>> EventArray = new ArrayList<>();

    private WeekViewListener mWeekViewListener;

    private int pressOffset=0;

    private boolean ViewEndable=true;

    public interface WeekViewListener{
        void CheckExistItem(long rid);
        void CreatRecord(int Type,EventRecord eventRecord);
        void CalEnable(boolean enable);
    }

    public void setmWeekViewListener(WeekViewListener mWeekViewListener) {
        this.mWeekViewListener = mWeekViewListener;
    }

    public void setViewEnable(boolean enable){
        ViewEndable = enable;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if (firstEntry) {
            ScreenHeight =height;
            ScreenWidth =width;
            CellHeight = (int)(height*mSurface.ViewHeightRatio)/mSurface.dividePart;
            mSurface.initSurface();

            CellWidth = mSurface.ViewWidth/CalUtil.LENTH_OF_WEEK;
            firstEntry = false;
            UpdateEventArray();
        }

        if(mTempRectCanvas != null){
            int WidthSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
            int HeightSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
            mTempRectCanvas.measure(WidthSpec, HeightSpec);
        }


        setMeasuredDimension(width, mSurface.ViewHeight);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mTempRectCanvas != null) mTempRectCanvas.layout(0,0,ScreenWidth,mSurface.EndHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = 0;
        Rect rectText = new Rect();
        mSurface.mEventTextPaint.getTextBounds("啊",0,1,rectText);
        for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
            EventManager eventManager = mEventManager.getEventMangerInDayOfWeek(i);
            for(int j = 0;j<eventManager.getNormalDayEventCount();j++){
                EventItem eventItem = EventArray.get(i).get(j);
                mSurface.ChangePaintColor(eventManager.getNormalType(j));
                canvas.drawRect(i * CellWidth + mSurface.TimeTextWidthInt + eventItem.left, eventItem.top, i * CellWidth + mSurface.TimeTextWidthInt + eventItem.right-CellWidth*mSurface.mExistGapRatio, eventItem.botton-CellWidth*mSurface.mExistGapRatio, mSurface.mNormalEventPaint);

                ArrayList<String> a =getSubString(eventManager.getNormalIntroduction(j),eventItem.botton-eventItem.top,eventItem.right-eventItem.left);
                if(eventItem.right-eventItem.left>= CellWidth) {
                    canvas.drawRect(i * CellWidth + mSurface.TimeTextWidthInt + rectText.width() * 2 / 3f + eventItem.left, eventItem.botton - rectText.height() * 3 / 2f * (a.size() + 1) + rectText.height() / 2, i * CellWidth + mSurface.TimeTextWidthInt + rectText.width(), eventItem.botton - rectText.height() + eventItem.left, mSurface.mExistItemRect);
                    for (int k = 0; k < a.size(); k++) {
                        canvas.drawText(a.get(k), rectText.width() * (7 / 4f) + i * CellWidth + mSurface.TimeTextWidthInt + eventItem.left, eventItem.botton - rectText.height() * 3 / 2f * (k + 1), mSurface.mEventTextPaint);
                    }
                }else{
                    for (int k = 0; k < a.size(); k++) {
                        canvas.drawText(a.get(k), i * CellWidth + mSurface.TimeTextWidthInt + eventItem.left, eventItem.botton - rectText.height() * 3 / 2f * (k + 1), mSurface.mEventTextPaint);
                    }
                }

            }
        }

        Rect rect = new Rect();
        int height = 0;
        for (int i = 0; i < 13; i++) {
            String TimeText ;
            if(i==0) TimeText = "12";
            else if(i<5) TimeText = "0"+2*i;
            else if(i<7&&i>4) TimeText=""+2*i;
            else if(i>6&&i<11) TimeText ="0"+2*(i-6);
            else TimeText = ""+2*(i-6);
            String smallText;
            if(i<6||i==12) smallText = "am";
            else smallText = "pm";



            mSurface.mTextPaint.getTextBounds(TimeText, 0, TimeText.length(), rect);

            canvas.drawText(TimeText, mSurface.TimeTextWidthInt / 3 - rect.width() / 2, height - CellHeight * 3 / 20 + rect.height() / 2, mSurface.mTextPaint);
            canvas.drawText(smallText, mSurface.TimeTextWidthInt *9/17, height - CellHeight *3/ 20 + rect.height() / 2, mSurface.mTextSmallPaint);
            canvas.drawLine(mSurface.TimeTextWidthInt, height,mSurface.TimeTextWidthInt+mSurface.ViewWidth, height, mSurface.mLinePaint);
            height +=2*CellHeight;
        }

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

    private ArrayList<String> getSubString(String s,int height,int width){
        int lenth = s.length();
        Rect rect = new Rect();
        mSurface.mEventTextPaint.getTextBounds("啊", 0, 1, rect);
        int hsize = height/(rect.height()*3/2)-1;
        int wsize =width/rect.width();
        int TextHeightSize;
        if(width >= CellWidth) {
            wsize-=2;
            if (lenth / wsize > hsize) TextHeightSize = hsize;
            else {
                TextHeightSize = lenth / wsize;
                if (lenth % wsize != 0&&TextHeightSize != hsize)
                    TextHeightSize++;

            }
        }else {
            if(wsize<=0) TextHeightSize = 0;
            else {
                if (lenth / wsize > hsize) TextHeightSize = hsize;
                else {
                    TextHeightSize = lenth / wsize;
                    if (lenth % wsize != 0&&TextHeightSize != hsize)
                        TextHeightSize++;

                }
            }
        }

        ArrayList<String> result = new ArrayList<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < TextHeightSize; i++) {
            do {
                if(end == s.length()) break;
                end ++;
                mSurface.mEventTextPaint.getTextBounds(s.substring(start, end),0,s.substring(start, end).length(),rect);
            }while((width < CellWidth&&rect.width()<width)||(width >= CellWidth&&rect.width()<width-rect.width()));
            if(s.length() !=end || (width < CellWidth&&rect.width()>width)||(width >= CellWidth&&rect.width()>width-rect.width()))end--;
            result.add(0,s.substring(start, end));
            start = end;
        }

        return result;

    }


    public void UpdateViewByTime(ArrayList<Integer> date){
        StartDate = new GregorianCalendar(date.get(0),date.get(1),date.get(2),0,0);
        TodayTimeInMills = StartDate.getTimeInMillis();
        mEventManager = EventManager.newInstance(((BaseActivity) mContext).getDBManager().getTableEvent(), EventManager.WEEK_VIEW_EVENT_MANAGER, TodayTimeInMills);
        UpdateEventArray();
        ClearExistRect();
        CleartempRect();
    }

    public void UpdateView(long rid){
        mEventManager.deleteByRid(rid);
        UpdateView(rid,true);
    }
    public void UpdateView(long rid,boolean exist){
        if(!exist) mEventManager.addEvent(rid);
        UpdateEventArray();
        CleartempRect();
        ClearExistRect();
        invalidate();
    }
    public void initView(ArrayList<Integer> date){
        setBackgroundColor(Color.WHITE);
        StartDate = new GregorianCalendar(date.get(0),date.get(1),date.get(2),0,0);
        setWillNotDraw(false);
        mSurface = new Surface();
        TodayTimeInMills = StartDate.getTimeInMillis();
        mEventManager = EventManager.newInstance(((BaseActivity) mContext).getDBManager().getTableEvent(), EventManager.WEEK_VIEW_EVENT_MANAGER, TodayTimeInMills);
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
                eventItem.top = (int)((24/mSurface.dividePart)*mSurface.ViewHeight*mEventManager.getEventMangerInDayOfWeek(i).getPositionRatioByTime(mEventManager.getEventMangerInDayOfWeek(i).getNormalEventByIndex(j).timebegin));
                eventItem.botton= (int)((24/mSurface.dividePart)*mSurface.ViewHeight*mEventManager.getEventMangerInDayOfWeek(i).getPositionRatioByTime(mEventManager.getEventMangerInDayOfWeek(i).getNormalEventByIndex(j).timeend));
                eventItem.colum = i;
                addNewEvent(eventItem);
            }
        }
        invalidate();
    }

    public void addNewEvent(EventItem eventItem) {
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
        for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {//遍历一周七个事件库获取要删除的事件

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
            ArrayList<List<Integer>> ChangeLists = new ArrayList<>();
            List<Integer> ChangeList = new ArrayList<>();
            boolean isChanged;
            int Head = aim.botton;
            int Tail = aim.top;
            do {
                int lengthNew = Head - Tail;
                isChanged = false;
                for (int j = 0; j < arrayList.size(); j++) {
                    EventItem temp = arrayList.get(j) ;
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

            if(ChangeList.size()!=0) {
                do {
                    EventItem tempNext=arrayList.get(ChangeList.get(0));
                    int NextTail = tempNext.top;
                    int index = 0;
                    for (int i = 0; i < ChangeList.size(); i++) {
                        EventItem tempfor= arrayList.get(ChangeList.get(i));
                        if(tempfor.top<tempNext.top){
                            tempNext = tempfor;
                            NextTail = tempNext.top;
                            index = i;
                        }
                    }
                    int NextHead = tempNext.botton;
                    int lengthNew = NextHead - NextTail;
                    boolean isDelete = false;
                    for (int i = 0; i < ChangeLists.size(); i++) {
                        for (int j = 0; j < ChangeLists.get(i).size(); j++) {
                            EventItem temp = arrayList.get(ChangeLists.get(i).get(j));
                            int lengthOld = temp.botton - temp.top;
                            if (Math.max(Math.abs(temp.botton - NextTail), Math.abs(NextHead - temp.top)) < (lengthNew + lengthOld) - 1) {
                                ChangeLists.get(i).add(ChangeList.get(index));
                                ChangeList.remove(index);
                                isDelete = true;
                                break;
                            }
                        }
                        if(isDelete) break;
                    }

                    if(!isDelete) {
                        List<Integer> Lists = new ArrayList<>();
                        Lists.add(ChangeList.get(index));
                        ChangeList.remove(index);
                        ChangeLists.add(Lists);
                    }
                } while (ChangeList.size() != 0);

                for (int i = 0; i < ChangeLists.size(); i++) {
                    RerangeList(ChangeLists.get(i), Before_colum);
                }
                invalidate();
            }
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

    public void UpdateScale(){
        firstEntry = true;
        scrollTo(0, 0);
        invalidate();
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

    private void tempRectUpdate(int start,int end,float x){
        int[] head = findIndex(x,start);
        tempRect.left =0;
        tempRect.top = start;
        tempRect.right =CellWidth;
        tempRect.botton = end;
        tempRect.exist =true;
        tempRect.colum = head[0];
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
        if(IndexX>6){
            IndexX=6;
        }else if(IndexX<0){
            IndexX = 0;
        }
        int[] result = new int[2];
        result[0] = IndexX;
        result[1] = Index;
        return result;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!ViewEndable) return false;
        if(event.getAction() == MotionEvent.ACTION_UP){
            switch (OPERATIONMODE){
                case TEMPRECT:
                    int positionTop = tempRect.top;
                    int positionBotton = tempRect.botton;
                    long offsetTop = Math.round((positionTop / (double) (mSurface.EndHeight) * mEventManager.MillsInOneDay));
                    long offsetBotton = Math.round((positionBotton/(double)(mSurface.EndHeight)*mEventManager.MillsInOneDay));
                    EventRecord eventRecord = new EventRecord();
                    eventRecord.timebegin = mEventManager.getEventMangerInDayOfWeek(tempRect.colum).DayView_Date+offsetTop;
                    eventRecord.timeend = mEventManager.getEventMangerInDayOfWeek(tempRect.colum).DayView_Date+offsetBotton;

                    mWeekViewListener.CreatRecord(NewEventView.HAVE_TIME, eventRecord);
                    gestureDetector.setIsLongpressEnabled(true);
                    CleartempRect();
                    mWeekViewListener.CalEnable(true);
                    OPERATIONMODE = SCREEN;
                    return true;
                case CONSUME_EXIST:
                    gestureDetector.setIsLongpressEnabled(true);
                    OPERATIONMODE = EXIST;
                    return true;
                case EXIST_DOWN:
                case EXIST_TOP:
                    OPERATIONMODE = EXIST;
                case EXIST:

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
                    existRect.timeBegin = mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+Math.round((newTop/(double)mSurface.EndHeight)*mEventManager.MillsInOneDay);
                    existRect.timeEnd =mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+Math.round((newEnd/(double)mSurface.EndHeight)*mEventManager.MillsInOneDay);



                    ExistRectUpdate((int)Math.round(newTop),(int)Math.round(newEnd));
                    pressOffset = 0;
                    Log.d("Test","ExistUp Top:"+existRect.top+"Exist Botton:"+existRect.botton);
                    break;

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
            //if((positionY > tempRect.top) && (positionY < tempRect.botton)&&(positionX>tempRect.left+tempRect.colum*CellWidth)&&(positionX<tempRect.right+(tempRect.colum+1)*CellWidth)) return true;

            return true;
        }else if(OPERATIONMODE == EXIST){
            //if((positionX<existRect.right+existRect.colum*CellWidth)&&(positionX>existRect.left+existRect.colum*CellWidth)) {
            if((positionX<existRect.right+existRect.colum*CellWidth)) {
                if (Math.abs(positionY - existRect.top) < StrechMinDistance) {
                    OPERATIONMODE = EXIST_TOP;
                    return true;
                } else if (Math.abs(positionY - existRect.botton) < StrechMinDistance) {
                    OPERATIONMODE = EXIST_DOWN;
                    return true;
                } else if ((positionY > existRect.top && (positionY < existRect.botton))){
                    pressOffset = (int)((existRect.top+existRect.botton)/2-(e.getY()+getScrollY()));
                    return true;
                }
                //UpdateTimeByMove();
            }

            mEventManager.UpdateEvent(existRect.timeBegin, existRect.timeEnd, existRect.rid);
            addNewEvent(existRect);
            mEventManager.addEvent(existRect.rid);
        }

        mWeekViewListener.CalEnable(true);
        OPERATIONMODE = SCREEN;
        ClearExistRect();
        CleartempRect();
        invalidate();
        return true;
    }
    /*

    private void UpdateTimeByMove() {
        deleteEvent(existRect.rid);
        mEventManager.deleteByRid(existRect.rid);
        long timebegin = mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+(long)((existRect.top/(float)(mSurface.EndHeight)*mEventManager.MillsInOneDay));
        long timeend = mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+(long)((existRect.botton/(float)(mSurface.EndHeight)*mEventManager.MillsInOneDay));
        mEventManager.UpdateEvent(timebegin, timeend, existRect.rid);
        addNewEvent(existRect);
        mEventManager.addEvent(existRect.rid);

        ClearExistRect();
        invalidate();
    }
*/
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
                //tempRectUpdate(positionY,positionX,true);
                //OPERATIONMODE=TEMPRECT;
            } else {
                mWeekViewListener.CheckExistItem(i);
            }
            return true;
        }else if(OPERATIONMODE==TEMPRECT){

        }else if(OPERATIONMODE == EXIST|| OPERATIONMODE == EXIST_DOWN || OPERATIONMODE == EXIST_TOP){
            long timebegin = mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+Math.round(existRect.top / (double) (mSurface.EndHeight) * mEventManager.MillsInOneDay);
            long timeend = mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+Math.round(existRect.botton / (double) (mSurface.EndHeight) * mEventManager.MillsInOneDay);
            mEventManager.UpdateEvent(timebegin, timeend, existRect.rid);
            addNewEvent(existRect);
            mEventManager.addEvent(existRect.rid);
            mWeekViewListener.CheckExistItem(existRect.rid);


            ClearExistRect();
            invalidate();
            return false;
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
                    if (getScrollY() >  mSurface.ScrollTop) {
                        if (getScrollY() -mSurface.ScrollTop< Math.abs(distanceY)) scrollTo(0,  mSurface.ScrollTop);
                        else scrollBy(0, (int) distanceY);
                    } else scrollTo(0,  mSurface.ScrollTop);
                }
                return true;
            case CONSUME_EXIST:
                OPERATIONMODE = EXIST;
                gestureDetector.setIsLongpressEnabled(true);
            case EXIST:
                int TimeHeight = existRect.botton-existRect.top;
                if(endx>existRect.colum*CellWidth+existRect.left&&endx<existRect.colum*CellWidth+existRect.right) {

                    if(scrollY+pressOffset-TimeHeight/2<0) ExistRectUpdate(0,TimeHeight);
                    else if(scrollY+TimeHeight/2+pressOffset >mSurface.EndHeight) ExistRectUpdate(mSurface.EndHeight-TimeHeight,mSurface.EndHeight);
                    else {
                        ExistRectUpdate(scrollY - TimeHeight / 2+pressOffset, scrollY + TimeHeight / 2+pressOffset);
                        if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                        if ((endy < 100 && getScrollY() > 0) || (endy >mSurface.ViewHeight - 100 && getScrollY() < mSurface.ScrollEndY)) {// 如果滑块的边界碰触到了屏幕的边界,开始滚动.
                            if (AutoScrollEnd == 0) {
                                if (endy < 100) AutoScrollEnd = -1;
                                if (endy >  mSurface.ViewHeight - 100) AutoScrollEnd = 1;
                                AutoScrollThread mthread = new AutoScrollThread();
                                mthread.start();
                            }
                        }
                    }
                }else{
                    int index[] = findIndex(endx,endy);
                    if(scrollY-TimeHeight/2<0) ExistRectUpdate(0,TimeHeight,existRect.rid,index[0]);
                    else if(scrollY+TimeHeight/2 >mSurface.EndHeight) ExistRectUpdate(mSurface.EndHeight-TimeHeight,mSurface.EndHeight,existRect.rid,index[0]);
                    else ExistRectUpdate(scrollY-TimeHeight/2+pressOffset,scrollY+TimeHeight/2+pressOffset,existRect.rid,index[0]);
                }
                return true;
            case EXIST_TOP:
                if((existRect.botton-existRect.top>StrechMinHeight||distanceY>0)&& scrollY<existRect.botton-StrechMinHeight) {
                    ExistRectUpdate(existRect.top - (int) distanceY, existRect.botton);
                    if (distanceY * AutoScrollEnd < 0) AutoScrollEnd = 0;
                    if ((endy < 100 && getScrollY() > 0) || (endy > mSurface.ViewHeight - 100 && getScrollY() < mSurface.ScrollEndY)) {
                        if (AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -2;
                            if (endy > mSurface.ViewHeight - 100) AutoScrollEnd = 2;
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
                    if ((endy < 100 && getScrollY() > 0) || (endy >mSurface.ViewHeight - 100 && getScrollY() <  mSurface.ScrollEndY)) {
                        if (AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -3;
                            if (endy > mSurface.ViewHeight - 100) AutoScrollEnd = 3;
                            AutoScrollThread mthread = new AutoScrollThread();
                            mthread.start();
                        }
                    }
                }
                return true;
            case TEMPRECT:

                int Scrolly = endy+getScrollY();
                tempRectUpdate(Scrolly - CellHeight / 2, Scrolly+CellHeight/2,endx);
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
                        if (temp.rid == i) break;
                    }
                }
                if(temp == null) return;
                pressOffset = (temp.top+temp.botton)/2-positionY;
                ExistRectUpdate(temp.top, temp.botton, temp.rid, temp.colum);
                mWeekViewListener.CalEnable(false);
                OPERATIONMODE = CONSUME_EXIST;
                deleteEvent(existRect.rid);
                mEventManager.deleteByRid(existRect.rid);
                existRect.timeBegin = mEventManager.getEventMangerInDayOfWeek(existRect.colum).getEventRecordByRid(existRect.rid).timebegin;
                existRect.timeEnd = mEventManager.getEventMangerInDayOfWeek(existRect.colum).getEventRecordByRid(existRect.rid).timeend;
                gestureDetector.setIsLongpressEnabled(false);
                MotionEvent ev = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(), e.getY(), 0);
                dispatchTouchEvent(ev);


            }else{

                tempRectUpdate(positionY - CellHeight / 2, positionY + CellHeight / 2, positionX);
                mWeekViewListener.CalEnable(false);
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

    private String showOperation(){
        switch (OPERATIONMODE){
            case TEMPRECT:
                return  "temprect";
            case SCREEN:
                return "screen";
            case EXIST:
                return "exist";
            default:
                return "other";
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
        float TimeTextWidthRatio = 1/7f;
        float ViewWidthRightPaddingRatio = 1/30f;
        int TimeTextWidthInt;
        int EndHeight ;
        int ViewWidth;
        int ViewHeight;
        int dividePart = 12;
        int ScrollEndY;
        float ViewHeightRatio =1-1/9f-1/35f;
        int ScrollTop;


        Paint mLinePaint;
        int mLineColor = Color.parseColor("#aaaaaa");

        float mExistGapRatio = 1/20f;

        Paint mTextPaint;
        Paint mTextSmallPaint;
        int mTextColor = Color.BLACK;

        Paint mEventTextPaint;
        float mEventTextPaintSizeRatio =1/5f;


        float mTextSizeRatio =3/10f;
        float mTextSmallSizeRatio =1/6f;
        int mTextSize;
        Paint tempRectPaint;
        int mtempRectColor = Color.BLUE;
        Paint ExistRectPaint;
        int mExistRectPaint = Color.YELLOW;
        float ExistRectShadowRatio = 1/20f;
        Paint mExistItemRect;
        Paint DashlinePaint;
        int DashlintColor = Color.parseColor("#888888");
        float DashlineLength = 3/2f;

        int OtherEventColor = getResources().getColor(R.color.OtherEventColor);
        int StudyEventColor = getResources().getColor(R.color.StudyEventColor);
        int EntertainmentEventColor = getResources().getColor(R.color.EntertainEventColor);
        int WorkEventColor = getResources().getColor(R.color.WorkEventColor);

        Paint mNormalEventPaint;
        int mNormalEvnentColor = Color.parseColor("#cccccc");

        Paint BackgroundPaint ;
        int BackgroundColor = Color.parseColor("#f1f1f1");


        float DashlineWidth = 1/55f;
        float DashlineGap = 1/5f;

        Paint TemporalTimePaint;
        float TemporalTimePaintRatio =1/4f;

        double CellUnit = 4d;


        void initSurface(){
            TimeTextWidthInt = (int)(TimeTextWidthRatio*ScreenWidth);
            ViewWidth = (int)(ScreenWidth*(1-ViewWidthRightPaddingRatio)) - TimeTextWidthInt;
            ViewHeight = (int)(ScreenHeight*ViewHeightRatio)+1;
            ScrollEndY = CellHeight*24-ViewHeight+(int)(TimeTextWidthInt*mTextSmallSizeRatio);
            EndHeight = CellHeight*24;
            ScrollTop = -(int)(TimeTextWidthInt*mTextSizeRatio);
            mLinePaint = new Paint();
            mLinePaint.setColor(mLineColor);
            mLinePaint.setAntiAlias(true);
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(mTextColor);
            TemporalTimePaint = new Paint();
            TemporalTimePaint.setTextSize((int) (TimeTextWidthInt * TemporalTimePaintRatio));
            mEventTextPaint = new Paint();
            mEventTextPaint.setAntiAlias(true);
            mEventTextPaint.setTextSize((int) (mSurface.TimeTextWidthInt * mSurface.mEventTextPaintSizeRatio));
            mExistItemRect = new Paint();
            mExistItemRect.setColor(Color.WHITE);
            mSurface.mTextPaint.setTextSize((int) (mSurface.TimeTextWidthInt * mSurface.mTextSizeRatio));
            mTextSmallPaint = new Paint();
            mTextSmallPaint.setAntiAlias(true);
            mTextSmallPaint.setColor(mTextColor);
            mTextSmallPaint.setTextSize((int) (mSurface.TimeTextWidthInt * mSurface.mTextSmallSizeRatio));
            mTextSize = (int)(TimeTextWidthInt*mTextSizeRatio);
            mTextPaint.setTextSize(mTextSize);
            tempRectPaint = new Paint();
            tempRectPaint.setColor(mtempRectColor);
            tempRectPaint.setAntiAlias(true);
            tempRectPaint.setStyle(Paint.Style.FILL);
            ExistRectPaint = new Paint();
            ExistRectPaint.setAntiAlias(true);
            ExistRectPaint.setColor(mExistRectPaint);
            ExistRectPaint.setStyle(Paint.Style.FILL);
            ExistRectPaint.setShadowLayer(CellHeight * ExistRectShadowRatio * 3, CellHeight * ExistRectShadowRatio, CellHeight * ExistRectShadowRatio, Color.parseColor("#b5b5b5"));
            mNormalEventPaint = new Paint();
            mNormalEventPaint.setColor(mNormalEvnentColor);
            mNormalEventPaint.setAntiAlias(true);
            mNormalEventPaint.setStyle(Paint.Style.FILL);
            DashlinePaint = new Paint();
            DashlinePaint.setStyle(Paint.Style.STROKE);
            DashlinePaint.setStrokeWidth(TimeTextWidthInt*DashlineWidth);
            PathEffect effect = new DashPathEffect(new float[]{TimeTextWidthInt*DashlineGap,TimeTextWidthInt*DashlineGap/3},1);
            DashlinePaint.setPathEffect(effect);
            DashlinePaint.setColor(DashlintColor);

            BackgroundPaint = new Paint();
            BackgroundPaint.setColor(BackgroundColor);
            BackgroundPaint.setAntiAlias(true);

        }

        void ChangePaintColor(int type){
            switch (type){
                case 0:
                    mNormalEventPaint.setColor(WorkEventColor);
                    ExistRectPaint.setColor(WorkEventColor);
                    break;
                case 1:
                    mNormalEventPaint.setColor(StudyEventColor);
                    ExistRectPaint.setColor(StudyEventColor);
                    break;
                case 2:
                    mNormalEventPaint.setColor(EntertainmentEventColor);
                    ExistRectPaint.setColor(EntertainmentEventColor);
                    break;
                case 3:
                    mNormalEventPaint.setColor(OtherEventColor);
                    ExistRectPaint.setColor(OtherEventColor);
                    break;
                default:
                    mNormalEventPaint.setColor(mNormalEvnentColor);
                    ExistRectPaint.setColor(mNormalEvnentColor);
                    break;
            }
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
            initCanvas();
        }

        private void initCanvas(){
            this.setAlpha(6/8f);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            if(tempRect.exist){
                canvas.drawRect(mSurface.TimeTextWidthInt+tempRect.colum * CellWidth  +tempRect.left,
                        tempRect.top,
                        mSurface.TimeTextWidthInt+tempRect.colum * CellWidth +tempRect.right,
                        tempRect.botton,mSurface.tempRectPaint);
            }
            if(existRect.exist){
                canvas.drawRect(mSurface.TimeTextWidthInt+existRect.colum * CellWidth+existRect.left,
                        0,
                        mSurface.TimeTextWidthInt+existRect.colum * CellWidth +existRect.right,
                        mSurface.EndHeight,
                        mSurface.BackgroundPaint);
                Rect rectText = new Rect();
                mSurface.mEventTextPaint.getTextBounds("啊",0,1,rectText);
                mSurface.ChangePaintColor(mEventManager.getNormalType(existRect.rid));
                canvas.drawRect(mSurface.TimeTextWidthInt+existRect.colum * CellWidth+existRect.left,
                        existRect.top,
                        mSurface.TimeTextWidthInt+existRect.colum * CellWidth +existRect.right,
                        existRect.botton,
                        mSurface.ExistRectPaint);
                ArrayList<String> a =getSubString(mEventManager.getNormalIntroduction(existRect.rid),existRect.botton-existRect.top,existRect.right-existRect.left);
                if(existRect.right-existRect.left>= CellWidth) {
                    canvas.drawRect(mSurface.TimeTextWidthInt+existRect.colum* CellWidth + rectText.width() * 2 / 3f + existRect.left,
                            existRect.botton - rectText.height() * 3 / 2f * (a.size() + 1) + rectText.height() / 2,
                            existRect.colum * CellWidth+ rectText.width()+mSurface.TimeTextWidthInt,
                            existRect.botton - rectText.height() + existRect.left,
                            mSurface.mExistItemRect);
                    for (int k = 0; k < a.size(); k++) {
                        canvas.drawText(a.get(k), rectText.width() * (7 / 4f) + existRect.colum * CellWidth + existRect.left+mSurface.TimeTextWidthInt, existRect.botton - rectText.height() * 3 / 2f * (k + 1), mSurface.mEventTextPaint);
                    }
                }else{
                    for (int k = 0; k < a.size(); k++) {
                        canvas.drawText(a.get(k), mSurface.TimeTextWidthInt+existRect.colum * CellWidth  + existRect.left, existRect.botton - rectText.height() * 3 / 2f * (k + 1), mSurface.mEventTextPaint);
                    }
                }
                //canvas.drawLine(mSurface.TimeTextWidthInt+existRect.colum * CellWidth +existRect.right,existRect.top,mSurface.TimeTextWidthInt+existRect.colum * CellWidth +existRect.right+CellWidth*mSurface.DashlineLength,existRect.top,mSurface.DashlinePaint);
                //canvas.drawLine(mSurface.TimeTextWidthInt+existRect.colum * CellWidth+existRect.left,existRect.botton,mSurface.TimeTextWidthInt+existRect.colum * CellWidth+existRect.left-CellWidth*mSurface.DashlineLength,existRect.botton,mSurface.DashlinePaint);
                canvas.drawLine(0,existRect.top,mSurface.TimeTextWidthInt+existRect.colum * CellWidth+existRect.left,existRect.top,mSurface.DashlinePaint);
                canvas.drawLine(0, existRect.botton, mSurface.TimeTextWidthInt + existRect.colum * CellWidth + existRect.left, existRect.botton, mSurface.DashlinePaint);

                double unit = (double)CellHeight/mSurface.CellUnit;
                double newTop;
                if(existRect.top%unit<unit/2){
                    newTop = existRect.top-existRect.top%unit;
                }else{
                    newTop = existRect.top-existRect.top%unit+unit;
                }
                long timebegin = mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+Math.round((newTop/(double)mSurface.EndHeight)*mEventManager.MillsInOneDay);

                double newEnd;
                if(existRect.botton%unit<unit/2){
                    newEnd = existRect.botton-existRect.botton%unit;
                }else{
                    newEnd = existRect.botton-existRect.botton%unit+unit;
                }
                long timeend = mEventManager.getEventMangerInDayOfWeek(existRect.colum).DayView_Date+Math.round((newEnd/(double)mSurface.EndHeight)*mEventManager.MillsInOneDay);

                GregorianCalendar temp = new GregorianCalendar();
                temp.setTimeInMillis(timebegin);
                Rect rect = new Rect();
                String startTime;
                if( temp.get(Calendar.HOUR)<10) {
                    startTime = "0"+temp.get(Calendar.HOUR) + ":";
                }else{
                    startTime = temp.get(Calendar.HOUR) + ":" ;
                }
                if(temp.get(Calendar.MINUTE)<10){
                    startTime += "0"+temp.get(Calendar.MINUTE);
                }else {
                    startTime += temp.get(Calendar.MINUTE);
                }
                mSurface.TemporalTimePaint.getTextBounds(startTime,0,startTime.length(),rect);
                canvas.drawText(startTime,mSurface.TimeTextWidthInt/2-rect.width()/2,existRect.top,mSurface.TemporalTimePaint);
                temp.setTimeInMillis(timeend);
                String endTime;
                if( temp.get(Calendar.HOUR)<10) {
                    endTime = "0"+temp.get(Calendar.HOUR)+":";
                }else{
                    endTime = temp.get(Calendar.HOUR)+":";
                }
                if(temp.get(Calendar.MINUTE)<10){
                    endTime += "0"+temp.get(Calendar.MINUTE);
                }else {
                    endTime += temp.get(Calendar.MINUTE);
                }
                mSurface.TemporalTimePaint.getTextBounds(endTime,0,startTime.length(),rect);
                canvas.drawText(endTime, mSurface.TimeTextWidthInt/2-rect.width()/2, existRect.botton, mSurface.TemporalTimePaint);




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

        long timeBegin;
        long timeEnd;
    }
}
