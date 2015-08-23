package com.Database;

import org.json.JSONObject;

/**
 * Created by benbush on 15/8/20.
 */
public class EventInvitationRecord extends DBRecord{
    public long rid;
    public long uid;
    public long fuid;
    public String msg;
    public int type;
    public long eventId;
    public long createtime;

    public EventInvitationRecord() {

    }

    public EventInvitationRecord(EventInvitationRecord eventInvitationRecord) {
        this.rid = eventInvitationRecord.rid;
        this.uid = eventInvitationRecord.uid;
        this.fuid = eventInvitationRecord.fuid;
        this.msg = eventInvitationRecord.msg;
        this.type = eventInvitationRecord.type;
        this.eventId = eventInvitationRecord.eventId;
        this.createtime = eventInvitationRecord.createtime;
    }

    public EventInvitationRecord(long uid, long fuid, String msg, int type, long eventId) {
        this.uid = uid;
        this.fuid = fuid;
        this.msg = msg;
        this.type = type;
        this.eventId = eventId;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
