package com.ahmetkilic.ophelia.ea_utilities.views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;


import com.ahmetkilic.ophelia.ea_utilities.enums.DateStyle;
import com.ahmetkilic.ophelia.ea_utilities.interfaces.DateSelectListener;
import com.ahmetkilic.ophelia.ea_utilities.tools.TimeUtils;
import com.ahmetkilic.ophelia.ea_utilities.tools.ViewTools;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nonnull;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class DatePickerTextView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Calendar myCalendar = Calendar.getInstance();
    private Date minDate, maxDate;
    private DateSelectListener dateSelectListener;
    private int dateStyle, timeStyle;

    public DatePickerTextView(Context context) {
        super(context);
        init();
        if (context instanceof DateSelectListener)
            dateSelectListener = (DateSelectListener) context;
    }

    public DatePickerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DatePickerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);
        setOnClickListener(this);
        setDateStyle(DateStyle.MEDIUM);
        setTimeStyle(DateStyle.MEDIUM);
    }

    /**
     * Activate auto size property for picker
     */
    public void useAutoSize() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }

    /**
     * Set date select listener for text view
     *
     * @param dateSelectListener date select listener
     */
    public void setDateSelectListener(DateSelectListener dateSelectListener) {
        this.dateSelectListener = dateSelectListener;
    }

    /**
     * Set currently selected date for text view
     *
     * @param date date to set
     */
    public void setSelectedDate(@Nonnull Date date) {
        myCalendar.setTime(date);
        updateText();
    }

    /**
     * Set date style of text view.Default is 'MEDIUM'
     *
     * @param dateStyle date style
     */
    public void setDateStyle(@DateStyle int dateStyle) {
        this.dateStyle = dateStyle;
    }

    /**
     * Set time style of text view.Use 'NONE' if you don't want time value..Default is 'MEDIUM'
     *
     * @param timeStyle time style
     */
    public void setTimeStyle(@DateStyle int timeStyle) {
        this.timeStyle = timeStyle;
    }

    /**
     * Get selected date from picker
     */
    public Date getSelectedDate() {
        return myCalendar.getTime();
    }

    /**
     * Set minimum selectable date
     *
     * @param minDate date object
     */
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    /**
     * Set maximum selectable date
     *
     * @param maxDate date object
     */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    @Override
    public void onClick(View v) {
        requestFocus();
        ViewTools.hideKeyboardFrom(getContext(), this);
        DatePickerDialog dialog = new DatePickerDialog(
                getContext(), this,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        );
        if (minDate != null)
            dialog.getDatePicker().setMinDate(minDate.getTime());
        if (maxDate != null)
            dialog.getDatePicker().setMaxDate(maxDate.getTime());

        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
        myCalendar.set(Calendar.MINUTE, 0);

        if (timeStyle != DateStyle.NONE) {
            new TimePickerDialog(
                    getContext(), this,
                    myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE), true

            ).show();
        } else {
            updateText();
            if (dateSelectListener != null)
                dateSelectListener.onDateSelected(getSelectedDate());
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);
        if (dateSelectListener != null)
            dateSelectListener.onDateSelected(getSelectedDate());
        updateText();
    }

    private void updateText() {
        if (timeStyle == DateStyle.NONE)
            setText(TimeUtils.getFormattedDateString(getSelectedDate(), dateStyle));
        else
            setText(TimeUtils.getFormattedDateString(getSelectedDate(), dateStyle, timeStyle));

    }
}
