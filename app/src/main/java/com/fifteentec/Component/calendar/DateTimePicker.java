package com.fifteentec.Component.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fifteentec.yoko.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateTimePicker {

    private int start_year;
    private int start_month;
    private int start_day;
    private int start_hour;
    private int start_minut;

    private int end_year;
    private int end_month;
    private int end_day;
    private int end_hour;
    private int end_minut;

    private Context mContext;

    private View DialogView = null;
    private TextView StartTextView = null;
    private TextView EndTextView = null;

    private DateTimePickerListener dateTimePickerListener;

    private int ScreenWidth;

    private float TextRatio = 1/18f;
    private float SizeRatio = 13/84f;

    public DateTimePicker(Context context,int screenWidth){
        mContext = context;
        ScreenWidth  = screenWidth;
    }

    public interface DateTimePickerListener{
        void DateChange(GregorianCalendar start,GregorianCalendar end);
    }

    public void setDateTimePickerListener(DateTimePickerListener dateTimePickerListener) {
        this.dateTimePickerListener = dateTimePickerListener;
    }

    private void UpdateView(boolean allDay){
        if(allDay) {
            if (DialogView != null) {
                String text = "开始时间:" ;
                StartTextView.setText(text);
                text = "结束时间:" ;
                EndTextView.setText(text);
            }
        }else{
            String text = "事件日期:" ;
            StartTextView.setText(text);
        }
    }

    private void resizePikcer(FrameLayout tp){
        List<NumberPicker> npList = findNumberPicker(tp);
        for(NumberPicker np:npList) {
            resizeNumberPicker(np);
        }
    }

    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i = 0;i<viewGroup.getChildCount();i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }
                else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(ScreenWidth*SizeRatio), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 2, 0);
        np.setLayoutParams(params);
    }

    public void initalDialog(GregorianCalendar startTime,GregorianCalendar endTime, final boolean allDay){
        start_year = startTime.get(Calendar.YEAR);
        start_month = startTime.get(Calendar.MONTH);
        start_day = startTime.get(Calendar.DAY_OF_MONTH);
        start_hour = startTime.get(Calendar.HOUR_OF_DAY);
        start_minut = startTime.get(Calendar.MINUTE);

        end_year = endTime.get(Calendar.YEAR);
        end_month = endTime.get(Calendar.MONTH);
        end_day = endTime.get(Calendar.DAY_OF_MONTH);
        end_hour = endTime.get(Calendar.HOUR_OF_DAY);
        end_minut = endTime.get(Calendar.MINUTE);

        if(allDay) {
            DialogView = View.inflate(mContext, R.layout.view_common_datetime, null);
            StartTextView = (TextView) DialogView.findViewById(R.id.start_text);
            StartTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenWidth * TextRatio);
            EndTextView = (TextView) DialogView.findViewById(R.id.end_text);
            EndTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenWidth * TextRatio);
            UpdateView(allDay);


            final DatePicker StartDatePicker = (DatePicker) DialogView.findViewById(R.id.start_datepicker);
            StartDatePicker.init(start_year, start_month, start_day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    start_year = year;
                    start_month = monthOfYear;
                    start_day = dayOfMonth;
                    GregorianCalendar start = new GregorianCalendar(start_year,start_month,start_day,start_hour,start_minut,0);
                    GregorianCalendar end = new GregorianCalendar(end_year,end_month,end_day,end_hour,end_minut,0);
                    if(start.getTimeInMillis()+5*60*1000>end.getTimeInMillis()){
                        end.add(Calendar.DAY_OF_MONTH, -1);
                        start_year = end.get(Calendar.YEAR);
                        start_month = end.get(Calendar.MONTH);
                        start_day = end.get(Calendar.DAY_OF_MONTH);
                        view.updateDate(start_year, start_month, start_day);
                        Toast.makeText(mContext,"最小间隔不可以小于五分钟",Toast.LENGTH_SHORT).show();
                    }
                    UpdateView(allDay);

                }
            });
            StartDatePicker.setCalendarViewShown(false);
            resizePikcer(StartDatePicker);


            final TimePicker StartTimePicker = (TimePicker) DialogView.findViewById(R.id.start_timepicker);
            StartTimePicker.setCurrentHour(start_hour);
            StartTimePicker.setCurrentMinute(start_minut);
            StartTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    start_hour = hourOfDay;
                    start_minut = minute;
                    GregorianCalendar start = new GregorianCalendar(start_year,start_month,start_day,start_hour,start_minut,0);
                    GregorianCalendar end = new GregorianCalendar(end_year,end_month,end_day,end_hour,end_minut,0);
                    if(start.getTimeInMillis()+5*60*1000>end.getTimeInMillis()){
                        end.add(Calendar.MINUTE, -5);
                        start_hour = end.get(Calendar.HOUR);
                        start_minut = end.get(Calendar.MINUTE);
                        StartTimePicker.setCurrentHour(start_hour);
                        StartTimePicker.setCurrentMinute(start_minut);
                        Toast.makeText(mContext,"最小间隔不可以小于五分钟",Toast.LENGTH_SHORT).show();
                    }
                    UpdateView(allDay);
                }
            });
            StartTimePicker.setIs24HourView(true);
            resizePikcer(StartTimePicker);


            DatePicker EndDatePicker = (DatePicker) DialogView.findViewById(R.id.end_datepicker);
            EndDatePicker.init(end_year, end_month, end_day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    end_year = year;
                    end_month = monthOfYear;
                    end_day = dayOfMonth;
                    GregorianCalendar start = new GregorianCalendar(start_year,start_month,start_day,start_hour,start_minut,0);
                    GregorianCalendar end = new GregorianCalendar(end_year,end_month,end_day,end_hour,end_minut,0);
                    if(start.getTimeInMillis()+5*60*1000>end.getTimeInMillis()){
                        start.add(Calendar.DAY_OF_MONTH, 1);
                        end_year = start.get(Calendar.YEAR);
                        end_month = start.get(Calendar.MONTH);
                        end_day = start.get(Calendar.DAY_OF_MONTH);
                        view.updateDate(end_year,end_month,end_day);
                        Toast.makeText(mContext,"最小间隔不可以小于五分钟",Toast.LENGTH_SHORT).show();
                    }
                    UpdateView(allDay);
                }
            });
            EndDatePicker.setCalendarViewShown(false);
            resizePikcer(EndDatePicker);


            final TimePicker EndTimePicker = (TimePicker) DialogView.findViewById(R.id.end_timepicker);
            EndTimePicker.setCurrentHour(end_hour);
            EndTimePicker.setCurrentMinute(end_minut);
            EndTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    end_hour = hourOfDay;
                    end_minut = minute;
                    GregorianCalendar start = new GregorianCalendar(start_year,start_month,start_day,start_hour,start_minut,0);
                    GregorianCalendar end = new GregorianCalendar(end_year,end_month,end_day,end_hour,end_minut,0);
                    if(start.getTimeInMillis()+5*60*1000>end.getTimeInMillis()){
                        start.add(Calendar.MINUTE,5);
                        end_hour = start.get(Calendar.HOUR);
                        end_minut = start.get(Calendar.MINUTE);
                        EndTimePicker.setCurrentHour(end_hour);
                        EndTimePicker.setCurrentMinute(end_minut);
                        Toast.makeText(mContext,"最小间隔不可以小于五分钟",Toast.LENGTH_SHORT).show();
                    }
                    UpdateView(allDay);
                }
            });
            EndTimePicker.setIs24HourView(true);
            resizePikcer(EndTimePicker);

        }else{
            DialogView = View.inflate(mContext,R.layout.view_allday_datetime,null);
            StartTextView  = (TextView)DialogView.findViewById(R.id.date_text);
            StartTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenWidth * TextRatio);
            UpdateView(allDay);

            DatePicker datePicker =(DatePicker)DialogView.findViewById(R.id.datepicker);
            datePicker.init(start_year, start_month, start_day, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    start_year = year;
                    start_month = monthOfYear;
                    start_day = dayOfMonth;
                    end_year = year;
                    end_month = monthOfYear;
                    end_day = dayOfMonth;
                    UpdateView(allDay);
                }
            });
            datePicker.setCalendarViewShown(false);

        }

        new AlertDialog.Builder(mContext).setView(DialogView).setPositiveButton("好嘞", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dateTimePickerListener != null) {
                    if(allDay) {
                        GregorianCalendar start = new GregorianCalendar(start_year, start_month, start_day, start_hour, start_minut, 0);
                        GregorianCalendar end = new GregorianCalendar(end_year, end_month, end_day, end_hour, end_minut, 0);
                        dateTimePickerListener.DateChange(start, end);
                    }else{
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(start_year, start_month, start_day, 0,0, 0);
                        dateTimePickerListener.DateChange(gregorianCalendar,gregorianCalendar);
                    }
                }
            }
        }).setNegativeButton("取消", null).show();

    }


}
