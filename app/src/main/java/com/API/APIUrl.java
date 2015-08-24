package com.API;

public class APIUrl {
    private final static String URL_BASE = "http://139.196.16.75:8080";

    public final static String URL_LOGIN = URL_BASE + "/" + "oauth/token";

    public final static String URL_REGISTER = URL_BASE + "/" + "signup/user";

    public final static String URL_CHANGE_PASSWORD = URL_BASE + "/" + "signup/user";

    public final static String URL_DELETE_USER_INFO = URL_BASE + "/" + "home";

    public final static String URL_REQUEST_TOKEN = URL_BASE + "/" + "oauth/token";

    public final static String URL_UPLOAD_BAIDUPUSH_USER_INFO = URL_BASE + "/" + "";

    public final static String URL_PUSH_INFO = URL_BASE + "/" + "pushinfo";

    private final static String URL_BASE_USER = URL_BASE + "/" + "user";

    public final static String URL_SYNC_FRIENDS = URL_BASE_USER + "/" + "myfriend" + "/" + "allinfo";

    public final static String URL_REQUEST_USER_INFO = URL_BASE_USER + "/" + "userinfo";

    public final static String URL_EVENT_UPDATE = URL_BASE_USER + "/" + "myschedule";

    public final static String URL_EVENT_GET = URL_BASE_USER + "/" + "myschedule" + "/" + "schedules";

    private final static String URL_DOWNLOAD_BASE = "";

    public final static String URL_AUDIO_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_IMAGE_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_CONTENT_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_TOPIC_INFO_DOWNLOAD = URL_DOWNLOAD_BASE + "/" + "";

    public final static String URL_GROUP_GET = URL_BASE_USER+"/myactivity/orgs";

    public final static String URL_JOINED_GROUP_GET = URL_BASE_USER+"/myactivity/watch/orgs";

    public final static String URL_EVENTS_GET = URL_BASE_USER+"/myactivity/activities";

    public final static String URL_JOINED_EVENTS_GET = URL_BASE_USER+"/myactivity/collect/activities";

    public final static String URL_SINGLE_EVENTS_GET = URL_BASE+"/public/activity/"+"";

}
