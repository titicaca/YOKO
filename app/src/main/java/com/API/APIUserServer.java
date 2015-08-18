package com.API;

import com.android.volley.RequestQueue;
import com.fifteentec.Component.User.UserServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIUserServer {
    private static APIUserServer server = new APIUserServer();

    public static APIUserServer getInstance() {
        return server;
    }

    public static Map<String, String> addTokenToHeaders(Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<String, String>();
            headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
        } else if (!headers.containsKey(APIKey.KEY_AUTHORIZATION)) {
            headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
        }

        return headers;
    }

    public static class JsonArrayPost extends APIServer.JsonArrayPost {
        public JsonArrayPost(String url, final JSONArray params,
                      final Map<String, String> headers,
                      final APICallbackResponse callbackResponse,
                      final RequestQueue queue, final Object tag) {
            super(url, params, addTokenToHeaders(headers), callbackResponse, queue, tag);
        }
    }

    public static class JsonGet extends APIServer.JsonGet {
        public JsonGet(String url, final JSONObject params,
                final Map<String, String> headers,
                final APICallbackResponse callbackResponse,
                final RequestQueue queue, final Object tag) {
            super(url, params, addTokenToHeaders(headers), callbackResponse, queue, tag);
        }
    }

    public static class JsonPost extends APIServer.JsonPost {
        public JsonPost(String url, final JSONObject params,
                 final Map<String, String> headers,
                 final APICallbackResponse callbackResponse,
                 final RequestQueue queue, final Object tag) {
            super(url, params, addTokenToHeaders(headers), callbackResponse, queue, tag);
        }
    }

    public static class JsonPut extends APIServer.JsonPut {
        public JsonPut(String url, final JSONObject params,
                final Map<String, String> headers,
                final APICallbackResponse callbackResponse,
                final RequestQueue queue, final Object tag) {
            super(url, params, addTokenToHeaders(headers), callbackResponse, queue, tag);
        }
    }

    public static class JsonDel extends APIServer.JsonDel {
        public JsonDel(String url, final JSONObject params,
                final Map<String, String> headers,
                final APICallbackResponse callbackResponse,
                final RequestQueue queue, final Object tag) {
            super(url, params, addTokenToHeaders(headers), callbackResponse, queue, tag);
        }
    }

    public static class StringGet extends APIServer.StringGet {
        public StringGet(String url, final JSONObject params,
                  final Map<String, String> headers,
                  final APICallbackResponse callbackResponse,
                  final RequestQueue queue, final Object tag) {
            super(url, params, addTokenToHeaders(headers), callbackResponse, queue, tag);
        }
    }

    public static class StringPost extends APIServer.StringPost {
        public StringPost(String url, final JSONObject params,
                   final Map<String, String> headers,
                   final APICallbackResponse callbackResponse,
                   final RequestQueue queue, final Object tag) {
            super(url, params, addTokenToHeaders(headers), callbackResponse, queue, tag);
        }
    }
}
