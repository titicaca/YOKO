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
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendJsonArrayPost(String url, JSONArray params,
                                  final APIJsonArrayCallbackResponse callbackResponse,
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
                        Log.v("VolleyError", error.toString());
                        error.printStackTrace();
                        JSONArray error_response = null;
                        try {
                            error_response = new JSONArray("[{status:error}]");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callbackResponse.setResponse(error_response);
                        callbackResponse.run();
                    }
                }
        );
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送Json格式的POST请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendJsonPost(String url, JSONObject params,
                             final APIJsonCallbackResponse callbackResponse,
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
                        Log.v("VolleyError", error.toString());
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
        );
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
                        Log.v("VolleyError", error.toString());
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
        );
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }

    /**
     * 用于发送String格式的POST请求
     * @param url 请求目标地址
     * @param params 请求所需参数
     * @param callbackResponse 用于接收返回的实例化回调类
     * @param queue Volley队列
     * @param tag 请求的Tag标签
     */
    public void sendStringPost(String url, final Map<String, String> params,
                              final APIStringCallbackResponse callbackResponse,
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
                        Log.v("VolleyError", error.toString());
                        error.printStackTrace();
                        String error_response = new String("success:failed");
                        callbackResponse.setResponse(error_response);
                        callbackResponse.run();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
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
                              final APIStringCallbackResponse callbackResponse,
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
                        Log.v("VolleyError", error.toString());
                        error.printStackTrace();
                        String error_response = new String("success:failed");
                        callbackResponse.setResponse(error_response);
                        callbackResponse.run();
                    }
                }
        );
        if (tag != null) request.setTag(tag);

        queue.add(request);
    }
}
