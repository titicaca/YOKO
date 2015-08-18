package com.fifteentec.Component.Parser;

/**
 * Created by Administrator on 2015/8/3.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonFriendList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public long id;
    public String nickname;
    public String mobile;
    public int sex;
    public String email;
    public String qq;
    public String wechat;
    public String weibo;
    public String picturelink;
    public int createdtime;
    public int status;
    public String location;
    public ArrayList<JsonFriendList> list = new ArrayList<JsonFriendList>();

    public void parsingJson(JSONObject jsonObject) throws Exception {
        if (null == jsonObject) {
            return;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.optJSONObject(i);
            JsonFriendList jp = new JsonFriendList();
            jp.id = json.optLong("id");
            jp.nickname = json.optString("nickname");
            jp.mobile = json.optString("mobile");
            jp.email = json.optString("email");
            jp.qq = json.optString("qq");
            jp.wechat = json.optString("wechat");
            jp.weibo = json.optString("weibo");
            jp.picturelink = json.optString("picturelink");
            jp.sex = json.optInt("sex");
            jp.createdtime = json.optInt("createdtime");
            jp.status = json.optInt("status");
            jp.location = json.optString("location");
            list.add(jp);
        }

    }
}

