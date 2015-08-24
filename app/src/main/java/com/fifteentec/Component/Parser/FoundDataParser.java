package com.fifteentec.Component.Parser;

import com.fifteentec.FoundItems.EventBrief;
import com.fifteentec.FoundItems.FavoriteBrief;
import com.fifteentec.FoundItems.GroupBrief;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        try {
            JSONArray groups = response.getJSONArray("list");
            for (int i = 0; i < groups.length(); ++i) {
                JSONObject group = (JSONObject)groups.get(i);
                String groupName=group.getString("name");
                String groupIntro=group.getString("introduction");
                String PicUrl = group.getString("picturelink");

                EventBrief briefItem = new EventBrief();
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
