package com.Database;

public class FriendInfoRecord {
    public int rid;
    public long uid;
    public long fuid;
    public String email;
    public String location;
    public String mobile;
    public String nickname;
    public String picturelink;
    public String qq;
    public int sex;
    public String wechat;
    public String weibo;
    public int collectnumber;
    public int enrollnumber;
    public int friendnumber;
    public long logintime;

    public FriendInfoRecord() {

    }

    public FriendInfoRecord(FriendInfoRecord friendInfoRecord) {
        this.rid = friendInfoRecord.rid;
        this.uid = friendInfoRecord.uid;
        this.fuid = friendInfoRecord.fuid;
        this.email = friendInfoRecord.email;
        this.location = friendInfoRecord.location;
        this.mobile = friendInfoRecord.mobile;
        this.nickname = friendInfoRecord.nickname;
        this.picturelink = friendInfoRecord.picturelink;
        this.qq = friendInfoRecord.qq;
        this.sex = friendInfoRecord.sex;
        this.wechat = friendInfoRecord.wechat;
        this.weibo = friendInfoRecord.weibo;
        this.collectnumber = friendInfoRecord.collectnumber;
        this.enrollnumber = friendInfoRecord.enrollnumber;
        this.friendnumber = friendInfoRecord.friendnumber;
        this.logintime = friendInfoRecord.logintime;
    }

    public FriendInfoRecord(long uid, long fuid, String email, String location,
                            String mobile, String nickname, String picturelink,
                            String qq, int sex, String wechat, String weibo,
                            int collectnumber, int enrollnumber, int friendnumber,
                            long logintime) {
        this.uid = uid;
        this.fuid = fuid;
        this.email = email;
        this.location = location;
        this.mobile = mobile;
        this.nickname = nickname;
        this.picturelink = picturelink;
        this.qq = qq;
        this.sex = sex;
        this.wechat = wechat;
        this.weibo = weibo;
        this.collectnumber = collectnumber;
        this.enrollnumber = enrollnumber;
        this.friendnumber = friendnumber;
        this.logintime = logintime;
    }
}
