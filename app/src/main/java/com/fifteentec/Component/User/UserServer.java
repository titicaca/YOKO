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
import com.API.APIUserServer;
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

    private String access_token = null;
    private String refresh_token = null;
    private SharedPreferences sp = null;
    private String mobile = null;
    private String username = null;
    private String password = null;
    private long userid = 0;
    private String nickname = null;
    private int sex = 0;
    private String location = null;
    private String email = null;
    private String qq = null;
    private String wechat = null;
    private String weibo = null;
    private String picturelink = null;
    private int collectnumber = 0;
    private int enrollnumber = 0;
    private int friendnumber = 0;

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
        access_token = sp.getString(APIKey.KEY_ACCESS_TOKEN, null);
        refresh_token = sp.getString(APIKey.KEY_REFRESH_TOKEN, null);
        try {
            password = APIEncrypt.AES.decrypt(APIKey.VALUE_AES_SEED_PASSWORD, sp.getString(APIKey.KEY_PASSWORD, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userid = sp.getLong(APIKey.KEY_USER_ID, 0);
        nickname = sp.getString(APIKey.KEY_NICKNAME, null);
        mobile = sp.getString(APIKey.KEY_MOBILE, null);
        sex = sp.getInt(APIKey.KEY_SEX, 0);
        location = sp.getString(APIKey.KEY_LOCATION, null);
        email = sp.getString(APIKey.KEY_EMAIL, null);
        qq = sp.getString(APIKey.KEY_QQ, null);
        wechat = sp.getString(APIKey.KEY_WECHAT, null);
        weibo = sp.getString(APIKey.KEY_WEIBO, null);
        picturelink = sp.getString(APIKey.KEY_PICTURE_LINK, null);
        collectnumber = sp.getInt(APIKey.KEY_COLLECT_NUMBER, 0);
        enrollnumber = sp.getInt(APIKey.KEY_ENROLL_NUMBER, 0);
        friendnumber = sp.getInt(APIKey.KEY_FRIEND_NUMBER, 0);
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

    public boolean clearPassword() {
        password = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_PASSWORD);
        return edit.commit();
    }

    public long getUserid() {
        return this.userid;
    }

    public boolean setUserid(final long userid) {
        this.userid = userid;
        Editor edit = sp.edit();
        edit.putLong(APIKey.KEY_USER_ID, userid);
        return edit.commit();
    }

    public boolean clearUserid() {
        userid = 0;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_USER_ID);
        return edit.commit();
    }

    public String getNickname() {
        return this.nickname;
    }

    public boolean setNickname(final String nickname) {
        this.nickname = nickname;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_NICKNAME, nickname);
        return edit.commit();
    }

    public boolean clearNickname() {
        nickname = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_NICKNAME);
        return edit.commit();
    }

    public String getMobile() {
        return this.mobile;
    }

    public boolean setMobile(final String mobile) {
        this.mobile = mobile;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_MOBILE, mobile);
        return edit.commit();
    }

    public boolean clearMobile() {
        mobile = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_MOBILE);
        return edit.commit();
    }

    public int getSex() {
        return this.sex;
    }

    public boolean setSex(final int sex) {
        this.sex = sex;
        Editor edit = sp.edit();
        edit.putInt(APIKey.KEY_SEX, sex);
        return edit.commit();
    }

    public boolean clearSex() {
        sex = 0;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_SEX);
        return edit.commit();
    }

    public String getLocation() {
        return this.location;
    }

    public boolean setLocation(final String location) {
        this.location = location;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_LOCATION, location);
        return edit.commit();
    }

    public boolean clearLocation() {
        location = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_LOCATION);
        return edit.commit();
    }

    public String getEmail() {
        return this.email;
    }

    public boolean setEmail(final String email) {
        this.email = email;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_EMAIL, email);
        return edit.commit();
    }

    public boolean clearEmail() {
        email = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_EMAIL);
        return edit.commit();
    }

    public String getQQ() {
        return this.qq;
    }

    public boolean setQQ(final String qq) {
        this.qq = qq;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_QQ, qq);
        return edit.commit();
    }

    public boolean clearQQ() {
        qq = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_QQ);
        return edit.commit();
    }

    public String getWechat() {
        return this.wechat;
    }

    public boolean setWechat(final String wechat) {
        this.wechat = wechat;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_WECHAT, wechat);
        return edit.commit();
    }

    public boolean clearWechat() {
        wechat = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_WECHAT);
        return edit.commit();
    }

    public String getWeibo() {
        return this.weibo;
    }

    public boolean setWeibo(final String weibo) {
        this.weibo = weibo;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_WEIBO, weibo);
        return edit.commit();
    }

    public boolean clearWeibo() {
        weibo = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_WEIBO);
        return edit.commit();
    }

    public String getPictureLink() {
        return this.picturelink;
    }

    public boolean setPictureLink(final String picturelink) {
        this.picturelink = picturelink;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_PICTURE_LINK, picturelink);
        return edit.commit();
    }

    public boolean clearPictureLink() {
        picturelink = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_PICTURE_LINK);
        return edit.commit();
    }

    public int getCollectNumber() {
        return this.collectnumber;
    }

    public boolean setCollectNumber(final int collectnumber) {
        this.collectnumber = collectnumber;
        Editor edit = sp.edit();
        edit.putInt(APIKey.KEY_COLLECT_NUMBER, collectnumber);
        return edit.commit();
    }

    public boolean clearCollectNumber() {
        collectnumber = 0;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_COLLECT_NUMBER);
        return edit.commit();
    }

    public int getEnrollNumber() {
        return this.enrollnumber;
    }

    public boolean setEnrollNumber(final int enrollnumber) {
        this.enrollnumber = enrollnumber;
        Editor edit = sp.edit();
        edit.putInt(APIKey.KEY_ENROLL_NUMBER, enrollnumber);
        return edit.commit();
    }

    public boolean clearEnrollNumber() {
        enrollnumber = 0;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_ENROLL_NUMBER);
        return edit.commit();
    }

    public int getFriendNumber() {
        return this.friendnumber;
    }

    public boolean setFriendNumber(final int friendnumber) {
        this.friendnumber = friendnumber;
        Editor edit = sp.edit();
        edit.putInt(APIKey.KEY_FRIEND_NUMBER, friendnumber);
        return edit.commit();
    }

    public boolean clearFriendNumber() {
        friendnumber = 0;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_FRIEND_NUMBER);
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

    public boolean clearAccessToken() {
        access_token = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_ACCESS_TOKEN);
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

    public boolean clearRefreshToken() {
        refresh_token = null;
        Editor edit = sp.edit();
        edit.remove(APIKey.KEY_REFRESH_TOKEN);
        return edit.commit();
    }

    public String getBaiduPushUserId() {
        return sp.getString(APIKey.KEY_BAIDU_PUSH_USER_ID, null);
    }

    public boolean setBaiduPushUserId(final String userId) {
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_BAIDU_PUSH_USER_ID, userId);
        return edit.commit();
    }

    public String getBaiduPushChannelId() {
        return sp.getString(APIKey.KEY_BAIDU_PUSH_CHANNEL_ID, null);
    }

    public boolean setBaiduPushChannelId(final String channelId) {
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_BAIDU_PUSH_CHANNEL_ID, channelId);
        return edit.commit();
    }

    public void autoLogin(final BaseActivity activity) {
        if (access_token == null || refresh_token == null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            return;
        }

        new APIUserServer.JsonGet(APIUrl.URL_REQUEST_USER_INFO,
                null, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                if (this.getResponse() != null) {
                    userGetUserInfo(activity);
                }

                Intent intent = new Intent(activity,
                        (this.getResponse() == null) ? LoginActivity.class : TabActivity.class);
                activity.startActivity(intent);
            }
        }, activity.getRequestQueue(), null).send();
    }

    public void userLogin(final LoginActivity activity, final String mobile, final String password) {
        JSONObject params = new JSONObject();
        Map<String, String> headers = new HashMap<String, String>();
        try {
            params.put(APIKey.KEY_USERNAME, APIKey.VALUE_ROLE_MOBILE_PREFIX + mobile);
            params.put(APIKey.KEY_PASSWORD, APIEncrypt.MD5.encode(password));
            params.put(APIKey.KEY_GRANT_TYPE, APIKey.KEY_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        headers.put(APIKey.KEY_AUTHORIZATION, APIKey.VALUE_REQUEST_TOKEN);
        headers.put(APIKey.KEY_ACCEPT, APIKey.VALUE_ACCEPT);

        new APIServer.TokenPost(APIUrl.URL_LOGIN,
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
                            UserServer.getInstance().setMobile(mobile);
                            UserServer.getInstance().setPassword(password);

                            userGetUserInfo(activity);

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
        }, activity.getRequestQueue(), null).send();
    }

    public void userLogout(final BaseActivity activity) {
        clearAccessToken();
        clearRefreshToken();
        clearUserid();
        clearNickname();
        clearSex();
        clearLocation();
        clearEmail();
        clearQQ();
        clearWechat();
        clearWeibo();
        clearPictureLink();
        clearCollectNumber();
        clearEnrollNumber();
        clearFriendNumber();

        Intent intent = new Intent(activity,
                LoginActivity.class);
        activity.startActivity(intent);
    }

    public void userRegister(final BaseActivity activity, final String mobile,
                             final String nickname, final String password) {
        JSONObject params = new JSONObject();
        try {
            params.put(APIKey.KEY_ROLE_MOBILE, mobile);
            params.put(APIKey.KEY_NAME, nickname);
            params.put(APIKey.KEY_PASSWORD, APIEncrypt.MD5.encode(password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        new APIServer.JsonPost(APIUrl.URL_REGISTER,
                params, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "注册失败!请重试!"
                                : "恭喜你!注册成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();
                if (this.getResponse() != null) {
                    UserServer.getInstance().setNickname(nickname);
                    UserServer.getInstance().setPassword(password);
                    Intent intent = new Intent(activity,
                            LoginActivity.class);
                    activity.startActivity(intent);
                }
            }
        }, activity.getRequestQueue(), null).send();
    }

    public void userChangePswd(final BaseActivity activity, final String mobile, final String password) {
        JSONObject params = new JSONObject();
        try {
            params.put(APIKey.KEY_ROLE_MOBILE, mobile);
            params.put(APIKey.KEY_PASSWORD, APIEncrypt.MD5.encode(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        new APIServer.JsonPut(APIUrl.URL_CHANGE_PASSWORD,
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
        }, activity.getRequestQueue(), null).send();
    }

    public void userGetUserInfo(final BaseActivity activity) {
        JSONObject params = new JSONObject();
        try {
            params.put(APIKey.KEY_BAIDU_PUSH_USER_ID, getBaiduPushUserId());
            params.put(APIKey.KEY_BAIDU_PUSH_CHANNEL_ID, getBaiduPushChannelId());
            params.put(APIKey.KEY_DEVICE_TYPE, APIKey.VALUE_DEVICE_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new APIUserServer.JsonPost(APIUrl.URL_PUSH_INFO,
                params, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                /*Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "获取用户信息失败!"
                                : "获取用户信息成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();*/

                if (this.getResponse() != null) {
                    try {
                        if (this.getResponse().has(APIKey.KEY_USER_ID)) {
                            setUserid(Long.parseLong(this.getResponse().get(APIKey.KEY_USER_ID).toString()));
                        }
                        if (this.getResponse().has(APIKey.KEY_NICKNAME)) {
                            setNickname(this.getResponse().get(APIKey.KEY_NICKNAME).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_MOBILE)) {
                            setMobile(this.getResponse().get(APIKey.KEY_MOBILE).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_SEX)) {
                            setSex(Integer.parseInt(this.getResponse().get(APIKey.KEY_SEX).toString()));
                        }
                        if (this.getResponse().has(APIKey.KEY_LOCATION)) {
                            setLocation(this.getResponse().get(APIKey.KEY_LOCATION).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_EMAIL)) {
                            setEmail(this.getResponse().get(APIKey.KEY_EMAIL).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_QQ)) {
                            setQQ(this.getResponse().get(APIKey.KEY_QQ).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_WECHAT)) {
                            setWechat(this.getResponse().get(APIKey.KEY_WECHAT).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_WEIBO)) {
                            setWeibo(this.getResponse().get(APIKey.KEY_WEIBO).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_PICTURE_LINK)) {
                            setPictureLink(this.getResponse().get(APIKey.KEY_PICTURE_LINK).toString());
                        }
                        if (this.getResponse().has(APIKey.KEY_COLLECT_NUMBER)) {
                            setCollectNumber(Integer.parseInt(this.getResponse().get(APIKey.KEY_COLLECT_NUMBER).toString()));
                        }
                        if (this.getResponse().has(APIKey.KEY_ENROLL_NUMBER)) {
                            setEnrollNumber(Integer.parseInt(this.getResponse().get(APIKey.KEY_ENROLL_NUMBER).toString()));
                        }
                        if (this.getResponse().has(APIKey.KEY_FRIEND_NUMBER)) {
                            setFriendNumber(Integer.parseInt(this.getResponse().get(APIKey.KEY_FRIEND_NUMBER).toString()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, activity.getRequestQueue(), null).send();

    }

    public void userDelUserInfo(final BaseActivity activity) {
        new APIUserServer.JsonDel(APIUrl.URL_REQUEST_USER_INFO,
                null, null, new APIJsonCallbackResponse() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), ((this.getResponse() == null)
                                ? "删除用户信息失败!"
                                : "删除用户信息成功!\n" + this.getResponse().toString()),
                        Toast.LENGTH_LONG).show();
            }
        }, activity.getRequestQueue(), null).send();
    }
}