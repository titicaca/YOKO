package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.fifteentec.Adapter.commonAdapter.FriendDetailsAdapter;
import com.fifteentec.yoko.R;

import java.util.ArrayList;

;

public class FriendDetailsActivity extends Activity {

    // private HorizontalListView hlv;
    private int position;
    private ViewPager vpager;
    private ArrayList<JsonParsing> jsonData = new ArrayList<JsonParsing>();

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
        jsonData = (ArrayList<JsonParsing>) intent.getSerializableExtra("json");
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
