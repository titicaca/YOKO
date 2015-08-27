package com.Database;

import org.json.JSONObject;

public class FriendInvitationRecord extends DBRecord{
    public long rid;
    public long uid;
    public long fuid;
    public String msg;
    public long createdtime;

    public FriendInvitationRecord() {

    }

    public FriendInvitationRecord(FriendInvitationRecord friendInvitationRecord) {
        this.rid = friendInvitationRecord.rid;
        this.uid = friendInvitationRecord.uid;
        this.fuid = friendInvitationRecord.fuid;
        this.msg = friendInvitationRecord.msg;
        this.createdtime = friendInvitationRecord.createdtime;
    }

    public FriendInvitationRecord(long uid, long fuid, String msg) {
        this.uid = uid;
        this.fuid = fuid;
        this.msg = msg;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
