package com.fifteentec.Fragment;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fifteentec.yoko.R;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class TabButtonFragment extends Fragment implements View.OnClickListener{

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

    private Ibutton mButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        View mInflater = inflater.inflate(R.layout.view_tab_button_layout,container,false);
        InitFm(mInflater);
        return mInflater;

    }

    public void setButton(Ibutton newButton)
    {
        this.mButton = newButton;
    }
    private void InitFm(View mInflater) {
        mTabBtn1 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_1);
        mTabBtn2 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_2);
        mTabBtn3 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_3);
        mTabBtn4 = (LinearLayout) mInflater.findViewById(R.id.tab_button_ll_4);

        mImBtn1 =(ImageView) mInflater.findViewById(R.id.tab_button_image_1);
        mImBtn2 =(ImageView) mInflater.findViewById(R.id.tab_button_image_2);
        mImBtn3 =(ImageView) mInflater.findViewById(R.id.tab_button_image_3);
        mImBtn4 =(ImageView) mInflater.findViewById(R.id.tab_button_image_4);

        mTabBtn1.setOnClickListener(this);
        mTabBtn2.setOnClickListener(this);
        mTabBtn3.setOnClickListener(this);
        mTabBtn4.setOnClickListener(this);

        setTabSelection(SelectCalendar);
    }

    public interface Ibutton
    {
        void  TabSelector(int id);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
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

    private void resetBtn()
    {
        mImBtn1 .setImageResource(R.drawable.calendar);
        mImBtn2 .setImageResource(R.drawable.found);
        mImBtn3 .setImageResource(R.drawable.friend);
        mImBtn4 .setImageResource(R.drawable.me);
    }

    private void setTabSelection(int index)
    {

        resetBtn();
        switch (index)
        {
            case SelectCalendar:
                mImBtn1.setImageResource(R.drawable.calendarselected);
                break;
            case SelectFound:
                mImBtn2.setImageResource(R.drawable.foundselceted);
                break;
            case SelectFriend:
                mImBtn3.setImageResource(R.drawable.friendselected);
                break;
            case SelectCount:
                mImBtn4.setImageResource(R.drawable.meselected);
                break;
            default:
                break;
        }
    }
}
