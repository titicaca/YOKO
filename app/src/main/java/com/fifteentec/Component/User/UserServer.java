package com.fifteentec.Component.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.API.APIFileServer;
import com.API.APIKey;
import com.API.APIServer;

public class UserServer {
    private static UserServer userServer = new UserServer();

    public static UserServer getInstance() {
        return userServer;
    }
    private final APIServer apiServer = APIServer.getInstance();
    private final APIFileServer apiFileServer = APIFileServer.getInstance();

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

    public void loadSharedPreferences(){
        phone = sp.getString(APIKey.KEY_PHONE, null);
        username = sp.getString(APIKey.KEY_USERNAME, null);
        password = sp.getString(APIKey.KEY_PASSWORD, null);
        access_token= sp.getString(APIKey.KEY_ACCESS_TOKEN, null);
        refresh_token = sp.getString(APIKey.KEY_REFRESH_TOKEN, null);
    }

    public void autoLogin(Context packageContext, Class<?> cls){
        if(access_token == null && refresh_token == null)
            return;

        Intent intent = new Intent(packageContext, cls);
        packageContext.startActivity(intent);
    }

    public void userLogin(Context packageContext, Class<?> cls){
        Intent intent = new Intent(packageContext, cls);
        packageContext.startActivity(intent);
    }

    public void userLogout(Context packageContext, Class<?> cls){
        Intent intent = new Intent(packageContext, cls);
        packageContext.startActivity(intent);
    }

    public boolean setPhone(final String phone) {
        this.phone = phone;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_PHONE, phone);
        return edit.commit();
    }

    public String getPhone() {
        return this.phone;
    }

    public boolean setUsername(final String username) {
        this.username = username;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_USERNAME, username);
        return edit.commit();
    }

    public String getUsername() {
        return this.username;
    }

    public boolean setPassword(final String password) {
        this.password = password;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_PASSWORD, password);
        return edit.commit();
    }

    public String GetPassword() {
        return this.password;
    }

    public boolean setAccessToken(final String access_token){
        this.access_token = access_token;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_ACCESS_TOKEN, access_token);
        return edit.commit();
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public boolean setRefreshToken(final String refresh_token){
        this.refresh_token = refresh_token;
        Editor edit = sp.edit();
        edit.putString(APIKey.KEY_REFRESH_TOKEN, refresh_token);
        return edit.commit();
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }
}