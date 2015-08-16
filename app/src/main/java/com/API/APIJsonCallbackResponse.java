package com.API;

import org.json.JSONObject;

/**
 * 用于接收Json格式的Response
 */
public abstract class APIJsonCallbackResponse implements APICallbackResponse<JSONObject> {
    private JSONObject response;

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public JSONObject getResponse() {
        return response;
    }
}
