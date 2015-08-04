package com.API;

interface APICallbackResponse<T> extends Runnable {
    public void setResponse(T response);
    public T getResponse();
}
