package com.fifteentec.Fragment.FoundFragment;


import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.ImageLoader;

;


/**
 * Created by cj on 2015/8/7.
 */
public class FoundMsgItem extends Fragment {
    private ImageView logo;
    private TextView name;
    private TextView intro;
    private TextView time;
    private ListView msgListView;
    private Button sendBtn;
    private RadioButton list_button;
    private RadioButton clear_button;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Resources resource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.found_msg_invited_item_list, null);
        resource = getResources();
        logo = (ImageView)view.findViewById(R.id.friend_logo4);
        intro = (TextView)view.findViewById(R.id.invited_content4);
        name = (TextView)view.findViewById(R.id.friend_name4);
        msgListView = (ListView)view.findViewById(R.id.listView_msg);
        time = (TextView)view.findViewById(R.id.sended_time4);
        sendBtn = (Button)view.findViewById(R.id.send_msg_btn);
        list_button = (RadioButton)view.findViewById(R.id.invited_btn);
        clear_button = (RadioButton)view.findViewById(R.id.clear_btn);

        if(getArguments().getBoolean("invited")){
            list_button.setText(resource.getString(R.string.button_invited));
        }
        else list_button.setText(resource.getString(R.string.button_inviting));

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_button.setChecked(false);
            }
        });



        name.setText(getArguments().getString("name"));
        intro.setText(getArguments().getString("intro"));
        time.setText(getArguments().getString("time"));



        if(null!=getArguments().getString("intro")&&!"".equals(getArguments().getString("intro"))){
            imageLoader.displayImage(getArguments().getString("intro"),logo);
        }
        else{
            logo.setImageResource(R.drawable.logo_default);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

}

