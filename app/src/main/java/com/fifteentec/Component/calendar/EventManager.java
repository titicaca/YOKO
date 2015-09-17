package com.fifteentec.Component.calendar;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.Database.EventRecord;
import com.Database.TableEvent;
import com.fifteentec.Component.User.UserServer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 该类用来管理视图内部日期与事件的管理.
 * 通过newInstance取得对象,针对不用的视图提供不同的初始化方式.
 */
public class EventManager {

    public final static int DAY_VIEW_EVENT_MANAGER =0x00;
    public final static int WEEK_VIEW_EVENT_MANAGER = 0x02;
    public final static int EVENT_LIST_EVENT_MANAGER = 0x03;
    public final static int MONTH_VIEW_EVENT_MANAGER= 0x04;
    public final static int WRONG_TYPE = 0x01;

    public final static int MillsInOneDay = 86400000;
    private int mType;
    private TableEvent mtableEvent;
    public long DayView_Date;

    private ArrayList<Long> AlldayEvent = new ArrayList<>();
    private ArrayList<Long> NormalEvent = new ArrayList<>();
    public ArrayList<DrawTag> drawTags = new ArrayList<>();

    private ArrayList<EventManager> WeekManager = new ArrayList<>();


    public EventRecord getEventRecordByRid(long rid){
        return mtableEvent.queryEventByRid(rid);
    }


    public void UpdateEventManager(){
        switch (mType){
            case DAY_VIEW_EVENT_MANAGER:
                long End = DayView_Date +MillsInOneDay;
                List<EventRecord> eventRecords=mtableEvent.queryEvent(UserServer.getInstance().getUserid(),DayView_Date,End);
                AlldayEvent.clear();
                NormalEvent.clear();
                for(EventRecord eventRecord:eventRecords){
                    if(eventRecord.timebegin == eventRecord.timeend&&eventRecord.timebegin==DayView_Date) AlldayEvent.add(eventRecord.rid);
                    else {
                        NormalEvent.add(eventRecord.rid);
                    }
                }
                break;
            case WEEK_VIEW_EVENT_MANAGER:
                WeekManager.clear();
                ArrayList<Long> DayStart = CalUtil.GetWeekBeginTimeInMills(DayView_Date);
                for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
                    EventManager temp = EventManager.newInstance(mtableEvent,DAY_VIEW_EVENT_MANAGER,DayStart.get(i));
                    WeekManager.add(temp);
                }

                break;
        }
    }

    public static EventManager newInstance(TableEvent tableEvent,int Type,long TimeInMill){
        EventManager eventManager;
        switch (Type){
            case DAY_VIEW_EVENT_MANAGER:
                eventManager = new EventManager(tableEvent);
                eventManager.mType = Type;
                eventManager.DayView_Date = TimeInMill;
                long End = eventManager.DayView_Date +MillsInOneDay;
                List<EventRecord> eventRecords= eventManager.mtableEvent.queryEvent(UserServer.getInstance().getUserid(),eventManager.DayView_Date,End);
                for(EventRecord eventRecord:eventRecords){
                    if(eventRecord.timebegin == eventRecord.timeend&&eventRecord.timebegin==eventManager.DayView_Date) eventManager.AlldayEvent.add(eventRecord.rid);
                    else {
                        eventManager.NormalEvent.add(eventRecord.rid);
                    }
                }
                return eventManager;
            case WEEK_VIEW_EVENT_MANAGER:
                eventManager = new EventManager(tableEvent);
                eventManager.mType = Type;
                eventManager.DayView_Date = TimeInMill;
                ArrayList<Long> DayStart = CalUtil.GetWeekBeginTimeInMills(TimeInMill);
                for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
                    EventManager temp = EventManager.newInstance(tableEvent,DAY_VIEW_EVENT_MANAGER,DayStart.get(i));
                    eventManager.WeekManager.add(temp);
                }
                return eventManager;
            case EVENT_LIST_EVENT_MANAGER:
                eventManager = new EventManager(tableEvent);
                eventManager.mType = Type;
                eventManager.DayView_Date = TimeInMill;
                GregorianCalendar temp = new GregorianCalendar();
                temp.setTimeInMillis(eventManager.DayView_Date);
                long TimeBegin = eventManager.DayView_Date;
                for (int i = 0; i < CalUtil.LENTH_OF_MONTH.get(temp.get(Calendar.MONTH)); i++) {
                    DrawTag drawTag = eventManager.newDrawTag();
                    long TimeEnd = TimeBegin +MillsInOneDay;
                    List<EventRecord> eventRecords1 = eventManager.mtableEvent.queryEvent(UserServer.getInstance().getUserid(),TimeBegin,TimeEnd);
                    for(EventRecord eventRecord:eventRecords1){
                        if(eventRecord.timebegin == eventRecord.timeend) drawTag.task= true;
                        else {
                            if(drawTag.event.size()<4){
                                drawTag.event.add(eventRecord.type);
                            }
                        }
                    }


                }

                return eventManager;
            case MONTH_VIEW_EVENT_MANAGER:
                eventManager = new EventManager(tableEvent);
                eventManager.mType = Type;
                eventManager.DayView_Date = TimeInMill;
                long EndTimeMonth = eventManager.DayView_Date +MillsInOneDay;
                List<EventRecord> eventRecords2 = eventManager.mtableEvent.queryEvent(UserServer.getInstance().getUserid(),eventManager.DayView_Date,EndTimeMonth);
                for(EventRecord eventRecord:eventRecords2){
                    if(eventRecord.timebegin == eventRecord.timeend) eventManager.AlldayEvent.add(eventRecord.rid);
                    else {
                        eventManager.NormalEvent.add(eventRecord.rid);
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

    public int getAllDayTag(int index){
        EventRecord eventRecord=mtableEvent.queryEventByRid(AlldayEvent.get(index));
        return eventRecord.type;
    }

    public EventRecord getNormalEventByIndex(int index){
        return mtableEvent.queryEventByRid(NormalEvent.get(index));
    }

    public String getIntroduction(long rid){
        return mtableEvent.queryEventByRid(rid).introduction;
    }

    public EventManager getEventMangerInDayOfWeek(int index){
        if(mType == WEEK_VIEW_EVENT_MANAGER){
            return WeekManager.get(index);
        }else{
            return null;
        }
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
        /*
        EventRecord eventRecord = mtableEvent.queryEventByRid(rid);
        if(eventRecord.timebegin==eventRecord.timeend&&eventRecord.timebegin==DayView_Date) return true;
        */
        return false;
    }
    public long getAllDayEventRid(int index){
        return mtableEvent.queryEventByRid(AlldayEvent.get(index)).rid;
    }

    public boolean addEvent(long rid){
        EventRecord eventRecord = mtableEvent.queryEventByRid(rid);
        if(mType == DAY_VIEW_EVENT_MANAGER) {
            if (eventRecord.timebegin == eventRecord.timeend && eventRecord.timebegin== DayView_Date) {
                AlldayEvent.add(rid);
                return true;
            } else {
                NormalEvent.add(rid);
                return false;
            }
        }else if(mType == WEEK_VIEW_EVENT_MANAGER){
            for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
                if(eventRecord.timebegin <WeekManager.get(i).DayView_Date+MillsInOneDay&&eventRecord.timebegin>=WeekManager.get(i).DayView_Date){
                    if(eventRecord.timebegin == eventRecord.timeend){
                        WeekManager.get(i).AlldayEvent.add(rid);
                        return true;
                    }else {
                        WeekManager.get(i).NormalEvent.add(rid);
                        return false;
                    }
                }

            }
        }
        return false;
    }

    public String getNormalIntroduction(int index){
        return mtableEvent.queryEventByRid(NormalEvent.get(index)).introduction;
    }

    public String getNormalIntroduction(long rid){
        return mtableEvent.queryEventByRid(rid).introduction;
    }

    public int getNormalType(int index){
        return mtableEvent.queryEventByRid(NormalEvent.get(index)).type;
    }

    public int getNormalType(long rid){
        return mtableEvent.queryEventByRid(rid).type;
    }


    public double getPositionRatioByTime(long TimeBegin){
        long offset = TimeBegin - DayView_Date;

        return offset/(double)MillsInOneDay;

    }

    public boolean deleteByRid(long rid){
        if(mType == WEEK_VIEW_EVENT_MANAGER){
            for (int i = 0; i < CalUtil.LENTH_OF_WEEK; i++) {
                EventManager eventManager = WeekManager.get(i);
                if(eventManager.deleteByRid(rid))
                    return true;
            }
        }else if(mType == DAY_VIEW_EVENT_MANAGER){
            for (int i = 0; i < NormalEvent.size(); i++) {
                if(NormalEvent.get(i) == rid){
                    NormalEvent.remove(i);
                    return true;
                }
            }
            for (int i = 0; i < AlldayEvent.size(); i++) {
                if(AlldayEvent.get(i) == rid){
                    AlldayEvent.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public void UpdateEvent(long timebegin, long timeend, long rid) {
        EventRecord eventRecord = mtableEvent.queryEventByRid(rid);
        eventRecord.timebegin = timebegin;
        eventRecord.timeend = timeend;
        mtableEvent.updateEvent(eventRecord);
    }

    public DrawTag newDrawTag(){
        DrawTag drawTag = new DrawTag();
        drawTags.add(drawTag);
        return drawTag;
    }


    public class DrawTag{
        boolean task = false;
        boolean festival=false;
        ArrayList<Integer> event= new ArrayList<>();

        DrawTag(){}

    }
}
