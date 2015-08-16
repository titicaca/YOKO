package com.Database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBTable {
    protected SQLiteDatabase db;
    protected String tableName;

    DBTable(){
    }

    DBTable(SQLiteDatabase db){
        this.db = db;
        setTableName();
        createUniqueIndex();
    }

    protected abstract void setTableName();

    public abstract void createUniqueIndex();

    public void clear() {
        try {
            db.delete(tableName, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public abstract void deleteUser(long uid);
}
