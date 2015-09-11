package com.fifteentec.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fifteentec.yoko.R;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class TabButtonFragment extends Fragment implements View.OnClickListener {

    private final int SelectCalendar = 0x00;
    private final int SelectFound = 0x01;
    private final int SelectFriend = 0x02;
    private final int SelectCount = 0x03;
    private LinearLayout mTabBtn1;
    private LinearLayout mTabBtn2;
    private LinearLayout mTabBtn3;
    private LinearLayout mTabBtn4;

    private ImageView mImBtn1;
    private ImageView mImBtn2;
    private ImageView mImBtn3;
    private ImageView mImBtn4;

    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;

    private Ibutton mButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View mInflater = inflater.inflate(R.layout.view_tab_button_layout, container, false);
        InitFm(mInflater);
        return mInflater;

    }

    public void setButton(Ibutton newButton) {
        this.mButton = newButton;
    }

    private void InitFm(View mInflater) {

        mTabBtn1 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_1);
        mTabBtn2 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_2);
        mTabBtn3 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_3);
        mTabBtn4 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_4);

        mImBtn1 = (ImageView) mInflater.findViewById(R.id.tab_button_image_1);
        mImBtn2 = (ImageView) mInflater.findViewById(R.id.tab_button_image_2);
        mImBtn3 = (ImageView) mInflater.findViewById(R.id.tab_button_image_3);
        mImBtn4 = (ImageView) mInflater.findViewById(R.id.tab_button_image_4);

        mTabBtn1.setOnClickListener(this);
        mTabBtn2.setOnClickListener(this);
        mTabBtn3.setOnClickListener(this);
        mTabBtn4.setOnClickListener(this);

        mTextView1 = (TextView) mInflater.findViewById(R.id.tab_button_text_1);
        mTextView2 = (TextView) mInflater.findViewById(R.id.tab_button_text_2);
        mTextView3 = (TextView) mInflater.findViewById(R.id.tab_button_text_3);
        mTextView4 = (TextView) mInflater.findViewById(R.id.tab_button_text_4);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        float Ratio  = 1/18f;
        float textsize = displayWidth/30f;
        ViewGroup.LayoutParams layoutParams = mImBtn1.getLayoutParams();
        layoutParams.width =(int)(displayWidth*Ratio);
        layoutParams.height=(int)(displayWidth*Ratio);
        mImBtn1.setLayoutParams(layoutParams);
        mImBtn2.setLayoutParams(layoutParams);
        mImBtn3.setLayoutParams(layoutParams);
        mImBtn4.setLayoutParams(layoutParams);
        mTextView1.setTextSize(TypedValue.COMPLEX_UNIT_PX,textsize);
        mTextView2.setTextSize(TypedValue.COMPLEX_UNIT_PX,textsize);
        mTextView3.setTextSize(TypedValue.COMPLEX_UNIT_PX,textsize);
        mTextView4.setTextSize(TypedValue.COMPLEX_UNIT_PX,textsize);

        setTabSelection(SelectCalendar);
    }

    public interface Ibutton {
        void TabSelector(int id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_button_ll_1:
                setTabSelection(SelectCalendar);
                mButton.TabSelector(R.integer.SelectorCal);
                break;
            case R.id.tab_button_ll_2:
                setTabSelection(SelectFound);
                mButton.TabSelector(R.integer.SelectorFrd);
                break;
            case R.id.tab_button_ll_3:
                setTabSelection(SelectFriend);
                mButton.TabSelector(R.integer.SelectorCir);
                break;
            case R.id.tab_button_ll_4:
                setTabSelection(SelectCount);
                mButton.TabSelector(R.integer.SelectorMe);
                break;

            default:
                break;
        }
    }

    private void resetBtn() {
        mImBtn1.setImageResource(R.drawable.calendar);
        mImBtn2.setImageResource(R.drawable.found);
        mImBtn3.setImageResource(R.drawable.friend);
        mImBtn4.setImageResource(R.drawable.me);

        mTextView1.setTextColor(Color.BLACK);
        mTextView2.setTextColor(Color.BLACK);
        mTextView3.setTextColor(Color.BLACK);
        mTextView4.setTextColor(Color.BLACK);


    }

    private void setTabSelection(int index) {

        resetBtn();
        switch (index) {
            case SelectCalendar:
                mImBtn1.setImageResource(R.drawable.calendarselected);
                mTextView1.setTextColor(getResources().getColor(R.color.appMainColor));
                break;
            case SelectFound:
                mImBtn2.setImageResource(R.drawable.foundselceted);
                mTextView2.setTextColor(getResources().getColor(R.color.appMainColor));
                break;
            case SelectFriend:
                mImBtn3.setImageResource(R.drawable.friendselected);
                mTextView3.setTextColor(getResources().getColor(R.color.appMainColor));
                break;
            case SelectCount:
                mImBtn4.setImageResource(R.drawable.meselected);
                mTextView4.setTextColor(getResources().getColor(R.color.appMainColor));
                break;
            default:
                break;
        }
    }
}
