package com.API;

/**
 * Created by benbush on 15/6/24.
 * 用于接收String格式的Response
 */
public abstract class APIStringCallbackResponse implements Runnable {
    private String response;

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
