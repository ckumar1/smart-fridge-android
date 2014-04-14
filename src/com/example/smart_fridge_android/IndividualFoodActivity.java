package com.example.smart_fridge_android;

import java.text.SimpleDateFormat;
import android.app.DatePickerDialog.OnDateSetListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IndividualFoodActivity extends Activity implements OnDateSetListener{

    private static final String STARTING_TAB = "startingTab";

    DatabaseHandler db;
	Food food = new Food();
		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_food_item);
        Intent intent = getIntent();
        String id = intent.getStringExtra("fid");
        
        db = new DatabaseHandler(this);
        db.getDatabaseName();
        
        food = db.getFoodById(Integer.parseInt(id));
        
        String name = food.getName();
        TextView nameText = (TextView) findViewById(R.id.FoodName);
        nameText.setText(name);
         
        int quantity = food.getQuantity();
        TextView quantityText = (TextView) findViewById(R.id.FoodQuantity);
        quantityText.setText(""+quantity);

        String path = food.getImagePath();
        if (path != null && !path.isEmpty()) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Log.w("Image", "Image Exists!");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.imgView);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(),
                        myBitmap.getHeight(), matrix, true);
                myImage.setImageBitmap(rotatedBitmap);

            }
        }
        
        TextView expDateText = (TextView) findViewById(R.id.ExpDate);

        expDateText.setText(food.getExpirationDate());

        expDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = new DatePickerDialogFragment(IndividualFoodActivity.this);
                newFragment.show(ft, "date_dialog");
            }
        });

        //nutritional information will be displayed and can edit; need to conntect to API
        String nutritionalInformation = food.getDescription();
        TextView nutritionalInfoText = (TextView) findViewById(R.id.nutritionalInformationText);
        nutritionalInfoText.setText(nutritionalInformation);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		TextView mDateTextView = (TextView)findViewById(R.id.ExpDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		mDateTextView.setText(dateFormat.format(cal.getTime()));
	}
	
//get id, intent to start activity
	public void onButtonClick(View v) {
		
		NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());
        NavigationBar.onTabsClicked(v, this);
                           
                    
        switch (v.getId()){
        
        case R.id.DeleteButton:
            String foodImgPath = food.getImagePath();
            if (foodImgPath != null && !foodImgPath.isEmpty()) {
                File food_image = new File(foodImgPath);
                Boolean deleted = food_image.delete(); //Delete the photo from your phone
                if (!deleted)
                    Log.w("Delete Food", "Food Image wasn't deleted");
            }
        	db.deleteFood(food);
        	
        	Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(STARTING_TAB, "food");
			this.startActivity(i);
        	break;      	
        
        case R.id.update_button:
        	String name = ((TextView)findViewById(R.id.FoodName)).getText().toString();
        	if(name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_LONG).show();
                break;
            }
            if(name.contains("<") && name.contains(">")){
                Toast.makeText(getApplicationContext(), "No tags (<>) allowed", Toast.LENGTH_LONG).show();
                break;
            }
        	food.setName(name);
                
            food.setExpirationDate(((TextView)findViewById(R.id.ExpDate)).getText().toString());
          
            try {
                int quantity = Integer.parseInt(((TextView)findViewById(R.id.FoodQuantity)).getText().toString());
                food.setQuantity(quantity);
            } catch(NumberFormatException e) {	}
            
            String nutrition = ((TextView)findViewById(R.id.nutritionalInformationText)).getText().toString();
            food.setDescription(nutrition);
            
            db.updateFood(food);

            Intent updateFoodIntent = new Intent(getApplicationContext(), MainActivity.class);
            updateFoodIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            updateFoodIntent.putExtra(STARTING_TAB, "food");
            startActivity(updateFoodIntent);
           
            break;
        }
	
	}	
}
