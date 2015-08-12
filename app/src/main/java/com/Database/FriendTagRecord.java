package com.Database;

public class FriendTagRecord {
    public int _id;
    public long uid;
    public long fuid;
    public long tagId;
    public String tagName;

    public FriendTagRecord(){
    }

    public FriendTagRecord(long uid, long fuid, long tagId, String tagName) {
        this.uid = uid;
        this.fuid = fuid;
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
