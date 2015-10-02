package com.fifteentec.Component.FoundItems;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by cj on 2015/10/2.
 */
public class ListButton extends ImageButton {

    private int index = -1;
    private int status = 0;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ListButton(Context context) {
        super(context);
        // TODO: do something here if you want
    }

    public ListButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO: do something here if you want
    }

    public ListButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO: do something here if you want
    }
}
