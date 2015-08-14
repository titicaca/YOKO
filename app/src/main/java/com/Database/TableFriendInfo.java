package com.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benbush on 15/8/13.
 */
public class TableFriendInfo extends DBTable {

    public TableFriendInfo(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public void setTableName() {
        this.tableName = DBConstants.TABLE_FRIEND_INFO;
    }

    @Override
    public void createUniqueIndex() {
        try {
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS " + DBConstants.TABLE_FRIEND_INFO + "_unique_index" + " ON " +
                    DBConstants.TABLE_FRIEND_INFO + " (" +
                    DBConstants.COLUMN_FRIEND_INFO_UID + ", " +
                    DBConstants.COLUMN_FRIEND_INFO_FUID +
                    ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(long uid) {
        db.beginTransaction();

        try {
            db.delete(DBConstants.TABLE_FRIEND_INFO,
                    DBConstants.COLUMN_FRIEND_INFO_UID + " = ?",
                    new String[]{String.valueOf(uid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFriendInfo(long uid, long fuid) {
        db.beginTransaction();

        try {
            db.delete(DBConstants.TABLE_FRIEND_INFO,
                    DBConstants.COLUMN_FRIEND_INFO_UID + " = ?" + " AND  " +
                    DBConstants.COLUMN_FRIEND_INFO_FUID + " = ?" + " AND ",
                    new String[]{String.valueOf(uid), String.valueOf(fuid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void updateFriendInfo(FriendInfoRecord friendInfoRecord) {
        db.beginTransaction();

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.COLUMN_FRIEND_INFO_EMAIL, friendInfoRecord.email);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_LOCATION, friendInfoRecord.location);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_MOBILE, friendInfoRecord.mobile);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_NICKNAME, friendInfoRecord.nickname);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_PICTURELINK, friendInfoRecord.picturelink);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_QQ, friendInfoRecord.qq);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_SEX, friendInfoRecord.sex);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_WECHAT, friendInfoRecord.wechat);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_WEIBO, friendInfoRecord.weibo);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_COLLECTNUMBER, friendInfoRecord.collectnumber);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_ENROLLNUMBER, friendInfoRecord.enrollnumber);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_FRIENDNUMBER, friendInfoRecord.friendnumber);
            cv.put(DBConstants.COLUMN_FRIEND_INFO_LOGINTIME, friendInfoRecord.logintime);

            db.update(DBConstants.TABLE_FRIEND_TAG, cv,
                    DBConstants.COLUMN_FRIEND_INFO_UID + " = ?" + " AND " +
                    DBConstants.COLUMN_FRIEND_INFO_FUID + " = ?",
                    new String[]{String.valueOf(friendInfoRecord.uid), String.valueOf(friendInfoRecord.fuid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addFriendInfo(FriendInfoRecord friendInfoRecord) {
        db.beginTransaction();

        try {
            db.execSQL("INSERT OR IGNORE INTO " + DBConstants.TABLE_FRIEND_INFO + "VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{friendInfoRecord.uid, friendInfoRecord.fuid, friendInfoRecord.email, friendInfoRecord.location,
                                friendInfoRecord.mobile, friendInfoRecord.nickname, friendInfoRecord.picturelink, friendInfoRecord.qq,
                                friendInfoRecord.sex, friendInfoRecord.wechat, friendInfoRecord.weibo, friendInfoRecord.collectnumber,
                                friendInfoRecord.enrollnumber, friendInfoRecord.friendnumber, friendInfoRecord.logintime}
            );

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public FriendInfoRecord queryFriendInfo(long uid, long fuid) {
        FriendInfoRecord friendInfoRecord = null;
        Cursor cs = null;
        try {
            cs = db.query(DBConstants.TABLE_FRIEND_INFO, null,
                    DBConstants.COLUMN_FRIEND_INFO_UID + " = ?" + " AND " +
                            DBConstants.COLUMN_FRIEND_INFO_FUID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(fuid)},
                    null, null, null);
            if (cs.getCount() == 1) {
                cs.moveToFirst();
                friendInfoRecord = new FriendInfoRecord();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return friendInfoRecord;
    }

    public List<FriendInfoRecord> queryFriendsInfo(long uid) {
        List<FriendInfoRecord>  friendInfoRecords = null;
        Cursor cs = null;
        try {
            cs = db.query(DBConstants.TABLE_FRIEND_INFO, null,
                    DBConstants.COLUMN_FRIEND_INFO_UID + " = ?",
                    new String[]{String.valueOf(uid)},
                    null, null, DBConstants.COLUMN_FRIEND_INFO_LOGINTIME + " DESC");
            friendInfoRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return friendInfoRecords;
    }

    private List<FriendInfoRecord> cursorToList(Cursor cs) {
        List<FriendInfoRecord> friendInfoRecords = new ArrayList<FriendInfoRecord>();

        int column_index_rid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_RID);
        int column_index_uid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_UID);
        int column_index_fuid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_FUID);
        int column_index_email = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_EMAIL);
        int column_index_location = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_LOCATION);
        int column_index_mobile = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_MOBILE);
        int column_index_nickname = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_NICKNAME);
        int column_index_picturelink = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_PICTURELINK);
        int column_index_qq = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_QQ);
        int column_index_sex = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_SEX);
        int column_index_wechat = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_WECHAT);
        int column_index_weibo = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_WEIBO);
        int column_index_collectnumber = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_COLLECTNUMBER);
        int column_index_enrollnumber = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_ENROLLNUMBER);
        int column_index_friendnumber = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_FRIENDNUMBER);
        int column_index_logintime = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INFO_LOGINTIME);

        try {
            while (cs.moveToNext()) {
                FriendInfoRecord friendInfoRecord = new FriendInfoRecord();
                friendInfoRecord.rid = cs.getInt(column_index_rid);
                friendInfoRecord.uid = cs.getLong(column_index_uid);
                friendInfoRecord.fuid = cs.getLong(column_index_fuid);
                friendInfoRecord.email = cs.getString(column_index_email);
                friendInfoRecord.location = cs.getString(column_index_location);
                friendInfoRecord.mobile = cs.getString(column_index_mobile);
                friendInfoRecord.nickname = cs.getString(column_index_nickname);
                friendInfoRecord.picturelink = cs.getString(column_index_picturelink);
                friendInfoRecord.qq = cs.getString(column_index_qq);
                friendInfoRecord.sex = cs.getInt(column_index_sex);
                friendInfoRecord.wechat = cs.getString(column_index_wechat);
                friendInfoRecord.weibo = cs.getString(column_index_weibo);
                friendInfoRecord.collectnumber = cs.getInt(column_index_collectnumber);
                friendInfoRecord.enrollnumber = cs.getInt(column_index_enrollnumber);
                friendInfoRecord.friendnumber = cs.getInt(column_index_friendnumber);
                friendInfoRecord.logintime = cs.getLong(column_index_logintime);
                friendInfoRecords.add(friendInfoRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return friendInfoRecords;
    }

    public void syncUser(long uid, List<FriendInfoRecord> friendInfoRecords) {
        db.beginTransaction();

        try {
            db.delete(DBConstants.TABLE_FRIEND_INFO,
                    DBConstants.COLUMN_FRIEND_INFO_UID + " = ?",
                    new String[]{String.valueOf(uid)});

            for (FriendInfoRecord friendInfoRecord : friendInfoRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + DBConstants.TABLE_FRIEND_INFO + " VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{friendInfoRecord.uid, friendInfoRecord.fuid, friendInfoRecord.email, friendInfoRecord.location,
                                friendInfoRecord.mobile, friendInfoRecord.nickname, friendInfoRecord.picturelink, friendInfoRecord.qq,
                                friendInfoRecord.sex, friendInfoRecord.wechat, friendInfoRecord.weibo, friendInfoRecord.collectnumber,
                                friendInfoRecord.enrollnumber, friendInfoRecord.friendnumber, friendInfoRecord.logintime}
                );
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
