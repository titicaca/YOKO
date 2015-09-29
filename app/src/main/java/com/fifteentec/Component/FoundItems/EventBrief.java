package com.fifteentec.Component.FoundItems;

/**
 * Created by cj on 2015/8/12.
 */
public class EventBrief {
    private String groupName="";
    private String eventIntro="";
    private String eventUri="";
    private String picUri="";
    private String logoUri="";
    private long timeBegin=0;
    private long timeEnd=0;
    private long time=0;
    private String location="";
    private String intro = "";
    private String name = "";
    private int peopleEnronroll = 0;
    private int PeopleAll = 0;
    private int id=0;

    public String getGroupName(){
        return groupName;
    }
    public String getName(){
        return name;
    }
    public int getID(){
        return id;
    }
    public String getEventIntro(){
        return eventIntro;
    }
    public long getTime(){
        return time;
    }
    public long getTimeBegin(){
        return timeBegin;
    }
    public long getTimeEnd(){
        return timeEnd;
    }
    public String getLocation(){ return location;}
    public String getPicUri(){
        return picUri;
    }
    public String getLogoUri(){
        return logoUri;
    }
    public String getEventUri(){
        return eventUri;
    }


    public void setName(String name){
        this.name = name;
    }
    public void setGroupName(String name){
        this.groupName = name;
    }
    public void setID(int id){
        this.id = id;
    }
    public void setEventIntro(String intro){
        this.eventIntro = intro;
    }
    public void setTime(long time){
        this.time = time;
    }
    public void setTimeBegin(long time){
        this.timeBegin = time;
    }
    public void setTimeEnd(long time){
        this.timeEnd = time;
    }
    public void setLocation(String location){ this.location = location;}

    public void setPicUri(String uri){
        this.picUri = uri;
    }
    public void setLogoUri(String uri){
        this.logoUri = uri;
    }
    public void setEventUri(String uri){
        this.eventUri = uri;
    }

}
