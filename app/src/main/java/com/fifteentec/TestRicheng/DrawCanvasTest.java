//package com.fifteentec.TestRicheng;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
///**
// * Created by Administrator on 2015/8/6.
// */
//public class DrawCanvasTest extends View implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
//
//    protected int screenWidth;
//    protected int screenHeight;
//    private Paint paint = null;
//    public float downX;
//    public float downY;
//    public int[] s = new int[0];
//    public ArrayList<CheckArrayTest> list = new ArrayList<CheckArrayTest>();
//    public int clickw;
//    public int clickh;
//    public boolean isAdd = false;
//
//    public DrawCanvasTest(Context context) {
//        super(context);
//        setOnTouchListener(this);
//        setOnClickListener(this);
//        setOnLongClickListener(this);
//    }
//
//    public DrawCanvasTest(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setOnTouchListener(this);
//        setOnClickListener(this);
//        setOnLongClickListener(this);
//    }
//
//    public DrawCanvasTest(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        setOnTouchListener(this);
//        setOnClickListener(this);
//        setOnLongClickListener(this);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//    }
//
//
//    /**
//     * 初始化获取屏幕宽高
//     */
//    protected void initScreenW_H() {
//        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
//        screenWidth = getResources().getDisplayMetrics().widthPixels;
//        paint = new Paint();
//    }
//
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        initScreenW_H();
//        paint.setColor(Color.BLUE);
//        paint.setStrokeWidth(1);
//        paint.setStyle(Paint.Style.STROKE);
//
//
//        for (int i = 0; i < 23; i++) {
//            Path path = new Path();
//            path.moveTo(0, 0 + i * screenHeight / 23);
//            path.lineTo(screenWidth, 0 + i * screenHeight / 23);
//            canvas.drawPath(path, paint);
//        }
//        for (int i = 0; i < 7; i++) {
//            Path path = new Path();
//            path.moveTo(0 + i * screenWidth / 7, 0);
//            path.lineTo(0 + i * screenWidth / 7, screenHeight);
//            canvas.drawPath(path, paint);
//        }
//        if (list.size() != 0) {
//            Paint p = new Paint();
//            p.setColor(Color.BLUE);
//            p.setStrokeWidth(1);
//            p.setStyle(Paint.Style.FILL);
//            for (int i = 0; i < list.size(); i++) {
//                int wCount = list.get(i).h;
//                int hCount = list.get(i).w;
//                canvas.drawRect(wCount * screenWidth / 7, hCount * screenHeight / 23, (wCount + 1) * screenWidth / 7, (hCount + 1) * screenHeight / 23, p);
//            }
//        }
//
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        for (int i = 0; i < 7; i++) {
//
//            float widLeft = screenWidth / 7 * i;
//            float widRight = screenWidth / 7 * (i + 1);
//            if (widLeft < downX && downX < widRight) {
//                for (int j = 0; j < 23; j++) {
//                    float heiUp = screenHeight / 23 * j;
//                    float heiDown = screenHeight / 23 * (j + 1);
//                    if (heiUp < downY && downY < heiDown) {
//                        clickh = j;
//                        clickw = i;
//                        break;
//                    }
//                }
//            }
//        }
//        if (list.size() == 0) {
//            CheckArrayTest catest = new CheckArrayTest();
//            catest.h = clickh;
//            catest.w = clickw;
//            list.add(catest);
//            invalidate();
//        } else {
//            isAdd = false;
//            for (int k = 0; k < list.size(); k++) {
//                if (list.get(k).w == clickh && list.get(k).h == clickw) {
//                    Toast.makeText(getContext(), "重复点击", Toast.LENGTH_SHORT).show();
//                    isAdd = true;
//                    break;
//                }
//            }
//            if (!isAdd) {
//                CheckArrayTest catest = new CheckArrayTest();
//                catest.h = clickw;
//                catest.w = clickh;
//                list.add(catest);
//                invalidate();
//                isAdd = false;
//            }
//        }
//
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getRawX();
//                downY = event.getRawY() - 60;
//                break;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        return false;
//    }
//}
