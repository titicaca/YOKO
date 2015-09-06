package com.fifteentec.Component.FoundItems;

/**
 * Created by cj on 2015/8/12.
 */
public class EventBrief {
    private String groupName="";
    private String eventIntro="";
    private String logoUri="";
    private String eventUri="";
    private String time="";
    private String location="";
    private String tags="";

    public String getGroupName(){
        return groupName;
    }
    public String getEventIntro(){
        return eventIntro;
    }
    public String getTime(){
        return time;
    }
    public String getLocation(){ return location;}
    public String getLogoUri(){
        return logoUri;
    }
    public String getEventUri(){
        return eventUri;
    }
    public String getTags(){
        return tags;
    }

    public void setGroupName(String name){
        this.groupName = name;
    }
    public void setEventIntro(String intro){
        this.eventIntro = intro;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setLocation(String location){ this.location = location;}

    public void setLogoUri(String uri){
        this.logoUri = uri;
    }
    public void setEventUri(String uri){
        this.eventUri = uri;
    }
    public void setTags(String tags){
        this.tags = tags;
    }

}
