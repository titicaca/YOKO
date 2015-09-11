package com.fifteentec.Component.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.RectF;
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
    private HorizontalScrollView mSavedPic;
    private BackGroundPage mBackGroundPage;
    private SwitchButton mAlldaySwitchBotton;

    private Surface mSurface;

    private ArrayList<Bitmap> SavedPic ;

    private int isNewEvent;
    public  final static int BLANK_EVENT = 0x00;
    public  final static int EXIST_EVENT = 0x01;
    public final static int HAVE_TIME = 0x03;

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
    private String introduction = "null";
    private long rid = 0;

    private boolean firstEntry = true;

    public interface NewEventListener{
        void CreateFinish(Bundle bundle,int isNewEvent);
        void addNewBitMap(boolean open);
        void CancelCreate();
        void deleteEvent(long rid);

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
                mView.BlankEvnentView();
                mView.isNewEvent= BLANK_EVENT;
                break;
            case EXIST_EVENT:
                mView.isNewEvent = EXIST_EVENT;
                mView.StartDate.setTimeInMillis(eventRecord.timebegin);
                mView.EndDate.setTimeInMillis(eventRecord.timeend);
                mView.TagSelected = eventRecord.type;
                mView.introduction = eventRecord.introduction;
                mView.RemindSelected = (int)eventRecord.remind;
                mView.rid = eventRecord.rid;
                mView.BlankEvnentView();

                break;
            case HAVE_TIME:
                mView.isNewEvent = HAVE_TIME;
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
        EndDate.add(Calendar.MINUTE, 1);
        SavedPic = new ArrayList<Bitmap>();

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
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(Mode == FUNCITON_MODE ){
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

        mAlldaySwitchBotton = SwitchButton.newInstance(mContext,R.drawable.switchbutton_frame,R.drawable.switchbutton_full_button,R.drawable.switchbutton_background_mask,R.drawable.switchbutton_circle_trumb);
        //addView(mAlldaySwitchBotton);


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
                mEventListener.deleteEvent(rid);
            }
        });
        addView(mDeleteButton);

        mOKButton = new ImageView(mContext);
        mOKButton.setImageResource(R.drawable.ok);
        mOKButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Introduction",introduction);
                bundle.putLong("StartTime",StartDate.getTimeInMillis());
                bundle.putLong("EndTime",EndDate.getTimeInMillis());
                bundle.putLong("Reminder",RemindSelected);
                bundle.putInt("Type",TagSelected);
                bundle.putLong("Rid",rid);
                mEventListener.CreateFinish(bundle,isNewEvent);
            }
        });
        addView(mOKButton);

        PicScrollChanged();

    }

    public void addNewPic(String Path){

        BitmapFactory.Options op= new BitmapFactory.Options();
        op.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(Path,op);
        int oHeight =op.outHeight;

        int contentHeight=(int)( mSurface.PicScrollRatio*mScreenHeight*(1-2*mSurface.InputPageUpdownPadding));
        int scale=oHeight/contentHeight;
        op.inJustDecodeBounds=false;
        op.inSampleSize=scale;
        Bitmap bm = BitmapFactory.decodeFile(Path,op);
        SavedPic.add(bm);
        PicScrollChanged();
    }

    public void PicScrollChanged(){
        if(SavedPic.size() > 0){
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            final LinearLayout Context;
            if(mSavedPic ==null){
                mSavedPic = new HorizontalScrollView(mContext);
                Context   = new LinearLayout(mContext);
                Context.setOrientation(LinearLayout.HORIZONTAL);
                Context.setLayoutParams(lp);
                mSavedPic.addView(Context);
                addView(mSavedPic);
            }
            else {
                Context=(LinearLayout)mSavedPic.getChildAt(0);
                Context.removeAllViews();
            }

            for(int i = 0;i<SavedPic.size();i++){
                ImageView pic = new ImageView(mContext);
                pic.setTag(i);
                pic.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int index = (int) v.getTag();
                        SavedPic.remove(index);
                        PicScrollChanged();
                        return true;
                    }
                });


                pic.setImageBitmap(SavedPic.get(i));
                lp.height=SavedPic.get(i).getHeight();
                lp.width=SavedPic.get(i).getWidth();
                pic.setLayoutParams(lp);
                if(Context !=null) Context.addView(pic);

            }

        }else{
            removeView(mSavedPic);
            mSavedPic = null;
        }
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
            firstEntry = false;
        }
        mSurface.initSurface();
        if(mBackGround != null){
            int widthspec = MeasureSpec.makeMeasureSpec(mScreenWidth, MeasureSpec.EXACTLY);
            int heightspec = MeasureSpec.makeMeasureSpec(mScreenHeight, MeasureSpec.EXACTLY);
            mBackGround.measure(widthspec, heightspec);
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

        setMeasuredDimension(mScreenWidth, mScreenHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {



        int InputSidePadding =(int) (mScreenWidth * mSurface.InputPageSidePadding);
        int InputUPdownPadding =(int) (mScreenHeight * mSurface.InputPageUpdownPadding);
        if(mBackGround !=null) mBackGround.layout(l,t,r,b);


        if(mInputPage !=null) {
            int lineheight = mInputPage.getLineHeight();
            int linecount = mInputPage.getLineCount()+1;
            int botton;
            /*
            Log.d("Test","LineHeight:"+lineheight+"LineCount:"+linecount);
            if(t +InputUPdownPadding+lineheight*linecount+mSurface.InputPageTextPadding<b-InputUPdownPadding-mSurface.InputPageTextPadding){
                botton = t +InputUPdownPadding+lineheight*linecount+mSurface.InputPageTextPadding;
            }else{
                botton =b-InputUPdownPadding-mSurface.InputPageTextPadding;
            }
            */
            botton = mScreenHeight-InputUPdownPadding-2*mSurface.bottonHeightBarPadding-mSurface.bottonBarHeight-mSurface.InputPageTextPadding;
            mInputPage.layout(l + mSurface.InputPageTextPadding + InputSidePadding,
                    t + InputUPdownPadding + mSurface.InputPageTextPadding,
                    r - InputSidePadding - mSurface.InputPageTextPadding,
                    botton);
        }

        if(mBackGroundPage !=null) {
            mBackGroundPage.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }

        /*
        if(mSavedPic !=null) mSavedPic.layout(l+ InputSidePadding+mSurface.FounctionBarPadding,
                b-InputUPdownPadding-mSurface.FounctionBarPadding-mSurface.FounctionBarHeight-mSavedPic.getMeasuredHeight()-mSurface.PicScrollPadding,
                r-InputSidePadding-mSurface.FounctionBarPadding,
                b-InputUPdownPadding-mSurface.FounctionBarPadding-mSurface.FounctionBarHeight-mSurface.PicScrollPadding);
*/
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
            mAlldaySwitchBotton.layout(0,0,mAlldaySwitchBotton.getMeasuredWidth(),mAlldaySwitchBotton.getMeasuredHeight());
        }

    }



    private class Surface{
        public int InputBackground = Color.WHITE;
        public float InputPageSidePadding = (1/20f);
        public float InputPageUpdownPadding =(1/20f);
        public float BackgroundAlpha = 0.3f;

        public float PicScrollRatio = 0.3f;
        public int PicScrollPadding=20;


        public int InputPageTextPadding ;
        float InputPageTextPaddingRatio = 1/1000f;
        float InputPageTextSize ;
        //float InputPageTextBaseSize = 10f;
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
        //float CheckBoxTextBaseSize = 5f;
        float CheckBoxTextSizeRatio = 1/25f;

        Paint BackGoundPagePaint = new Paint();
        Paint DashLinePaint = new Paint();
        float ShadowOffsetRatio = 1/50f;
        float RealLineWidthRatio= 1/30f;
        float GapLineWidthRatio= 1/60f;
        float DashLineWidth = 1/200f;

        float ItemHeight = 1/30f;


        void initSurface(){
            InputPageTextPadding = (int)(mScreenWidth*InputPageTextPaddingRatio);
            BackGoundPagePaint.setColor(Color.WHITE);
            //BackGoundPagePaint.setShadowLayer(mScreenHeight * ShadowOffsetRatio / 2, mScreenHeight * ShadowOffsetRatio, mScreenHeight * ShadowOffsetRatio, Color.BLACK);
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
            DashLinePaint.setStrokeWidth(mScreenWidth*DashLineWidth);
            addBarMid = (int)(mScreenWidth*addBarMidRatio);
            IconSide =(int)(mScreenWidth*IconSideRatio);
            IconInterval =(int)(mScreenWidth*IconIntervalRatio);
            CheckBoxPadding = (int)(mScreenWidth*CheckBoxPaddingRatio);
            CheckRectPadding = (int)(mScreenWidth*CheckBoxRectPadingRatio);
            CheckBoxTextSize =(mScreenWidth*CheckBoxTextSizeRatio);




        }

    }


    private class  InputPage extends EditText{


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
            Log.d("Test","Size:"+mSurface.InputPageTextSize);
            setTextSize(TypedValue.COMPLEX_UNIT_PX,mSurface.InputPageTextSize);
            setGravity(Gravity.TOP);
            setHint(HintString);
            if(introduction != "null") setText(introduction);
            int effectWidth = (int)(mScreenWidth*(1-2*mSurface.InputPageSidePadding))-2*mSurface.InputPageTextPadding;
            setMaxEms(effectWidth/(int)(getTextSize()*(7/6f)));
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


        private int RectColor= Color.parseColor("#666666");

        private List<String > ReminedList = new ArrayList<>(Arrays.asList(
                "开始前","提前5分钟","提前10分钟","提前30分钟","提前1个小时","提前1天","提前1周"
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
            RectPaint.setPathEffect(ef);
            RectPaint.setStrokeWidth(3);
            for (int i = 0; i < TagSelectedList.size(); i++) {
                Drawable temp = TagSelectedList.get(i);
                temp.setBounds(0,0,(int)TextSize,(int)TextSize);
            }
            itemHeight  = (int)(mScreenHeight*mSurface.ItemHeight);

        }

        public void PopoutAnim(int Target){
            removeAllViews();
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            //lp.height = itemHeight;
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




                    final TextView startDate = new TextView(mContext);
                    startDate.setTag(TIME);
                    startDate.setLayoutParams(lp);

                    startDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,TextSize);
                    String showDate = StartDate.get(Calendar.YEAR)+"年"+(StartDate.get(Calendar.MONTH)+1)+"月"+StartDate.get(Calendar.DAY_OF_MONTH)+"日";
                    startDate.setText(showDate);


                    TextView start = new TextView(mContext);
                    start.setText("开始时间:");
                    start.setTextColor(Color.parseColor("#CD3333"));
                    start.getPaint().setFakeBoldText(true);
                    start.setTag(TIME);
                    start.setLayoutParams(lp);
                    start.setTextSize(TypedValue.COMPLEX_UNIT_SP,TextSize);


                    final TextView startTime = new TextView(mContext);
                    if(StartDate.get(Calendar.AM_PM) == Calendar.PM){
                        startTime.setText("下午"+StartDate.get(Calendar.HOUR)+":"+StartDate.get(Calendar.MINUTE));
                    } else{
                        startTime.setText("上午"+StartDate.get(Calendar.HOUR)+":"+StartDate.get(Calendar.MINUTE));
                    }
                    startTime.setTag(TIME);

                    startTime.setLayoutParams(lp);
                    startTime.setTextSize(TextSize);
                    start.setTextSize(TypedValue.COMPLEX_UNIT_SP,TextSize);


                    final TextView endDate = new TextView(mContext);
                    String showEndDate = EndDate.get(Calendar.YEAR)+"年"+(EndDate.get(Calendar.MONTH)+1)+"月"+EndDate.get(Calendar.DAY_OF_MONTH)+"日";
                    endDate.setText(showEndDate);
                    endDate.setTag(TIME);
                    endDate.setLayoutParams(lp);
                    endDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,TextSize);




                    TextView end= new TextView(mContext);
                    end.setText("结束时间:");
                    end.setTag(TIME);
                    end.setLayoutParams(lp);
                    end.setTextColor(Color.parseColor("#CD2626"));
                    end.getPaint().setFakeBoldText(true);
                    end.setTextSize(TypedValue.COMPLEX_UNIT_SP,TextSize);


                    final TextView endTime =new TextView(mContext);
                    if(EndDate.get(Calendar.AM_PM) ==Calendar.PM){
                        endTime.setText("下午" + EndDate.get(Calendar.HOUR) + ":" + EndDate.get(Calendar.MINUTE));
                    } else{
                        endTime.setText("上午"+EndDate.get(Calendar.HOUR)+":"+EndDate.get(Calendar.MINUTE));
                    }
                    endTime.setTag(TIME);
                    endTime.setLayoutParams(lp);
                    endTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,TextSize);


                    startTime.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePicker mTP = new TimePicker(mContext);
                            if (StartDate.get(Calendar.AM_PM) == Calendar.PM) {
                                mTP.setCurrentHour(StartDate.get(Calendar.HOUR) + 12);
                            } else {
                                mTP.setCurrentHour(StartDate.get(Calendar.HOUR));
                            }
                            mTP.setCurrentMinute(StartDate.get(Calendar.MINUTE));
                            mTP.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                @Override
                                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                                    if (hourOfDay < 12) {
                                        StartDate.set(Calendar.AM_PM, Calendar.AM);
                                        StartDate.set(Calendar.HOUR, hourOfDay);
                                    } else {
                                        StartDate.set(Calendar.AM_PM, Calendar.PM);
                                        StartDate.set(Calendar.HOUR, hourOfDay - 12);
                                    }
                                    StartDate.set(Calendar.MINUTE, minute);

                                    if (StartDate.get(Calendar.AM_PM) == Calendar.PM) {
                                        startTime.setText("下午" + StartDate.get(Calendar.HOUR) + ":" + StartDate.get(Calendar.MINUTE));
                                    } else {
                                        startTime.setText("上午" + StartDate.get(Calendar.HOUR) + ":" + StartDate.get(Calendar.MINUTE));
                                    }
                                    if (StartDate.getTimeInMillis() > EndDate.getTimeInMillis()) {
                                        EndDate.setTimeInMillis(StartDate.getTimeInMillis());
                                        EndDate.add(Calendar.MINUTE, 1);
                                        String showDate = EndDate.get(Calendar.YEAR) + "年" + EndDate.get(Calendar.MONTH) + "月" + EndDate.get(Calendar.DAY_OF_MONTH) + "日";
                                        endDate.setText(showDate);
                                        if (EndDate.get(Calendar.AM_PM) == Calendar.PM) {
                                            endTime.setText("下午" + EndDate.get(Calendar.HOUR) + ":" + EndDate.get(Calendar.MINUTE));
                                        } else {
                                            endTime.setText("上午" + EndDate.get(Calendar.HOUR) + ":" + EndDate.get(Calendar.MINUTE));
                                        }
                                    }
                                }
                            });
                            new AlertDialog.Builder(mContext).setView(mTP).setPositiveButton("好嘞", null).show();
                        }
                    });

                    startDate.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatePicker mDP = new DatePicker(mContext);
                            mDP.init(StartDate.get(Calendar.YEAR), StartDate.get(Calendar.MONTH), StartDate.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    StartDate.set(year, monthOfYear, dayOfMonth);
                                    String showDate = StartDate.get(Calendar.YEAR) + "年" + StartDate.get(Calendar.MONTH) + "月" + StartDate.get(Calendar.DAY_OF_MONTH) + "日";
                                    startDate.setText(showDate);
                                    if (StartDate.getTimeInMillis() > EndDate.getTimeInMillis()) {
                                        EndDate.setTimeInMillis(StartDate.getTimeInMillis());
                                        EndDate.add(Calendar.MINUTE, 1);
                                        String showDateNext = EndDate.get(Calendar.YEAR) + "年" + EndDate.get(Calendar.MONTH) + "月" + EndDate.get(Calendar.DAY_OF_MONTH) + "日";
                                        endDate.setText(showDate);
                                        if (EndDate.get(Calendar.AM_PM) == Calendar.PM) {
                                            endTime.setText("下午" + EndDate.get(Calendar.HOUR) + ":" + EndDate.get(Calendar.MINUTE));
                                        } else {
                                            endTime.setText("上午" + EndDate.get(Calendar.HOUR) + ":" + EndDate.get(Calendar.MINUTE));
                                        }
                                    }
                                }
                            });
                            new AlertDialog.Builder(mContext).setView(mDP).setPositiveButton("好嘞",null).show();
                        }
                    });

                    endDate.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatePicker mDP = new DatePicker(mContext);
                            mDP.init(EndDate.get(Calendar.YEAR), EndDate.get(Calendar.MONTH), EndDate.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    EndDate.set(year, monthOfYear, dayOfMonth);
                                    String showDate = EndDate.get(Calendar.YEAR) + "年" + EndDate.get(Calendar.MONTH) + "月" + EndDate.get(Calendar.DAY_OF_MONTH) + "日";
                                    endDate.setText(showDate);
                                    if (StartDate.getTimeInMillis() > EndDate.getTimeInMillis()) {
                                        StartDate.setTimeInMillis(EndDate.getTimeInMillis());
                                        StartDate.add(Calendar.MINUTE, 1);
                                        String showDateNext = StartDate.get(Calendar.YEAR) + "年" + StartDate.get(Calendar.MONTH) + "月" + StartDate.get(Calendar.DAY_OF_MONTH) + "日";
                                        startDate.setText(showDate);
                                        if (EndDate.get(Calendar.AM_PM) == Calendar.PM) {
                                            startTime.setText("下午" + StartDate.get(Calendar.HOUR) + ":" + StartDate.get(Calendar.MINUTE));
                                        } else {
                                            startTime.setText("上午" + StartDate.get(Calendar.HOUR) + ":" + StartDate.get(Calendar.MINUTE));
                                        }
                                    }
                                }
                            });
                            new AlertDialog.Builder(mContext).setView(mDP).setPositiveButton("好嘞", null).show();
                        }
                    });

                    endTime.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePicker mTP = new TimePicker(mContext);
                            if (StartDate.get(Calendar.AM_PM) == Calendar.PM) {
                                mTP.setCurrentHour(EndDate.get(Calendar.HOUR) + 12);
                            } else {
                                mTP.setCurrentHour(EndDate.get(Calendar.HOUR));
                            }
                            mTP.setCurrentMinute(EndDate.get(Calendar.MINUTE));
                            mTP.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                @Override
                                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                                    if (hourOfDay < 12) {
                                        EndDate.set(Calendar.AM_PM, Calendar.AM);
                                        EndDate.set(Calendar.HOUR, hourOfDay);
                                    } else {
                                        EndDate.set(Calendar.AM_PM, Calendar.PM);
                                        EndDate.set(Calendar.HOUR, hourOfDay - 12);
                                    }
                                    EndDate.set(Calendar.MINUTE, minute);
                                    if (EndDate.get(Calendar.AM_PM) == Calendar.PM) {
                                        endTime.setText("下午" + EndDate.get(Calendar.HOUR) + ":" + EndDate.get(Calendar.MINUTE));
                                    } else {
                                        endTime.setText("上午" + EndDate.get(Calendar.HOUR) + ":" + EndDate.get(Calendar.MINUTE));
                                    }
                                    if (StartDate.getTimeInMillis() > EndDate.getTimeInMillis()) {
                                        StartDate.setTimeInMillis(EndDate.getTimeInMillis());
                                        StartDate.add(Calendar.MINUTE, 1);
                                        String showDate = StartDate.get(Calendar.YEAR) + "年" + StartDate.get(Calendar.MONTH) + "月" + StartDate.get(Calendar.DAY_OF_MONTH) + "日";
                                        startDate.setText(showDate);
                                        if (EndDate.get(Calendar.AM_PM) == Calendar.PM) {
                                            startTime.setText("下午" + StartDate.get(Calendar.HOUR) + ":" + StartDate.get(Calendar.MINUTE));
                                        } else {
                                            startTime.setText("上午" + StartDate.get(Calendar.HOUR) + ":" + StartDate.get(Calendar.MINUTE));
                                        }
                                    }
                                }
                            });
                            new AlertDialog.Builder(mContext).setView(mTP).setPositiveButton("好嘞", null).show();
                        }
                    });

                    addView(start);
                    addView(startDate);
                    addView(startTime);
                    addView(end);
                    addView(endDate);
                    addView(endTime);
                    FunctionActivate =TIME;
                    break;
                case PIC:
                    LayoutParams ll_lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                    LinearLayout ll = new LinearLayout(mContext);
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    ll.setLayoutParams(ll_lp);
                    TextView photo = new TextView(mContext);
                    photo.setText("拍照!");
                    photo.setTextSize(50);
                    photo.setPadding(20, 20, 20, 20);
                    photo.setLayoutParams(ll_lp);

                    ll.addView(photo);
                    TextView album = new TextView(mContext);
                    album .setText("相册~");
                    album .setTextSize(50);
                    album.setPadding(20, 20, 20, 20);
                    album .setLayoutParams(ll_lp);

                    ll.addView(album);
                    final AlertDialog a = new AlertDialog.Builder(mContext).setView(ll).setNegativeButton("不玩了", null).show();


                    photo.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEventListener.addNewBitMap(true);
                            a.dismiss();
                        }
                    });
                    album.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEventListener.addNewBitMap(false);
                            a.dismiss();
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
            canvas.drawRoundRect(drawRect,20,20,RectPaint);
        }
    }

    private class FunctionBar extends ViewGroup{

        private ImageView InviteIcon;
        private ImageView PicIcon;
        private ImageView ReminedIcon;
        private ImageView TagIcon;
        private ImageView TimeIcon;

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
            TimeIcon = new ImageView(mContext);
            TimeIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionCall(TIME);
                    cancelAll();
                    TimeIcon.setImageResource(R.drawable.time2);
                }
            });
            cancelAll();
            addView(InviteIcon);
            addView(PicIcon);
            addView(ReminedIcon);
            addView(TagIcon);
            addView(TimeIcon);
        }

        private void cancelAll(){
            InviteIcon.setImageResource(R.drawable.invite);
            ReminedIcon.setImageResource(R.drawable.remined);
            TagIcon.setImageResource(R.drawable.tag1);
            TimeIcon.setImageResource(R.drawable.time1);
            PicIcon.setImageResource(R.drawable.pic1);

        }



        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSpec = MeasureSpec.makeMeasureSpec(mSurface.IconSide,MeasureSpec.EXACTLY);
            int heightSpec = MeasureSpec.makeMeasureSpec(mSurface.IconSide,MeasureSpec.EXACTLY);
            if(TimeIcon != null) TimeIcon.measure(widthSpec,heightSpec);
            if(InviteIcon != null) InviteIcon.measure(widthSpec,heightSpec);
            if(PicIcon != null) PicIcon.measure(widthSpec,heightSpec);
            if(ReminedIcon != null) ReminedIcon.measure(widthSpec,heightSpec);
            if(TagIcon != null) TagIcon.measure(widthSpec,heightSpec);
            setMeasuredDimension(mSurface.IconSide,MeasureSpec.getSize(heightMeasureSpec));
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            if(PicIcon != null) PicIcon.layout(0,mSurface.IconSide*4+Interval*4,mSurface.IconSide,mSurface.IconSide*5+Interval*4);
            if(TagIcon != null) TagIcon.layout(0,mSurface.IconSide+Interval,mSurface.IconSide,mSurface.IconSide*2+Interval);
            if(TimeIcon != null) TimeIcon.layout(0,mSurface.IconSide*3+Interval*3,mSurface.IconSide,mSurface.IconSide*4+Interval*3);
            if(ReminedIcon != null) ReminedIcon.layout(0,0,mSurface.IconSide,mSurface.IconSide);
            if(InviteIcon != null) InviteIcon.layout(0,mSurface.IconSide*2+Interval*2,mSurface.IconSide,mSurface.IconSide*3+Interval*2);




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
