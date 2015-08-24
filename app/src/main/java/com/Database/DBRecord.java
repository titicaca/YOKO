package com.Database;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/14.
 */
public abstract class DBRecord implements Serializable {
    public abstract JSONObject toJson();
}
