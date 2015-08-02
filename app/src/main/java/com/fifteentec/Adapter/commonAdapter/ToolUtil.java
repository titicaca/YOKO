package com.fifteentec.Adapter.commonAdapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class ToolUtil {

    public static ViewGroup.LayoutParams makeRec(View view)
    {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int view_height = lp.height;
        int view_width = lp.width;
        if(view_height>= view_width)
        {
            lp.height = view_width;
        }
        else
        {
            lp.width = view_height;
        }

        return lp;
    }

    public static ViewGroup.LayoutParams makeRec(View view,int height,int width)
    {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height =  height;
        lp.width = width;
        return lp;
    }

    public static int getDividWidth(Context context,int num)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int mScreenWidth = outMetrics.widthPixels;
        return mScreenWidth/num;
    }
    public static int getDividHeight(Context context,int num)
    {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int mScreenHeight = outMetrics.heightPixels;
        return mScreenHeight/num;
    }

    public static void MarkIn(int a )
    {
        Log.d("MarkIn","Here In"+String.valueOf(a));
    }
    public static String DisplayCal(GregorianCalendar date)
    {
        return date.get(Calendar.YEAR)+"/"+date.get(Calendar.MONTH)+"/"+date.get(Calendar.DAY_OF_MONTH);
    }

    public static ArrayList<Integer> MergeArrayList(ArrayList<Integer> a,ArrayList<Integer> b)
    {
        ArrayList<Integer> c = new ArrayList<Integer>(a.size()+b.size());
        for(int i = 0;i<a.size();i++)
        {
            c.add(a.get(i));
        }
        for (int i=0;i<b.size();i++)
        {
            c.add(b.get(i));
        }
        return c;
    }

    public static ViewGroup.LayoutParams makeRec(View convertView,int side)
    {
        ViewGroup.LayoutParams lp = convertView.getLayoutParams();
        lp.width = side;
        lp.height = side;

        return lp;
    }

}

