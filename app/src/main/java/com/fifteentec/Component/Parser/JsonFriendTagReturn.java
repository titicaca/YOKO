package com.fifteentec.Component.Parser;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/13.
 */
public class JsonFriendTagReturn implements Serializable {

    public long id;
    public Boolean isAdd;

    public void JsonParsing(JSONObject jb) {

        if (null == jb) {
            return;
        }
        id = jb.optLong("msg");
        isAdd = jb.optBoolean("result");
    }

}
