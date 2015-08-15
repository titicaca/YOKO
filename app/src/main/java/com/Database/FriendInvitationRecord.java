package com.Database;

/**
 * Created by benbush on 15/8/15.
 */
public class FriendInvitationRecord {
    public int rid;
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

    public FriendInvitationRecord(long uid, long fuid, String msg, long createtime) {
        this.uid = uid;
        this.fuid = fuid;
        this.msg = msg;
        this.createtime = createtime;
    }
}
