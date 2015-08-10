package com.API;

/**
 * 用于接收String格式的Response
 */
public abstract class APIStringCallbackResponse implements APICallbackResponse<String> {
    private String response;

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}