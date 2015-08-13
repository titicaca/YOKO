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
         * 这张表存储好友关系，需要与服务器同步。该表采用冗余结构，因sqlite不支持join操作，故而为方便查询管理而设计。
         * 第一列是_id，为record的id
         * 第二列是uid，为用户id
         * 第三列是fuid，为用户的friend的uid
         * 第四列是email，为firend的email
         * 第五列是email，为firend的location
         * 第六列是email，为firend的mobile
         * 第七列是email，为firend的nickname
         * 第八列是email，为firend的picturelink
         * 第九列是email，为firend的qq
         * 第十列是email，为firend的sex
         * 第十一列是email，为firend的wechat
         * 第十二列是email，为firend的weibo
         * 第十三列是email，为firend的好友收藏活动的数量
         * 第十四列是email，为firend的报名参与活动的数量
         * 第十五列是email，为firend的好友的好友的数量
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
                DBConstants.COLUMN_FRIEND_INFO_COLLECTIONNUMBER + " INTEGER, " +
                DBConstants.COLUMN_FRIEND_INFO_ENROLLNUMBER + " INTEGER, " +
                DBConstants.COLUMN_FRIEND_INFO_FRIENDNUMBER + " INTEGER" +
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