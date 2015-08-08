package com.fifteentec.Component.User;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.API.APIEncrypt;
import com.API.APIFileServer;
import com.API.APIJsonCallbackResponse;
import com.API.APIKey;
import com.API.APIServer;
import com.API.APIUrl;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.LoginActivity;
import com.fifteentec.yoko.TestActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserServer {
    private static UserServer userServer = new UserServer();

    public static UserServer getInstance() {
        return userServer;
    }

    private final APIServer apiServer = APIServer.getInstance();
    private final APIFileServer apiFileServer = APIFileServer.getInstance();

    private Application application = null;

    private SharedPreferences sp = null;
    private String phone = null;
    private String username = null;
    private String password = null;
    private String access_token = null;
    private String refresh_token = null;

    public SharedPreferences getSharedPreferences() {
        return this.sp;
    }

    public void setSharedPreferences(SharedPreferences sp) {
        this.sp = sp;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return this.application;
    }

    public void loadSharedPreferences() {
        phone = sp.getString(APIKey.KEY_PHONE, null);
        username = sp.getString(APIKey.KEY_USERNAME, null);
        try {
            password = APIEncrypt.AES.decrypt(APIKey.KEY_AES_SEED_PASSWORD, sp.getString(APIKey.KEY_PASSWORD, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        access_token = sp.getString(APIKey.KEY_ACCESS_TOKEN, null);
        refresh_token = sp.getString(APIKey.KEY_REFRESH_TOKEN, null);
    }

    public String getPhone() {
        return this.phone;
    }

    public boolean setPhone(final String phone) {
        this.phone = phone;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_PHONE, phone);
        return edit.commit();
    }

    public String getUsername() {
        return this.username;
    }

    public boolean setUsername(final String username) {
        this.username = username;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_USERNAME, username);
        return edit.commit();
    }

    public String getPassword() {
        return this.password;
    }

    public boolean setPassword(final String password) {
        this.password = password;
        Editor edit = sp.edit();
        try {
            edit.putString(APIKey.KEY_PASSWORD, APIEncrypt.AES.encrypt(APIKey.KEY_AES_SEED_PASSWORD, password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return edit.commit();
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public boolean setAccessToken(final String access_token) {
        this.access_token = access_token;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_ACCESS_TOKEN, access_token);
        return edit.commit();
    }

    public String getRefreshToken() {
        return this.refresh_token;
    }

    public boolean setRefreshToken(final String refresh_token) {
        this.refresh_token = refresh_token;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_REFRESH_TOKEN, refresh_token);
        return edit.commit();
    }

    public void autoLogin(final BaseActivity activity) {
        if (access_token == null || refresh_token == null)
            return;

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());

        APIServer.JsonGet jsonGet = new APIServer.JsonGet(APIUrl.URL_REQUEST_USER_INFO,
                null, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Intent intent = new Intent(activity,
                        (this.getResponse() == null) ? LoginActivity.class : TestActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }, activity.getRequestQueue(), null);
        jsonGet.send();
    }

    public void userLogin(final LoginActivity activity, final String phone, final String password) {
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

        APIServer._JsonPost _jsonPost = new APIServer._JsonPost(APIUrl.URL_LOGIN,
                params, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "登录失败!"
                                : "登录成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();
                if (this.getResponse() != null) {
                    try {
                        UserServer.getInstance().setAccessToken(APIKey.KEY_ACCESS_TONEN_HEADER_PREFIX +
                                this.getResponse().getString(APIKey.KEY_ACCESS_TOKEN));
                        UserServer.getInstance().setRefreshToken(
                                this.getResponse().getString(APIKey.KEY_REFRESH_TOKEN));
                        UserServer.getInstance().setPhone(phone);
                        UserServer.getInstance().setPassword(password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(activity,
                            TestActivity.class);
                    activity.startActivity(intent);
                    activity.getAuthTask().afterPostExecute(true);
                } else {
                    activity.getAuthTask().afterPostExecute(false);
                }
            }
        }, activity.getRequestQueue(), null);

        _jsonPost.send();
    }

    public void userLogout() {

    }

    public void userRegister(final BaseActivity activity, final String phone,
                             final String username, final String password) {
        JSONObject params = new JSONObject();
        try {
            params.put(APIKey.KEY_ROLE_MOBILE, phone);
            params.put(APIKey.KEY_NAME, username);
            params.put(APIKey.KEY_PASSWORD, APIEncrypt.MD5.encode(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        APIServer.JsonPost jsonPost = new APIServer.JsonPost(APIUrl.URL_REGISTER,
                params, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "注册失败!请重试!"
                                : "恭喜你!注册成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();
                if (this.getResponse() != null) {
                    UserServer.getInstance().setUsername(username);
                    UserServer.getInstance().setPassword(password);
                    Intent intent = new Intent(activity,
                            LoginActivity.class);
                    activity.startActivity(intent);
                }
            }
        }, activity.getRequestQueue(), null);
        jsonPost.send();
    }

    public void userChangePswd(final BaseActivity activity, final String phone, final String password) {
        JSONObject params = new JSONObject();
        try {
            params.put(APIKey.KEY_ROLE_MOBILE, phone);
            params.put(APIKey.KEY_PASSWORD, APIEncrypt.MD5.encode(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        APIServer.JsonPut jsonPut = new APIServer.JsonPut(APIUrl.URL_CHANGE_PASSWORD,
                params, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "修改密码失败!请重试!"
                                : "恭喜你!修改密码成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();
                if (this.getResponse() != null) {
                    UserServer.getInstance().setPassword(password);
                    Intent intent = new Intent(activity,
                            LoginActivity.class);
                    activity.startActivity(intent);
                }
            }
        }, activity.getRequestQueue(), null);
        jsonPut.send();
    }

    public void userGetUserInfo(final BaseActivity activity) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());

        APIServer.JsonGet jsonGet = new APIServer.JsonGet(APIUrl.URL_REQUEST_USER_INFO,
                null, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "获取用户信息失败!"
                                : "获取用户信息成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();
            }
        }, activity.getRequestQueue(), null);
        jsonGet.send();
    }
}