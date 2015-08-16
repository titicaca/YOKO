package com.fifteentec.Adapter.commonAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/7/19 0019.
 */
public class ViewHolder {

    private final SparseArray<View> mViews;
    private View mCovertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mViews = new SparseArray<>();
        mCovertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mCovertView.setTag(this);
    }

    public static ViewHolder get(Context context, View covertView, ViewGroup parent, int layoutId, int position) {
        if (covertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }

        return (ViewHolder) covertView.getTag();

    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mCovertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }

    public View getCovertView() {
        return mCovertView;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public ViewHolder setAlpha(int viewId, int alpha) {
        TextView view = getView(viewId);
        view.setAlpha(alpha);
        return this;
    }


    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

}