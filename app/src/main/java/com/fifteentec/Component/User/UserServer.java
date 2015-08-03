package com.fifteentec.Component.User;

import android.content.SharedPreferences;

import com.API.APIFileServer;
import com.API.APIServer;

/**
 * Created by benbush on 15/8/3.
 */
public class UserServer {
    private static UserServer userServer = new UserServer();

    public static UserServer getInstance() {
        return userServer;
    }
    private final APIServer apiServer = APIServer.getInstance();
    private final APIFileServer apiFileServer = APIFileServer.getInstance();

    private SharedPreferences sp = null;

    public SharedPreferences getSharedPreferences() {
        return this.sp;
    }

    public void setSharedPreferences(SharedPreferences sp) {
        this.sp = sp;
    }
}
