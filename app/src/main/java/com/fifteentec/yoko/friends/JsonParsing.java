package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonParsing implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
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
    public ArrayList<JsonParsing> list = new ArrayList<JsonParsing>();

    public void parsingJson(JSONObject jsonObject) throws Exception {
        if (null == jsonObject) {
            return;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("allfriends");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.optJSONObject(i);
            JsonParsing jp = new JsonParsing();
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
            list.add(jp);
        }

    }
}

