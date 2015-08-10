package com.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Friend friend;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = dbHelper.getWritableDatabase();
        friend = new Friend(db);
    }

    public void closeDB() {
        db.close();
    }

    public Friend getFriend(){
        return this.friend;
    }
}