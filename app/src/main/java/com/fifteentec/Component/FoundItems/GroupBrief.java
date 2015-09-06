package com.fifteentec.Component.FoundItems;

/**
 * Created by cj on 2015/8/12.
 */
public class GroupBrief {
    private String groupName="";
    private String groupIntro="";
    private String logoUri="";
    private String bigPicUri="";
    private String smallPicUriLeft="";
    private String smallPicUriMid="";
    private String smallPicUriRight="";

    public String getGroupName(){
        return groupName;
    }
    public String getGroupIntro(){
        return groupIntro;
    }
    public String getLogoUri(){
        return logoUri;
    }
    public String getBigPicUri(){
        return bigPicUri;
    }
    public String getSmallPicUriLeft(){return smallPicUriLeft;}
    public String getSmallPicUriMid(){return smallPicUriMid;}
    public String getSmallPicUriRight(){return smallPicUriRight;}


    public void setGroupName(String name){
        this.groupName = name;
    }
    public void setGroupIntro(String intro){
        this.groupIntro = intro;
    }
    public void setLogoUri(String uri){
        this.logoUri = uri;
    }
    public void setBigPicUri(String uri){
        this.bigPicUri = uri;
    }
    public void setSmallPicUriLeft(String uri){
        this.smallPicUriLeft = uri;
    }
    public void setSmallPicUriMid(String uri){
        this.smallPicUriRight = uri;
    }
    public void setSmallPicUriRight(String uri){
        this.smallPicUriRight = uri;
    }

}
