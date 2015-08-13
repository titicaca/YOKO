package com.fifteentec.item;

import android.graphics.Bitmap;

/**
 * Created by cj on 2015/8/12.
 */
public class GroupBrief {
    private String groupName="";
    private String groupIntro="";
    private String logoUri="";

    public String getGroupName(){
        return groupName;
    }
    public String getGroupIntro(){
        return groupIntro;
    }
    public String getLogoUri(){
        return logoUri;
    }

    public void setGroupName(String name){
        this.groupName = name;
    }
    public void setGroupIntro(String intro){
        this.groupIntro = intro;
    }

    public void setLogoUri(String uri){
        this.logoUri = uri;
    }

}
