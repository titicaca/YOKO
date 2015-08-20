package com.fifteentec.Component.calendar;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.Database.EventRecord;
import com.Database.TableEvent;
import com.fifteentec.Component.User.UserServer;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    public final static int DAY_VIEW_EVENT_MANAGER =0x00;

    public final static int WRONG_TYPE = 0x01;

    public final static int MillsInOneDay = 115740000;
    private int mType;
    private TableEvent mtableEvent;
    public long DayView_Date;

    private ArrayList<Long> AlldayEvent = new ArrayList<>();
    private ArrayList<Long> NormalEvent = new ArrayList<>();


    public static EventManager newInstance(TableEvent tableEvent,int Type,long TimeInMill){
        switch (Type){
            case DAY_VIEW_EVENT_MANAGER:
                EventManager eventManager = new EventManager(tableEvent);
                eventManager.mType = Type;
                eventManager.DayView_Date = TimeInMill;
                long End = eventManager.DayView_Date +MillsInOneDay;
                List<EventRecord> eventRecords= eventManager.mtableEvent.queryEvent(UserServer.getInstance().getUserid(),eventManager.DayView_Date,End);
                Log.d("Test","Begin:"+eventManager.DayView_Date+" End:"+End);
                Log.d("Test","Size:"+eventRecords.size());
                for(EventRecord eventRecord:eventRecords){
                    if(eventRecord.timebegin == eventRecord.timeend) eventManager.AlldayEvent.add(eventRecord.rid);
                    else {
                        eventManager.NormalEvent.add(eventRecord.rid);
                        Log.d("Test", "Normal Size:" + eventManager.NormalEvent.size());
                    }

                }
                return eventManager;
            default:
                break;
        }
        return null;
    }

    EventManager(TableEvent tableEvent){
        this.mtableEvent = tableEvent;
    }

    public int getAllDayEventCount(){
        return AlldayEvent.size();
    }
    public int getNormalDayEventCount(){
        return NormalEvent.size();
    }
    public String getAllDayIntroduciton(int index){
        EventRecord eventRecord=mtableEvent.queryEventByRid(AlldayEvent.get(index));
        return eventRecord.introduction;
    }

    public EventRecord getNormalEventByIndex(int index){
        return mtableEvent.queryEventByRid(NormalEvent.get(index));
    }

    public String getIntroduction(long rid){
        return mtableEvent.queryEventByRid(rid).introduction;
    }

    public boolean removeEvent(long rid){

        for (int i = 0; i < AlldayEvent.size(); i++) {
            long a = mtableEvent.queryEventByRid(AlldayEvent.get(i)).rid;
            if(a == rid) {
                AlldayEvent.remove(i);
                return true;
            }
        }
        for (int i = 0; i < NormalEvent.size(); i++) {
            long a = mtableEvent.queryEventByRid(NormalEvent.get(i)).rid;
            if(a == rid) {
                NormalEvent.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isAllDayEventExist(long rid){
        for (int i = 0; i < AlldayEvent.size(); i++) {
            long a = mtableEvent.queryEventByRid(AlldayEvent.get(i)).rid;
            if(a == rid) return true;
        }
        return false;
    }
    public long getAllDayEventRid(int index){
        return mtableEvent.queryEventByRid(AlldayEvent.get(index)).rid;
    }

    public boolean addEvent(long rid){
        EventRecord eventRecord=mtableEvent.queryEventByRid(rid);
        if(eventRecord.timebegin == eventRecord.timeend){
            AlldayEvent.add(rid);
            return true;
        }else {
            NormalEvent.add(rid);

            return false;
        }
    }

    public String getNormalIntroduction(int index){
        return mtableEvent.queryEventByRid(NormalEvent.get(index)).introduction;
    }


    public float getPositionRatioByTime(long TimeBegin){
        long offset = TimeBegin - DayView_Date;


        return offset/(float)MillsInOneDay;

    }


    public void UpdateEvent(long timebegin, long timeend, long rid) {
        EventRecord eventRecord = mtableEvent.queryEventByRid(rid);
        eventRecord.timebegin = timebegin;
        eventRecord.timeend = timeend;
        mtableEvent.updateEvent(eventRecord);
    }
}
