package com.example.smart_fridge_android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;


public class AddFoodActivity extends Activity implements OnDateSetListener{

	private Button add;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_food);
		add = (Button) findViewById(R.id.addBtn);
        if (add != null)
        	add.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		TextView mDateTextView = (TextView)findViewById(R.id.dateSelector);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		mDateTextView.setText(dateFormat.format(cal.getTime()));
	}

	protected void onPause() {
		super.onPause();
		add.setVisibility(View.VISIBLE); //Make button visible again when you leave this view.
	}
	
	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
    	
		switch (v.getId()){
		
		case R.id.addBtn:
		case R.id.logoutBtn:
    		navBar.onButtonClick(v, this);
    		break;
		case R.id.manualBtn:
			setContentView(R.layout.add_food_manual);
			TextView mDateTextView = (TextView)findViewById(R.id.dateSelector);
			mDateTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					DialogFragment newFragment = new DatePickerDialogFragment(AddFoodActivity.this);
					newFragment.show(ft, "date_dialog");
				}
			});
			break;
		}
	}
}
