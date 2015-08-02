package com.API;

import java.io.InputStream;

/**
 * Created by benbush on 15/7/6.
 * 用于接收InputStream格式的Response
 */
public abstract class APIFileCallbackResponse implements Runnable{
    private InputStream response;

    public InputStream getResponse() {
        return this.response;
    }

    public void setResponse(InputStream response) {
        this.response = response;
    }
}
