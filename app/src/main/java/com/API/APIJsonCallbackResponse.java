package com.API;

import org.json.JSONObject;

/**
 * Created by benbush on 15/6/19.
 * 用于接收Json格式的Response
 */
public abstract class APIJsonCallbackResponse implements APICallbackResponse{
    private JSONObject response;

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public JSONObject getResponse() {
        return response;
    }
}
