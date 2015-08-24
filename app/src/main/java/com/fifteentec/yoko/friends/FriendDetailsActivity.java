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
import com.fifteentec.yoko.R;

import java.util.ArrayList;

;

public class FriendDetailsActivity extends Activity {

    //判断到详细页时是第几页
    private int position;
    //显示的横滑控件
    private ViewPager vpager;
    //好友信息list
    private ArrayList<FriendInfoRecord> jsonData = new ArrayList<FriendInfoRecord>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_detalis);
        vpager = (ViewPager) findViewById(R.id.vPager);
        Intent intent = getIntent();
        //从好友列表传进来的position
        position = intent.getIntExtra("position", 0);
        //获取好友信息
        jsonData = (ArrayList<FriendInfoRecord>) intent.getSerializableExtra("json");
        //好友详情适配器
        FriendDetailsAdapter friendDetailsAdapter = new FriendDetailsAdapter(this,
                jsonData);
        vpager.setAdapter(friendDetailsAdapter);
        //设置当前显示的item
        vpager.setCurrentItem(position);

    }

}
