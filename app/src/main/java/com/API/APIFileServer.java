package com.API;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.InputStream;
import android.util.Log;

public class APIFileServer {
    private static APIFileServer server = new APIFileServer();
    public static APIFileServer getInstance() {
        return server;
    }

    /**
     * 用于想向务器发起文件下载请求
     * @param url 文件地址
     * @param callbackResponse 用于接收网络反馈的对象
     */
    public void sendFileGet(String url, final APIFileCallbackResponse callbackResponse) {
        Log.v("url", url);

        Request request = new Request.Builder().url(url).build();
        //在新线程中执行下载任务
        new Thread(new networkTask(request, callbackResponse)).start();
    }

    private class networkTask implements Runnable {
        private Request request;
        private APIFileCallbackResponse callbackResponse;

        public networkTask(Request request, final APIFileCallbackResponse callbackResponse) {
            this.request = request;
            this.callbackResponse = callbackResponse;
        }

        @Override
        public void run() {
            Response response = null;
            InputStream fileStream = null;
            try {
                response = new OkHttpClient().newCall(request).execute();
                fileStream = response.body().byteStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            callbackResponse.setResponse(fileStream);
            callbackResponse.run();
        }
    }
}
