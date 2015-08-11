package com.Database;

public class FriendRecord {
    public int _id;
    public String uid;
    public String fuid;
    public String tagId;
    public String tagName;

    public FriendRecord(){
    }

    public FriendRecord(String uid, String fuid, String tagId, String tagName) {
        this.uid = uid;
        this.fuid = fuid;
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
