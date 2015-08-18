package com.Database;

public class FriendTagRecord extends DBRecord {
    public long rid;
    public long uid;
    public long fuid;
    public long tagId;
    public String tagName;

    public FriendTagRecord() {

    }

    public FriendTagRecord(FriendTagRecord friendTagRecord) {
        this.rid = friendTagRecord.rid;
        this.uid = friendTagRecord.uid;
        this.fuid = friendTagRecord.fuid;
        this.tagId = friendTagRecord.tagId;
        this.tagName = friendTagRecord.tagName;
    }

    public FriendTagRecord(long uid, long fuid, long tagId, String tagName) {
        this.uid = uid;
        this.fuid = fuid;
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
