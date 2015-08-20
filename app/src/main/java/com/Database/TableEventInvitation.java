package com.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benbush on 15/8/20.
 */
public class TableEventInvitation extends DBTable {

    public TableEventInvitation(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected void setTableName() {
        this.tableName = DBConstants.TABLE_EVENT_INVITATION;
    }

    @Override
    public void createUniqueIndex() {
        try {
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS " + tableName + "_unique_index" + " ON " +
                    tableName + " (" +
                    DBConstants.COLUMN_EVENT_INVITATION_UID + ", " +
                    DBConstants.COLUMN_EVENT_INVITATION_FUID + ", " +
                    DBConstants.COLUMN_EVENT_INVITATION_TYPE + ", " +
                    DBConstants.COLUMN_EVENT_INVITATION_EVENTID +
                    ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(long uid) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_EVENT_INVITATION_UID + " = ?" + " OR " +
                    DBConstants.COLUMN_EVENT_INVITATION_FUID + "  = ?",
                    new String[]{String.valueOf(uid), String.valueOf(uid)});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteEventInvitation(long rid) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_EVENT_INVITATION_RID + " = ?",
                    new String[]{String.valueOf(rid)});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteEventInvitation(long uid, long fuid, int type, long eventId) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_EVENT_INVITATION_UID + " = ?" + " AND " +
                    DBConstants.COLUMN_EVENT_INVITATION_FUID + " = ?" + " AND " +
                    DBConstants.COLUMN_EVENT_INVITATION_TYPE + " = ?" + " AND " +
                    DBConstants.COLUMN_EVENT_INVITATION_EVENTID + " = ?",
                    new String[]{String.valueOf(uid), String.valueOf(fuid), String.valueOf(type), String.valueOf(eventId)});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public long addEventInvitation(EventInvitationRecord eventInvitationRecord) {
        long rid;

        db.beginTransaction();

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.COLUMN_EVENT_INVITATION_UID, eventInvitationRecord.uid);
            cv.put(DBConstants.COLUMN_EVENT_INVITATION_FUID, eventInvitationRecord.fuid);
            cv.put(DBConstants.COLUMN_EVENT_INVITATION_MSG, eventInvitationRecord.msg);
            cv.put(DBConstants.COLUMN_EVENT_INVITATION_TYPE, eventInvitationRecord.type);
            cv.put(DBConstants.COLUMN_EVENT_INVITATION_EVENTID, eventInvitationRecord.eventId);
            cv.put(DBConstants.COLUMN_EVENT_INVITATION_CREATETIME, System.currentTimeMillis());

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

    public List<EventInvitationRecord> queryEventInvitationByUID(long uid) {
        List<EventInvitationRecord> eventInvitationRecords = null;
        Cursor cs = null;

        try {
            cs = db.query(tableName, null,
                    DBConstants.COLUMN_EVENT_INVITATION_UID + " = ?",
                    new String[]{String.valueOf(uid)},
                    null, null, DBConstants.COLUMN_EVENT_INVITATION_CREATETIME + "DESC");
            eventInvitationRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return eventInvitationRecords;
    }

    public List<EventInvitationRecord> queryEventInvitationByFUID(long fuid) {
        List<EventInvitationRecord> eventInvitationRecords = null;
        Cursor cs = null;

        try {
            cs = db.query(tableName, null,
                    DBConstants.COLUMN_EVENT_INVITATION_FUID + " = ?",
                    new String[]{String.valueOf(fuid)},
                    null, null, DBConstants.COLUMN_EVENT_INVITATION_CREATETIME + "DESC");
            eventInvitationRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return eventInvitationRecords;
    }

    private List<EventInvitationRecord> cursorToList(Cursor cs) {
        List<EventInvitationRecord> eventInvitationRecords = new ArrayList<EventInvitationRecord>();

        int column_index_rid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INVITATION_RID);
        int column_index_uid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INVITATION_UID);
        int column_index_fuid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INVITATION_FUID);
        int column_index_msg = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INVITATION_MSG);
        int column_index_type = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INVITATION_TYPE);
        int column_index_eventId = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INVITATION_EVENTID);
        int column_index_createtime = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INVITATION_CREATETIME);

        try {
            while (cs.moveToNext()) {
                EventInvitationRecord eventInvitationRecord = new EventInvitationRecord();
                eventInvitationRecord.rid = cs.getInt(column_index_rid);
                eventInvitationRecord.uid = cs.getLong(column_index_uid);
                eventInvitationRecord.fuid = cs.getLong(column_index_fuid);
                eventInvitationRecord.msg = cs.getString(column_index_msg);
                eventInvitationRecord.type = cs.getInt(column_index_type);
                eventInvitationRecord.eventId = cs.getLong(column_index_eventId);
                eventInvitationRecord.createtime = cs.getLong(column_index_createtime);
                eventInvitationRecords.add(eventInvitationRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventInvitationRecords;
    }
}
