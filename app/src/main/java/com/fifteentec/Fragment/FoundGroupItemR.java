package com.fifteentec.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.ImageLoader;

;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundGroupItemR extends Fragment {
    private ImageView logo;
    private TextView name;
    private TextView intro;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.found_group_recomended_item_detail, null);
        logo = (ImageView)view.findViewById(R.id.group_detail_logor);
        intro = (TextView)view.findViewById(R.id.intro_content2);
        name = (TextView)view.findViewById(R.id.group_detail_namer);

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

