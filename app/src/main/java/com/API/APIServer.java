package com.API;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class APIServer {
    public final static int VALUE_NETWORK_CONNECTION_ERROR = -1;
    public final static int VALUE_BAD_REQUEST = 400;
    public final static int VALUE_UNAUTHORIZED = 401;
    public final static String STRING_ERROR_STATUS_CODE = "error_status_code";
    public final static String STRING_NETWORK_CONNECTION_ERROR = "无法连接到服务器";

    private static APIServer server = new APIServer();

    public static APIServer getInstance() {
        return server;
    }

    public abstract static class APINetworkRequest implements Response.ErrorListener {
        protected APICallbackResponse callbackResponse;
        protected RequestQueue queue;
        protected Request request;

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.v("VolleyError", (error.networkResponse == null)
                    ? "error.networkResponse = null"
                    : new String(error.networkResponse.data));

            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == VALUE_UNAUTHORIZED) {
                    requestAccessToken(request, queue);
                }
            } else {
                try {
                    Toast.makeText(BaseActivity.getCurrentActivity().getApplicationContext(),
                            STRING_NETWORK_CONNECTION_ERROR,
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.callbackResponse.setResponse(null);
                this.callbackResponse.run();
            }
        }

        public void send() {
            this.queue.add(this.request);
        }
    }

    public static class JsonArrayPost extends APINetworkRequest {
        /**
         * 用于发送JsonArray格式的POST请求
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public JsonArrayPost(String url, JSONArray params, final Map<String, String> headers,
                             final APICallbackResponse callbackResponse,
                             final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;
            this.request = new JsonArrayRequest(
                    Method.GET,
                    url,
                    params,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers == null)
                        return Collections.<String, String>emptyMap();

                    if (headers.containsKey(APIKey.KEY_AUTHORIZATION)) {
                        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
                    }
                    return headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }
    }

    public static class JsonPost extends APINetworkRequest {
        /**
         * 用于发送Json格式的POST请求
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public JsonPost(String url, JSONObject params, final Map<String, String> headers,
                        final APICallbackResponse callbackResponse,
                        final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;

            this.request = new JsonObjectRequest(
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers == null)
                        return Collections.<String, String>emptyMap();

                    if (headers.containsKey(APIKey.KEY_AUTHORIZATION)) {
                        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
                    }
                    return headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }
    }

    public static class JsonPut extends APINetworkRequest {
        /**
         * 用于发送Json格式的PUT请求
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public JsonPut(String url, JSONObject params, final Map<String, String> headers,
                       final APICallbackResponse callbackResponse,
                       final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;

            this.request = new JsonObjectRequest(
                    Method.PUT,
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers == null)
                        return Collections.<String, String>emptyMap();

                    if (headers.containsKey(APIKey.KEY_AUTHORIZATION)) {
                        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
                    }
                    return headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }
    }

    public static class JsonGet extends APINetworkRequest {
        /**
         * 用于发送Json格式的Get请求
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public JsonGet(String url, JSONObject params, final Map<String, String> headers,
                       final APICallbackResponse callbackResponse,
                       final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;

            if (params != null) {
                Iterator<String> keys = params.keys();

                boolean first = true;

                while (keys.hasNext()) {
                    String key = keys.next();

                    if (first) {
                        url += "?";
                        first = false;
                    } else {
                        url += "&";
                    }

                    try {
                        url += URLEncoder.encode(key, "UTF-8") + "="
                                + URLEncoder.encode(params.get(key).toString(), "UTF-8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            this.request = new JsonObjectRequest(
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers == null)
                        return Collections.<String, String>emptyMap();

                    if (headers.containsKey(APIKey.KEY_AUTHORIZATION)) {
                        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
                    }
                    return headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }
    }

    public static class StringPost extends APINetworkRequest {
        /**
         * 用于发送String格式的POST请求
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public StringPost(String url, final Map<String, String> params,
                          final Map<String, String> headers,
                          final APICallbackResponse callbackResponse,
                          final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;

            this.request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers == null)
                        return Collections.<String, String>emptyMap();

                    if (headers.containsKey(APIKey.KEY_AUTHORIZATION)) {
                        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
                    }
                    return headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }
    }

    public static class StringGet extends APINetworkRequest {
        /**
         * 用于发送String格式的Get请求
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public StringGet(String url, final Map<String, String> params,
                         final Map<String, String> headers,
                         final APICallbackResponse callbackResponse,
                         final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;

            if (params != null) {
                Iterator<String> keys = params.keySet().iterator();

                boolean first = true;

                while (keys.hasNext()) {
                    String key = keys.next();

                    if (first) {
                        url += "?";
                        first = false;
                    } else {
                        url += "&";
                    }

                    try {
                        url += URLEncoder.encode(key, "UTF-8") + "="
                                + URLEncoder.encode(params.get(key), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            this.request = new StringRequest(
                    Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    if (headers == null)
                        return Collections.<String, String>emptyMap();

                    if (headers.containsKey(APIKey.KEY_AUTHORIZATION)) {
                        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
                    }
                    return headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }
    }

    public static class AccessTokenPost extends APINetworkRequest {
        /**
         * 用于发送Json格式的POST请求(access_token)
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public AccessTokenPost(String url, JSONObject params, final Map<String, String> headers,
                               final APICallbackResponse callbackResponse,
                               final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;

            if (params != null) {
                Iterator<String> keys = params.keys();

                boolean first = true;

                while (keys.hasNext()) {
                    String key = keys.next();

                    if (first) {
                        url += "?";
                        first = false;
                    } else {
                        url += "&";
                    }

                    try {
                        url += URLEncoder.encode(key, "UTF-8") + "="
                                + URLEncoder.encode(params.get(key).toString(), "UTF-8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            this.request = new JsonObjectRequest(
                    Method.POST,
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return (headers == null) ? Collections.<String, String>emptyMap() : headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.v("VolleyError", (error.networkResponse == null)
                    ? "error.networkResponse = null"
                    : new String(error.networkResponse.data));

            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == VALUE_BAD_REQUEST) {
                    JSONObject error_response = new JSONObject();
                    try {
                        error_response.put(STRING_ERROR_STATUS_CODE, VALUE_BAD_REQUEST);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    this.callbackResponse.setResponse(error_response);
                    this.callbackResponse.run();
                }
            } else {
                try {
                    Toast.makeText(BaseActivity.getCurrentActivity().getApplicationContext(),
                            STRING_NETWORK_CONNECTION_ERROR,
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.callbackResponse.setResponse(null);
                this.callbackResponse.run();
            }
        }
    }

    public static class TokenPost extends APINetworkRequest {
        /**
         * 用于发送Json格式的POST请求(access_token + refresh_token)
         *
         * @param url              请求目标地址
         * @param params           请求所需参数
         * @param headers          请求所需文件头
         * @param callbackResponse 用于接收返回的实例化回调类
         * @param queue            Volley队列
         * @param tag              请求的Tag标签
         */
        public TokenPost(String url, JSONObject params, final Map<String, String> headers,
                         final APICallbackResponse callbackResponse,
                         final RequestQueue queue, final Object tag) {
            this.callbackResponse = callbackResponse;
            this.queue = queue;

            if (params != null) {
                Iterator<String> keys = params.keys();

                boolean first = true;

                while (keys.hasNext()) {
                    String key = keys.next();

                    if (first) {
                        url += "?";
                        first = false;
                    } else {
                        url += "&";
                    }

                    try {
                        url += URLEncoder.encode(key, "UTF-8") + "="
                                + URLEncoder.encode(params.get(key).toString(), "UTF-8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            this.request = new JsonObjectRequest(
                    Method.POST,
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            callbackResponse.setResponse(response);
                            callbackResponse.run();
                        }
                    }, this) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return (headers == null) ? Collections.<String, String>emptyMap() : headers;
                }
            };
            if (tag != null) this.request.setTag(tag);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.v("VolleyError", (error.networkResponse == null)
                    ? "error.networkResponse = null"
                    : new String(error.networkResponse.data));

            if (error.networkResponse != null) {
                if (error.networkResponse.statusCode == VALUE_BAD_REQUEST) {
                    JSONObject error_response = new JSONObject();
                    try {
                        error_response.put(STRING_ERROR_STATUS_CODE, VALUE_BAD_REQUEST);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    this.callbackResponse.setResponse(error_response);
                    this.callbackResponse.run();
                }
            } else {
                try {
                    Toast.makeText(BaseActivity.getCurrentActivity().getApplicationContext(),
                            STRING_NETWORK_CONNECTION_ERROR,
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.callbackResponse.setResponse(null);
                this.callbackResponse.run();
            }
        }
    }

    /**
     * 使用refresh_token请求access_token
     *
     * @param request volley网络请求
     * @param queue   volley队列
     */
    private static void requestAccessToken(final Request request, final RequestQueue queue) {
        JSONObject params = new JSONObject();
        Map<String, String> headers = new HashMap<String, String>();
        try {
            params.put(APIKey.KEY_GRANT_TYPE, APIKey.KEY_REFRESH_TOKEN);
            params.put(APIKey.KEY_REFRESH_TOKEN, UserServer.getInstance().getRefreshToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        headers.put(APIKey.KEY_AUTHORIZATION, APIKey.KEY_REQUEST_TOKEN_VALUE);
        headers.put(APIKey.KEY_ACCEPT, APIKey.KEY_ACCEPT_VALUE);
        APIServer.AccessTokenPost accessTokenPost = new APIServer.AccessTokenPost(APIUrl.URL_REQUEST_TOKEN,
                params, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                if (this.getResponse() != null) {
                    try {
                        if (this.getResponse().has(STRING_ERROR_STATUS_CODE) &&
                                Integer.parseInt(this.getResponse().get(STRING_ERROR_STATUS_CODE).toString())
                                        == VALUE_BAD_REQUEST) {
                            requestToken(UserServer.getInstance().getPhone(),
                                    UserServer.getInstance().getPassword(),
                                    request, queue);
                        } else {
                            if (this.getResponse().has(APIKey.KEY_ACCESS_TOKEN)) {
                                UserServer.getInstance().setAccessToken(APIKey.KEY_ACCESS_TONEN_HEADER_PREFIX +
                                        this.getResponse().getString(APIKey.KEY_ACCESS_TOKEN));
                                queue.add(request);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, queue, null);

        accessTokenPost.send();
    }

    /**
     * 使用手机号和密码请求Token
     *
     * @param phone    手机号码
     * @param password 密码
     * @param request  volley网络请求
     * @param queue    volley队列
     */
    private static void requestToken(final String phone, final String password,
                                     final Request request, final RequestQueue queue) {
        JSONObject params = new JSONObject();
        Map<String, String> headers = new HashMap<String, String>();
        try {
            params.put(APIKey.KEY_USERNAME, APIKey.KEY_ROLE_MOBILE_PREFIX + phone);
            params.put(APIKey.KEY_PASSWORD, APIEncrypt.MD5.encode(password));
            params.put(APIKey.KEY_GRANT_TYPE, APIKey.KEY_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        headers.put(APIKey.KEY_AUTHORIZATION, APIKey.KEY_REQUEST_TOKEN_VALUE);
        headers.put(APIKey.KEY_ACCEPT, APIKey.KEY_ACCEPT_VALUE);

        APIServer.TokenPost tokenPost = new APIServer.TokenPost(APIUrl.URL_REQUEST_TOKEN,
                params, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                if (this.getResponse() != null) {
                    try {
                        if (this.getResponse().has(STRING_ERROR_STATUS_CODE) &&
                                Integer.parseInt(this.getResponse().get(STRING_ERROR_STATUS_CODE).toString())
                                        == VALUE_BAD_REQUEST) {
                            Intent intent = new Intent(BaseActivity.getCurrentActivity(),
                                    LoginActivity.class);
                            BaseActivity.getCurrentActivity().startActivity(intent);
                        } else {
                            if (this.getResponse().has(APIKey.KEY_ACCESS_TOKEN) &&
                                    this.getResponse().has(APIKey.KEY_REFRESH_TOKEN)) {
                                UserServer.getInstance().setAccessToken(APIKey.KEY_ACCESS_TONEN_HEADER_PREFIX +
                                        this.getResponse().getString(APIKey.KEY_ACCESS_TOKEN));
                                UserServer.getInstance().setRefreshToken(
                                        this.getResponse().getString(APIKey.KEY_REFRESH_TOKEN));
                                queue.add(request);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, queue, null);

        tokenPost.send();
    }
}
