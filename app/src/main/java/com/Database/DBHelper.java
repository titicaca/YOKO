package com.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "YOKO_DB";
    private final static int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //尽在创建数据库时被调用一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 这张表存储好友关系，需要与服务器同步。该表采用冗余结构，因sqlite不支持join操作，故而为方便查询管理而设计。
         * 第一列是_id，为record的id
         * 第二列是uid，为用户id
         * 第三列是fuid，为用户的friend的uid
         * 第四列是tagId，为朋友标签所属tag的id
         * 第五列是tagName，为该标签的名称
         */
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstants.TABLE_FRIEND_TAG +
                "(" + DBConstants.COLUMN_FRIEND_TAG_RID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COLUMN_FRIEND_TAG_UID + " INT8, " +
                DBConstants.COLUMN_FRIEND_TAG_FUID + " INT8, " +
                DBConstants.COLUMN_FRIEND_TAG_TAGID + " INT8," +
                DBConstants.COLUMN_FRIEND_TAG_TAGNAME + " CHAR(255)" +
                ")");

        /**
         * 这张表存储好友信息
         * 第一列是_id，为record的id
         * 第二列是uid，为用户id
         * 第三列是fuid，为用户的friend的uid
         * 第四列是email，为firend的email
         * 第五列是location，为firend的location
         * 第六列是mobile，为firend的mobile
         * 第七列是nickname，为firend的nickname
         * 第八列是picturelink，为firend的picturelink
         * 第九列是qq，为firend的qq
         * 第十列是sex，为firend的sex
         * 第十一列是wechat，为firend的wechat
         * 第十二列是weibo，为firend的weibo
         * 第十三列是collectnumber，为firend的好友收藏活动的数量
         * 第十四列是enrollnumber，为firend的报名参与活动的数量
         * 第十五列是friendnumber，为firend的好友的好友的数量
         * 第十六列是logintime，为firend的好友的好友的数量
         */
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstants.TABLE_FRIEND_INFO +
                "(" + DBConstants.COLUMN_FRIEND_INFO_RID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COLUMN_FRIEND_INFO_UID + " INT8, " +
                DBConstants.COLUMN_FRIEND_INFO_FUID + " INT8, " +
                DBConstants.COLUMN_FRIEND_INFO_EMAIL + " CHAR(255), " +
                DBConstants.COLUMN_FRIEND_INFO_LOCATION + " CHAR(255), " +
                DBConstants.COLUMN_FRIEND_INFO_MOBILE + " CHAR(15), " +
                DBConstants.COLUMN_FRIEND_INFO_NICKNAME + " CHAR(255), " +
                DBConstants.COLUMN_FRIEND_INFO_PICTURELINK + " CHAR(255), " +
                DBConstants.COLUMN_FRIEND_INFO_QQ + " CHAR(15), " +
                DBConstants.COLUMN_FRIEND_INFO_SEX + " INT1, " +
                DBConstants.COLUMN_FRIEND_INFO_WECHAT + " CHAR(255), " +
                DBConstants.COLUMN_FRIEND_INFO_WEIBO + " CHAR(255), " +
                DBConstants.COLUMN_FRIEND_INFO_COLLECTNUMBER + " INTEGER, " +
                DBConstants.COLUMN_FRIEND_INFO_ENROLLNUMBER + " INTEGER, " +
                DBConstants.COLUMN_FRIEND_INFO_FRIENDNUMBER + " INTEGER, " +
                DBConstants.COLUMN_FRIEND_INFO_LOGINTIME + " INT8" +
                ")");

        /**
         * 这张表存储用户好友邀请
         * 第一列是_id，为record的id
         * 第二列是uid，为发起邀请的用户id
         * 第三列是fuid，为被邀请的用户id
         * 第四列是msg，为邀请的留言信息
         * 第五列是createtime，为邀请创建时间
         */
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstants.TABLE_FRIEND_INVITATION +
                "(" + DBConstants.COLUMN_FRIEND_INVITATION_RID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COLUMN_FRIEND_INVITATION_UID + " INT8, " +
                DBConstants.COLUMN_FRIEND_INVITATION_FUID + " INT8, " +
                DBConstants.COLUMN_FRIEND_INVITATION_MSG + " CHAR(255), " +
                DBConstants.COLUMN_FRIEND_INVITATION_CREATETIME + " INT8" +
                ")");

        /**
         * 这张表存储用户的自定义事件
         * 第一列是_id，为record的id
         * 第二列是uid，为用户id
         * 第三列是serverid，为用户的用户的serverid
         * 第四列是introduciton，为用户的introduciton
         * 第五列是localpicturelink，为用户的localpicturelink
         * 第六列是remotepitcurelink，为用户的remotepitcurelink
         * 第七列是remind，为用户的remind
         * 第八列是timebegin，为用户的timebegin
         * 第九列是timeend，为用户的timeend
         * 第十列是type，为用户的type
         * 第十一列是property，为用户的property
         * 第十二列是detaillink，为用户的detaillink
         * 第十三列是status，为用户的status 0表示有效，1表示被删除
         * 第十四列是modified，标示该条event是否有修改 0表示未修改，1表示已修改
         * 第十五列是updatetime，为记录的updatetime
         */
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstants.TABLE_EVENT +
                "(" + DBConstants.COLUMN_EVENT_RID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBConstants.COLUMN_EVENT_UID + " INT8, " +
                DBConstants.COLUMN_EVENT_SEVERID + " INT8, " +
                DBConstants.COLUMN_EVENT_INTRODUCTION + " TEXT, " +
                DBConstants.COLUMN_EVENT_LOCALPICTURELINK + " CHAR(255), " +
                DBConstants.COLUMN_EVENT_REMOTEPICTURELINK + " CHAR(255), " +
                DBConstants.COLUMN_EVENT_REMIND + " INT8, " +
                DBConstants.COLUMN_EVENT_TIMEBEGIN + " INT8, " +
                DBConstants.COLUMN_EVENT_TIMEEND + " INT8, " +
                DBConstants.COLUMN_EVENT_TYPE + " INT1, " +
                DBConstants.COLUMN_EVENT_PROPERTY + " INT1, " +
                DBConstants.COLUMN_EVENT_DETAILLINK + " CHAR(255), " +
                DBConstants.COLUMN_EVENT_STATUS + " INT1, " +
                DBConstants.COLUMN_EVENT_MODIFIED + " INT1, " +
                DBConstants.COLUMN_EVENT_UPDATETIME + "INT8" +
                ")");
    }

    /**
     * 如果DATABASE_VERSION值被修改,系统发现现有数据库版本不同,即会调用onUpgrade
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO:
        Log.v("DATABASE", "version: " + db.getVersion());
    }
}