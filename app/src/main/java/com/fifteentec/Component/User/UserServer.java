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
import com.fifteentec.yoko.TabActivity;

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
            password = APIEncrypt.AES.decrypt(APIKey.VALUE_AES_SEED_PASSWORD, sp.getString(APIKey.KEY_PASSWORD, null));
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
            edit.putString(APIKey.KEY_PASSWORD, APIEncrypt.AES.encrypt(APIKey.VALUE_AES_SEED_PASSWORD, password));
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

    public boolean setBaiduPushUserId(final String userId) {
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_BAIDUPUSH_USERID, userId);
        return edit.commit();
    }

    public boolean setBaiduPushChannelId(final String channelId) {
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_BAIDUPUSH_USERID, channelId);
        return edit.commit();
    }

    public void autoLogin(final BaseActivity activity) {
        if (access_token == null || refresh_token == null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            return;
        }

        Map<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        APIServer.JsonGet jsonGet = new APIServer.JsonGet(APIUrl.URL_REQUEST_USER_INFO,
                null, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Intent intent = new Intent(activity,
                        (this.getResponse() == null) ? LoginActivity.class : TabActivity.class);
                activity.startActivity(intent);
            }
        }, activity.getRequestQueue(), null);
        jsonGet.send();
    }

    public void userLogin(final LoginActivity activity, final String phone, final String password) {
        JSONObject params = new JSONObject();
        Map<String, String> headers = new HashMap<String, String>();
        try {
            params.put(APIKey.KEY_USERNAME, APIKey.VALUE_ROLE_MOBILE_PREFIX + phone);
            params.put(APIKey.KEY_PASSWORD, APIEncrypt.MD5.encode(password));
            params.put(APIKey.KEY_GRANT_TYPE, APIKey.KEY_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        headers.put(APIKey.KEY_AUTHORIZATION, APIKey.VALUE_REQUEST_TOKEN);
        headers.put(APIKey.KEY_ACCEPT, APIKey.VALUE_ACCEPT);

        APIServer.TokenPost tokenPost = new APIServer.TokenPost(APIUrl.URL_LOGIN,
                params, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                try {
                    if (this.getResponse() != null) {
                        if (this.getResponse().has(APIServer.STRING_ERROR_STATUS_CODE) &&
                                Integer.parseInt(this.getResponse().get(APIServer.STRING_ERROR_STATUS_CODE).toString())
                                        == APIServer.VALUE_BAD_REQUEST) {
                            Toast.makeText(activity.getApplicationContext(), "登录失败!",
                                    Toast.LENGTH_LONG).show();
                            activity.getAuthTask().afterPostExecute(false, this.getResponse());
                        } else {
/*
                            Toast.makeText(activity.getApplicationContext(), "登录成功!\n" + this.getResponse().toString(),
                                    Toast.LENGTH_LONG).show();
*/
                            UserServer.getInstance().setAccessToken(APIKey.VALUE_ACCESS_TONEN_HEADER_PREFIX +
                                    this.getResponse().getString(APIKey.KEY_ACCESS_TOKEN));
                            UserServer.getInstance().setRefreshToken(
                                    this.getResponse().getString(APIKey.KEY_REFRESH_TOKEN));
                            UserServer.getInstance().setPhone(phone);
                            UserServer.getInstance().setPassword(password);

                            Intent intent = new Intent(activity,
                                    TabActivity.class);
                            activity.startActivity(intent);
                            activity.getAuthTask().afterPostExecute(true, null);
                        }
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "登录失败!",
                                Toast.LENGTH_LONG).show();
                        JSONObject error_response = new JSONObject();
                        error_response.put(APIServer.STRING_ERROR_STATUS_CODE, APIServer.VALUE_NETWORK_CONNECTION_ERROR);
                        activity.getAuthTask().afterPostExecute(false, error_response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity.getRequestQueue(), null);

        tokenPost.send();
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

    public void userDelUserInfo(final BaseActivity activity) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(APIKey.KEY_AUTHORIZATION, UserServer.getInstance().getAccessToken());

        APIServer.JsonDel jsonDel = new APIServer.JsonDel(APIUrl.URL_REQUEST_USER_INFO,
                null, headers, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "获取用户信息失败!"
                                : "获取用户信息成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();
            }
        }, activity.getRequestQueue(), null);
        jsonDel.send();
    }
}