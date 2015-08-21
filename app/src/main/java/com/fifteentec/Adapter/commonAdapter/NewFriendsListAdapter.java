package com.fifteentec.Adapter.commonAdapter;

/**
 * Created by Administrator on 2015/8/3.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.API.APIJsonCallbackResponse;
import com.API.APIUrl;
import com.API.APIUserServer;
import com.Database.DBManager;
import com.Database.FriendInfoRecord;
import com.Database.FriendInvitationRecord;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewFriendsListAdapter extends BaseAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context c;
    private List<FriendInvitationRecord> listdata;
    private DBManager dbManager;
    private BaseActivity activity;

    public NewFriendsListAdapter(Context c, List<FriendInvitationRecord> listdata, DBManager dbManager, BaseActivity activity) {
        this.c = c;
        this.listdata = listdata;
        this.dbManager = dbManager;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int arg0) {
        return listdata.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        // 列表中 没有数据
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.newfriends_list_item, null);
            viewHolder.newfriends_item_tv_add = (TextView) convertView
                    .findViewById(R.id.newfriends_item_tv_add);
            viewHolder.newfriends_item_tv_check = (TextView) convertView.findViewById(R.id.newfriends_item_tv_check);
            viewHolder.newfriends_item_tv_delete = (TextView) convertView.findViewById(R.id.newfriends_item_tv_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final TextView tv_add = viewHolder.newfriends_item_tv_add;
        final int po = arg0;
        viewHolder.newfriends_item_tv_add
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        newfriendInvi(po);
                    }
                });
        viewHolder.newfriends_item_tv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.getTableFriendInvitation().deleteFriendInvitation(listdata.get(po).rid);
                listdata.remove(po);
                notifyDataSetChanged();
            }
        });
        viewHolder.newfriends_item_tv_check.setText(listdata.get(po).msg);
        return convertView;
    }

    public class ViewHolder {
        TextView newfriends_item_tv_add;
        TextView newfriends_item_tv_delete;
        TextView newfriends_item_tv_check;
    }

    private void newfriendInvi(final int position) {

        new APIUserServer.JsonPut(APIUrl.URL_NEWFRIEND_INVI + listdata.get(position).uid, null, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                JSONObject response = this.getResponse();
                if (response != null) {
                    dbManager.getTableFriendInvitation().deleteFriendInvitation(listdata.get(position).rid);
                    dbManager.getTableFriendInfo().addFriendInfo(jsonFriendParsing(response));
                    listdata.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(c, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, activity.getRequestQueue(), null).send();

    }

    private FriendInfoRecord jsonFriendParsing(JSONObject ob) {
        FriendInfoRecord firecord = new FriendInfoRecord();
        firecord.collectnumber = ob.optInt("collectnumber");
        firecord.email = ob.optString("email");
        firecord.enrollnumber = ob.optInt("enrollnumber");
        firecord.friendnumber = ob.optInt("friendnumber");
        firecord.fuid = ob.optLong("id");
        firecord.location = ob.optString("location");
        firecord.logintime = ob.optLong("logintime");
        firecord.mobile = ob.optString("mobile");
        firecord.nickname = ob.optString("nickname");
        firecord.picturelink = ob.optString("picturelink");
        firecord.qq = ob.optString("qq");
        firecord.rid = ob.optLong("rid");
        firecord.sex = ob.optInt("sex");

//        firecord.uid = ob.optLong("uid");
        firecord.uid = UserServer.getInstance().getUserid();
        firecord.wechat = ob.optString("wechat");
        firecord.weibo = ob.optString("weibo");
        return firecord;
    }

}