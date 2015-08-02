package com.fifteentec.Adapter.commonAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
/**
 * Õ®”√µƒAdapter
 */
public abstract class commonAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDate;
    protected final int mItemLayoutId;

    /**
     * ”√dateµƒ ˝æ›≥ı ºªØadapter£¨‘⁄contextŒ™»›∆˜£¨ItemLayoutŒ™±Í«©‘™Àÿµƒ≤ºæ÷
     * @param context
     * @param date
     * @param ItemLayoutId
     */
    public commonAdapter(Context context,List<T> date,int ItemLayoutId)
    {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDate = date;
        this.mItemLayoutId = ItemLayoutId;
    }

    /**
     * µ√µΩ◊” ”ÕºµƒView
     * @param position µ⁄º∏∏ˆ◊”‘™Àÿ
     * @param convertView  ◊”‘™ÀÿµƒView
     * @param parent    ∆∏∏»›∆˜
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        int test = 0;
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, (T) getItem(position));
        return viewHolder.getCovertView();
    }

    public abstract void convert(ViewHolder helper, T item);

    private ViewHolder getViewHolder(int position, View convertView,ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }


    public int getCount()
    {
        return mDate.size();
    }

    public Object getItem(int position)
    {
        return mDate.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

}

