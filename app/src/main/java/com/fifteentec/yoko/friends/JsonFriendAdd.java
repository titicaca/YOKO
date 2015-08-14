package com.fifteentec.yoko.friends;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/14.
 */
public class JsonFriendAdd implements Serializable {

    public int id;
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
    public ArrayList<JsonFriendAdd> list = new ArrayList<JsonFriendAdd>();

    public void JsonParsing(JSONObject json) {
        if (json == null) {
            return;
        }
        list = new ArrayList<JsonFriendAdd>();
        JsonFriendAdd jp = new JsonFriendAdd();
        jp.id = json.optInt("id");
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
