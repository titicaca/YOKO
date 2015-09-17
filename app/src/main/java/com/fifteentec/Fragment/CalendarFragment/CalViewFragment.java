package com.fifteentec.Fragment.CalendarFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.Database.DBManager;
import com.Database.EventRecord;
import com.fifteentec.Component.User.UserServer;
import com.fifteentec.Component.calendar.CalendarController;
import com.fifteentec.Component.calendar.DayEventView;
import com.fifteentec.Component.calendar.NewEventView;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class CalViewFragment extends Fragment {

    //private CalView mCalView;

    private CalendarController mDate;

    private TextView mCalendarFilter;
    private ImageView mEventListButton;
    private FragmentManager mFragmentManager;
    private EventListViewFragment mListView;
    private FrameLayout mMainView;
    private NewEventView mNewEventView;
    private DayEventView mdayEventView;
    private WeekEventFragment mWeekEventFragment;

    private final int EVENT_LIST = 0x00;
    private final int CAL_VIEW_MONTH_TAP = 0x01;

    private final String IMAGE_TYPE = "image/*";
    private final String CAMERA_PATH =Environment.getExternalStorageDirectory().getPath()+"/"+Environment.DIRECTORY_DCIM+"/";
    private final String YOKO_STORAGE_PATH = Environment.getExternalStorageDirectory().getPath()+"/YOKO/EventPic/";
    private String RECENT_FILE_NAME;

    private final int IMAGE_NEWEVENT_CODE = 0x00;
    private final int IMAGE_OPENCAMERA_CODE = 0x01;

    private BaseActivity activity;
    private DBManager dbManager;

    private final String INTRODUCTION ="Introduction";
    private final String TIMEBEGIN = "StartTime";
    private final String TIMEEND ="EndTime";
    private final String TYPE = "Type";
    private final String REMIND ="Reminder";
    private final String RID = "Rid";
    private final String LOCALPICLINK = "LocalPicLink";

    private final int DAY_EVENT_VIEW = 0x00;

    private boolean month = true;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(mDate == null)
        {
            mDate = new CalendarController();
        }
        mFragmentManager = getFragmentManager();
        super.onCreate(savedInstanceState);

        this.activity = (BaseActivity)this.getActivity();
        this.dbManager = this.activity.getDBManager();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_main_layout, container, false);
        mMainView = (FrameLayout) view.findViewById(R.id.id_cal_main_all);

        mCalendarFilter = (TextView) view.findViewById(R.id.id_calendar);
        mCalendarFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWeekEventFragment.UpdateScale();
            }
        });
        mEventListButton = (ImageView) view.findViewById(R.id.id_eventlist_button);
        mEventListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                if(month){
                    if(mWeekEventFragment !=null){
                        if(mListView != null) fragmentTransaction.hide(mListView);
                        fragmentTransaction.show(mWeekEventFragment).commit();

                    }else {
                        if(mListView != null) fragmentTransaction.hide(mListView);
                        mWeekEventFragment = WeekEventFragment.newInstance(mDate.getCurArray());
                        fragmentTransaction.add(R.id.id_event_content,mWeekEventFragment).commit();
                    }
                    month = false;
                }else{
                    if(mListView !=null){
                        if(mWeekEventFragment != null) fragmentTransaction.hide(mWeekEventFragment);
                        fragmentTransaction.show(mListView).commit();
                    }else {
                        if(mWeekEventFragment != null) fragmentTransaction.hide(mWeekEventFragment);
                        mListView = EventListViewFragment.newInstance(mDate.getCurArray());
                        fragmentTransaction.add(R.id.id_event_content,mListView).commit();
                    }
                    month =true;
                }
            }
        });

        ImageView NewEvent = (ImageView)view.findViewById(R.id.id_new_event);
        NewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewEvent(NewEventView.BLANK_EVENT,null);
            }
        });

        TextView today = (TextView)view.findViewById(R.id.id_today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekEventFragment.UpdateToView(mDate.getNowCalendar());
            }
        });

        FragmentTransaction mTrans = mFragmentManager.beginTransaction();
        mListView = EventListViewFragment.newInstance(mDate.getNowArray());
        /*
        mListView.setEventFragmentListener(new EventListViewFragment.EventListFragmentListener() {
            @Override
            public void ListDateChange(ArrayList<Integer> list) {
                mDate.UpdateCur(list);
            }
        });*/

        mWeekEventFragment = WeekEventFragment.newInstance(mDate.getNowArray());
        mWeekEventFragment.setmWeekViewFragmentLinstener(new WeekEventFragment.WeekViewFragmentLinstener() {
            @Override
            public void CheckExist(long rid) {
                CreateNewEvent(NewEventView.EXIST_EVENT, dbManager.getTableEvent().queryEventByRid(rid));
            }

            @Override
            public void CreateRecord(int TYPE, EventRecord eventRecord) {
                CreateNewEvent(TYPE, eventRecord);
            }

            @Override
            public void UpdateTime(ArrayList<Integer> Date) {
                mDate.UpdateCur(Date);
            }

            @Override
            public void ShowDetailView(GregorianCalendar date) {
                showDayEventView(date);
            }
        });

        mTrans.add(R.id.id_event_content,mListView);
        mTrans.hide(mListView);
        mTrans.add(R.id.id_event_content,mWeekEventFragment);
        mTrans.hide(mWeekEventFragment);
        mTrans.show(mWeekEventFragment).commit();


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String Path = null;
        switch (requestCode) {
            case IMAGE_NEWEVENT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().managedQuery(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    Path = cursor.getString(column_index);
                    try {
                        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                            cursor.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case IMAGE_OPENCAMERA_CODE:
                if(resultCode == Activity.RESULT_OK){
                    Path = CAMERA_PATH+RECENT_FILE_NAME;

                }
                break;
        }

        if(Path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(Path);
            File file = new File(YOKO_STORAGE_PATH);

            if (!file.exists()) {
                file.mkdirs();
            }

            File f_file = new File(YOKO_STORAGE_PATH,RECENT_FILE_NAME);

            if (f_file.exists()){
                f_file.delete();
            }

            try {
                f_file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fOut = new FileOutputStream(f_file);
                if(fOut != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
                }
                fOut.flush();
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mNewEventView != null) {
                mNewEventView.addNewPic(YOKO_STORAGE_PATH + RECENT_FILE_NAME);
            }

        }
        RECENT_FILE_NAME = null;
    }

    private void CreateNewEvent(int type,EventRecord eventRecord){
        mNewEventView = NewEventView.newInstance(getActivity(),type,eventRecord);
        mNewEventView.setNewEventListtenr(new NewEventView.NewEventListener() {
            @Override
            public void CreateFinish(Bundle bundle,int isNewEvent) {
                CancelCreate();
                EventRecord eventRecord = new EventRecord();

                eventRecord.introduction = bundle.getString(INTRODUCTION);
                eventRecord.timebegin = bundle.getLong(TIMEBEGIN);
                eventRecord.timeend = bundle.getLong(TIMEEND);
                eventRecord.type = bundle.getInt(TYPE);
                eventRecord.remind = bundle.getLong(REMIND);
                eventRecord.timebegin = bundle.getLong(TIMEBEGIN);
                eventRecord.uid = UserServer.getInstance().getUserid();
                eventRecord.localpicturelink = bundle.getString(LOCALPICLINK);

                if(isNewEvent != NewEventView.EXIST_EVENT) {
                    dbManager.getTableEvent().addEvent(eventRecord);
                    EventRecordUpdate();
                }else{
                    eventRecord.rid = bundle.getLong(RID);
                    dbManager.getTableEvent().updateEvent(eventRecord);
                    EventRecordUpdate();
                }


            }

            @Override
            public void addNewBitMap(boolean open) {
                if(open){
                    Intent Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        return;
                    }
                    GregorianCalendar date = new GregorianCalendar();
                    RECENT_FILE_NAME = "YOKO"+date.get(Calendar.YEAR)+""+(date.get(Calendar.MONTH)+1)+""+date.get(Calendar.DAY_OF_MONTH)+""+date.get(Calendar.HOUR)+""+date.get(Calendar.MINUTE)+""+date.get(Calendar.SECOND)+".jpg";
                    File dir = new File(CAMERA_PATH);
                    if(!dir.exists()){
                        dir.mkdirs();
                    }
                    File file =new File(CAMERA_PATH,RECENT_FILE_NAME);
                    Uri imageUri = Uri.fromFile(file);
                    Camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(Camera, IMAGE_OPENCAMERA_CODE);
                }else {
                    GregorianCalendar date = new GregorianCalendar();
                    RECENT_FILE_NAME = "YOKO"+date.get(Calendar.YEAR)+""+(date.get(Calendar.MONTH)+1)+""+date.get(Calendar.DAY_OF_MONTH)+""+date.get(Calendar.HOUR)+""+date.get(Calendar.MINUTE)+""+date.get(Calendar.SECOND)+".jpg";
                    Intent getAlbum = new Intent(Intent.ACTION_PICK);
                    getAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                    startActivityForResult(getAlbum, IMAGE_NEWEVENT_CODE);
                }
            }

            @Override
            public void CancelCreate() {
                mMainView.removeView(mNewEventView);
                mNewEventView =null;
            }

            @Override
            public void deleteEvent(long rid) {
                CancelCreate();
                dbManager.getTableEvent().deleteEvent(rid);
                if(mWeekEventFragment != null){
                    mWeekEventFragment.deleteRecord(rid);
                }
                if(mdayEventView !=null){
                    mdayEventView.deleteView(rid);
                }
            }
        });
        mMainView.addView(mNewEventView);
    }


    private void EventRecordUpdate() {
        if(mdayEventView != null){
            mdayEventView.UpdateDayEventView();
        }
        if(mWeekEventFragment != null){
            mWeekEventFragment.EventRecordUpdate();
        }
    }





    private void showDayEventView(GregorianCalendar date) {
        if (mdayEventView == null) {
            mdayEventView = DayEventView.newInstance(getActivity(), date);
            mdayEventView.setTag(DAY_EVENT_VIEW);
            mdayEventView.setDayEventViewListener(new DayEventView.DayEventViewListener() {
                @Override
                public void createRecord(int Type,EventRecord eventRecord) {
                    CreateNewEvent(Type,eventRecord);
                }

                @Override
                public void closeDayView() {
                    mMainView.removeView(mdayEventView);
                    mWeekEventFragment.UpdateWeekView();
                    mdayEventView = null;
                }

                @Override
                public void deleteRecord(long rid) {
                    dbManager.getTableEvent().deleteEvent(rid);
                }

                @Override
                public void checkExist(long rid) {

                    CreateNewEvent(NewEventView.EXIST_EVENT,dbManager.getTableEvent().queryEventByRid(rid));
                }

            });

            mMainView.addView(mdayEventView);
        }
    }
}
