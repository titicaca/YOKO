package com.API;

import java.io.InputStream;

/**
 * 用于接收InputStream格式的Response
 */
public abstract class APIFileCallbackResponse implements APICallbackResponse<InputStream>{
    private InputStream response;

    public InputStream getResponse() {
        return this.response;
    }

    public void setResponse(InputStream response) {
        this.response = response;
    }
}
