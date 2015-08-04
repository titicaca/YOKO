package com.API;

/**
 * Created by benbush on 15/8/4.
 */
interface APICallbackResponse extends Runnable {
    public void setResponse(Object response);
    public Object getResponse();
}
