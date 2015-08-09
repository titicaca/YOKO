package com.fifteentec.item;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import com.fifteentec.yoko.R;

/**
 * Created by cj on 2015/8/9.
 */
public class MsgBoxBtn extends RelativeLayout{
    private ImageView imgView;
    private TextView textView;

    public MsgBoxBtn(Context context) {
        super(context,null);
    }

    public MsgBoxBtn(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.msgbox_btn, this,true);

        this.imgView = (ImageView)findViewById(R.id.imgview);
        this.textView = (TextView)findViewById(R.id.textview);

        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImgResource(int resourceID) {
        this.imgView.setImageResource(resourceID);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }
    private void generateCountIcon(int count){
        //获取屏幕规格
        int iconSize = (int)getResources().getDimension(android.R.dimen.app_icon_size);
        int size = iconSize/6;
        int numSize;
        String strCount;
        Bitmap msgIcon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(msgIcon);

        Paint iconPaint = new Paint();
        iconPaint.setColor(Color.RED);
        //消除锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.drawCircle(iconSize-size-size/2, size, size, iconPaint);

        strCount = count + "";
        if(strCount.length() == 1){
            numSize = iconSize-size-size/3-size/3;
        }else if(strCount.length() == 2){
            numSize = iconSize-size-size/3-size/3-size/3;
        }else if(strCount.length() == 3){
            numSize = iconSize-size-size/3-size/3-size/3-size/3;
        }else{
            numSize = 0;
        }

        Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
        countPaint.setColor(Color.WHITE);
        countPaint.setTextSize(12f);
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(String.valueOf(count), numSize, size+size/4, countPaint);
        if(imgView!=null){
            imgView.setImageBitmap(msgIcon);
        }

    }

}
