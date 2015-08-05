package com.fifteentec.Component.calendar;


import android.content.Context;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;


public class EventListView extends ViewGroup{
    private Context mcontext ;
    private EventController mEvent;


    public EventListView(Context context) {
        super(context, null);
        mcontext = context;

    }

    public void init(ArrayList<Integer> date){

        //int height = this.getLayoutParams().height;
        //int itemHeight =0;
        GregorianCalendar temp = new GregorianCalendar(date.get(0),date.get(1),date.get(2));
        mEvent = new EventController(temp);
        temp.add(Calendar.DAY_OF_MONTH, -1);
        TextView mTx = new TextView(mcontext);
        mTx.setText(""+temp.get(Calendar.DAY_OF_MONTH));
        addView(mTx);
        ListView mLv = new ListView(mcontext);
        mLv.setAdapter(mEvent.getAdapter(temp));
        addView(mLv);
        temp.add(Calendar.DAY_OF_MONTH, 1);

        /*
        do {
            mTx = new TextView(mcontext);
            mTx.setText(""+temp.get(Calendar.DAY_OF_MONTH));
            addView(mTx);
            mLv = new ListView(mcontext);
            mEvent = new EventController(temp);
            mLv.setAdapter(mEvent.getAdapter(temp));
            addView(mLv);
            temp.add(Calendar.DAY_OF_MONTH, 1);
            itemHeight += mLv.getLayoutParams().height;
            mExistItem+=2;
        }while(itemHeight>height);

        scrollTo(0,getChildAt(1).getLayoutParams().height);
        */
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        if(widthMode ==MeasureSpec.UNSPECIFIED|| heightMode == MeasureSpec.UNSPECIFIED){
            throw new IllegalArgumentException("Wrong Argument");
        }

        int count = getChildCount();

        for(int i = 0 ;i<count;i++){
            View ChildView = getChildAt(i);
            LayoutParams lp = ChildView.getLayoutParams();

            int childWidthMeasureSpec =MeasureSpec.makeMeasureSpec(lp.width,MeasureSpec.EXACTLY);
            int childHeightMeasureSpec =MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.EXACTLY);
            ChildView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
        }

        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int CurHeight=0;
        int mExistItem = getChildCount();
        for(int i =0; i < mExistItem/2;i++){
            View v =getChildAt(i*2+1);

            View v1 =getChildAt(i*2);
            if(v !=null && v1!=null) {
                int ItemHeight = v.getMeasuredHeight();
                v.layout(100, CurHeight, r, CurHeight+ItemHeight);
                v1.layout(0,CurHeight,l+100,CurHeight+v1.getMeasuredHeight());
                CurHeight+=ItemHeight;
            }
        }

    }
    private class EventController{

        GregorianCalendar mTodayDate;
        ArrayList<Integer> mCurDate;

        private EventController(ArrayList<Integer> date){
            mTodayDate = new GregorianCalendar(date.get(0),date.get(1),date.get(2));
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

        public ArrayList<String> getEvenet(ArrayList<Integer> date){
            String a = new String();
            a= "year" +date.get(0) +"+month" +date.get(1)+ "+day"+date.get(2)+"+week"+date.get(3);
            int b = (int)(Math.random()*10);
            ArrayList<String> temp = new ArrayList<>();
            temp.add(a);
            for(int i = 0;i<b;i++){
                temp.add("test:"+i);
            }
            return temp;
        }

        public ListAdapter getAdapter(ArrayList<Integer> date) {
            return new EventAdapter(mcontext,getEvenet(date),getColor());
        }

        public ListAdapter getAdapter(GregorianCalendar date) {
            ArrayList<Integer> a= new ArrayList<>(Arrays.asList(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH),date.get(Calendar.DAY_OF_WEEK)));
            return new EventAdapter(mcontext,getEvenet(a),getColor());
        }

        public int getColor() {
            Random random =new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            return Color.rgb(r,g,b);
        }
    };


    private class EventAdapter extends BaseAdapter{


        private Context mcontext;
        private ArrayList<String> Items;
        private final int mPaddingLeft =20;
        private final int mPaddingRight = 20;
        private final int mPaddingTop=20 ;
        private final int mPaddingBotton=20;
        private int mcolor;

        public EventAdapter(Context context,ArrayList<String> Item,int color){
            this.mcontext = context;
            this.Items=Item;
            this.mcolor= color;
        }

        @Override
        public int getCount() {
            return Items.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mTx = new TextView(mcontext);
            mTx.setText(Items.get(position));
            mTx.setBackgroundColor(mcolor);
            //LayoutParams lp =mTx.getLayoutParams();
            //lp.width += mPaddingLeft+mPaddingRight;
            //lp.height+=mPaddingTop+mPaddingBotton;
            //mTx.setLayoutParams(lp);
            return mTx;
        }
    }


}
