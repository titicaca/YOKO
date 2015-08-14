package com.fifteentec.Component.Parser;

import com.Database.FriendInfoRecord;
import com.Database.FriendTagRecord;
import com.Service.DataSyncService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benbush on 15/8/13.
 */
public class DataSyncServerParser {
    public static List<FriendTagRecord> parseFriendResponseToFriendTag(final long uid, final JSONObject response) {
        List<FriendTagRecord> friendTagRecords = new ArrayList<FriendTagRecord>();

        try {
            JSONArray friends = response.getJSONArray("list");
            for (int i = 0; i < friends.length(); ++i) {
                JSONObject friend = (JSONObject)friends.get(i);
                long fuid;
                if (friend.isNull("friend")) {
                    fuid = 0;
                } else {
                    fuid = friend.getJSONObject("friend").getLong("id");
                }

                JSONArray tags = friend.getJSONArray("tags");
                for (int j = 0; j < tags.length(); ++j) {
                    JSONObject tag = (JSONObject)tags.get(j);
                    long tagId = tag.getLong("id");
                    String tagName = tag.getString("tagname");

                    FriendTagRecord friendTagRecord = new FriendTagRecord(uid, fuid, tagId, tagName);
                    friendTagRecords.add(friendTagRecord);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendTagRecords;
    }

    public static List<FriendInfoRecord> parseFriendResponseToFriendInfo(final long uid, final JSONObject response) {
        List<FriendInfoRecord> friendInfoRecords = new ArrayList<FriendInfoRecord>();

        try {
            JSONArray friends = response.getJSONArray("list");
            for (int i = 0; i < friends.length(); ++i) {
                JSONObject friend = (JSONObject)friends.get(i);

                if (friend.isNull("friend")) continue;
                JSONObject friendInfo = friend.getJSONObject("friend");
                long fuid = friendInfo.getLong("id");
                String email = friendInfo.getString("email");
                String location = friendInfo.getString("location");
                String mobile = friendInfo.getString("mobile");
                String nickname = friendInfo.getString("nickname");
                String picturelink = friendInfo.getString("picturelink");
                String qq= friendInfo.getString("qq");
                int sex = friendInfo.getInt("sex");
                String wechat = friendInfo.getString("wechat");
                String weibo = friendInfo.getString("weibo");
                int collectnumber = friendInfo.getInt("collectnumber");
                int enrollnumber = friendInfo.getInt("enrollnumber");
                int friendnumber = friendInfo.getInt("friendnumber");

                FriendInfoRecord friendInfoRecord = new FriendInfoRecord(uid, fuid, email, location, mobile, nickname, picturelink, qq, sex, wechat, weibo, collectnumber, enrollnumber, friendnumber);
                friendInfoRecords.add(friendInfoRecord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendInfoRecords;
    }
}
