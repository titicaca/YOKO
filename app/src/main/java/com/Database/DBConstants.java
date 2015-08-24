package com.Database;

public class DBConstants {
    //for table friend_tag
    public final static String TABLE_FRIEND_TAG = "friend_tag";
    public final static String COLUMN_FRIEND_TAG_RID = "_id";
    public final static String COLUMN_FRIEND_TAG_UID = "uid";
    public final static String COLUMN_FRIEND_TAG_FUID = "fuid";
    public final static String COLUMN_FRIEND_TAG_TAGID = "tagId";
    public final static String COLUMN_FRIEND_TAG_TAGNAME = "tagName";

    //for table friend_info
    public final static String TABLE_FRIEND_INFO = "friend_info";
    public final static String COLUMN_FRIEND_INFO_RID = "_id";
    public final static String COLUMN_FRIEND_INFO_UID = "uid";
    public final static String COLUMN_FRIEND_INFO_FUID = "fuid";
    public final static String COLUMN_FRIEND_INFO_EMAIL = "email";
    public final static String COLUMN_FRIEND_INFO_LOCATION = "location";
    public final static String COLUMN_FRIEND_INFO_MOBILE = "mobile";
    public final static String COLUMN_FRIEND_INFO_NICKNAME = "nickname";
    public final static String COLUMN_FRIEND_INFO_PICTURELINK = "picturelink";
    public final static String COLUMN_FRIEND_INFO_QQ = "qq";
    public final static String COLUMN_FRIEND_INFO_SEX = "sex";
    public final static String COLUMN_FRIEND_INFO_WECHAT = "wechat";
    public final static String COLUMN_FRIEND_INFO_WEIBO = "weibo";
    public final static String COLUMN_FRIEND_INFO_COLLECTNUMBER = "collectnumber";
    public final static String COLUMN_FRIEND_INFO_ENROLLNUMBER = "enrollnumber";
    public final static String COLUMN_FRIEND_INFO_FRIENDNUMBER = "friendnumber";
    public final static String COLUMN_FRIEND_INFO_LOGINTIME = "logintime";

    //for table friend_invitation
    public final static String TABLE_FRIEND_INVITATION = "friend_invitation";
    public final static String COLUMN_FRIEND_INVITATION_RID = "_id";
    public final static String COLUMN_FRIEND_INVITATION_UID = "uid";
    public final static String COLUMN_FRIEND_INVITATION_FUID = "fuid";
    public final static String COLUMN_FRIEND_INVITATION_MSG = "msg";
    public final static String COLUMN_FRIEND_INVITATION_CREATEDTIME = "createdtime";

    //for table event_invitation
    public final static String TABLE_EVENT_INVITATION = "event_invitation";
    public final static String COLUMN_EVENT_INVITATION_RID = "_id";
    public final static String COLUMN_EVENT_INVITATION_UID = "uid";
    public final static String COLUMN_EVENT_INVITATION_FUID = "fuid";
    public final static String COLUMN_EVENT_INVITATION_MSG = "msg";
    public final static String COLUMN_EVENT_INVITATION_TYPE = "type";
    public final static String COLUMN_EVENT_INVITATION_EVENTID = "eventId";
    public final static String COLUMN_EVENT_INVITATION_CREATEDTIME = "createdtime";

    //for table event
    public final static String TABLE_EVENT = "event";
    public final static String COLUMN_EVENT_RID = "_id";
    public final static String COLUMN_EVENT_UID = "uid";
    public final static String COLUMN_EVENT_SEVERID = "serverid";
    public final static String COLUMN_EVENT_INTRODUCTION = "introduction";
    public final static String COLUMN_EVENT_LOCALPICTURELINK = "localpicturelink";
    public final static String COLUMN_EVENT_REMOTEPICTURELINK = "remotepitcurelink";
    public final static String COLUMN_EVENT_REMIND = "remind";
    public final static String COLUMN_EVENT_TIMEBEGIN = "timebegin";
    public final static String COLUMN_EVENT_TIMEEND = "timeend";
    public final static String COLUMN_EVENT_TYPE = "type";
    public final static String COLUMN_EVENT_PROPERTY = "property";
    public final static String COLUMN_EVENT_DETAILLINK = "detaillink";
    public final static String COLUMN_EVENT_STATUS = "status";
    public final static String COLUMN_EVENT_MODIFIED = "modified";
    public final static String COLUMN_EVENT_UPDATETIME = "updatetime";
}
