package com.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private TableFriendTag tableFriendTag;
    private TableFriendInfo tableFriendInfo;
    private TableEvent tableEvent;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = dbHelper.getWritableDatabase();
        tableFriendTag = new TableFriendTag(db);
        tableFriendInfo = new TableFriendInfo(db);
        tableEvent = new TableEvent(db);
    }

    public void closeDB() {
        db.close();
    }

    public TableFriendTag getTableFriendTag(){
        return this.tableFriendTag;
    }

    public TableFriendInfo getTableFriendInfo() {
        return this.tableFriendInfo;
    }

    public TableEvent getTableEvent() {
        return this.tableEvent;
    }
}
