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

public class SwitchButton extends View implements View.OnClickListener{

    private Bitmap mSwitchMask,mSwitchFrame,mSwitchLong,mSwitchBotton;
    private int mMaxMoveLength;

    private Rect mDst,mSrc;

    private Paint mPaint;

    private int mDeltaX = 0;
    private float mLastX = 0;
    private float mCurrentX = 0;


    private float  ratio = 1;
    private boolean mSwitchOn = true;

    private boolean mFlag = false;


    private SwitchButtonListener mSwitchButtonListener=null;


    @Override
    public void onClick(View v) {
        Log.d("Test","here");
        mDeltaX = mSwitchOn?mMaxMoveLength:-mMaxMoveLength;
        mSwitchOn = !mSwitchOn;
        if(mSwitchButtonListener!=null) mSwitchButtonListener.ButtonSwitch(this,mSwitchOn);
        invalidate();
        mDeltaX = 0;
    }


    public interface SwitchButtonListener{
        void ButtonSwitch(SwitchButton switchButton,boolean isOn);
    }

    public void setmSwitchButtonListener(SwitchButtonListener mSwitchButtonListener) {
        this.mSwitchButtonListener = mSwitchButtonListener;
    }

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static SwitchButton newInstance(Context context,int Back,int full,int mask,int botton,boolean on){
        SwitchButton switchButton = new SwitchButton(context);
        switchButton.initSwitch(Back, full, mask, botton,on);

        return switchButton;
    }

    public void initSwitch(int Back,int full,int mask,int botton,boolean on){


        mSwitchOn = on;
        mSwitchMask = BitmapFactory.decodeResource(getResources(), mask);
        mSwitchFrame = BitmapFactory.decodeResource(getResources(),Back);
        mSwitchBotton = BitmapFactory.decodeResource(getResources(),botton);
        mSwitchLong = BitmapFactory.decodeResource(getResources(),full);
        setOnClickListener(this);



        mMaxMoveLength = mSwitchLong.getWidth() - mSwitchFrame.getWidth();
        mDst = new Rect(0,0,(int)(mSwitchFrame.getWidth()*ratio),(int)(mSwitchFrame.getHeight()*ratio));
        mSrc = new Rect();


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

    }


    public void ChangeButtonHeight(int height){
        ratio = (float)height/mSwitchFrame.getHeight();
        mDst = new Rect(0,0,(int)(mSwitchFrame.getWidth()*ratio),(int)(mSwitchFrame.getHeight()*ratio));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)(mSwitchFrame.getWidth()*ratio),(int)( mSwitchFrame.getHeight()*ratio));
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
        canvas.drawBitmap(mSwitchFrame, null, mDst, null);
        canvas.drawBitmap(mSwitchMask, null, mDst, mPaint);
        canvas.restoreToCount(count);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
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
                if (Math.abs(mDeltaX) > mMaxMoveLength/4 && Math.abs(mDeltaX) < mMaxMoveLength / 2) {
                    mDeltaX = 0;
                    invalidate();
                    return true;
                } else if (Math.abs(mDeltaX) > mMaxMoveLength / 2 && Math.abs(mDeltaX) <= mMaxMoveLength) {
                    mDeltaX = mDeltaX > 0 ? mMaxMoveLength : -mMaxMoveLength;
                    mSwitchOn = !mSwitchOn;
                    if(mSwitchButtonListener != null) {
                        mSwitchButtonListener.ButtonSwitch(this, mSwitchOn);
                    }
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
