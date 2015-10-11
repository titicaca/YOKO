package com.fifteentec.Component.Parser;

import android.util.Log;

import com.fifteentec.Component.FoundItems.EventBrief;
import com.fifteentec.Component.FoundItems.FavoriteBrief;
import com.fifteentec.Component.FoundItems.GroupBrief;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by benbush on 15/8/24.
 */
public class FoundDataParser {
    public static List<GroupBrief> parseGroupBriefInfo(final JSONObject response) {
        List<GroupBrief> groupBriefs = new ArrayList<GroupBrief>();

        try {
            JSONArray groups = response.getJSONArray("list");
            for (int i = 0; i < groups.length(); ++i) {
                JSONObject group = (JSONObject)groups.get(i);
                String groupName=group.getString("name");
                String groupIntro=group.getString("introduction");
                String bigPicUrl = group.getString("picturelink");

                GroupBrief briefItem = new GroupBrief ();
                briefItem.setGroupName(groupName);
                briefItem.setGroupIntro(groupIntro);
                //briefItem.setBigPicUri(bigPicUrl);

                groupBriefs.add(briefItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groupBriefs;
    }

    public static List<EventBrief> parseEventBriefInfo(final JSONObject response) {
        List<EventBrief> eventBriefs = new ArrayList<EventBrief>();
   //     Log.e("parser","start parsing" );
        try {
            JSONArray groups = response.getJSONArray("list");
            for (int i = 0; i < groups.length(); ++i) {
                JSONObject group = (JSONObject)groups.get(i);
                long id = group.getLong("id");
                String location = group.getString("location");
                String time = group.getString("createdtime");
                String name = group.getString("name");
                int peopleEnroll = group.getInt("peopleenroll");
                String timeEnd = group.getString("timeend");
                int peopleAll = group.getInt("peopleall");
                String timeBegin = group.getString("timebegin");
                String detailLink = group.getString("detaillink");
                String groupIntro=group.getString("introduction");
                String PicUrl = group.getString("picturelink");


                EventBrief briefItem = new EventBrief();
                
                briefItem.setID(id);
                briefItem.setEventIntro(groupIntro);
                briefItem.setEventUri(detailLink);
                briefItem.setPicUri(PicUrl);
                briefItem.setLocation(location);
                if(time=="null"){
                    briefItem.setTime(0);
                }
                else briefItem.setTime(Long.valueOf(time));
                if(timeBegin=="null"){
                    briefItem.setTimeBegin(0);
                }
                else briefItem.setTimeBegin(Long.valueOf(timeBegin));
                if(timeEnd=="null"){
                    briefItem.setTimeEnd(0);
                }
                else briefItem.setTimeEnd(Long.valueOf(timeEnd));

                briefItem.setName(name);

                eventBriefs.add(briefItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return eventBriefs;
    }

    public static List<FavoriteBrief> parseFavoriteBriefInfo(final JSONObject response) {
        List<FavoriteBrief> eventBriefs = new ArrayList<FavoriteBrief>();
        try {
            JSONArray groups = response.getJSONArray("list");
            for (int i = 0; i < groups.length(); ++i) {
                JSONObject group = (JSONObject) groups.get(i);
                String groupName = group.getString("name");
                String groupIntro = group.getString("introduction");
                String PicUrl = group.getString("picturelink");

                FavoriteBrief briefItem = new FavoriteBrief();
                briefItem.setGroupName(groupName);
                briefItem.setEventIntro(groupIntro);
                briefItem.setEventUri(PicUrl);

                eventBriefs.add(briefItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return eventBriefs;
    }
}
