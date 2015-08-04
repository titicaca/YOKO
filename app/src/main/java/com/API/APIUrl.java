package com.API;

public class APIUrl {
    private final static String URL_BASE = "http://192.168.0.110:8080";

    public final static String URL_HOME = URL_BASE + "/" + "home";

    public final static String URL_LOGIN = URL_BASE + "/" + "oauth/token";

    public final static String URL_RESET_PASSWORD = URL_BASE + "/" + "";

    public final static String URL_REGISTER = URL_BASE + "/" + "signup/user";

    public final static String URL_CHANGE_PASSWORD = URL_BASE + "/" + "signup/user";

    public final static String URL_SEND_SMS = URL_BASE + "/" + "";

    public final static String URL_USER_UPDATE = URL_BASE + "/" + "";

    public final static String URL_GET_TOPICS_INFO = URL_BASE + "/" + "";

    public final static String URL_POST_USER_BEHAVIOR = URL_BASE + "/" + "";

    private final static String URL_DOWNLOAD_BASE = "";

    public final static String URL_AUDIO_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_IMAGE_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_CONTENT_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_TOPIC_INFO_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";
}
