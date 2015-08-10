package com.API;

public class APIUrl {
    private final static String URL_BASE = "http://192.168.0.110:8080";

    public final static String URL_FRIENDLIST = URL_BASE + "/friend/all";

    public final static String URL_HOME = URL_BASE + "/" + "home";
    
    public final static String URL_LOGIN = URL_BASE + "/" + "oauth/token";

    public final static String URL_REGISTER = URL_BASE + "/" + "signup/user";

    public final static String URL_CHANGE_PASSWORD = URL_BASE + "/" + "signup/user";

    public final static String URL_REQUEST_USER_INFO = URL_BASE + "/" + "user";

    public final static String URL_REQUEST_TOKEN = URL_BASE + "/" + "oauth/token";

    private final static String URL_DOWNLOAD_BASE = "";

    public final static String URL_AUDIO_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_IMAGE_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_CONTENT_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_TOPIC_INFO_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";
}
