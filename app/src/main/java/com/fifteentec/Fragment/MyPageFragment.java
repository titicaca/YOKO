package com.fifteentec.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fifteentec.yoko.R;

/**
 * Created by Administrator on 2015/8/21.
 */
public class MyPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_page_fragment, container, false);
        return v;

    }
}
