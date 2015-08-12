package com.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "YOKO_DB";
    private final static int DATABASE_VERSION = 1;

    public final static String TABLE_FRIEND_TAG = "friend_tag";
    public final static String COLUMN_FRIEND_TAG_RID = "_id";
    public final static String COLUMN_FRIEND_TAG_UID = "uid";
    public final static String COLUMN_FRIEND_TAG_FUID = "fuid";
    public final static String COLUMN_FRIEND_TAG_TAGID = "tagId";
    public final static String COLUMN_FRIEND_TAG_TAGNAME = "tagName";

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
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FRIEND_TAG +
                "(" + COLUMN_FRIEND_TAG_RID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FRIEND_TAG_UID + " INT8, " +
                COLUMN_FRIEND_TAG_FUID + " INT8, " +
                COLUMN_FRIEND_TAG_TAGID + " INT8," +
                COLUMN_FRIEND_TAG_TAGNAME + " CHAR(255)" +
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