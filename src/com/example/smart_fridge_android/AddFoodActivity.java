package com.example.smart_fridge_android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddFoodActivity extends Activity implements OnDateSetListener{

    private static final String STARTING_TAB = "startingTab";
    static final int CAMERA_RESULT = 1;
    private Uri imageUri; //This is the uri we get from taking a photo and storing it
    private String imagePath;


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_food);
        removeAddButton();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		TextView mDateTextView = (TextView)findViewById(R.id.dateSelector);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		mDateTextView.setText(dateFormat.format(cal.getTime()));
	}

	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());
    	
		switch (v.getId()){

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
		case R.id.imageBtn:
            //only make intent if phone has a camera
            if (Camera.getNumberOfCameras() > 0) {
                dispatchTakePictureIntent();
            }
            else {
                Toast.makeText(getApplicationContext(), "No camera found", Toast.LENGTH_LONG).show();
            }
			break;
		case R.id.addMan:
			Food food = new Food();
			String name = ((TextView)findViewById(R.id.nameField)).getText().toString();
			if(name.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_LONG).show();
				break;
			}
            if(name.contains("<") && name.contains(">")){
                Toast.makeText(getApplicationContext(), "No tags (<>) allowed", Toast.LENGTH_LONG).show();
                break;
            }
			food.setName(name);
			food.setExpirationDate(((TextView)findViewById(R.id.dateSelector)).getText().toString());

            if (imagePath != null && !imagePath.isEmpty()) {
                food.setImagePath(imagePath);
			}
            else {
				food.setImagePath("");
			}
			
			try {
				int quantity = Integer.parseInt(((TextView)findViewById(R.id.quantityField)).getText().toString());
				food.setQuantity(quantity);
			} catch(NumberFormatException e) {	}
			food.setCategory(((TextView)findViewById(R.id.foodgroupField)).getText().toString());
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			db.addFood(food);
			
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(STARTING_TAB, "food");
			this.startActivity(i);
			break;
		}

        removeAddButton();
	}
	
	private void dispatchTakePictureIntent() {
		//camera stuff
		Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

		//folder stuff
		File imagesFolder = new File(Environment.getExternalStorageDirectory(), "fridge_images");
		imagesFolder.mkdirs();

		//filePath = "/fridge_images/food_image" + timeStamp + ".png" ;
		File image = new File(imagesFolder, "food_image" + timeStamp + ".png");
		Uri uriSavedImage = Uri.fromFile(image);

		imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
		imagePath = image.toString();
		startActivityForResult(imageIntent, CAMERA_RESULT);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
	    }
	}

    private void removeAddButton(){
        Button addButton = (Button) findViewById(R.id.addBtn);
        addButton.setVisibility(View.INVISIBLE);
    }
}
