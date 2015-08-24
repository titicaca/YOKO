package com.fifteentec.Component.Parser;

import com.Database.FriendInfoRecord;
import com.Database.FriendInvitationRecord;
import com.Database.FriendTagRecord;
import com.Service.DataSyncService;
import com.fifteentec.FoundItems.EventBrief;
import com.fifteentec.FoundItems.FavoriteBrief;
import com.fifteentec.FoundItems.GroupBrief;

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
                long logintime = friendInfo.getLong("logintime");

                FriendInfoRecord friendInfoRecord = new FriendInfoRecord(uid, fuid, email, location, mobile, nickname, picturelink, qq, sex, wechat, weibo, collectnumber, enrollnumber, friendnumber, logintime);
                friendInfoRecords.add(friendInfoRecord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendInfoRecords;
    }

    public static List<FriendInvitationRecord> parseFriendInvitationResponse(final JSONObject response) {
        List<FriendInvitationRecord> friendInvitationRecords = new ArrayList<FriendInvitationRecord>();

        try {
            JSONArray records = response.getJSONArray("list");
            for (int i = 0; i < records.length(); ++i) {
                JSONObject record = (JSONObject)records.get(i);

                long uid = record.getLong("user_id");
                long fuid = record.getLong("friend_id");
                String msg = record.getString("msg");
                long createdtime = record.getLong("createdtime");

                FriendInvitationRecord friendInvitationRecord = new FriendInvitationRecord();
                friendInvitationRecord.uid = uid;
                friendInvitationRecord.fuid = fuid;
                friendInvitationRecord.msg = msg;
                friendInvitationRecord.createdtime = createdtime;

                friendInvitationRecords.add(friendInvitationRecord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return friendInvitationRecords;
    }
}
