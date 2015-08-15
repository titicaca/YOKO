package com.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benbush on 15/8/14.
 */
public class TableEvent extends DBTable {

    public TableEvent(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public void setTableName() {
        this.tableName = DBConstants.TABLE_EVENT;
    }

    @Override
    public void createUniqueIndex() {

    }

    @Override
    public void deleteUser(long uid) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_EVENT_UID + " = ?",
                    new String[]{String.valueOf(uid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void addEvent(EventRecord eventRecord) {
        db.beginTransaction();

        try {
            db.execSQL("INSERT OR IGNORE INTO " + tableName + "VALUES(NULL, ?, NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{eventRecord.uid, eventRecord.introduciton, eventRecord.localpicturelink,
                            eventRecord.remotepitcurelink, eventRecord.remind, eventRecord.timebegin,
                            eventRecord.timeend, eventRecord.type, eventRecord.property,
                            eventRecord.detaillink, eventRecord.status, System.currentTimeMillis()}
            );

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteEvent(int rid) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_EVENT_RID + " = ?",
                    new String[]{String.valueOf(rid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void updateEvent(EventRecord eventRecord) {
        db.beginTransaction();

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.COLUMN_EVENT_INTRODUCTION, eventRecord.introduciton);
            cv.put(DBConstants.COLUMN_EVENT_LOCALPICTURELINK, eventRecord.localpicturelink);
            cv.put(DBConstants.COLUMN_EVENT_REMOTEPICTURELINK, eventRecord.remotepitcurelink);
            cv.put(DBConstants.COLUMN_EVENT_REMIND, eventRecord.remind);
            cv.put(DBConstants.COLUMN_EVENT_TIMEBEGIN, eventRecord.timebegin);
            cv.put(DBConstants.COLUMN_EVENT_TIMEEND, eventRecord.timeend);
            cv.put(DBConstants.COLUMN_EVENT_TYPE, eventRecord.type);
            cv.put(DBConstants.COLUMN_EVENT_PROPERTY, eventRecord.property);
            cv.put(DBConstants.COLUMN_EVENT_DETAILLINK, eventRecord.detaillink);
            cv.put(DBConstants.COLUMN_EVENT_STATUS, eventRecord.status);
            cv.put(DBConstants.COLUMN_EVENT_UPDATETIME, System.currentTimeMillis());

            db.update(tableName, cv,
                    DBConstants.COLUMN_EVENT_RID + " = ?",
                    new String[]{String.valueOf(eventRecord.rid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public EventRecord queryEvent(int rid) {
        EventRecord eventRecord = null;
        Cursor cs = null;
        try {
            cs = db.query(tableName, null,
                    DBConstants.COLUMN_EVENT_RID + " = ?",
                    new String[]{String.valueOf(rid)},
                    null, null, null);
            if (cs.getCount() == 1) {
                cs.moveToFirst();
                eventRecord = new EventRecord();

                int column_index_rid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_RID);
                int column_index_uid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_UID);
                int column_index_serverid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_SEVERID);
                int column_index_introduciton = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INTRODUCTION);
                int column_index_localpicturelink = cs.getColumnIndex(DBConstants.COLUMN_EVENT_LOCALPICTURELINK);
                int column_index_remotepitcurelink = cs.getColumnIndex(DBConstants.COLUMN_EVENT_REMOTEPICTURELINK);
                int column_index_remind = cs.getColumnIndex(DBConstants.COLUMN_EVENT_REMIND);
                int column_index_timebegin = cs.getColumnIndex(DBConstants.COLUMN_EVENT_TIMEBEGIN);
                int column_index_timeend = cs.getColumnIndex(DBConstants.COLUMN_EVENT_TIMEEND);
                int column_index_type = cs.getColumnIndex(DBConstants.COLUMN_EVENT_TYPE);
                int column_index_property = cs.getColumnIndex(DBConstants.COLUMN_EVENT_PROPERTY);
                int column_index_detaillink = cs.getColumnIndex(DBConstants.COLUMN_EVENT_DETAILLINK);
                int column_index_status = cs.getColumnIndex(DBConstants.COLUMN_EVENT_STATUS);
                int column_index_updatetime = cs.getColumnIndex(DBConstants.COLUMN_EVENT_UPDATETIME);

                eventRecord.rid = cs.getInt(column_index_rid);
                eventRecord.uid = cs.getLong(column_index_uid);
                eventRecord.serverid = cs.getLong(column_index_serverid);
                eventRecord.introduciton = cs.getString(column_index_introduciton);
                eventRecord.localpicturelink = cs.getString(column_index_localpicturelink);
                eventRecord.remotepitcurelink = cs.getString(column_index_remotepitcurelink);
                eventRecord.remind = cs.getLong(column_index_remind);
                eventRecord.timebegin = cs.getLong(column_index_timebegin);
                eventRecord.timeend = cs.getLong(column_index_timeend);
                eventRecord.type = cs.getInt(column_index_type);
                eventRecord.property = cs.getInt(column_index_property);
                eventRecord.detaillink = cs.getString(column_index_detaillink);
                eventRecord.status = cs.getInt(column_index_status);
                eventRecord.updatetime = column_index_updatetime;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return eventRecord;
    }

    public List<EventRecord> queryEvent(long uid, long startTime, long endTime) {
        List<EventRecord> eventRecords = null;
        Cursor cs = null;
        try {
            cs = db.query(tableName, null,
                    DBConstants.COLUMN_EVENT_UID + " = ?" + " AND " +
                    DBConstants.COLUMN_EVENT_TIMEBEGIN + " >= ?" + " AND " +
                    DBConstants.COLUMN_EVENT_TIMEBEGIN + " <= ?",
                    new String[]{String.valueOf(uid), String.valueOf(startTime), String.valueOf(endTime)},
                    null, null, DBConstants.COLUMN_EVENT_TIMEBEGIN);
            eventRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return eventRecords;
    }

    public List<EventRecord> queryEvent(long uid) {
        List<EventRecord> eventRecords = null;
        Cursor cs = null;
        try {
            cs = db.query(tableName, null,
                    DBConstants.COLUMN_EVENT_UID + " = ?",
                    new String[]{String.valueOf(uid)},
                    null, null, DBConstants.COLUMN_EVENT_TIMEBEGIN);
            eventRecords = cursorToList(cs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cs != null) cs.close();
        }

        return eventRecords;
    }

    private List<EventRecord> cursorToList(Cursor cs) {
        List<EventRecord> eventRecords = new ArrayList<EventRecord>();

        int column_index_rid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_RID);
        int column_index_uid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_UID);
        int column_index_serverid = cs.getColumnIndex(DBConstants.COLUMN_EVENT_SEVERID);
        int column_index_introduciton = cs.getColumnIndex(DBConstants.COLUMN_EVENT_INTRODUCTION);
        int column_index_localpicturelink = cs.getColumnIndex(DBConstants.COLUMN_EVENT_LOCALPICTURELINK);
        int column_index_remotepitcurelink = cs.getColumnIndex(DBConstants.COLUMN_EVENT_REMOTEPICTURELINK);
        int column_index_remind = cs.getColumnIndex(DBConstants.COLUMN_EVENT_REMIND);
        int column_index_timebegin = cs.getColumnIndex(DBConstants.COLUMN_EVENT_TIMEBEGIN);
        int column_index_timeend = cs.getColumnIndex(DBConstants.COLUMN_EVENT_TIMEEND);
        int column_index_type = cs.getColumnIndex(DBConstants.COLUMN_EVENT_TYPE);
        int column_index_property = cs.getColumnIndex(DBConstants.COLUMN_EVENT_PROPERTY);
        int column_index_detaillink = cs.getColumnIndex(DBConstants.COLUMN_EVENT_DETAILLINK);
        int column_index_status = cs.getColumnIndex(DBConstants.COLUMN_EVENT_STATUS);
        int column_index_updatetime = cs.getColumnIndex(DBConstants.COLUMN_EVENT_UPDATETIME);

        try {
            while (cs.moveToNext()) {
                EventRecord eventRecord = new EventRecord();
                eventRecord.rid = cs.getInt(column_index_rid);
                eventRecord.uid = cs.getLong(column_index_uid);
                eventRecord.serverid = cs.getLong(column_index_serverid);
                eventRecord.introduciton = cs.getString(column_index_introduciton);
                eventRecord.localpicturelink = cs.getString(column_index_localpicturelink);
                eventRecord.remotepitcurelink = cs.getString(column_index_remotepitcurelink);
                eventRecord.remind = cs.getLong(column_index_remind);
                eventRecord.timebegin = cs.getLong(column_index_timebegin);
                eventRecord.timeend = cs.getLong(column_index_timeend);
                eventRecord.type = cs.getInt(column_index_type);
                eventRecord.property = cs.getInt(column_index_property);
                eventRecord.detaillink = cs.getString(column_index_detaillink);
                eventRecord.status = cs.getInt(column_index_status);
                eventRecord.updatetime = column_index_updatetime;
                eventRecords.add(eventRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventRecords;
    }

    public void syncUser(long uid, List<EventRecord> eventRecords) {
        db.beginTransaction();

        try {
            db.delete(tableName,
                    DBConstants.COLUMN_EVENT_UID + " = ?",
                    new String[]{String.valueOf(uid)});

            for (EventRecord eventRecord : eventRecords) {
                db.execSQL("INSERT OR IGNORE INTO " + tableName + "VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{eventRecord.uid, eventRecord.serverid, eventRecord.introduciton,
                                eventRecord.localpicturelink, eventRecord.remotepitcurelink,
                                eventRecord.remind, eventRecord.timebegin, eventRecord.timeend,
                                eventRecord.type, eventRecord.property, eventRecord.detaillink,
                                eventRecord.status, eventRecord.updatetime}
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

