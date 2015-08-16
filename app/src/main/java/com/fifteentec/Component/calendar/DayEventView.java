package com.fifteentec.Component.calendar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Handler;

import android.widget.TextView;
import android.widget.Toast;

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

    private Context mcontext;
    private GregorianCalendar mDate;
    private List<String> AllDayEvent = new ArrayList<>(Arrays.asList(
            "AllDay1",
            "AllDay2",
            "AllDay3",
            "AllDay4",
            "AllDay5",
            "AllDay6",
            "AllDay7"
            ));
    private List<String> NormalEvnet = new ArrayList<>();
    private ArrayList<int[]> NormalEvnetPosition = new ArrayList<>();

    private int mScreenWidth;
    private int mScreenHeight;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public static DayEventView newInstance(Context context,GregorianCalendar today){
        DayEventView dayEventView = new DayEventView(context);

        dayEventView.init(today);
        return dayEventView;
    }

    public DayEventView(Context context) {
        this(context,null);
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

        mBackgroundView = new View(mcontext);
        mBackgroundView.setBackgroundColor(mSurface.BackgoundColor);
        mBackgroundView.setAlpha(mSurface.BackgroundAlpha);
        addView(mBackgroundView);

        mPageView = new ImageView(mcontext);
        mPageView.setBackgroundColor(mSurface.PageColor);
        addView(mPageView);

        mDateView = new TextView(mcontext);
        String dateShow = mDate.get(Calendar.YEAR)+"年"+(mDate.get(Calendar.MONTH)+1)+"月"+mDate.get(Calendar.DAY_OF_MONTH)+"日";
        mDateView.setText(dateShow);
        mDateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSurface.TextSize);
        addView(mDateView);

        mAllDayView = new AllDayView(mcontext);
        addView(mAllDayView);

        mNormalEvent = new NormalEvent(mcontext);
        addView(mNormalEvent);

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);



        int SidePadding = (int)(mScreenWidth*mSurface.SidePadding);
        int UpdownPadding =(int)(mScreenHeight*mSurface.UpdownPadding);


        if(mBackgroundView!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mScreenWidth,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mScreenHeight,MeasureSpec.EXACTLY);
            mBackgroundView.measure(widthSpec, heightSpec);
        }

        if(mDateView != null){
            LayoutParams lp = mDateView.getLayoutParams();
            int widthSpec = MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.TextHeight,MeasureSpec.EXACTLY);
            mDateView.measure(widthSpec,heightSpec);
        }


        if(mPageView!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec(mScreenWidth-2*SidePadding,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mScreenHeight-2*UpdownPadding,MeasureSpec.EXACTLY);
            mPageView.measure(widthSpec, heightSpec);
        }

        int AllDayWidth =(int) ((mScreenWidth -2*(SidePadding+mSurface.PageSidePadding))*mSurface.AllDayViewWidth);
        int AllDayHeight = mScreenHeight -2*(UpdownPadding+mSurface.PageUpdownPadding)-mSurface.TextHeight-mSurface.AllDayViewPadding;
        if(mAllDayView!= null){


            int widthSpec = MeasureSpec.makeMeasureSpec(AllDayWidth,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(AllDayHeight,MeasureSpec.EXACTLY);
            mAllDayView.measure(widthSpec, heightSpec);
        }

        int NormalEvnentWidth =(int) ((mScreenWidth -2*(SidePadding+mSurface.PageSidePadding))*(1-mSurface.AllDayViewWidth));
        if(mNormalEvent!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec(NormalEvnentWidth,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(AllDayHeight,MeasureSpec.EXACTLY);
            mNormalEvent.measure(widthSpec,heightSpec);
        }
               /*if(!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec();
            int heightSpec = MeasureSpec.makeMeasureSpec();
            .measure(widthSpec,heightSpec);
        }
               /*if(!= null){
            int widthSpec = MeasureSpec.makeMeasureSpec();
            int heightSpec = MeasureSpec.makeMeasureSpec();
            .measure(widthSpec,heightSpec);
        }

         */
        setMeasuredDimension(mScreenWidth,mScreenHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int SidePadding = (int)(mScreenWidth*mSurface.SidePadding);
        int UpdownPadding =(int)(mScreenHeight*mSurface.UpdownPadding);

        if( mBackgroundView!= null) mBackgroundView.layout(l,t,r,b);

        if( mDateView!= null) mDateView.layout(
                l+SidePadding+mSurface.PageSidePadding,
                t+UpdownPadding+mSurface.PageUpdownPadding,
                l+SidePadding+mSurface.PageSidePadding+mDateView.getMeasuredWidth(),
                t+UpdownPadding+mSurface.PageUpdownPadding+mDateView.getMeasuredHeight());


        if( mPageView!= null) {

            int left =l+SidePadding;
            int top =t+UpdownPadding;
            int right =r-SidePadding;
            int botton =b-UpdownPadding;
            mPageView.layout(left, top, right, botton);
        }

        if(mAllDayView != null) {

            int left =l+SidePadding+mSurface.PageSidePadding;
            int top =t+UpdownPadding+mSurface.PageUpdownPadding*2+mSurface.TextHeight;
            int right =left+mAllDayView.getMeasuredWidth();
            int botton =top +mAllDayView.getMeasuredHeight();
            mAllDayView.layout(left,top,right,botton);
        }

        if( mNormalEvent!= null) {

            int right =r-SidePadding-mSurface.PageSidePadding;
            int left =right-mNormalEvent.getMeasuredWidth();
            int top =t+UpdownPadding+mSurface.PageUpdownPadding*2+mSurface.TextHeight;
            int botton =top+mNormalEvent.getMeasuredHeight();
            mNormalEvent.layout(left, top, right,botton);
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

    private class Surface{


        float  SidePadding=(1/20f);
        float UpdownPadding =(1/20f);

        int BackgoundColor = Color.BLACK;
        float BackgroundAlpha = 0.5f;

        int TextHeight = 100;
        int TextSize = 25;

        int PageSidePadding = 50;
        int PageUpdownPadding =50;
        int PageColor = Color.WHITE;

        float AllDayViewWidth = (1/4f);
        int AllDayItemHeight =100;
        int AllDayViewPadding=50;

        Paint LinePaint;
        int LineColor = Color.BLACK;
        int LineWidth = 3;
        int LinePadding =80;

        Paint NormalText;
        int NormalEventViewDivid =8;
        int NormalEvnetLineLength =100;
        int NormalEvnetTextColor=Color.BLACK;
        int NormalEvnetTextSize =50;

        Paint tempRectPaint;
        int tempRectColor=Color.BLUE;
        int tempRectWidth = 10;

        Paint NormalEventPaint;
        int NormalEventColor = Color.GRAY;

        public Surface(){
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
        }

    }

    private class AllDayView extends ViewGroup implements GestureDetector.OnGestureListener{

        int[] tempRect = new int[5];
        int FullHeight=0;
        int DownChildId = -1;

        GestureDetector mgestureDetector;
        public AllDayView(Context context) {
            this(context, null);
        }

        public AllDayView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public AllDayView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initAllDay();
        }

        private void initAllDay() {
            mgestureDetector = new GestureDetector(mcontext,this);
            for(int i =0;i<AllDayEvent.size();i++){
                tempRectView tx = new tempRectView(mcontext);
                String item = AllDayEvent.get(i);
                tx.setText(item);
                tx.setTag(i);
                addView(tx);
            }
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            for(int i =0;i<getChildCount();i++){
                View temp = getChildAt(i);
                int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.AllDayItemHeight, MeasureSpec.EXACTLY);
                temp.measure(widthSpec, heightSpec);
                FullHeight +=mSurface.AllDayItemHeight;
            }

            setMeasuredDimension(width, height);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int height =0;
            for (int i =0;i<getChildCount();i++){
                View temp = getChildAt(i);
                temp.layout(0, height, temp.getMeasuredWidth(), height + temp.getMeasuredHeight());
                height +=temp.getMeasuredHeight();
            }
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) DownChildId = -1;
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
            Toast.makeText(mcontext,""+DownChildId,Toast.LENGTH_SHORT).show();
            if(DownChildId != -1){
                tempRectView temp = (tempRectView)getChildAt(DownChildId);
                Toast.makeText(mcontext,temp.getText(),Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if(DownChildId != -1){
                removeViewAt(DownChildId);
                AllDayEvent.remove(DownChildId);
                for(int i = 0;i<getChildCount();i++){
                    View temp = getChildAt(i);
                    int id = (int)temp.getTag();
                    if(id >DownChildId){
                        temp.setTag(id-1);
                    }
                }
            }else{
                String a = "AllDay"+AllDayEvent.size();
                AllDayEvent.add(a);
                tempRectView tx = new tempRectView(mcontext);
                tx.setText(a);
                tx.setTag(getChildCount());
                addView(tx);

            }

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        private class tempRectView extends TextView{

            int id;
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
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    DownChildId = (int)getTag();
                }
                Log.d("Test","Child Down");
                return false;
            }
        }
    }

    private class NormalEvent extends ViewGroup implements GestureDetector.OnGestureListener{

        int CellHeight;
        int EndY;
        int ScrollEndY;
        int ViewWidth;
        int ViewHeight;

        int TesTNum = 0;

        int[] tempRect = new int[5];

        private GestureDetector gestureDetector;
        private tempRectView mtempRectView;



        final int TEMPRECT =0x00;
        final int SCREEN =0x01;
        final int NONE = 0x02;
        final int AUTO_SCROLL_DOWN = 0x03;
        final int AUTO_SCROLL_UP = 0x04;
        int OPERATIONMODE =NONE;

        int AutoScrollEnd =0;
        int AutoScrollMove =100;


        Handler handler = new Handler(){
            public void handleMessage(Message ms){
                switch (ms.what){
                    case AUTO_SCROLL_DOWN:
                        tempRectUpdate(tempRect[1]+AutoScrollMove,tempRect[3]+AutoScrollMove);
                        break;
                    case AUTO_SCROLL_UP:
                        tempRectUpdate(tempRect[1]-AutoScrollMove,tempRect[3]-AutoScrollMove);
                        break;

                }
                super.handleMessage(ms);
            }
        };

        public NormalEvent(Context context) {
            this(context, null);
        }

        public NormalEvent(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public NormalEvent(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initNormal();
        }

        private void initNormal() {
            setWillNotDraw(false);
            mtempRectView = new tempRectView(mcontext);
            addView(mtempRectView);
            gestureDetector = new GestureDetector(getContext(),this);
            tempRect[4] = -1;
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
            setMeasuredDimension(width, height);
        }



        @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawLine(mSurface.LinePadding, 0, mSurface.LinePadding, EndY, mSurface.LinePaint);
            for(int i = 0;i<NormalEvnetPosition.size();i++){
                int[] temp = NormalEvnetPosition.get(i);
                canvas.drawRect(temp[0], temp[1], temp[2], temp[3], mSurface.NormalEventPaint);
                canvas.drawText(NormalEvnet.get(i), (temp[0] + temp[2]) / 2, (temp[1] + temp[3]) / 2, mSurface.NormalText);

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





        private void addNewEvent(){
            addNewEvent(TesTNum);
            TesTNum++;
        }
        private void addNewEvent(int num) {
            NormalEvnet.add("" + num);
            List<Integer> ChangeList = new ArrayList<>();
            int[] a =new int[4];
            a[0] = mSurface.LinePadding;
            a[1] = tempRect[1];
            a[2] = ViewWidth;
            a[3] = tempRect[3];
            NormalEvnetPosition.add(a);
            ChangeList.add(NormalEvnetPosition.size() - 1);
            boolean isChanged ;
            int Head = tempRect[3];
            int Tail = tempRect[1];
            do {
                int lengthNew = Head-Tail;
                isChanged = false;
                for (int j = 0; j < NormalEvnetPosition.size(); j++) {
                    int[] temp = NormalEvnetPosition.get(j);
                    int lengthOld = temp[3] - temp[1];
                    if (Math.max(Math.abs(temp[3] - Tail), Math.abs(Head - temp[1])) < (lengthNew + lengthOld) - 1) {
                        boolean isSame = false;
                        for(int i =0;i<ChangeList.size();i++) {
                            if(ChangeList.get(i)==j) {
                                isSame = true;
                                break;
                            }
                        }
                        if(!isSame) {
                            if(temp[3]>Head) Head = temp[3];
                            if(temp[1]<Tail) Tail = temp[1];
                            ChangeList.add(j);
                            isChanged = true;
                        }
                    }
                }

            }while (isChanged);


            RerangeList(ChangeList);
            CleartempRect();
            invalidate();

        }

        private void deleteEvent(int index){
            int[] aim = NormalEvnetPosition.get(index);
            List<Integer> ChangeList = new ArrayList<>();
            boolean isChanged;
            int Head = aim[3];
            int Tail = aim[1];
            NormalEvnet.remove(index);
            NormalEvnetPosition.remove(index);
            do {
                int lengthNew = Head-Tail;
                isChanged = false;
                for (int j = 0; j < NormalEvnetPosition.size(); j++) {
                    int[] temp = NormalEvnetPosition.get(j);
                    int lengthOld = temp[3] - temp[1];
                    if (Math.max(Math.abs(temp[3] - Tail), Math.abs(Head - temp[1])) < (lengthNew + lengthOld) - 1) {
                        boolean isSame = false;
                        for(int i =0;i<ChangeList.size();i++) {
                            if(ChangeList.get(i)==j) {
                                isSame = true;
                                break;
                            }
                        }
                        if(!isSame) {
                            if(temp[3]>Head) Head = temp[3];
                            if(temp[1]<Tail) Tail = temp[1];
                            ChangeList.add(j);
                            isChanged = true;
                        }
                    }
                }
            }while (isChanged);


            RerangeList(ChangeList);
            invalidate();

        }

        private void RerangeList(List<Integer> ChangeList){
            ArrayList<Integer> HandleList = new ArrayList<>();
            ArrayList<Integer> ModifList = new ArrayList<>();
            int divide = 0;

            while(ChangeList.size()!=0){
                int getIndex =0;
                int[] hand = NormalEvnetPosition.get(ChangeList.get(0));
                for(int i =0;i<ChangeList.size();i++){
                    int[] temp = NormalEvnetPosition.get(ChangeList.get(i));
                    if(temp[1]<hand[1]){
                        hand =temp;
                        getIndex =i;
                    }
                }

                int behind =-1;
                int lengthhand = hand[3]-hand[1];

                for(int i =0;i<HandleList.size();i++){
                    int[] temp = NormalEvnetPosition.get(HandleList.get(i));
                    int lengthtemp = temp[3]-temp[1];
                    if(Math.max(Math.abs(temp[3] - hand[1]), Math.abs(hand[3])-temp[1])>(lengthtemp+lengthhand)-1){
                        behind = i;
                        break;
                    }
                }

                if(behind == -1){
                    divide ++;
                    int dividePart = (ViewWidth-mSurface.LinePadding)/divide;
                    for(int i =0;i<ModifList.size()/2;i++){
                        int[] temp = NormalEvnetPosition.get(ModifList.get(i*2));
                        temp[0] = mSurface.LinePadding+dividePart*(ModifList.get(i*2+1)-1);
                        temp[2] = temp[0] +dividePart;
                    }

                    hand[0] = mSurface.LinePadding+dividePart*(divide-1);
                    hand[2] = hand[0] +dividePart;

                    ModifList.add(ChangeList.get(getIndex));
                    ModifList.add(divide);

                    HandleList.add(ChangeList.get(getIndex));
                }else{
                    int[] temp = NormalEvnetPosition.get(HandleList.get(behind));
                    hand[0] =temp[0];
                    hand[2] =temp[2];
                    ModifList.add(ChangeList.get(getIndex));
                    ModifList.add(behind + 1);
                    HandleList.remove(behind);
                    HandleList.add(behind, ChangeList.get(getIndex));

                }


                ChangeList.remove(getIndex);
            }

        }
        private void CleartempRect() {
            tempRect[0] = 0;
            tempRect[1] =0;
            tempRect[2] = 0;
            tempRect[3] = 0;
            tempRect[4] = -1;
            mtempRectView.invalidate();
        }

        private void tempRectUpdate(float y) {
            int head = findIndex(y);
            tempRect[0] = mSurface.LinePadding;
            tempRect[1] = head*CellHeight;
            tempRect[2] = ViewWidth;
            tempRect[3] = (head+1)*CellHeight;
            tempRect[4] = -1;
            mtempRectView.invalidate();

        }

        private void tempRectUpdate(int start,int end){
            tempRect[0] = mSurface.LinePadding;
            tempRect[1] = start;
            tempRect[2] = ViewWidth;
            tempRect[3] = end;
            mtempRectView.invalidate();

        }
        private void tempRectUpdate(int start,int end,int num) {

            tempRect[4] =num;
            tempRectUpdate(start, end);


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
                if((positionY > tempRect[1]) && (positionY < tempRect[3])) return true;
            }
            OPERATIONMODE = SCREEN;
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

                int i = isExist(positionY, positionX);
                if (i == -1) {
                    tempRectUpdate(positionY);
                    OPERATIONMODE=TEMPRECT;

                } else {
                    CleartempRect();
                    Toast.makeText(mcontext, "Show detail of " + i, Toast.LENGTH_SHORT).show();
                }
                return true;
            }else if(OPERATIONMODE==TEMPRECT){
                OPERATIONMODE =NONE;
                if(tempRect[4] == -1) addNewEvent();
                else addNewEvent(tempRect[4]);
                return true;
            }
            return false;
        }

        private int isExist(int positionY,int positionX) {
            for(int i = 0;i<NormalEvnetPosition.size();i++){
                int[] temp = NormalEvnetPosition.get(i);
                if((temp[1]<positionY)&&
                        (temp[3]>positionY)&&
                        (temp[0]<positionX)&&
                        (temp[2]>positionX))
                    return i;
            }
            return -1;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
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
                case TEMPRECT:
                    final int endy = (int)e2.getY();
                    tempRectUpdate(tempRect[1] -(int)distanceY,tempRect[3] - (int)distanceY);
                    if(distanceY*AutoScrollEnd<0) AutoScrollEnd =0;
                    if((endy <100&&getScrollY()>0) || (endy > ViewHeight-100&&getScrollY()<ScrollEndY)){
                        if(AutoScrollEnd == 0) {
                            if (endy < 100) AutoScrollEnd = -1;
                            if (endy > ViewHeight - 100) AutoScrollEnd = 1;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("Test", "run id"+getId());
                                    while (AutoScrollEnd != 0) {
                                        Log.d("Test", "runnable");
                                        if (AutoScrollEnd > 0) {
                                            if (getScrollY() < ScrollEndY) {
                                                if (ScrollEndY - getScaleY() > AutoScrollMove) {
                                                    scrollBy(0, AutoScrollMove);
                                                    handler.sendEmptyMessage(AUTO_SCROLL_DOWN);
                                                }
                                                else {
                                                    scrollTo(0, ScrollEndY);
                                                    break;
                                                }
                                            }else {
                                                scrollTo(0, ScrollEndY);
                                                break;
                                            }
                                        } else {
                                            if (getScrollY() > 0) {
                                                if (getScaleY() < AutoScrollMove) {
                                                    scrollBy(0, -AutoScrollMove);
                                                    handler.sendEmptyMessage(AUTO_SCROLL_UP);
                                                }
                                                else {
                                                    scrollTo(0, 0);
                                                    break;
                                                }
                                            }else{
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
                            }).start();
                        }

                    }

                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onLongPress(MotionEvent e) {
            int positionX = (int)e.getX();
            int positionY = (int)e.getY()+getScrollY();
            int i =isExist(positionY,positionX);
            if(!(i==-1)){
                int[] temp = NormalEvnetPosition.get(i);
                tempRectUpdate(temp[1], temp[3], Integer.valueOf(NormalEvnet.get(i)));
                deleteEvent(i);
                invalidate();
                OPERATIONMODE = TEMPRECT;

            }
            MotionEvent ev = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, e.getX(), e.getY(), 0);
            dispatchTouchEvent(ev);

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP){
                AutoScrollEnd = 0;
            }
            return gestureDetector.onTouchEvent(event);
        }



        private class tempRectView extends View{

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
                if(!((tempRect[0]==tempRect[2])&&(tempRect[1]==tempRect[3]))){
                    canvas.drawRect(tempRect[0],tempRect[1],tempRect[2],tempRect[3],mSurface.tempRectPaint);
                }
            }
        }
    }
}
