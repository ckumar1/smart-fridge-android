package com.example.smart_fridge_android;

import java.text.SimpleDateFormat;
import android.app.DatePickerDialog.OnDateSetListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
           
            break;
        }
	
	}	
}
