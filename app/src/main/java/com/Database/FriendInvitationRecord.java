package com.Database;

public class FriendInvitationRecord {
    public long rid;
    public long uid;
    public long fuid;
    public String msg;
    public long createtime;

    public FriendInvitationRecord() {

    }

    public FriendInvitationRecord(FriendInvitationRecord friendInvitationRecord) {
        this.rid = friendInvitationRecord.rid;
        this.uid = friendInvitationRecord.uid;
        this.fuid = friendInvitationRecord.fuid;
        this.msg = friendInvitationRecord.msg;
        this.createtime = friendInvitationRecord.createtime;
    }

    public FriendInvitationRecord(long uid, long fuid, String msg) {
        this.uid = uid;
        this.fuid = fuid;
        this.msg = msg;
    }
}
