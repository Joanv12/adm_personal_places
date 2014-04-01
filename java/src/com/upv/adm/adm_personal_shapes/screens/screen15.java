package com.upv.adm.adm_personal_shapes.screens;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.CustomActionBarActivity;

public class screen15 extends CustomActionBarActivity {

	// date
	private TextView mDateDisplay;
	
	private Button mPickDate;
	
	private int 
			mYear,
			mMonth,
			mDay;
	
	static final int DATE_DIALOG_ID = 0;

	// time
	private TextView mTimeDisplay;
	private Button mPickTime;
	private int 
			mHour,
			mMinute;
	static final int TIME_DIALOG_ID = 1;

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplaydate();
		}
	};

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplaytime();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.screen15);
		initControls();
	}
	
	public void initControls(){
		
		// date
		// capture our View elements
		mDateDisplay = (TextView) findViewById(R.id.edittext_dateDisplay);
		mPickDate = (Button) findViewById(R.id.button_pickDate);
		// add a click listener to the button
		mPickDate.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		// get the current date
		final Calendar cdate = Calendar.getInstance();
		mYear = cdate.get(Calendar.YEAR);
		mMonth = cdate.get(Calendar.MONTH);
		mDay = cdate.get(Calendar.DAY_OF_MONTH);
		// display the current date (this method is below)
		updateDisplaydate();

		// time
		// capture our View elements
		mTimeDisplay = (TextView) findViewById(R.id.edittext_timeDisplay);
		mPickTime = (Button) findViewById(R.id.button_pickTime);
		// add a click listener to the button
		mPickTime.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});
		// get the current time
		final Calendar ctime = Calendar.getInstance();
		mHour = ctime.get(Calendar.HOUR_OF_DAY);
		mMinute = ctime.get(Calendar.MINUTE);
		// display the current date
		updateDisplaytime();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case TIME_DIALOG_ID:       
				return new TimePickerDialog(this,                	
						mTimeSetListener, mHour, mMinute, false); 
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this,                    
    					mDateSetListener,                    
    					mYear, mMonth, mDay);
		}
		return null;
	}

	// updates the date in the TextView
	private void updateDisplaydate() {
		mDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}

	// updates the time we display in the TextView
	private void updateDisplaytime() {
		mTimeDisplay.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

}
