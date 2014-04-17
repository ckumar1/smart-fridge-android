package com.smart_fridge.smart_fridge_android;

import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
 
public class DatePickerDialogFragment extends DialogFragment {
 
	private OnDateSetListener mDateSetListener;
 
	public DatePickerDialogFragment() {
		// nothing to see here, move along
	}
 
	public DatePickerDialogFragment(OnDateSetListener callback) {
		mDateSetListener = (OnDateSetListener) callback;
	}
 
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Calendar cal = Calendar.getInstance();
 
		return new DatePickerDialog(getActivity(),
				mDateSetListener, cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
	}
 
}
