package com.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Friend extends DBTable {

    public Friend(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public void setTableName() {
        this.tableName = DBHelper.TABLE_FRIEND;
    }

    @Override
    public void createUniqueIndex() {
        try {
            db.execSQL("CREATE UNIQUE INDEX " + DBHelper.TABLE_FRIEND + "_unique_index" + " ON " +
                    DBHelper.TABLE_FRIEND + " (" +
                    DBHelper.FRIEND_UID + ", " +
                    DBHelper.FRIEND_FUID + ", " +
                    DBHelper.FRIEND_TAGID + ", " +
                    DBHelper.FRIEND_TAGNAME + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remoteSync() {

    }

    public void addFriend(FriendRecord friendRecord) {
        db.beginTransaction();

        try {
            db.execSQL("INSERT OR IGNORE INTO " + DBHelper.TABLE_FRIEND + " VALUES(NULL, ?, ?, ?, ?)",
                    new Object[]{friendRecord.uid, friendRecord.fuid, friendRecord.tagId, friendRecord.tagName});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addFriends(List<FriendRecord> friendRecords) {
        db.beginTransaction();

        try {
            for (FriendRecord friendRecord : friendRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + DBHelper.TABLE_FRIEND + " VALUES(NULL, ?, ?, ?, ?)",
                        new Object[]{friendRecord.uid, friendRecord.fuid, friendRecord.tagId, friendRecord.tagName});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateFuid(FriendRecord newFriendRecord, FriendRecord oldFriendRecord) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_FUID, newFriendRecord.fuid);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " AND " +
                            DBHelper.FRIEND_FUID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriendRecord.uid, oldFriendRecord.fuid, oldFriendRecord.tagId, oldFriendRecord.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTagId(FriendRecord newFriendRecord, FriendRecord oldFriendRecord) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_TAGID, newFriendRecord.tagId);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " AND " +
                            DBHelper.FRIEND_FUID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriendRecord.uid, oldFriendRecord.fuid, oldFriendRecord.tagId, oldFriendRecord.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTagName(FriendRecord newFriendRecord, FriendRecord oldFriendRecord) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_TAGNAME, newFriendRecord.tagName);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " AND " +
                            DBHelper.FRIEND_FUID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriendRecord.uid, oldFriendRecord.fuid, oldFriendRecord.tagId, oldFriendRecord.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFriend(FriendRecord newFriendRecord, FriendRecord oldFriendRecord) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_FUID, newFriendRecord.fuid);
            cv.put(DBHelper.FRIEND_TAGID, newFriendRecord.tagId);
            cv.put(DBHelper.FRIEND_TAGNAME, newFriendRecord.tagName);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " AND " +
                            DBHelper.FRIEND_FUID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriendRecord.uid, oldFriendRecord.fuid, oldFriendRecord.tagId, oldFriendRecord.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(FriendRecord friendRecord) {
        try {
            db.delete(DBHelper.TABLE_FRIEND,
                    DBHelper.FRIEND_UID + " = ?" + " AND " +
                            DBHelper.FRIEND_FUID + " = ?",
                    new String[]{friendRecord.uid, friendRecord.fuid});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriendTag(FriendRecord friendRecord) {
        try {
            db.delete(DBHelper.TABLE_FRIEND,
                    DBHelper.FRIEND_UID + " = ?" + " AND " +
                            DBHelper.FRIEND_FUID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGID + " = ?" + " AND " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{friendRecord.uid, friendRecord.fuid, friendRecord.tagId, friendRecord.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FriendRecord> queryFriend(FriendRecord friendRecord) {
        try {
            Cursor cs = db.query(DBHelper.TABLE_FRIEND, null,
                    DBHelper.FRIEND_UID + " = ?" + " AND " +
                            DBHelper.FRIEND_FUID + " = ?",
                    new String[]{friendRecord.uid, friendRecord.fuid},
                    null, null, DBHelper.FRIEND_FUID);
            List<FriendRecord> friendRecords = cursorToList(cs);
            cs.close();
            return friendRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FriendRecord> queryAll() {
        try {
            Cursor cs = db.query(DBHelper.TABLE_FRIEND, null, null, null,
                    null, null, DBHelper.FRIEND_FUID);
            List<FriendRecord> friendRecords = cursorToList(cs);
            cs.close();
            return friendRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FriendRecord> cursorToList(Cursor cs) {
        List<FriendRecord> friendRecords = new ArrayList<FriendRecord>();
        try {
            while (cs.moveToNext()) {
                FriendRecord friendRecord = new FriendRecord();
                friendRecord._id = cs.getInt(cs.getColumnIndex(DBHelper.FRIEND_ID));
                friendRecord.uid = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_UID));
                friendRecord.fuid = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_FUID));
                friendRecord.tagId = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_TAGID));
                friendRecord.tagName = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_TAGNAME));
                friendRecords.add(friendRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendRecords;
    }
}
