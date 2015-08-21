package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.Database.FriendInfoRecord;
import com.fifteentec.Adapter.commonAdapter.FriendDetailsAdapter;
import com.fifteentec.Component.Parser.JsonFriendList;
import com.fifteentec.yoko.R;

import java.util.ArrayList;

;

public class FriendDetailsActivity extends Activity {

    // private HorizontalListView hlv;
    private int position;
    private ViewPager vpager;
    private ArrayList<FriendInfoRecord> jsonData = new ArrayList<FriendInfoRecord>();

    // private HlistviewAdapter hladapter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_detalis);
        // hlv = (HorizontalListView) findViewById(R.id.horizon_listview);
        vpager = (ViewPager) findViewById(R.id.vPager);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        jsonData = (ArrayList<FriendInfoRecord>) intent.getSerializableExtra("json");
        FriendDetailsAdapter fdadapter = new FriendDetailsAdapter(this,
                jsonData);
        vpager.setAdapter(fdadapter);
        vpager.setCurrentItem(position);

        // hladapter = new HlistviewAdapter(this, position);
        //
        // hlv.setAdapter(hladapter);
        //
        // // hlv.setSelection(position);
        //
        // hlv.clearFocus();
        // hlv.post(new Runnable() {
        // @Override
        // public void run() {
        // hladapter.notifyDataSetChanged();
        // hlv.setSelection(position);
        // }
        // });
    }

}
