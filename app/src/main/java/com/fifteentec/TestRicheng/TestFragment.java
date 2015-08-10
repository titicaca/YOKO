package com.fifteentec.TestRicheng;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class TestFragment extends Fragment {


    private GridView gv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.test_fragmet, container, false);
        DrawCanvasViewgroupTest drawCanvasViewgroupTest = new DrawCanvasViewgroupTest(getActivity());
        drawCanvasViewgroupTest.Init();

        return drawCanvasViewgroupTest;
    }
}
