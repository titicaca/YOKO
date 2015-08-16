package com.Database;

public class EventRecord {
    public int rid;
    public long uid;
    public long serverid;
    public String introduciton;
    public String localpicturelink;
    public String remotepitcurelink;
    public long remind;
    public long timebegin;
    public long timeend;
    public int type;
    public int property;
    public String detaillink;
    public int status;
    public int modified;
    public long updatetime;

    public EventRecord() {

    }

    public EventRecord(EventRecord eventRecord) {
        this.rid = eventRecord.rid;
        this.uid = eventRecord.uid;
        this.serverid = eventRecord.serverid;
        this.introduciton = eventRecord.introduciton;
        this.localpicturelink = eventRecord.localpicturelink;
        this.remotepitcurelink = eventRecord.remotepitcurelink;
        this.remind = eventRecord.remind;
        this.timebegin = eventRecord.timebegin;
        this.timeend = eventRecord.timeend;
        this.type = eventRecord.type;
        this.property = eventRecord.property;
        this.detaillink = eventRecord.detaillink;
        this.status = eventRecord.status;
        this.modified = eventRecord.modified;
        this.updatetime = eventRecord.updatetime;
    }

    public EventRecord(int rid, long uid, long serverid, String introduciton,
                       String localpicturelink, String remotepitcurelink,
                       long remind, long timebegin, long timeend, int type,
                       int property, String detaillink, int status, int modified,
                       long updatetime) {
        this.rid = rid;
        this.uid = uid;
        this.serverid = serverid;
        this.introduciton = introduciton;
        this.localpicturelink = localpicturelink;
        this.remotepitcurelink = remotepitcurelink;
        this.remind = remind;
        this.timebegin = timebegin;
        this.timeend = timeend;
        this.type = type;
        this.property = property;
        this.detaillink = detaillink;
        this.status = status;
        this.modified = modified;
        this.updatetime = updatetime;
    }
}
