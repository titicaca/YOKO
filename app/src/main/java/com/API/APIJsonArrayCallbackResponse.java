package com.API;

import org.json.JSONArray;

/**
 * Created by benbush on 15/7/10.
 * 用于接收JsonArray格式的Response
 */
public abstract class APIJsonArrayCallbackResponse implements APICallbackResponse{
    private JSONArray response;

    public JSONArray getResponse() {
        return this.response;
    }

    public void setResponse(JSONArray response) {
        this.response = response;
    }
}
