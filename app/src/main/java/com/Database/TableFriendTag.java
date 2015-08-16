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
    protected void setTableName() {
        this.tableName = DBConstants.TABLE_FRIEND_TAG;
    }

    @Override
    public void createUniqueIndex() {
        try {
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS " + DBConstants.TABLE_FRIEND_TAG + "_unique_index" + " ON " +
                    DBConstants.TABLE_FRIEND_TAG + " (" +
                    DBConstants.COLUMN_FRIEND_TAG_UID + ", " +
                    DBConstants.COLUMN_FRIEND_TAG_FUID + ", " +
                    DBConstants.COLUMN_FRIEND_TAG_TAGID +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(long uid) {
        db.beginTransaction();

        try {
            db.delete(DBConstants.TABLE_FRIEND_TAG,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?",
                    new String[]{String.valueOf(uid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addFriendTag(FriendTagRecord friendTagRecord) {
        db.beginTransaction();

        try {
            db.execSQL("INSERT OR IGNORE INTO " + DBConstants.TABLE_FRIEND_TAG + " VALUES(NULL, ?, ?, ?, ?)",
                    new Object[]{friendTagRecord.uid, friendTagRecord.fuid, friendTagRecord.tagId, friendTagRecord.tagName});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addFriendTags(List<FriendTagRecord> friendTagRecords) {
        db.beginTransaction();

        try {
            for (FriendTagRecord friendTagRecord : friendTagRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + DBConstants.TABLE_FRIEND_TAG + " VALUES(NULL, ?, ?, ?, ?)",
                        new Object[]{friendTagRecord.uid, friendTagRecord.fuid, friendTagRecord.tagId, friendTagRecord.tagName});
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public void updateTagName(long uid, long tagId, String newTagName) {
        db.beginTransaction();

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.COLUMN_FRIEND_TAG_TAGNAME, newTagName);
            db.update(DBConstants.TABLE_FRIEND_TAG, cv,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_TAGID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(tagId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFriend(long uid, long fuid) {
        db.beginTransaction();

        try {
            db.delete(DBConstants.TABLE_FRIEND_TAG,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_FUID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(fuid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteTag(long uid, long tagId) {
        db.beginTransaction();

        try {
            db.delete(DBConstants.TABLE_FRIEND_TAG,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_TAGID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(tagId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFriendTag(FriendTagRecord friendTagRecord) {
        db.beginTransaction();

        try {
            db.delete(DBConstants.TABLE_FRIEND_TAG,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_FUID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_TAGID + " = ?",
                    new String[]{String.valueOf(friendTagRecord.uid), String.valueOf(friendTagRecord.fuid), String.valueOf(friendTagRecord.tagId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<FriendTagRecord> queryTagsByFriend(long uid, long fuid) {
        List<FriendTagRecord>  friendTagRecords = null;
        Cursor cs = null;
        try {
            cs = db.query(DBConstants.TABLE_FRIEND_TAG, null,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_FUID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_TAGID + " <> 0",
                    new String[]{String.valueOf(uid), String.valueOf(fuid)},
                    null, null, DBConstants.COLUMN_FRIEND_TAG_TAGID);
            friendTagRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return friendTagRecords;
    }

    public List<FriendTagRecord> queryFriendsByTag(long uid, long tagId) {
        List<FriendTagRecord> friendTagRecords = null;
        Cursor cs = null;

        try {
            cs = db.query(DBConstants.TABLE_FRIEND_TAG, null,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_TAG_FUID + " <> 0" + " AND" +
                            DBConstants.COLUMN_FRIEND_TAG_TAGID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(tagId)},
                    null, null, DBConstants.COLUMN_FRIEND_TAG_FUID);
            friendTagRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return friendTagRecords;
    }

    public List<FriendTagRecord> queryTag(long uid) {
        List<FriendTagRecord> friendTagRecords = null;
        Cursor cs = null;

        try {
            cs = db.query(DBConstants.TABLE_FRIEND_TAG, null,
                    DBConstants.COLUMN_FRIEND_INFO_UID + " = ?" + " AND " +
                    DBConstants.COLUMN_FRIEND_INFO_FUID + " = 0",
                    new String[]{String.valueOf(uid)},
                    null, null, DBConstants.COLUMN_FRIEND_TAG_TAGID);
            friendTagRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return friendTagRecords;
    }

    public List<FriendTagRecord> queryAll() {
        List<FriendTagRecord> friendTagRecords = null;
        Cursor cs = null;

        try {
            cs = db.query(DBConstants.TABLE_FRIEND_TAG, null, null, null,
                    null, null, DBConstants.COLUMN_FRIEND_TAG_FUID);
            friendTagRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return friendTagRecords;
    }

    private List<FriendTagRecord> cursorToList(Cursor cs) {
        List<FriendTagRecord> friendTagRecords = new ArrayList<FriendTagRecord>();

        int column_index_rid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_TAG_RID);
        int column_index_uid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_TAG_UID);
        int column_index_fuid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_TAG_FUID);
        int column_index_tagId = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_TAG_TAGID);
        int column_index_tagName = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_TAG_TAGNAME);

        try {
            while (cs.moveToNext()) {
                FriendTagRecord friendTagRecord = new FriendTagRecord();
                friendTagRecord.rid = cs.getInt(column_index_rid);
                friendTagRecord.uid = cs.getLong(column_index_uid);
                friendTagRecord.fuid = cs.getLong(column_index_fuid);
                friendTagRecord.tagId = cs.getLong(column_index_tagId);
                friendTagRecord.tagName = cs.getString(column_index_tagName);
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
            db.delete(DBConstants.TABLE_FRIEND_TAG,
                    DBConstants.COLUMN_FRIEND_TAG_UID + " = ?",
                    new String[]{String.valueOf(uid)});

            for (FriendTagRecord friendTagRecord : friendTagRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + DBConstants.TABLE_FRIEND_TAG + " VALUES(NULL, ?, ?, ?, ?)",
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
