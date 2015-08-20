package com.fifteentec.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;


public class WeekEventFragment extends Fragment {

    private static final String DATE = "Date";

    private ArrayList<Integer> mCurDate = new ArrayList<>();

    public static WeekEventFragment newInstance(ArrayList<Integer> date){
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(DATE, date);
        WeekEventFragment weekEventFragment = new WeekEventFragment();
        weekEventFragment.setArguments(bundle);
        return weekEventFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mCurDate = bundle.getIntegerArrayList(DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
