package com.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Friend extends DBTable {
    public int _id;
    public String uid;
    public String fuid;
    public String tagId;
    public String tagName;

    public Friend() {
    }

    public Friend(SQLiteDatabase db) {
        super(db);
    }

    public Friend(String uid, String fuid, String tagId, String tagName) {
        this.uid = uid;
        this.fuid = fuid;
        this.tagId = tagId;
        this.tagName = tagName;
    }

    @Override
    public void setTableName() {
        this.tableName = DBHelper.TABLE_FRIEND;
    }

    @Override
    public void createUniqueIndex() {
        try {
            db.execSQL("create unique index " + DBHelper.TABLE_FRIEND + "_unique_index" + " on " +
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

    public void addFriend(Friend friend) {
        db.beginTransaction();

        try {
            db.execSQL("insert or ignore into " + DBHelper.TABLE_FRIEND + " values(null, ?, ?, ?, ?)",
                    new Object[]{friend.uid, friend.fuid, friend.tagId, friend.tagName});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addFriends(List<Friend> friends) {
        db.beginTransaction();

        try {
            for (Friend friend : friends) {
                db.execSQL("insert or ignore into " + DBHelper.TABLE_FRIEND + " values(null, ?, ?, ?, ?)",
                        new Object[]{friend.uid, friend.fuid, friend.tagId, friend.tagName});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateFuid(Friend newFriend, Friend oldFriend) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_FUID, newFriend.fuid);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " and " +
                            DBHelper.FRIEND_FUID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriend.uid, oldFriend.fuid, oldFriend.tagId, oldFriend.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTagId(Friend newFriend, Friend oldFriend) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_TAGID, newFriend.tagId);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " and " +
                            DBHelper.FRIEND_FUID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriend.uid, oldFriend.fuid, oldFriend.tagId, oldFriend.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTagName(Friend newFriend, Friend oldFriend) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_TAGNAME, newFriend.tagName);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " and " +
                            DBHelper.FRIEND_FUID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriend.uid, oldFriend.fuid, oldFriend.tagId, oldFriend.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFriend(Friend newFriend, Friend oldFriend) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FRIEND_FUID, newFriend.fuid);
            cv.put(DBHelper.FRIEND_TAGID, newFriend.tagId);
            cv.put(DBHelper.FRIEND_TAGNAME, newFriend.tagName);
            db.update(DBHelper.TABLE_FRIEND, cv,
                    DBHelper.FRIEND_UID + " = ?" + " and " +
                            DBHelper.FRIEND_FUID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{oldFriend.uid, oldFriend.fuid, oldFriend.tagId, oldFriend.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(Friend friend) {
        try {
            db.delete(DBHelper.TABLE_FRIEND,
                    DBHelper.FRIEND_UID + " = ?" + " and " +
                            DBHelper.FRIEND_FUID + " = ?",
                    new String[]{friend.uid, friend.fuid});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFriendTag(Friend friend) {
        try {
            db.delete(DBHelper.TABLE_FRIEND,
                    DBHelper.FRIEND_UID + " = ?" + " and " +
                            DBHelper.FRIEND_FUID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGID + " = ?" + " and " +
                            DBHelper.FRIEND_TAGNAME + " = ?",
                    new String[]{friend.uid, friend.fuid, friend.tagId, friend.tagName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Friend> queryFriend(Friend friend) {
        try {
            Cursor cs = db.query(DBHelper.TABLE_FRIEND, null,
                    DBHelper.FRIEND_UID + " = ?" + " and " +
                            DBHelper.FRIEND_FUID + " = ?",
                    new String[]{friend.uid, friend.fuid},
                    null, null, DBHelper.FRIEND_FUID);
            List<Friend> friends = cursorToList(cs);
            cs.close();
            return friends;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Friend> queryAll() {
        try {
            Cursor cs = db.query(DBHelper.TABLE_FRIEND, null, null, null,
                    null, null, DBHelper.FRIEND_FUID);
            List<Friend> friends = cursorToList(cs);
            cs.close();
            return friends;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Friend> cursorToList(Cursor cs) {
        List<Friend> friends = new ArrayList<Friend>();
        try {
            while (cs.moveToNext()) {
                Friend friend = new Friend();
                friend._id = cs.getInt(cs.getColumnIndex(DBHelper.FRIEND_ID));
                friend.uid = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_UID));
                friend.fuid = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_FUID));
                friend.tagId = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_TAGID));
                friend.tagName = cs.getString(cs.getColumnIndex(DBHelper.FRIEND_TAGNAME));
                friends.add(friend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friends;
    }
}
