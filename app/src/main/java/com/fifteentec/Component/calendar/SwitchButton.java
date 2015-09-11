package com.fifteentec.Component.calendar;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fifteentec.yoko.R;

public class SwitchButton extends View{

    private Bitmap mSwitchMask,mSwitchFrame,mSwitchLong,mSwitchBotton;
    private int mMaxMoveLength;

    private Rect mDst,mSrc;

    private Paint mPaint;

    private int mDeltaX = 0;
    private float mLastX = 0;
    private float mCurrentX = 0;

    private boolean mSwitchOn = true;

    private boolean mFlag = false;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static SwitchButton newInstance(Context context,int Back,int full,int mask,int botton){
        SwitchButton switchButton = new SwitchButton(context);
        switchButton.initSwitch(Back, full, mask, botton);
        return switchButton;
    }

    public void initSwitch(int Back,int full,int mask,int botton){
        mSwitchMask = BitmapFactory.decodeResource(getResources(), mask);
        mSwitchBotton = BitmapFactory.decodeResource(getResources(),botton);
        mSwitchFrame = BitmapFactory.decodeResource(getResources(),Back);
        mSwitchLong = BitmapFactory.decodeResource(getResources(),full);

        mMaxMoveLength = mSwitchLong.getWidth() - mSwitchFrame.getWidth();
        mDst = new Rect(0,0,mSwitchFrame.getWidth(),mSwitchFrame.getHeight());
        mSrc = new Rect();


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDeltaX > 0 || mDeltaX == 0 && mSwitchOn) {
            if(mSrc != null) {
                mSrc.set(mMaxMoveLength - mDeltaX, 0, mSwitchLong.getWidth()
                        - mDeltaX, mSwitchFrame.getHeight());
            }
        } else if(mDeltaX < 0 || mDeltaX == 0 && !mSwitchOn){
            if(mSrc != null) {
                mSrc.set(-mDeltaX, 0, mSwitchFrame.getWidth() - mDeltaX,
                        mSwitchFrame.getHeight());
            }
        }
        int count = canvas.saveLayer(new RectF(mDst), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(mSwitchLong, mSrc, mDst, null);
        canvas.drawBitmap(mSwitchBotton,mSrc, mDst, null);
        canvas.drawBitmap(mSwitchFrame, 0, 0, null);
        canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
        canvas.restoreToCount(count);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mDeltaX = (int) (mCurrentX - mLastX);
                if ((mSwitchOn && mDeltaX < 0) || (!mSwitchOn && mDeltaX > 0)) {
                    mFlag = true;
                    mDeltaX = 0;
                }

                if (Math.abs(mDeltaX) > mMaxMoveLength) {
                    mDeltaX = mDeltaX > 0 ? mMaxMoveLength : - mMaxMoveLength;
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mDeltaX) > 0 && Math.abs(mDeltaX) < mMaxMoveLength / 2) {
                    mDeltaX = 0;
                    invalidate();
                    return true;
                } else if (Math.abs(mDeltaX) > mMaxMoveLength / 2 && Math.abs(mDeltaX) <= mMaxMoveLength) {
                    mDeltaX = mDeltaX > 0 ? mMaxMoveLength : -mMaxMoveLength;
                    mSwitchOn = !mSwitchOn;
                    /*
                    if(mListener != null) {
                        mListener.onChange(this, mSwitchOn);
                    }*/
                    invalidate();
                    mDeltaX = 0;
                    return true;
                } else if(mDeltaX == 0 && mFlag) {
                    mDeltaX = 0;
                    mFlag = false;
                    return true;
                }
                return super.onTouchEvent(event);
            default:
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }



}
