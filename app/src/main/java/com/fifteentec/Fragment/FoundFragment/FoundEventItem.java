package com.fifteentec.Fragment.FoundFragment;


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
public class FoundEventItem extends Fragment {
    private ImageView logo;
    private TextView name;
    private TextView groupName;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.found_event_item_detail, null);
        logo = (ImageView)view.findViewById(R.id.group_detail_event_logo);
        groupName = (TextView)view.findViewById(R.id.group_detail_name);
        name = (TextView)view.findViewById(R.id.event_detail_name);

        name.setText(getArguments().getString("name"));
        groupName.setText(getArguments().getString("groupName"));


//        if(null!=getArguments().getString("tags")&&!"".equals(getArguments().getString("tags"))){
//            tags.setText("");
//        }
//        if(null!=getArguments().getString("intro")&&!"".equals(getArguments().getString("intro"))){
//            imageLoader.displayImage(getArguments().getString("intro"),logo);
//        }
//        else{
//            logo.setImageResource(R.drawable.logo_default);
//        }

        return view;
    }

}

