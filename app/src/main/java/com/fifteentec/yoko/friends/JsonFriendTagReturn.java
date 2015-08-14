package com.fifteentec.yoko.friends;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/13.
 */
public class JsonFriendTagReturn implements Serializable {

    public String id;
    public Boolean isAdd;

    public void JsonParsing(JSONObject jb) {

        if (null == jb) {
            return;
        }
        id = jb.optString("msg");
        isAdd = jb.optBoolean("result");
    }

}
