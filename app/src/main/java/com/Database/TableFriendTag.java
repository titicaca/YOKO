package com.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TableFriendTag extends DBTable {

    public TableFriendTag(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public void setTableName() {
        this.tableName = DBHelper.TABLE_FRIEND_TAG;
    }

    @Override
    public void createUniqueIndex() {
        try {
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS " + DBHelper.TABLE_FRIEND_TAG + "_unique_index" + " ON " +
                    DBHelper.TABLE_FRIEND_TAG + " (" +
                    DBHelper.COLUMN_FRIEND_TAG_UID + ", " +
                    DBHelper.COLUMN_FRIEND_TAG_FUID + ", " +
                    DBHelper.COLUMN_FRIEND_TAG_TAGID + ", " +
                    DBHelper.COLUMN_FRIEND_TAG_TAGNAME + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFriendTag(FriendTagRecord friendTagRecord) {
        db.beginTransaction();

        try {
            db.execSQL("INSERT OR IGNORE INTO " + DBHelper.TABLE_FRIEND_TAG + " VALUES(NULL, ?, ?, ?, ?)",
                    new Object[]{friendTagRecord.uid, friendTagRecord.fuid, friendTagRecord.tagId, friendTagRecord.tagName});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addFriendTags(List<FriendTagRecord> friendTagRecords) {
        db.beginTransaction();

        try {
            for (FriendTagRecord friendTagRecord : friendTagRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + DBHelper.TABLE_FRIEND_TAG + " VALUES(NULL, ?, ?, ?, ?)",
                        new Object[]{friendTagRecord.uid, friendTagRecord.fuid, friendTagRecord.tagId, friendTagRecord.tagName});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateTagName(long uid, long tagId, String newTagName) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COLUMN_FRIEND_TAG_TAGNAME, newTagName);
            db.update(DBHelper.TABLE_FRIEND_TAG, cv,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_TAGID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(tagId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(long uid, long fuid) {
        try {
            db.delete(DBHelper.TABLE_FRIEND_TAG,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_FUID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(fuid)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTag(long uid, long tagId) {
        try {
            db.delete(DBHelper.TABLE_FRIEND_TAG,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_TAGID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(tagId)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(long uid) {
        try {
            db.delete(DBHelper.TABLE_FRIEND_TAG,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?",
                    new String[]{String.valueOf(uid)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriendTag(FriendTagRecord friendTagRecord) {
        try {
            db.delete(DBHelper.TABLE_FRIEND_TAG,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_FUID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_TAGID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_TAGNAME + " = ?",
                    new String[]{String.valueOf(friendTagRecord.uid), String.valueOf(friendTagRecord.fuid), String.valueOf(friendTagRecord.tagId), friendTagRecord.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FriendTagRecord> queryTagsByFriend(long uid, long fuid) {
        try {
            Cursor cs = db.query(DBHelper.TABLE_FRIEND_TAG, null,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_FUID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(fuid)},
                    null, null, DBHelper.COLUMN_FRIEND_TAG_TAGID);
            List<FriendTagRecord> friendTagRecords = cursorToList(cs);
            cs.close();
            return friendTagRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FriendTagRecord> queryFriendsByTag(long uid, long tagId) {
        try {
            Cursor cs = db.query(DBHelper.TABLE_FRIEND_TAG, null,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBHelper.COLUMN_FRIEND_TAG_FUID + " <> ?" + "AND" +
                            DBHelper.COLUMN_FRIEND_TAG_TAGID + " = ?",
                    new String[]{String.valueOf(uid), "0", String.valueOf(tagId)},
                    null, null, DBHelper.COLUMN_FRIEND_TAG_FUID);
            List<FriendTagRecord> friendTagRecords = cursorToList(cs);
            cs.close();
            return friendTagRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FriendTagRecord> queryAll() {
        try {
            Cursor cs = db.query(DBHelper.TABLE_FRIEND_TAG, null, null, null,
                    null, null, DBHelper.COLUMN_FRIEND_TAG_FUID);
            List<FriendTagRecord> friendTagRecords = cursorToList(cs);
            cs.close();
            return friendTagRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FriendTagRecord> cursorToList(Cursor cs) {
        List<FriendTagRecord> friendTagRecords = new ArrayList<FriendTagRecord>();
        try {
            while (cs.moveToNext()) {
                FriendTagRecord friendTagRecord = new FriendTagRecord();
                friendTagRecord._id = cs.getInt(cs.getColumnIndex(DBHelper.COLUMN_FRIEND_TAG_RID));
                friendTagRecord.uid = cs.getLong(cs.getColumnIndex(DBHelper.COLUMN_FRIEND_TAG_UID));
                friendTagRecord.fuid = cs.getLong(cs.getColumnIndex(DBHelper.COLUMN_FRIEND_TAG_FUID));
                friendTagRecord.tagId = cs.getLong(cs.getColumnIndex(DBHelper.COLUMN_FRIEND_TAG_TAGID));
                friendTagRecord.tagName = cs.getString(cs.getColumnIndex(DBHelper.COLUMN_FRIEND_TAG_TAGNAME));
                friendTagRecords.add(friendTagRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendTagRecords;
    }

    public void syncUser(long uid, List<FriendTagRecord> friendTagRecords) {
        db.beginTransaction();
        try {
            db.delete(DBHelper.TABLE_FRIEND_TAG,
                    DBHelper.COLUMN_FRIEND_TAG_UID + " = ?",
                    new String[]{String.valueOf(uid)});

            for (FriendTagRecord friendTagRecord : friendTagRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + DBHelper.TABLE_FRIEND_TAG + " VALUES(NULL, ?, ?, ?, ?)",
                        new Object[]{friendTagRecord.uid, friendTagRecord.fuid, friendTagRecord.tagId, friendTagRecord.tagName});
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
