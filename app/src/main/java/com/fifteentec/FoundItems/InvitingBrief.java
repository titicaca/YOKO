package com.fifteentec.FoundItems;

/**
 * Created by cj on 2015/8/12.
 */
public class InvitingBrief {
    private String friendName="";
    private String invitedContent="";
    private String logoUri="";
    private String time="";
    private Boolean checked = false;
    private Boolean newArgue = false;

    public String getFriendName(){
        return friendName;
    }
    public String getInvitedContent(){
        return invitedContent;
    }
    public String getTime(){
        return time;
    }
    public String getLogoUri(){
        return logoUri;
    }
    public Boolean getChecked(){
        return checked;
    }
    public Boolean getArgue(){
        return newArgue;
    }

    public void setFriendName(String friend){
        this.friendName=friend;
    }
    public void setInvitedContent(String invited){
        this.invitedContent = invited;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setLogoUri(String uri){
        this.logoUri = uri;
    }
    public void setChecked(Boolean checked){
        this.checked=checked;
    }
    public void setArgue(Boolean newArgue){
        this.newArgue = newArgue;
    }

}
