package com.fifteentec.TestRicheng;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/6.
 */
public class DrawCanvasViewgroupTest extends ViewGroup implements View.OnLongClickListener, View.OnClickListener, View.OnTouchListener {

    public DrawCanvasTest drawCanvasTest;
    public DragScaleView dragScaleView;
    public boolean isLongClick = false;
    public boolean aBoolean;
    public DrawCanvasViewgroupTest drawCanvasViewgroupTest;
    protected int screenWidth;
    protected int screenHeight;
    private Paint paint = null;
    public float downX;
    public float downY;
    public float downViewY;
    public int[] s = new int[0];
    public ArrayList<CheckArrayTest> list = new ArrayList<CheckArrayTest>();
    public int clickw;
    public int clickh;
    public boolean isAdd = false;
    //移动时的坐标
    private int leftP;
    private int topP;
    private int rightP;
    private int bottomP;
    //按下时删除list的位置
    private int pressListpositon;

    public DrawCanvasViewgroupTest(Context context) {
        super(context);
    }

    public DrawCanvasViewgroupTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCanvasViewgroupTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawCanvasViewgroupTest(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public void Init() {
        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        drawCanvasTest = new DrawCanvasTest(getContext());
//        drawCanvasTest.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        addView(drawCanvasTest);
        dragScaleView = new DragScaleView(getContext());
        //关闭view的硬件加速   并不好用。。。
//        dragScaleView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        dragScaleView.setVisibility(View.INVISIBLE);
        addView(dragScaleView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int index = 0; index < getChildCount(); index++) {
            View v = getChildAt(index);
            if (index == 1) {
                v.layout(l, t, screenWidth / 7, screenHeight / 23);
            } else {
                v.layout(l, t, r, b);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//            dragScaleView.setClickable(true);
//        int measureWidth = measureWidth(widthMeasureSpec);
//        int measureHeight = measureHeight(heightMeasureSpec);
//        // 计算自定义的ViewGroup中所有子控件的大小
//        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        // 设置自定义的控件MyViewGroup的大小
//        setMeasuredDimension(measureWidth, measureHeight);

    }

//    private int measureWidth(int pWidthMeasureSpec) {
//        int result = 0;
//        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
//        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸
//
//        switch (widthMode) {
//            /**
//             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
//             * MeasureSpec.AT_MOST。
//             *
//             *
//             * MeasureSpec.EXACTLY是精确尺寸，
//             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
//             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
//             *
//             *
//             * MeasureSpec.AT_MOST是最大尺寸，
//             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
//             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
//             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
//             *
//             *
//             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
//             * 通过measure方法传入的模式。
//             */
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.EXACTLY:
//                result = widthSize;
//                break;
//        }
//        return result;
//    }

//    private int measureHeight(int pHeightMeasureSpec) {
//        int result = 0;
//
//        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
//
//        switch (heightMode) {
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.EXACTLY:
//                result = heightSize;
//                break;
//        }
//        return result;
//    }


    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    public class DrawCanvasTest extends View implements View.OnTouchListener, View.OnLongClickListener, OnClickListener {


        public DrawCanvasTest(Context context) {
            super(context);
            setOnTouchListener(DrawCanvasTest.this);
            setOnClickListener(DrawCanvasTest.this);
            setOnLongClickListener(DrawCanvasTest.this);
        }

        public DrawCanvasTest(Context context, AttributeSet attrs) {
            super(context, attrs);
            setOnTouchListener(DrawCanvasTest.this);
            setOnLongClickListener(DrawCanvasTest.this);
            setOnClickListener(DrawCanvasTest.this);
        }

        public DrawCanvasTest(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setOnTouchListener(DrawCanvasTest.this);
            setOnLongClickListener(DrawCanvasTest.this);
            setOnClickListener(DrawCanvasTest.this);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }

        /**
         * 初始化获取屏幕宽高
         */
        protected void initScreenW_H() {
            screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
            screenWidth = getResources().getDisplayMetrics().widthPixels;
            paint = new Paint();
        }

        public void initOnDraw() {
            //在view外invalidate时，需要禁用硬件加速
            invalidate();
            forceLayout();
            requestLayout();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            initScreenW_H();
//            new DrawCanvasViewgroupTest(getContext()).initScreenW_H();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);


            for (int i = 0; i < 23; i++) {
                Path path = new Path();
                path.moveTo(0, 0 + i * screenHeight / 23);
                path.lineTo(screenWidth, 0 + i * screenHeight / 23);
                canvas.drawPath(path, paint);
            }
            for (int i = 0; i < 7; i++) {
                Path path = new Path();
                path.moveTo(0 + i * screenWidth / 7, 0);
                path.lineTo(0 + i * screenWidth / 7, screenHeight);
                canvas.drawPath(path, paint);
            }
            if (list.size() != 0) {
                Paint p = new Paint();
                p.setColor(Color.BLUE);
                p.setStrokeWidth(1);
                p.setStyle(Paint.Style.FILL);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isMoveorClick == 0) {
                        int wCount = list.get(i).h;
                        int hCount = list.get(i).w;
                        canvas.drawRect(wCount * screenWidth / 7, hCount * screenHeight / 23, (wCount + 1) * screenWidth / 7, (hCount + 1) * screenHeight / 23, p);
                    } else {
                        int wCount = list.get(i).w;
                        int top = list.get(i).top;
                        int bottom = list.get(i).bottom;
                        canvas.drawRect(wCount * screenWidth / 7, top, (wCount + 1) * screenWidth / 7, bottom, p);
                    }
                }
            }

        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Toast.makeText(getContext(), "wangge", Toast.LENGTH_SHORT).show();
                    downX = event.getRawX();
                    downY = event.getRawY() - 60;
                    downViewY = event.getY();
                    break;
            }
            return false;
        }

        @Override
        public boolean onLongClick(View v) {
            if (list.size() != 0) {
                Toast.makeText(getContext(), "changan", Toast.LENGTH_SHORT).show();
                isLongClick = true;
                for (int i = 0; i < 7; i++) {
                    float widLeft = screenWidth / 7 * i;
                    float widRight = screenWidth / 7 * (i + 1);
                    if (widLeft < downX && downX < widRight) {
                        for (int j = 0; j < 23; j++) {
                            float heiUp = screenHeight / 23 * j;
                            float heiDown = screenHeight / 23 * (j + 1);
                            if (heiUp < downY && downY < heiDown) {
                                clickh = j;
                                clickw = i;
                                break;
                            }
                        }
                    }
                }

                for (int k = 0; k < list.size(); k++) {
                    if (list.get(k).isMoveorClick == 0) {
                        if (list.get(k).w == clickh && list.get(k).h == clickw) {
                            int wCount = list.get(k).h;
                            int hCount = list.get(k).w;
                            pressListpositon = k;
                            dragScaleView.layout(wCount * screenWidth / 7, hCount * screenHeight / 23, (wCount + 1) * screenWidth / 7, (hCount + 1) * screenHeight / 23);
                            dragScaleView.setVisibility(View.VISIBLE);
                            break;
                        }
                    } else {
                        if (list.get(k).w == clickw && list.get(k).top - downY < 30) {
                            Toast.makeText(getContext(), "是我要的方块区域", Toast.LENGTH_SHORT).show();
                            int wCount = list.get(k).w;
                            pressListpositon = k;
                            dragScaleView.layout(wCount * screenWidth / 7, list.get(k).top, (wCount + 1) * screenWidth / 7, list.get(k).bottom);
                            dragScaleView.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            Toast.makeText(getContext(), "此处为空白区域", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            return true;
        }

        @Override
        public void onClick(View v) {

            if (isLongClick) {
                Toast.makeText(getContext(), "解除长按状态", Toast.LENGTH_SHORT).show();
                dragScaleView.setVisibility(View.INVISIBLE);
                isLongClick = false;
            } else {
                for (int i = 0; i < 7; i++) {

                    float widLeft = screenWidth / 7 * i;
                    float widRight = screenWidth / 7 * (i + 1);
                    if (widLeft < downX && downX < widRight) {
                        for (int j = 0; j < 23; j++) {
                            float heiUp = screenHeight / 23 * j;
                            float heiDown = screenHeight / 23 * (j + 1);
                            if (heiUp < downY && downY < heiDown) {
                                clickh = j;
                                clickw = i;
                                break;
                            }
                        }
                    }
                }

                if (list.size() == 0) {
                    CheckArrayTest catest = new CheckArrayTest();
                    catest.h = clickw;
                    catest.w = clickh;
                    catest.isMoveorClick = 0;
                    list.add(catest);
                    invalidate();
                } else {
                    isAdd = false;
                    for (int k = 0; k < list.size(); k++) {
                        if (list.get(k).w == clickh && list.get(k).h == clickw) {
                            Toast.makeText(getContext(), "重复点击", Toast.LENGTH_SHORT).show();
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd) {
                        CheckArrayTest catest = new CheckArrayTest();
                        catest.h = clickw;
                        catest.w = clickh;
                        catest.isMoveorClick = 0;
                        list.add(catest);
                        invalidate();
                        isAdd = false;
                    }
                }
            }
        }
    }

    public class DragScaleView extends View implements View.OnTouchListener {
        protected int screenWidth;
        protected int screenHeight;
        protected int lastX;
        protected int lastY;
        private int oriLeft;
        private int oriRight;
        private int oriTop;
        private int oriBottom;
        private int dragDirection;
        private static final int TOP = 0x15;
        private static final int LEFT = 0x16;
        private static final int BOTTOM = 0x17;
        private static final int RIGHT = 0x18;
        private static final int LEFT_TOP = 0x11;
        private static final int RIGHT_TOP = 0x12;
        private static final int LEFT_BOTTOM = 0x13;
        private static final int RIGHT_BOTTOM = 0x14;
        private static final int CENTER = 0x19;
        private int offset = 0;
        private int wCount;
        private int hCount;
        protected Paint paint = new Paint();


        /**
         * 初始化获取屏幕宽高
         */
        protected void initScreenW_H() {
            screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
            screenWidth = getResources().getDisplayMetrics().widthPixels;
//            topEnd = screenHeight / 23;
//            bottomEnd = 2 * screenHeight / 23;
        }

        public DragScaleView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            setOnTouchListener(DragScaleView.this);
            initScreenW_H();
        }

        public DragScaleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setOnTouchListener(DragScaleView.this);
            initScreenW_H();
        }

        public DragScaleView(Context context) {
            super(context);
            setOnTouchListener(DragScaleView.this);
            initScreenW_H();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(4.0f);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(getContext(), "move", Toast.LENGTH_SHORT).show();
                oriLeft = v.getLeft();
                oriRight = v.getRight();
                oriTop = v.getTop();
                oriBottom = v.getBottom();
                lastY = (int) event.getRawY();
                lastX = (int) event.getRawX();
                //测试用坐标
                downX = event.getRawX();
                downY = event.getRawY() - 60;
                dragDirection = getDirection(v, (int) event.getX(),
                        (int) event.getY());
                list.remove(pressListpositon);
                dragScaleView.invalidate();
            }
            // 处理拖动事件
            delDrag(v, event, action);
            invalidate();
            return true;
        }

        /**
         * 触摸点为中心->>移动
         *
         * @param v
         * @param dx
         * @param dy
         */
        private void center(View v, int dx, int dy) {
            int left = v.getLeft() + dx;
            int top = v.getTop() + dy;
            int right = v.getRight() + dx;
            int bottom = v.getBottom() + dy;
            if (left < -offset) {
                left = -offset;
                right = left + v.getWidth();
            }
            if (right > screenWidth + offset) {
                right = screenWidth + offset;
                left = right - v.getWidth();
            }
            if (top < -offset) {
                top = -offset;
                bottom = top + v.getHeight();
            }
            if (bottom > screenHeight + offset) {
                bottom = screenHeight + offset;
                top = bottom - v.getHeight();
            }
            leftP = left % (screenWidth / 7);
            topP = top;
            rightP = right % (screenWidth / 7) + 1;
            bottomP = bottom;
            v.layout(left, top, right, bottom);
        }

        /**
         * 处理拖动事件
         *
         * @param v
         * @param event
         * @param action
         */
        protected void delDrag(View v, MotionEvent event, int action) {
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    //移动后的dx dy

                    int dx = (int) event.getRawX() - lastX;
                    int dy = (int) event.getRawY() - lastY;
                    switch (dragDirection) {
//                        case LEFT: // 左边缘
//                            left(v, dx);
//                            break;
//                        case RIGHT: // 右边缘
//                            right(v, dx);
//                            break;
                        case BOTTOM: // 下边缘
                            bottom(v, dy);
                            break;
                        case TOP: // 上边缘
                            top(v, dy);
                            break;
                        case CENTER: // 点击中心-->>移动
                            center(v, dx, dy);
                            break;
//                        case LEFT_BOTTOM: // 左下
//                            left(v, dx);
//                            bottom(v, dy);
//                            break;
//                        case LEFT_TOP: // 左上
//                            left(v, dx);
//                            top(v, dy);
//                            break;
//                        case RIGHT_BOTTOM: // 右下
//                            right(v, dx);
//                            bottom(v, dy);
//                            break;
//                        case RIGHT_TOP: // 右上
//                            right(v, dx);
//                            top(v, dy);
//                            break;
                    }
                    if (dragDirection != CENTER) {
                        v.layout(oriLeft, oriTop, oriRight, oriBottom);
                    }
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    int upX = (int) event.getRawX();
                    int upY = (int) event.getRawY();
                    for (int i = 0; i < 7; i++) {
                        float widLeft = screenWidth / 7 * i;
                        float widRight = screenWidth / 7 * (i + 1);
                        if (widLeft < upX && upX < widRight) {
                            for (int j = 0; j < 23; j++) {
                                float heiUp = screenHeight / 23 * j;
                                float heiDown = screenHeight / 23 * (j + 1);
                                if (heiUp < upY && upY < heiDown) {
                                    //控制在哪一个格子
                                    clickh = j - 1;
                                    clickw = i;
                                    break;
                                }
                            }
                        }
                    }
                    if (dragDirection == CENTER) {
//                        v.layout(oriLeft, oriTop, oriRight, oriBottom);
//                    } else {
                        v.layout(clickw * screenWidth / 7, topP, (clickw + 1) * screenWidth / 7, bottomP);
                    }
                    dragDirection = 0;
                    CheckArrayTest checkArrayTest = new CheckArrayTest();
                    checkArrayTest.top = topP;
                    checkArrayTest.bottom = bottomP;
                    checkArrayTest.isMoveorClick = 1;
                    checkArrayTest.w = clickw;
                    list.add(pressListpositon, checkArrayTest);
//                    new DrawCanvasTest(getContext()).initOnDraw();
                    dragScaleView.invalidate();
                    break;
            }
        }

        /**
         * 触摸点为上边缘
         *
         * @param v
         * @param dy
         */
        private void top(View v, int dy) {
            oriTop += dy;
            if (oriTop < -offset) {
                oriTop = -offset;
            }
            //最低高度为单个网格的高度
            if (oriBottom - oriTop - 2 * offset < screenHeight / 23) {
                oriTop = oriBottom - 2 * offset - screenHeight / 23;
            }
        }

        /**
         * 触摸点为下边缘
         *
         * @param v
         * @param dy
         */
        private void bottom(View v, int dy) {
            oriBottom += dy;
            if (oriBottom > screenHeight + offset) {
                oriBottom = screenHeight + offset;
            }
            if (oriBottom - oriTop - 2 * offset < screenHeight / 23) {
                oriBottom = screenHeight / 23 + oriTop + 2 * offset;
            }
        }

        /**
         * 触摸点为右边缘
         *
         * @param v
         * @param dx
         */
        private void right(View v, int dx) {
            oriRight += dx;
            if (oriRight > screenWidth + offset) {
                oriRight = screenWidth + offset;
            }
            if (oriRight - oriLeft - 2 * offset < 200) {
                oriRight = oriLeft + 2 * offset + 200;
            }
        }

        /**
         * 触摸点为左边缘
         *
         * @param v
         * @param dx
         */
        private void left(View v, int dx) {
            oriLeft += dx;
            if (oriLeft < -offset) {
                oriLeft = -offset;
            }
            if (oriRight - oriLeft - 2 * offset < 200) {
                oriLeft = oriRight - 2 * offset - 200;
            }
        }

        /**
         * 获取触摸点flag
         *
         * @param v
         * @param x
         * @param y
         * @return
         */
        protected int getDirection(View v, int x, int y) {
            int left = v.getLeft();
            int right = v.getRight();
            int bottom = v.getBottom();
            int top = v.getTop();
//            if (x < 40 && y < 40) {
//                return LEFT_TOP;
//            }
//            if (y < 40 && right - left - x < 40) {
//                return RIGHT_TOP;
//            }
//            if (x < 40 && bottom - top - y < 40) {
//                return LEFT_BOTTOM;
//            }
//            if (right - left - x < 40 && bottom - top - y < 40) {
//                return RIGHT_BOTTOM;
//            }
//            if (x < 40) {
//                return LEFT;
//            }
            if (y < 10) {
                return TOP;
            }
//            if (right - left - x < 40) {
//                return RIGHT;
//            }
            if (bottom - top - y < 10) {
                return BOTTOM;
            }
            return CENTER;
        }

        /**
         * 获取截取宽度
         *
         * @return
         */
        public int getCutWidth() {
            return getWidth() - 2 * offset;
        }

        /**
         * 获取截取高度
         *
         * @return
         */
        public int getCutHeight() {
            return getHeight() - 2 * offset;
        }

    }
}
