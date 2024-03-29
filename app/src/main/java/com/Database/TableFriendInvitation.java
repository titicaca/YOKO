package com.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TableFriendInvitation extends DBTable {

    public TableFriendInvitation(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected void setTableName() {
        this.tableName = DBConstants.TABLE_FRIEND_INVITATION;
    }

    @Override
    public void createUniqueIndex() {
        try {
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS " + tableName + "_unique_index" + " ON " +
                    tableName + " (" +
                    DBConstants.COLUMN_FRIEND_INVITATION_UID + ", " +
                    DBConstants.COLUMN_FRIEND_INVITATION_FUID +
                    ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(long fuid) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_FRIEND_INVITATION_FUID + " = ?",
                    new String[]{String.valueOf(fuid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFriendInvitation(long rid) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_FRIEND_INVITATION_RID + " = ?",
                    new String[]{String.valueOf(rid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public long addFriendInvitation(FriendInvitationRecord friendInvitationRecord) {
        long rid;

        db.beginTransaction();

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.COLUMN_FRIEND_INVITATION_UID, friendInvitationRecord.uid);
            cv.put(DBConstants.COLUMN_FRIEND_INVITATION_FUID, friendInvitationRecord.fuid);
            cv.put(DBConstants.COLUMN_FRIEND_INVITATION_MSG, friendInvitationRecord.msg);
            cv.put(DBConstants.COLUMN_FRIEND_INVITATION_CREATEDTIME, System.currentTimeMillis());

            rid = db.insert(tableName, null, cv);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            rid = -1;
        } finally {
            db.endTransaction();
        }

        return rid;
    }

    public List<FriendInvitationRecord> queryFriendInvitation(long fuid) {
        List<FriendInvitationRecord> friendInvitationRecords = null;
        Cursor cs = null;

        try {
            cs = db.query(tableName, null,
                    DBConstants.COLUMN_FRIEND_INVITATION_FUID + " = ?",
                    new String[]{String.valueOf(fuid)},
                    null, null, DBConstants.COLUMN_FRIEND_INVITATION_CREATEDTIME + " DESC ");
            friendInvitationRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return friendInvitationRecords;
    }

    private List<FriendInvitationRecord> cursorToList(Cursor cs) {
        List<FriendInvitationRecord> friendInvitationRecords = new ArrayList<FriendInvitationRecord>();

        int column_index_rid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INVITATION_RID);
        int column_index_uid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INVITATION_UID);
        int column_index_fuid = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INVITATION_FUID);
        int column_index_msg = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INVITATION_MSG);
        int column_index_createdtime = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INVITATION_CREATEDTIME);

        try {
            while (cs.moveToNext()) {
                FriendInvitationRecord friendInvitationRecord = new FriendInvitationRecord();
                friendInvitationRecord.rid = cs.getInt(column_index_rid);
                friendInvitationRecord.uid = cs.getLong(column_index_uid);
                friendInvitationRecord.fuid = cs.getLong(column_index_fuid);
                friendInvitationRecord.msg = cs.getString(column_index_msg);
                friendInvitationRecord.createdtime = cs.getLong(column_index_createdtime);
                friendInvitationRecords.add(friendInvitationRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return friendInvitationRecords;
    }

    public void syncUser(final long uid, List<FriendInvitationRecord> friendInvitationRecords) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_FRIEND_INVITATION_UID + " = ?" + " OR " +
                    DBConstants.COLUMN_FRIEND_INVITATION_FUID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(uid)});

            for (FriendInvitationRecord friendInvitationRecord : friendInvitationRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + tableName + " VALUES(NULL, ?, ?, ?, ?)",
                        new Object[]{friendInvitationRecord.uid, friendInvitationRecord.fuid, friendInvitationRecord.msg, friendInvitationRecord.createdtime}
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
