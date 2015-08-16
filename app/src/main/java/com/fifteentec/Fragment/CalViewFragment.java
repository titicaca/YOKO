package com.fifteentec.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Database.DBManager;
import com.Database.EventRecord;
import com.Database.TableEvent;
import com.fifteentec.Component.calendar.CalView;
import com.fifteentec.Component.calendar.CalendarController;
import com.fifteentec.Component.calendar.DayEventView;
import com.fifteentec.Component.calendar.EventListView;
import com.fifteentec.Component.calendar.NewEventView;
import com.fifteentec.yoko.BaseActivity;
import com.fifteentec.yoko.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalViewFragment extends Fragment {

    private CalView mCalView;

    private CalendarController mDate;

    private TextView mMonthText;
    private TextView mYearText;
    private FragmentManager mFragmentManager;
    private EventListViewFragment mListView;
    private FrameLayout mMainView;
    private NewEventView mNewEventView;

    private final int EVENT_LIST = 0x00;
    private final int CAL_VIEW_MONTH_TAP = 0x01;

    private final String IMAGE_TYPE = "image/*";
    private final String CAMERA_PATH = "/sdcard/DCIM/Camera/";
    private String RECENT_FILE_NAME;

    private final int IMAGE_NEWEVENT_CODE = 0x00;
    private final int IMAGE_OPENCAMERA_CODE = 0x01;

    private BaseActivity activity;
    private DBManager dbManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(mDate == null)
        {
            mDate = new CalendarController();
        }
        mFragmentManager = getFragmentManager();
        super.onCreate(savedInstanceState);

        this.activity = (BaseActivity)this.getActivity();
        this.dbManager = (DBManager)this.activity.getDBManager();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar_main_layout, container, false);
        mMainView = (FrameLayout) view.findViewById(R.id.id_cal_main_all);

        mMonthText = (TextView) view.findViewById(R.id.id_cal_view_month);
        mMonthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalView.SwitchMode();
            }
        });
        mYearText = (TextView) view.findViewById(R.id.id_cal_view_year);
        mYearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewEvent();
            }
        });
        mCalView =  (CalView) view.findViewById(R.id.id_cal_view);
        mCalView.init(mDate.getNowCalendar());
        mCalView.setCalViewListner(new CalView.CalViewListener() {
            @Override
            public void DateChange(GregorianCalendar arry) {
                mDate.UpdateCur(arry);
                UpdateTime(CAL_VIEW_MONTH_TAP);

            }
        });

        FragmentTransaction mTrans = mFragmentManager.beginTransaction();
        mListView = EventListViewFragment.newInstance(mDate.getNowArray());
        mListView.setEventFragmentListener(new EventListViewFragment.EventListFragmentListener() {
            @Override
            public void ListDateChange(ArrayList<Integer> list) {
                mDate.UpdateCur(list);
                UpdateTime(EVENT_LIST);
            }
        });
        mTrans.add(R.id.id_event_content,mListView).commit();
        showDayEventView();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_NEWEVENT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().managedQuery(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String Path = cursor.getString(column_index);
                    try {
                        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                            cursor.close();
                        }
                    } catch (Exception e) {
                        Log.e("YOKO", "error:" + e);
                    }
                    if (mNewEventView != null) {
                        mNewEventView.addNewPic(Path);
                    }
                }
                break;
            case IMAGE_OPENCAMERA_CODE:
                if(resultCode == Activity.RESULT_OK){
                    /*
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    String sdStatus = Environment.getExternalStorageState();
                    if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                        Log.v("YOKO","Storage is not mounted");
                        return;
                    }
                    GregorianCalendar date =new GregorianCalendar();
                    String name = "YOKO"+date.get(Calendar.YEAR)+""+(date.get(Calendar.MONTH)+1)+""+date.get(Calendar.DAY_OF_MONTH)+""+date.get(Calendar.HOUR)+""+date.get(Calendar.MINUTE)+".jpg";

                    File file =new File("/sdcard/DCIM/Camera/");
                    file.mkdirs();
                    String fileName ="/sdcard/DCIM/Camera/"+name;

                    FileOutputStream b= null;
                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
                    }catch (FileNotFoundException e){
                        Log.e("YOKO",e.toString());
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mNewEventView != null) {
                        mNewEventView.addNewPic(fileName);
                    }

                    Uri aim = data.getData();
                    String[] proj = { MediaStore.Images.Media.DATA };
                    Cursor actualimagecursor = getActivity().managedQuery(aim,proj,null,null,null);
                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    actualimagecursor.moveToFirst();
                    String img_path = actualimagecursor.getString(actual_image_column_index);
                    Log.d("Test",img_path);
                    */
                    if(RECENT_FILE_NAME != null&&mNewEventView != null) mNewEventView.addNewPic(CAMERA_PATH+RECENT_FILE_NAME);
                }
        }
    }

    private void CreateNewEvent(){
        mNewEventView = NewEventView.newInstance(getActivity(),NewEventView.BLANK_EVENT);
        mNewEventView.setNewEventListtenr(new NewEventView.NewEventListener() {
            @Override
            public void CreateFinish(Bundle bundle) {
                mMainView.removeView(mNewEventView);
                mNewEventView =null;
                EventRecord eventRecord = new EventRecord();
                eventRecord.introduciton = bundle.getString("introduction");
                dbManager.getTableEvent().addEvent(eventRecord);

            }

            @Override
            public void addNewBitMap(boolean open) {
                if(open){
                    Intent Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String storage = Environment.getExternalStorageState();
                    if(!storage.equals(Environment.MEDIA_MOUNTED)){
                        return;
                    }
                    GregorianCalendar date = new GregorianCalendar();
                    RECENT_FILE_NAME = "YOKO"+date.get(Calendar.YEAR)+""+(date.get(Calendar.MONTH)+1)+""+date.get(Calendar.DAY_OF_MONTH)+""+date.get(Calendar.HOUR)+""+date.get(Calendar.MINUTE)+".jpg";
                    File file =new File(CAMERA_PATH,RECENT_FILE_NAME);
                    Uri imageUri = Uri.fromFile(file);
                    Camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(Camera, IMAGE_OPENCAMERA_CODE);
                }else {
                    Intent getAlbum = new Intent(Intent.ACTION_PICK);
                    getAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_TYPE);
                    startActivityForResult(getAlbum, IMAGE_NEWEVENT_CODE);
                }
            }
        });
        mMainView.addView(mNewEventView);
    }



    private void UpdateTime(int Updater) {
        mMonthText.setText(mDate.MONTH_NAME.get(mDate.getCurMonth()));
        mYearText.setText(mDate.getCurYear() + "");
        switch (Updater){
            case CAL_VIEW_MONTH_TAP:
                mListView.UpdateTime(mDate.getCurArray());
                break;
            case EVENT_LIST:
                GregorianCalendar temp =mDate.getCurCalendar();
                mCalView.UpdateTime(temp);
        }
    }

    private void showDayEventView(){
        DayEventView dayEventView = DayEventView.newInstance(getActivity(),mDate.getCurCalendar());
        mMainView.addView(dayEventView);
    }
}
