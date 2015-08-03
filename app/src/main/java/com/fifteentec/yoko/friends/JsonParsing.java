package com.fifteentec.yoko.friends;

/**
 * Created by Administrator on 2015/8/3.
 */

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

public class JsonParsing implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String id;
    public String name;
    public ArrayList<JsonParsing> list = new ArrayList<JsonParsing>();

    public void parsingJson(JSONObject jsonObject) throws Exception {
        if (null == jsonObject) {
            return;
        }
        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("name");
    }
}

