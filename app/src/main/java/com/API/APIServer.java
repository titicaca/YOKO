package com.API;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class APIServer {
    public final static int VALUE_NETWORK_CONNECTION_ERROR = -1;

    private static APIServer server = new APIServer();
    public static APIServer getInstance() {
        return server;
    }

    /**
     * 用于发送JsonArray格式的POST请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param headers 请求所需文件头
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendJsonArrayPost(String url, JSONArray params, final Map<String, String> headers,
                                  final APICallbackResponse callbackResponse,
                                  RequestQueue queue, final Object tag) {
        JsonArrayRequest request = new JsonArrayRequest(
                Method.GET,
                url,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callbackResponse.setResponse(response);
                        callbackResponse.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("VolleyEroor", new String(error.networkResponse.data));

                        callbackResponse.setResponse(null);
                        callbackResponse.run();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return (headers == null) ? Collections.<String, String>emptyMap() : headers;
            }
        };
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送Json格式的POST请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param headers 请求所需文件头
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendJsonPost(String url, JSONObject params, final Map<String, String> headers,
                             final APICallbackResponse callbackResponse,
                             RequestQueue queue, final Object tag) {
        JsonObjectRequest request = new JsonObjectRequest(
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callbackResponse.setResponse(response);
                        callbackResponse.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("VolleyEroor", new String(error.networkResponse.data));

                        callbackResponse.setResponse(null);
                        callbackResponse.run();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return (headers == null) ? Collections.<String, String>emptyMap() : headers;
            }
        };
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送Json格式的PUT请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param headers 请求所需文件头
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendJsonPut(String url, JSONObject params, final Map<String, String> headers,
                            final APIJsonCallbackResponse callbackResponse,
                            RequestQueue queue, final Object tag) {
        JsonObjectRequest request = new JsonObjectRequest(
                Method.PUT,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callbackResponse.setResponse(response);
                        callbackResponse.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("VolleyError", error.toString());
                        Log.v("VolleyError_StatusCode", ((Integer) error.networkResponse.statusCode).toString());
                        error.printStackTrace();

                        byte[] htmlBodyBytes = error.networkResponse.data;
                        Log.v("VolleyError", new String(htmlBodyBytes), error);

                        JSONObject error_response = new JSONObject();
                        try {
                            error_response.put(APIKey.KEY_STATUS, VALUE_NETWORK_CONNECTION_ERROR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callbackResponse.setResponse(error_response);
                        callbackResponse.run();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return (headers == null) ? Collections.<String, String>emptyMap() : headers;
            }
        };
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送Json格式的Get请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendJsonGet(String url, JSONObject params,
                            final APICallbackResponse callbackResponse,
                            RequestQueue queue, final Object tag) {
        if (params != null) {
            Iterator<String> keys = params.keys();

            boolean first = true;

            while (keys.hasNext()) {
                String key = keys.next();

                if (first) {
                    url += "?";
                    first =false;
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

        JsonObjectRequest request = new JsonObjectRequest(
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callbackResponse.setResponse(response);
                        callbackResponse.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("VolleyEroor", new String(error.networkResponse.data));

                        callbackResponse.setResponse(null);
                        callbackResponse.run();
                    }
                }
        );
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送Json格式的Post请求，用于登录
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param headers 请求所需文件头
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void _sendJsonPost(String url, JSONObject params, final Map<String, String> headers,
                              final APIJsonCallbackResponse callbackResponse,
                              RequestQueue queue, final Object tag) {
        if (params != null) {
            Iterator<String> keys = params.keys();

            boolean first = true;

            while (keys.hasNext()) {
                String key = keys.next();

                if (first) {
                    url += "?";
                    first =false;
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

        JsonObjectRequest request = new JsonObjectRequest(
                Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callbackResponse.setResponse(response);
                        callbackResponse.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("VolleyError", error.toString());
                        //Log.v("VolleyError_StatusCode", ((Integer)error.networkResponse.statusCode).toString());
                        error.printStackTrace();
                        JSONObject error_response = new JSONObject();
                        try {
                            error_response.put(APIKey.KEY_STATUS, VALUE_NETWORK_CONNECTION_ERROR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callbackResponse.setResponse(error_response);
                        callbackResponse.run();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return (headers == null) ? Collections.<String, String>emptyMap() : headers;
            }
        };
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送String格式的POST请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param headers 请求所需文件头
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendStringPost(String url, final Map<String, String> params,
                               final Map<String, String> headers,
                               final APICallbackResponse callbackResponse,
                              RequestQueue queue, final Object tag) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callbackResponse.setResponse(response);
                        callbackResponse.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("VolleyEroor", new String(error.networkResponse.data));

                        callbackResponse.setResponse(null);
                        callbackResponse.run();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return (headers == null) ? Collections.<String, String>emptyMap() : headers;
            }
        };
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送String格式的Get请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendStringGet(String url, final Map<String, String> params,
                              final APICallbackResponse callbackResponse,
                              RequestQueue queue, final Object tag) {
        if (params != null) {
            Iterator<String> keys = params.keySet().iterator();

            boolean first = true;

            while (keys.hasNext()) {
                String key = keys.next();

                if (first) {
                    url += "?";
                    first =false;
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

        Log.v("url:", url);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callbackResponse.setResponse(response);
                        callbackResponse.run();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("VolleyEroor", new String(error.networkResponse.data));

                        callbackResponse.setResponse(null);
                        callbackResponse.run();
                    }
                }
        );
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }
}
