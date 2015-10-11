package com.fifteentec.Component.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.Database.EventRecord;
import com.fifteentec.yoko.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class NewEventView extends ViewGroup{

    private Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private View mBackGround;
    private InputPage mInputPage;
    private ImageView mAddButton;
    private FunctionBar mFunctionBar;
    private FunctionView mFunctionView;
    private ImageView mCancelButton;
    private ImageView mOKButton;
    private ImageView mDeleteButton;
    private ImageView mSavedPic = null;
    private BackGroundPage mBackGroundPage;
    private SwitchButton mAlldaySwitchBotton;
    private EventTime mEventTime;


    private Surface mSurface;


    private int isNewEvent;
    public  final static int BLANK_EVENT = 0x00;
    public  final static int EXIST_EVENT = 0x01;
    public final static int HAVE_TIME = 0x03;
    public final static int ALL_DAY_EVENT = 0x04;

    private final int TEXT_INPUT = 0x00;
    private final int FUNCITON_MODE = 0x10;


    private final int NONFUNCTION =0x00;
    private final int REMINED = 0x01;
    private final int TAG =0x02;
    private final int TIME =0x03;
    private final int PIC =0x04;


    private NewEventListener mEventListener;
    private int Mode=TEXT_INPUT;


    private int RemindSelected =-1;
    private int TagSelected =3;
    private GregorianCalendar StartDate;
    private GregorianCalendar EndDate;
    private GregorianCalendar AllDayDate = new GregorianCalendar();
    final private String DefaultText = " ";
    private String introduction = DefaultText;
    private long rid = 0;
    private String PicPath = null;

    private boolean AllDaySwitch = true;

    private boolean firstEntry = true;

    private ImageLoader imageLoader=null;

    public interface NewEventListener{
        void CreateFinish(Bundle bundle,int isNewEvent);
        void addNewBitMap(boolean open);
        void CancelCreate();
        void deleteEvent(long rid);
        void ShowPic(String Path);

    }


    public void setNewEventListtenr(NewEventListener temp){
        this.mEventListener = temp;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public static NewEventView newInstance(Context context,int OpenType,EventRecord eventRecord){
        NewEventView mView = new NewEventView(context);
        mView.init();

        switch (OpenType){
            case BLANK_EVENT:

                mView.isNewEvent= BLANK_EVENT;
                mView.BlankEvnentView();
                break;
            case EXIST_EVENT:
                mView.isNewEvent = EXIST_EVENT;
                mView.StartDate.setTimeInMillis(eventRecord.timebegin);
                mView.EndDate.setTimeInMillis(eventRecord.timeend);
                mView.TagSelected = eventRecord.type;
                mView.introduction = eventRecord.introduction;
                mView.RemindSelected = (int)eventRecord.remind;
                mView.rid = eventRecord.rid;
                mView.PicPath = eventRecord.localpicturelink;
                mView.BlankEvnentView();

                break;
            case HAVE_TIME:
                mView.isNewEvent = HAVE_TIME;
                mView.StartDate.setTimeInMillis(eventRecord.timebegin);
                mView.EndDate.setTimeInMillis(eventRecord.timeend);
                mView.BlankEvnentView();

                break;
            case ALL_DAY_EVENT:

                mView.isNewEvent= ALL_DAY_EVENT;
                mView.StartDate.setTimeInMillis(eventRecord.timebegin);
                mView.EndDate.setTimeInMillis(eventRecord.timeend);
                mView.BlankEvnentView();
                break;

        }
        return mView;
    }


    private void init(){
        mSurface = new Surface();
        StartDate= new GregorianCalendar();
        EndDate = new GregorianCalendar();
        EndDate.add(Calendar.HOUR_OF_DAY, 1);


    }
    public NewEventView(Context context) {
        this(context, null);
    }

    public NewEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }



    private void BlankEvnentView(){

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mContext);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);


        if(StartDate.getTimeInMillis()==EndDate.getTimeInMillis()){
            AllDaySwitch = false;
            AllDayDate.setTimeInMillis(StartDate.getTimeInMillis());
        }


        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (Mode == FUNCITON_MODE) {
                    ChangeMode(TEXT_INPUT);
                    return true;
                }
                return false;
            }

        });
        mBackGround = new View(mContext);
        mBackGround.setBackgroundColor(Color.BLACK);
        mBackGround.setAlpha(mSurface.BackgroundAlpha);
        addView(mBackGround);








        mBackGroundPage = new BackGroundPage(mContext);
        addView(mBackGroundPage);

        mInputPage = new InputPage(mContext);
        addView(mInputPage);
        mFunctionBar = new FunctionBar(mContext);

        if(PicPath != null){
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnFail(R.drawable.cat).considerExifParams(true)
                    .build();
            initalPicView();

            String imageUrl = ImageDownloader.Scheme.FILE.wrap(PicPath);
            imageLoader.displayImage(imageUrl, mSavedPic, options);
        }



        if(AllDaySwitch) mAlldaySwitchBotton = SwitchButton.newInstance(mContext,R.drawable.switchbutton_frame,R.drawable.switchbutton_full_button,R.drawable.switchbutton_background_mask,R.drawable.switchbutton_circle_trumb,true);
        else mAlldaySwitchBotton = SwitchButton.newInstance(mContext,R.drawable.switchbutton_frame,R.drawable.switchbutton_full_button,R.drawable.switchbutton_background_mask,R.drawable.switchbutton_circle_trumb,false);
        mAlldaySwitchBotton.setmSwitchButtonListener(new SwitchButton.SwitchButtonListener() {
            @Override
            public void ButtonSwitch(SwitchButton switchButton, boolean isOn) {
                AllDaySwitch = isOn;
                if (AllDaySwitch) {
                    if (EndDate.getTimeInMillis() == StartDate.getTimeInMillis()) {
                        GregorianCalendar gregorianCalendar = new GregorianCalendar();
                        gregorianCalendar.setTimeInMillis(StartDate.getTimeInMillis());
                        gregorianCalendar.add(Calendar.HOUR, 1);
                        EndDate.setTimeInMillis(gregorianCalendar.getTimeInMillis());
                    }
                } else {
                    AllDayDate = new GregorianCalendar(StartDate.get(Calendar.YEAR), StartDate.get(Calendar.MONTH), StartDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                }
                mEventTime.initEventTime();
            }
        });
        addView(mAlldaySwitchBotton);


        mAddButton = new ImageView(mContext);
        mAddButton.setImageResource(R.drawable.function);
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Mode != FUNCITON_MODE) ChangeMode(FUNCITON_MODE);
                else ChangeMode(TEXT_INPUT);
            }
        });
        addView(mAddButton);

        mCancelButton = new ImageView(mContext);
        mCancelButton.setImageResource(R.drawable.cancel);
        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.CancelCreate();
            }
        });

        addView(mCancelButton);

        mDeleteButton = new ImageView(mContext);
        mDeleteButton.setImageResource(R.drawable.delete);
        mDeleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView delete = new TextView(mContext);
                delete.setText("是否删除该事件?");
                delete.setPadding(100, 100, 100, 100);
                delete.setGravity(Gravity.CENTER_HORIZONTAL);
                delete.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSurface.EventTimeBigTextSize * mScreenHeight/2);
                new AlertDialog.Builder(mContext).setView(delete).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEventListener.deleteEvent(rid);
                    }
                }).setNegativeButton("取消",null).show();
            }
        });
        addView(mDeleteButton);

        mEventTime = new EventTime(mContext);
        mEventTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker dateTimePicker = new DateTimePicker(mContext, mScreenWidth);
                dateTimePicker.initalDialog(StartDate, EndDate, AllDaySwitch);
                dateTimePicker.setDateTimePickerListener(new DateTimePicker.DateTimePickerListener() {
                    @Override
                    public void DateChange(GregorianCalendar start, GregorianCalendar end) {
                        StartDate = CalUtil.CopyDate(start);
                        if (end.getTimeInMillis() > start.getTimeInMillis() + 5 * 60 * 1000)
                            EndDate = CalUtil.CopyDate(end);
                        else EndDate.setTimeInMillis(StartDate.getTimeInMillis() + (5 * 60 * 1000));
                        if (!AllDaySwitch) AllDayDate = CalUtil.CopyDate(start);
                        mEventTime.initEventTime();
                    }
                });
            }
        });
        addView(mEventTime);

        mOKButton = new ImageView(mContext);
        mOKButton.setImageResource(R.drawable.ok);
        mOKButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Introduction", introduction);
                if (AllDaySwitch) {
                    bundle.putLong("StartTime", StartDate.getTimeInMillis());
                    bundle.putLong("EndTime", EndDate.getTimeInMillis());
                } else {
                    bundle.putLong("StartTime", AllDayDate.getTimeInMillis());
                    bundle.putLong("EndTime", AllDayDate.getTimeInMillis());
                }
                bundle.putString("LocalPicLink", PicPath);
                bundle.putLong("Reminder", RemindSelected);
                bundle.putInt("Type", TagSelected);
                bundle.putLong("Rid", rid);
                mEventListener.CreateFinish(bundle, isNewEvent);
            }
        });
        addView(mOKButton);


    }

    private void initalPicView(){
        mSavedPic = new ImageView(mContext);
        mSavedPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mSavedPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PicPath != null)
                    mEventListener.ShowPic(PicPath);
            }
        });
        addView(mSavedPic);
    }

    public void addNewPic(String Path){


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.cat).considerExifParams(true)
                .build();
        if(mSavedPic ==null) {
            initalPicView();
        }
        String imageUrl = ImageDownloader.Scheme.FILE.wrap(Path);
        imageLoader.displayImage(imageUrl, mSavedPic, options);
        if(PicPath != null) {
            File file = new File(PicPath);
            file.delete();
        }
        PicPath = Path;
        invalidate();
    }



    ///////////////////////////////////////////////////////////
    private void CallFunctionAni() {
        addView(mFunctionBar);
    }

    private void ChangeMode(int mMode) {
        switch (mMode){
            case TEXT_INPUT:
                removeView(mFunctionBar);
                mInputPage.setEnabled(true);
                mInputPage.setClickable(true);
                Mode = TEXT_INPUT;
                FunctionCall(NONFUNCTION);
                break;
            case FUNCITON_MODE:
                CallFunctionAni();
                mInputPage.setEnabled(false);
                mInputPage.setClickable(false);
                Mode = FUNCITON_MODE;
                FunctionCall(NONFUNCTION);
                break;
        }
    }

    /////////////////////////////////////////////////////////////////////////
    private void FunctionCall(int target){

        switch (target) {
            case NONFUNCTION:
                if(mFunctionView !=null){
                    removeView(mFunctionView);
                    mFunctionView=null;
                }
                break;
            case TAG:
            case REMINED:
            case TIME:
            case PIC:
                if(mFunctionView ==null) {
                    mFunctionView = new FunctionView(mContext);
                    addView(mFunctionView);
                }
                mFunctionView.PopoutAnim(target);
                break;


        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);


        if(firstEntry) {
            mSurface.initSurface();
            mInputPage.init();
            mFunctionBar.init();
            mCancelButton.setPadding(mSurface.bottonBarPadding, mSurface.bottonHeightBarPadding, mSurface.bottonBarPadding, mSurface.bottonHeightBarPadding);
            mOKButton.setPadding(mSurface.bottonBarPadding, mSurface.bottonHeightBarPadding, mSurface.bottonBarPadding, mSurface.bottonHeightBarPadding);
            mDeleteButton.setPadding(mSurface.bottonBarPadding, mSurface.bottonHeightBarPadding, mSurface.bottonBarPadding,mSurface.bottonHeightBarPadding);
            mEventTime.initEventTime();
            mAlldaySwitchBotton.ChangeButtonHeight(mSurface.EventTimeViewHeight * 12/17);
            firstEntry = false;
        }
        mSurface.initSurface();
        if(mBackGround != null){
            int widthspec = MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.EXACTLY);
            mBackGround.measure(widthspec, heightspec);
        }

        if(mEventTime != null){
            int heightspec= MeasureSpec.makeMeasureSpec(mSurface.EventTimeViewHeight, MeasureSpec.EXACTLY);
            int widthspec = MeasureSpec.makeMeasureSpec(mSurface.EventTimeViewHeight, MeasureSpec.EXACTLY);
            mEventTime.measure(widthspec, heightspec);
        }

        if(mInputPage != null) {
            int heightspec= MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.AT_MOST);
            int widthspec = MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.AT_MOST);
            mInputPage.measure(widthspec, heightspec);
        }

        if(mBackGroundPage != null) {
            int heightspec= MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.EXACTLY);
            int widthspec = MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.EXACTLY);
            mBackGroundPage.measure(widthspec, heightspec);
        }

        if(mAddButton != null){
            int widthspec = MeasureSpec.makeMeasureSpec(mSurface.addBarHeight,MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mSurface.addBarHeight,MeasureSpec.EXACTLY);
            mAddButton.measure(widthspec,heightspec);
        }

        if(mFunctionBar !=null){
            int widthspec = MeasureSpec.makeMeasureSpec(mSurface.IconSide,MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec((mSurface.IconSide+mSurface.IconInterval)*mFunctionBar.getChildCount()+mSurface.addBarHeight,MeasureSpec.EXACTLY);
            mFunctionBar.measure(widthspec, heightspec);
        }
        if(mFunctionView !=null) {
            int widthspec = MeasureSpec.makeMeasureSpec(mScreenWidth,MeasureSpec.AT_MOST);
            int heightspec = MeasureSpec.makeMeasureSpec(mScreenHeight,MeasureSpec.AT_MOST);
            mFunctionView.measure(widthspec,heightspec);
        }

        if(mCancelButton != null){
            int widthspec = MeasureSpec.makeMeasureSpec(mSurface.bottonBarHeight*4/5+2*mSurface.bottonBarPadding,MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mSurface.bottonBarHeight*4/5+2*mSurface.bottonHeightBarPadding,MeasureSpec.EXACTLY);
            mCancelButton.measure(widthspec,heightspec);
        }

        if(mOKButton != null){
            int widthspec = MeasureSpec.makeMeasureSpec(mSurface.bottonBarHeight+2*mSurface.bottonBarPadding,MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mSurface.bottonBarHeight+2*mSurface.bottonHeightBarPadding,MeasureSpec.EXACTLY);
            mOKButton.measure(widthspec,heightspec);
        }

        if(mDeleteButton !=null){
            int widthspec = MeasureSpec.makeMeasureSpec(mSurface.bottonBarHeight+2*mSurface.bottonBarPadding,MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mSurface.bottonBarHeight+2*mSurface.bottonHeightBarPadding,MeasureSpec.EXACTLY);
            mDeleteButton.measure(widthspec,heightspec);
        }

        if(mAlldaySwitchBotton != null){
            int widthspec = MeasureSpec.makeMeasureSpec(mScreenWidth,MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mScreenHeight,MeasureSpec.EXACTLY);
            mAlldaySwitchBotton.measure(widthspec,heightspec);
        }

        if(mSavedPic != null) {
            int widthspec = MeasureSpec.makeMeasureSpec(mScreenWidth-2*(int)(mScreenWidth * mSurface.InputPageSidePadding),MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mSurface.PicView,MeasureSpec.EXACTLY);
            mSavedPic.measure(widthspec,heightspec);
        }

        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {



        int InputSidePadding =(int) (mScreenWidth * mSurface.InputPageSidePadding);
        int InputUPdownPadding =(int) (mScreenHeight * mSurface.InputPageUpdownPadding);
        if(mBackGround !=null) mBackGround.layout(l,t,r,b);


        if(mInputPage !=null) {
            int botton = mScreenHeight-InputUPdownPadding-2*mSurface.bottonHeightBarPadding-mSurface.bottonBarHeight-mSurface.InputPageTextPadding;
            int top = mSavedPic==null?t + InputUPdownPadding+mSurface.EventTimeViewHeight:t + InputUPdownPadding+mSurface.EventTimeViewHeight+mSavedPic.getMeasuredHeight();
            mInputPage.layout(l +  InputSidePadding,
                    top,
                    r - InputSidePadding,
                    botton);
        }

        if(mBackGroundPage !=null) {
            mBackGroundPage.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }

        if(mEventTime != null){
            mEventTime.layout(InputSidePadding,InputUPdownPadding,InputSidePadding+mEventTime.getMeasuredWidth(),InputUPdownPadding+mEventTime.getMeasuredHeight());
        }

        if(mAddButton != null) mAddButton.layout(
                r-InputSidePadding-mAddButton.getMeasuredWidth()/2-mSurface.addBarMid,
                b-InputUPdownPadding-mAddButton.getMeasuredHeight()-mSurface.bottonBarHeight-2*mSurface.bottonBarPadding,
                r-InputSidePadding-mSurface.addBarMid+mAddButton.getMeasuredWidth()/2,
                b-InputUPdownPadding-mSurface.bottonBarHeight-2*mSurface.bottonHeightBarPadding);
        if(mFunctionBar != null) mFunctionBar.layout(
                r-InputSidePadding-mFunctionBar.getMeasuredWidth()/2-mSurface.addBarMid,
                b-InputUPdownPadding-mSurface.bottonBarHeight-2*mSurface.bottonHeightBarPadding-mFunctionBar.getMeasuredHeight(),
                r-InputSidePadding-mSurface.addBarMid+mFunctionBar.getMeasuredWidth()/2+2,
                b-InputUPdownPadding-mSurface.bottonBarHeight-2*mSurface.bottonHeightBarPadding);
        if(mFunctionView !=null){
            int right = r-InputSidePadding-mSurface.addBarHeight;
            int left = right-mFunctionView.getMeasuredWidth();
            int top = b-InputUPdownPadding-mSurface.bottonBarHeight-2*mSurface.bottonHeightBarPadding-mFunctionBar.getMeasuredHeight();
            int botton =top + mFunctionBar.getMeasuredHeight();
            mFunctionView.layout(left, top, right, botton);
        }
        if(mCancelButton !=null){
            mCancelButton.layout(
                    l+InputSidePadding,
                    b-InputUPdownPadding-mCancelButton.getMeasuredHeight()-(mSurface.bottonHeightBarPadding*2+mSurface.bottonBarHeight-mCancelButton.getMeasuredHeight())/2,
                    l+InputSidePadding+mCancelButton.getMeasuredWidth(),
                    b-InputUPdownPadding-(mSurface.bottonHeightBarPadding*2+mSurface.bottonBarHeight-mCancelButton.getMeasuredHeight())/2);
        }

        if(mOKButton != null){
            mOKButton.layout(
                    r-InputSidePadding-mOKButton.getMeasuredWidth(),
                    b-InputUPdownPadding-mOKButton.getMeasuredHeight(),
                    r-InputSidePadding,
                    b-InputUPdownPadding);
        }

        if(mDeleteButton != null){
            mDeleteButton.layout(
                    mScreenWidth/2-mOKButton.getMeasuredWidth()/2,
                    b-InputUPdownPadding-mOKButton.getMeasuredHeight(),
                    mScreenWidth/2+mOKButton.getMeasuredWidth()/2,
                    b-InputUPdownPadding);
        }

        if(mAlldaySwitchBotton !=null){
            mAlldaySwitchBotton.layout(r-InputSidePadding-mAlldaySwitchBotton.getMeasuredWidth()- mSurface.InputPageTextPadding,t+InputUPdownPadding+(mSurface.EventTimeViewHeight-mAlldaySwitchBotton.getMeasuredHeight())/2,r-InputSidePadding- mSurface.InputPageTextPadding,t+InputUPdownPadding+(mSurface.EventTimeViewHeight+mAlldaySwitchBotton.getMeasuredHeight())/2);
        }

        if(mSavedPic != null){
            mSavedPic.layout(l+InputSidePadding,t+InputUPdownPadding+mSurface.EventTimeViewHeight,l+InputSidePadding+mSavedPic.getMeasuredWidth(),t+InputUPdownPadding+mSurface.EventTimeViewHeight+mSavedPic.getMeasuredHeight());
        }


    }



    private class Surface{
        int InputBackground = Color.WHITE;
        float InputPageSidePadding = (1/20f);
        float InputPageUpdownPadding =(1/20f);
        float BackgroundAlpha = 0.3f;

        float PicViewRatio = 1/3f;
        int PicView;

        float EventTimeViewHeightRatio = 1/15f;
        int EventTimeViewHeight;
        float EventTimeBigTextSize = 1/20f;
        float EventTimeSmallTextSize = 1/55f;

        public int InputPageTextPadding ;
        float InputPageTextPaddingRatio = 1/50f;
        float InputPageTextSize ;
        float InputPageTextSizeRatio = 1/20f;


        int bottonBarHeight;
        float bottonBarHeightRatio = 1/25f;
        int bottonBarPadding;
        int bottonHeightBarPadding;
        float bottonBarPaddingRatio = 1/40f;

        float addBarHeightRatio = 1/8f;
        int addBarHeight;
        float addBarMidRatio = 1/15f;
        int addBarMid;

        public int IconSide;
        float IconSideRatio = 1/13f;
        public int IconInterval;
        float IconIntervalRatio = 1/25f;

        public int CheckBoxPadding;
        float CheckBoxPaddingRatio = 1/50f;
        public int CheckRectPadding;
        float CheckBoxRectPadingRatio = 1/80f;
        float CheckBoxTextSize;
        float CheckBoxTextSizeRatio = 1/25f;

        Paint BackGoundPagePaint = new Paint();
        Paint DashLinePaint = new Paint();
        float RealLineWidthRatio= 1/30f;
        float GapLineWidthRatio= 1/60f;
        float DashLineWidth = 1/200f;

        float ItemHeight = 1/30f;

        float PicTextSizeRatio = 1/40f;



        void initSurface(){
            InputPageTextPadding = (int)(mScreenWidth*InputPageTextPaddingRatio);
            BackGoundPagePaint.setColor(Color.WHITE);
            InputPageTextSize =  (mScreenWidth*InputPageTextSizeRatio);
            addBarHeight = (int)(mScreenWidth*addBarHeightRatio);
            bottonBarHeight = (int) (mScreenHeight*bottonBarHeightRatio);
            bottonBarPadding = (int)(mScreenHeight*bottonBarPaddingRatio);
            bottonHeightBarPadding=(int)(mScreenHeight*bottonBarPaddingRatio*2/3);
            PathEffect effect = new DashPathEffect(new float[]{mScreenWidth*RealLineWidthRatio,mScreenWidth*GapLineWidthRatio},1);
            DashLinePaint.setColor(getResources().getColor(R.color.DashlineColor));
            DashLinePaint.setPathEffect(effect);
            DashLinePaint.setStyle(Paint.Style.STROKE);
            DashLinePaint.setAntiAlias(true);
            DashLinePaint.setStrokeWidth(mScreenWidth * DashLineWidth);
            addBarMid = (int)(mScreenWidth*addBarMidRatio);
            IconSide =(int)(mScreenWidth*IconSideRatio);
            IconInterval =(int)(mScreenWidth*IconIntervalRatio);
            CheckBoxPadding = (int)(mScreenWidth*CheckBoxPaddingRatio);
            CheckRectPadding = (int)(mScreenWidth*CheckBoxRectPadingRatio);
            CheckBoxTextSize =(mScreenWidth*CheckBoxTextSizeRatio);
            EventTimeViewHeight = (int)(mScreenHeight*EventTimeViewHeightRatio);
            PicView = (int)(mScreenHeight*PicViewRatio);
        }

    }


    private class   InputPage extends EditText{


        private String HintString = "诶嘿嘿...";


        private TextWatcher mtextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                introduction = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        public InputPage(Context context) {
            super(context);
        }

        public InputPage(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public InputPage(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(Mode != TEXT_INPUT) return false;
            return super.onTouchEvent(event);
        }


        public void init(){
            setBackgroundColor(mSurface.InputBackground);
            setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.InputPageTextSize);
            setGravity(Gravity.TOP);
            setHint(HintString);
            if(introduction != DefaultText) setText(introduction);
            int effectWidth = (int)(mScreenWidth*(1-2*mSurface.InputPageSidePadding))-2*mSurface.InputPageTextPadding;
            setMaxEms(effectWidth/(int)(this.getTextSize()*(7/6f)));
            addTextChangedListener(mtextWatcher);
        }

    }

    private class FunctionView extends ViewGroup{

        public int FunctionActivate = NONFUNCTION;

        private int CheckboxPadding;
        private int RectPadding;
        private float TextSize ;
        private int itemHeight;

        private Paint RectPaint;
        private Paint RectFillPaint;


        private int RectColor= Color.parseColor("#666666");

        private List<String > ReminedList = new ArrayList<>(Arrays.asList(
                "不提醒","提前5分钟","提前10分钟","提前30分钟","提前1个小时","提前1天"
        ));

        private List<String> TagList = new ArrayList<>(Arrays.asList(
                "工作","学习","娱乐","其他"
        ));

        private List<Drawable> TagSelectedList = new ArrayList<>(Arrays.asList(
                getResources().getDrawable(R.drawable.radiobtn_work),getResources().getDrawable(R.drawable.radiobtn_study),getResources().getDrawable(R.drawable.radiobtn_entertainment),getResources().getDrawable(R.drawable.radiobtn_other)
        ));

        public FunctionView(Context context) {
            this(context, null);
        }

        public FunctionView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public FunctionView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        private void initView() {

            TextSize=mSurface.CheckBoxTextSize;
            CheckboxPadding = mSurface.CheckBoxPadding;
            RectPadding = mSurface.CheckRectPadding;
            setWillNotDraw(false);
            RectPaint = new Paint();
            RectPaint.setAntiAlias(true);
            RectPaint.setColor(RectColor);
            RectPaint.setStyle(Paint.Style.STROKE);
            PathEffect ef = new DashPathEffect(new float[]{15,5},1);
            //RectPaint.setPathEffect(ef);
            RectPaint.setStrokeWidth(3);
            RectFillPaint = new Paint();
            RectFillPaint.setAntiAlias(true);
            RectFillPaint.setColor(Color.WHITE);
            RectFillPaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < TagSelectedList.size(); i++) {
                Drawable temp = TagSelectedList.get(i);
                temp.setBounds(0,0,(int)TextSize,(int)TextSize);
            }
            itemHeight  = (int)(mScreenHeight*mSurface.ItemHeight);

        }

        public void PopoutAnim(int Target){
            removeAllViews();
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            switch (Target){
                case REMINED:

                    RadioGroup radioGroup = new RadioGroup(mContext);
                    for(int i = 0;i<ReminedList.size();i++){
                        RadioButton radioButton = new RadioButton(mContext);
                        radioButton.setText(ReminedList.get(i));
                        radioButton.setLayoutParams(lp);
                        radioButton.setTag(i);
                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextSize);
                        radioGroup.addView(radioButton);
                        if(i==RemindSelected){
                            radioButton.setChecked(true);
                        }
                    }
                    radioGroup.setTag(REMINED);
                    radioGroup.setLayoutParams(lp);
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton tempButton = (RadioButton)findViewById(checkedId);
                            RemindSelected = (int)(tempButton.getTag());
                        }
                    });
                    addView(radioGroup);
                    FunctionActivate = REMINED;
                    break;
                case TAG:
                    RadioGroup radioGroupTags = new RadioGroup(mContext);
                    for(int i = 0;i<TagList.size();i++){
                        RadioButton radioButton = new RadioButton(mContext);
                        radioButton.setText(TagList.get(i));
                        radioButton.setLayoutParams(lp);
                        radioButton.setTag(i);
                        Drawable temp = TagSelectedList.get(i);
                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, TextSize);
                        radioButton.setButtonDrawable(android.R.color.transparent);
                        temp.setBounds(0, 0, itemHeight, itemHeight);
                        radioButton.setCompoundDrawablePadding(itemHeight / 3);
                        radioButton.setPadding(itemHeight/3,itemHeight/3,itemHeight/3,itemHeight/3);
                        radioButton.setCompoundDrawables(temp, null, null, null);

                        radioGroupTags.addView(radioButton);
                        if(i==TagSelected){
                            radioButton.setChecked(true);
                        }
                    }
                    radioGroupTags.setTag(TAG);
                    radioGroupTags.setLayoutParams(lp);
                    radioGroupTags.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton tempButton = (RadioButton)findViewById(checkedId);
                            TagSelected = (int)(tempButton.getTag());
                        }
                    });
                    addView(radioGroupTags);
                    FunctionActivate =TAG;
                    break;
                case TIME:
                    FunctionActivate =TIME;
                    break;
                case PIC:
                    LayoutParams ll_lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                    LinearLayout ll = new LinearLayout(mContext);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setLayoutParams(ll_lp);
                    TextView photo = new TextView(mContext);
                    photo.setText("拍照");
                    photo.setTextSize(mScreenHeight * mSurface.PicTextSizeRatio);
                    photo.setPadding((int) (mScreenHeight * mSurface.PicTextSizeRatio / 2), (int) (mScreenHeight * mSurface.PicTextSizeRatio / 2), (int) (mScreenHeight * mSurface.PicTextSizeRatio / 2), (int) (mScreenHeight * mSurface.PicTextSizeRatio / 2));

                    ll.addView(photo);
                    TextView album = new TextView(mContext);
                    album .setText("相册");
                    album .setTextSize(mScreenHeight * mSurface.PicTextSizeRatio);
                    album.setPadding((int) (mScreenHeight * mSurface.PicTextSizeRatio / 2), (int) (mScreenHeight * mSurface.PicTextSizeRatio / 2), (int) (mScreenHeight * mSurface.PicTextSizeRatio / 2), (int) (mScreenHeight * mSurface.PicTextSizeRatio / 2));

                    ll.addView(album);
                    final AlertDialog dialogView = new AlertDialog.Builder(mContext).setView(ll).setNegativeButton("不玩了", null).show();


                    photo.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEventListener.addNewBitMap(true);
                            dialogView.dismiss();
                        }
                    });
                    album.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEventListener.addNewBitMap(false);
                            dialogView.dismiss();
                        }
                    });
                    ChangeMode(TEXT_INPUT);
                    break;
            }
            invalidate();
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthspec=0;
            int heightspec=0;
            int ParentWidth =0;
            int ParentHeight =0;
            for(int i =0;i<getChildCount();i++) {
                View temp = getChildAt(i);
                LayoutParams lp = temp.getLayoutParams();
                if (lp.width >= 0)
                    widthspec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
                else if (lp.width == -1)
                    widthspec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
                else if (lp.width == -2)
                    widthspec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);

                if (lp.height >= 0)
                    heightspec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
                else if (lp.height == -1)
                    heightspec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
                else if (lp.width == -2)
                    heightspec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);

                temp.measure(widthspec, heightspec);
                ParentWidth = Math.max(ParentWidth, temp.getMeasuredWidth());
                ParentHeight += temp.getMeasuredHeight();

            }
                ParentHeight += CheckboxPadding*2;
                ParentHeight+=RectPadding*2;
                ParentWidth +=CheckboxPadding*2;
                ParentWidth +=RectPadding*2;
                setMeasuredDimension(ParentWidth, ParentHeight);

        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int tempheight=0;
            if(FunctionActivate == REMINED) tempheight=0;
            else if(FunctionActivate == TAG) tempheight = mSurface.IconSide+mSurface.IconInterval;
            int Padding = CheckboxPadding + RectPadding;
            for(int i =0;i<getChildCount();i++){
                View temp = getChildAt(i);

                if((int)(temp.getTag())==TAG||(int)(temp.getTag())==REMINED) {

                    temp.layout(Padding, Padding+tempheight, getMeasuredWidth() - Padding, getMeasuredHeight() - Padding+tempheight);
                }
                if((int)(temp.getTag()) == TIME){
                    temp.layout(Padding,Padding+tempheight,Padding+temp.getMeasuredWidth(),Padding+tempheight+temp.getMeasuredHeight());
                    tempheight += temp.getMeasuredHeight();
                }

            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int tempheight=0;
            if(FunctionActivate == REMINED) tempheight=0;
            else if(FunctionActivate == TAG) tempheight = mSurface.IconSide+mSurface.IconInterval;
            RectF drawRect = new RectF(CheckboxPadding,tempheight+CheckboxPadding,getMeasuredWidth()-CheckboxPadding,tempheight+getMeasuredHeight() - CheckboxPadding);
            canvas.drawRoundRect(drawRect, 20, 20, RectPaint);
            canvas.drawRoundRect(drawRect,20,20,RectFillPaint);
        }
    }

    private class FunctionBar extends ViewGroup{

        private ImageView InviteIcon;
        private ImageView PicIcon;
        private ImageView ReminedIcon;
        private ImageView TagIcon;

        private int Interval;

        public FunctionBar(Context context) {
            this(context,null);
        }

        public FunctionBar(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public FunctionBar(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            cancelAll();
        }

        private void init(){
            Interval = mSurface.IconInterval;
            InviteIcon = new ImageView(mContext);
            InviteIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelAll();
                    InviteIcon.setImageResource(R.drawable.invite);
                }
            });
            PicIcon = new ImageView(mContext);
            PicIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionCall(PIC);
                    cancelAll();
                    PicIcon.setImageResource(R.drawable.pic2);
                }
            });
            ReminedIcon = new ImageView(mContext);
            ReminedIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionCall(REMINED);
                    cancelAll();
                    ReminedIcon.setImageResource(R.drawable.remined2);
                }
            });
            TagIcon = new ImageView(mContext);
            TagIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionCall(TAG);
                    cancelAll();
                    TagIcon.setImageResource(R.drawable.tags);
                }
            });
            cancelAll();
            addView(InviteIcon);
            addView(PicIcon);
            addView(ReminedIcon);
            addView(TagIcon);
        }

        private void cancelAll(){
            InviteIcon.setImageResource(R.drawable.invite);
            ReminedIcon.setImageResource(R.drawable.remined);
            TagIcon.setImageResource(R.drawable.tag1);
            PicIcon.setImageResource(R.drawable.pic1);

        }



        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.IconSide,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.IconSide,MeasureSpec.EXACTLY);
            if(InviteIcon != null) InviteIcon.measure(widthSpec,heightSpec);
            if(PicIcon != null) PicIcon.measure(widthSpec,heightSpec);
            if(ReminedIcon != null) ReminedIcon.measure(widthSpec,heightSpec);
            if(TagIcon != null) TagIcon.measure(widthSpec,heightSpec);
            setMeasuredDimension(mSurface.IconSide,MeasureSpec.getSize(heightMeasureSpec));
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int time = 0;
            if(ReminedIcon != null) ReminedIcon.layout(0,(mSurface.IconSide+Interval)*time,mSurface.IconSide,(mSurface.IconSide+Interval)*time+mSurface.IconSide);
            time++;
            if(TagIcon != null) TagIcon.layout(0,(mSurface.IconSide+Interval)*time,mSurface.IconSide,mSurface.IconSide+(mSurface.IconSide+Interval)*time);
            time++;
            if(InviteIcon != null) InviteIcon.layout(0,(mSurface.IconSide+Interval)*time,mSurface.IconSide,mSurface.IconSide+(mSurface.IconSide+Interval)*time);
            time++;
            if(PicIcon != null) PicIcon.layout(0,(mSurface.IconSide+Interval)*time,mSurface.IconSide,mSurface.IconSide+(mSurface.IconSide+Interval)*time);





        }
    }

    private class EventTime extends ViewGroup{

        View firstTime =null;
        View lastTime = null;

        Paint paint= new Paint();

        public EventTime(Context context) {
            this(context, null);
        }

        public EventTime(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public EventTime(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        void initEventTime(){
            this.removeAllViews();
            firstTime = null;
            lastTime = null;
            if(!AllDaySwitch){
                firstTime = inflate(mContext, R.layout.view_new_event_date, null);
                TextView Day = (TextView) firstTime.findViewById(R.id.id_new_event_date);
                Day.setText(TwoLetterNum(AllDayDate.get(Calendar.DAY_OF_MONTH)));
                Day.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.EventTimeBigTextSize * mScreenHeight);
                Day.setTypeface(Typeface.DEFAULT_BOLD);

                TextView StartTime = (TextView) firstTime.findViewById(R.id.id_new_event_starttime);
                StartTime.setText("全天");
                StartTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);

                TextView EndTime = (TextView) firstTime.findViewById(R.id.id_new_event_endtime);
                EndTime.setText("事件");
                EndTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);

            }else {
                if (StartDate.get(Calendar.YEAR) == EndDate.get(Calendar.YEAR) && StartDate.get(Calendar.MONTH) == EndDate.get(Calendar.MONTH) && StartDate.get(Calendar.DAY_OF_MONTH) == EndDate.get(Calendar.DAY_OF_MONTH)) {
                    firstTime = inflate(mContext, R.layout.view_new_event_date, null);
                    TextView Day = (TextView) firstTime.findViewById(R.id.id_new_event_date);
                    Day.setText(TwoLetterNum(StartDate.get(Calendar.DAY_OF_MONTH)));
                    Day.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.EventTimeBigTextSize * mScreenHeight);
                    Day.setTypeface(Typeface.DEFAULT_BOLD);

                    TextView StartTime = (TextView) firstTime.findViewById(R.id.id_new_event_starttime);

                    String startTime = new String();
                    if (StartDate.get(Calendar.AM_PM) == Calendar.PM) {
                        startTime = "" + (StartDate.get(Calendar.HOUR) + 12);
                    } else {
                        startTime = TwoLetterNum(StartDate.get(Calendar.HOUR));
                    }
                    startTime += ":" + TwoLetterNum(StartDate.get(Calendar.MINUTE));
                    StartTime.setText(startTime);
                    StartTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);

                    TextView EndTime = (TextView) firstTime.findViewById(R.id.id_new_event_endtime);

                    String endTime = new String();
                    if (EndDate.get(Calendar.AM_PM) == Calendar.PM) {
                        endTime = "" + (EndDate.get(Calendar.HOUR) + 12);
                    } else {
                        endTime = TwoLetterNum(EndDate.get(Calendar.HOUR));
                    }
                    endTime += ":" + TwoLetterNum(EndDate.get(Calendar.MINUTE));
                    EndTime.setText(endTime);
                    EndTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);
                } else {
                    firstTime = inflate(mContext, R.layout.view_new_event_date, null);
                    TextView Day = (TextView) firstTime.findViewById(R.id.id_new_event_date);
                    Day.setText(TwoLetterNum(StartDate.get(Calendar.DAY_OF_MONTH)));
                    Day.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.EventTimeBigTextSize * mScreenHeight);
                    Day.setTypeface(Typeface.DEFAULT_BOLD);

                    TextView StartTime = (TextView) firstTime.findViewById(R.id.id_new_event_starttime);

                    String startTime = new String();
                    if (StartDate.get(Calendar.AM_PM) == Calendar.PM) {
                        startTime = "" + (StartDate.get(Calendar.HOUR) + 12);
                    } else {
                        startTime = TwoLetterNum(StartDate.get(Calendar.HOUR));
                    }
                    startTime += ":" + TwoLetterNum(StartDate.get(Calendar.MINUTE));
                    StartTime.setText(startTime);
                    StartTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);

                    TextView textView = (TextView) firstTime.findViewById(R.id.id_new_event_endtime);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);

                    lastTime = inflate(mContext, R.layout.view_new_event_date, null);

                    TextView DayTwo = (TextView) lastTime.findViewById(R.id.id_new_event_date);
                    DayTwo.setText(TwoLetterNum(EndDate.get(Calendar.DAY_OF_MONTH)));
                    DayTwo.setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.EventTimeBigTextSize * mScreenHeight);
                    DayTwo.setTypeface(Typeface.DEFAULT_BOLD);

                    TextView EndTime = (TextView) lastTime.findViewById(R.id.id_new_event_endtime);
                    String endTime = new String();
                    if (EndDate.get(Calendar.AM_PM) == Calendar.PM) {
                        endTime = "" + (EndDate.get(Calendar.HOUR) + 12);
                    } else {
                        endTime = TwoLetterNum(EndDate.get(Calendar.HOUR));
                    }
                    endTime += ":" + TwoLetterNum(EndDate.get(Calendar.MINUTE));
                    EndTime.setText(endTime);
                    EndTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);

                    textView = (TextView) lastTime.findViewById(R.id.id_new_event_starttime);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mScreenHeight * mSurface.EventTimeSmallTextSize);
                }
            }


            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            this.setWillNotDraw(false);

            if(firstTime != null){
                this.addView(firstTime);
            }

            if(lastTime != null){
                this.addView(lastTime);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            float Ratio = 2.3f;
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthSize = 0;
            if(firstTime != null){
                int widthSpec = MeasureSpec.makeMeasureSpec((int)(heightSize*Ratio),MeasureSpec.EXACTLY);
                int heightSpec = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY);
                firstTime.measure(widthSpec,heightSpec);
                widthSize += (int)(heightSize*Ratio);
            }

            if(lastTime != null){
                int widthSpec = MeasureSpec.makeMeasureSpec((int)(heightSize*Ratio),MeasureSpec.EXACTLY);
                int heightSpec = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY);
                lastTime.measure(widthSpec,heightSpec);
                widthSize += (int)(heightSize*Ratio) + heightSize*(3/30);
            }

            setMeasuredDimension(widthSize, heightSize);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

            if (firstTime != null) {
                firstTime.layout(0,0,firstTime.getMeasuredWidth(),firstTime.getMeasuredHeight());
            }

            if(lastTime != null){
                lastTime.layout(this.getMeasuredWidth()- lastTime.getMeasuredWidth(),0,this.getMeasuredWidth(),lastTime.getMeasuredHeight());
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(lastTime != null&&firstTime != null){
                canvas.drawRect(firstTime.getWidth()+firstTime.getHeight()/30,firstTime.getWidth()/10,firstTime.getWidth()+firstTime.getHeight()*2/30, firstTime.getHeight(),paint);
            }
        }
    }

    private String TwoLetterNum(int num){
        if(num<10){
            return "0"+num;
        }else{
            return ""+num;
        }
    }

    private class BackGroundPage extends View{

        public BackGroundPage(Context context) {
            this(context, null);
        }

        public BackGroundPage(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public BackGroundPage(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int InputSidePadding =(int) (mScreenWidth * mSurface.InputPageSidePadding);
            int InputUPdownPadding =(int) (mScreenHeight * mSurface.InputPageUpdownPadding);
            canvas.drawRect(InputSidePadding,InputUPdownPadding,mScreenWidth-InputSidePadding,mScreenHeight-InputUPdownPadding,mSurface.BackGoundPagePaint);
            canvas.drawLine(InputSidePadding,
                    mScreenHeight-InputUPdownPadding-2*mSurface.bottonHeightBarPadding-mSurface.bottonBarHeight,
                    mScreenWidth-InputSidePadding,
                    mScreenHeight-InputUPdownPadding-2*mSurface.bottonHeightBarPadding-mSurface.bottonBarHeight,
                    mSurface.DashLinePaint);
        }
    }




}
