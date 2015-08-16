package com.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benbush on 15/8/15.
 */
public class TableFriendInvitation extends DBTable {

    public TableFriendInvitation(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public void setTableName() {
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

    public void deleteFriendInvitation(int rid) {
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

    public void addFriendInvitation(FriendInvitationRecord friendInvitationRecord) {
        db.beginTransaction();

        try {
            db.execSQL("INSERT OR IGNORE INTO " + tableName + " VALUES(NULL, ?, ?, ?, ?)",
                    new Object[]{friendInvitationRecord.uid, friendInvitationRecord.fuid, friendInvitationRecord.msg, System.currentTimeMillis()}
            );
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<FriendInvitationRecord> queryFriendInvitation(long fuid) {
        List<FriendInvitationRecord> friendInvitationRecords = null;
        Cursor cs = null;
        try {
            cs = db.query(tableName, null,
                    DBConstants.COLUMN_FRIEND_INVITATION_FUID + " = ?",
                    new String[]{String.valueOf(fuid)},
                    null, null, DBConstants.COLUMN_FRIEND_INVITATION_CREATETIME + "DESC");
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
        int column_index_createtime = cs.getColumnIndex(DBConstants.COLUMN_FRIEND_INVITATION_CREATETIME);

        try {
            while (cs.moveToNext()) {
                FriendInvitationRecord friendInvitationRecord = new FriendInvitationRecord();
                friendInvitationRecord.rid = cs.getInt(column_index_rid);
                friendInvitationRecord.uid = cs.getLong(column_index_uid);
                friendInvitationRecord.fuid = cs.getLong(column_index_fuid);
                friendInvitationRecord.msg = cs.getString(column_index_msg);
                friendInvitationRecord.createtime = cs.getLong(column_index_createtime);
                friendInvitationRecords.add(friendInvitationRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return friendInvitationRecords;
    }
}
