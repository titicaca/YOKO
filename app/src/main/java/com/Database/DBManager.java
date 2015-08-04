package com.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by benbush on 15/8/3.
 */
public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = dbHelper.getWritableDatabase();
    }

    public void closeDB() {
        db.close();
    }
}
