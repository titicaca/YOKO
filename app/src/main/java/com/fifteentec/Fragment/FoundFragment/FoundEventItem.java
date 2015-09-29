package com.fifteentec.Fragment.FoundFragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private int id;
    private RelativeLayout detail;
    private ImageButton add;
    private ImageButton back;
    private FragmentManager mFragmentManager;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    long currentTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.found_event_item_detail, null);
        logo = (ImageView)view.findViewById(R.id.group_detail_event_logo);
        groupName = (TextView)view.findViewById(R.id.group_detail_name);
        name = (TextView)view.findViewById(R.id.event_detail_name);
        add = (ImageButton)view.findViewById(R.id.add);
        back = (ImageButton)view.findViewById(R.id.back_arrow);
        mFragmentManager = FoundEventItem.this.getFragmentManager();
        final SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("event_take_part_info", Context.MODE_PRIVATE);

        Boolean joined = sharedPreferences.getBoolean("id" + id, false);
        if(joined)add.setBackgroundResource(R.drawable.takepart_j);
        else add.setBackgroundResource(R.drawable.takepart);
        id= getArguments().getInt("id");
        currentTime = (long)System.currentTimeMillis();
        if((currentTime>getArguments().getLong("timeEnd"))&&(getArguments().getLong("timeEnd")!=0)){
            add.setImageResource(R.drawable.takepart_n);
            add.setClickable(false);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean joined = sharedPreferences.getBoolean("id" + id, false);
                if(!joined){
                    add.setBackgroundResource(R.drawable.takepart_j);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("id"+id, true);
                    editor.commit();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragmentManager.getBackStackEntryCount()>0){
                    mFragmentManager.popBackStack();
                }

            }
        });

        name.setText(getArguments().getString("name"));
        groupName.setText(getArguments().getString("groupName"));



        if(null!=getArguments().getString("intro")&&!"".equals(getArguments().getString("intro"))){
            imageLoader.displayImage(getArguments().getString("intro"),logo);
        }
        else{
            logo.setImageResource(R.drawable.logo_default);
        }

        return view;
    }

}

