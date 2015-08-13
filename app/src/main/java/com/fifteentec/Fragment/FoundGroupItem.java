package com.fifteentec.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fifteentec.FoundAdapter.EventAdapter;
import com.fifteentec.item.EventBrief;
import com.fifteentec.item.GroupBrief;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundGroupItem extends Fragment {
    private ImageView logo;
    private TextView name;
    private TextView intro;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.found_group_item_detail, null);
        logo = (ImageView)view.findViewById(R.id.group_detail_logo);
        intro = (TextView)view.findViewById(R.id.intro_content);
        name = (TextView)view.findViewById(R.id.group_detail_name);

        name.setText(getArguments().getString("name"));
        intro.setText(getArguments().getString("intro"));

        if(null!=getArguments().getString("intro")&&!"".equals(getArguments().getString("intro"))){
            imageLoader.displayImage(getArguments().getString("intro"),logo);
        }
        else{
            logo.setImageResource(R.drawable.logo_default);
        }

        return view;
    }

}

