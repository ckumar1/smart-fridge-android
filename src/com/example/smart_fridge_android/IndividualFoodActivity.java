package com.example.smart_fridge_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IndividualFoodActivity extends Activity {

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
        
        String expDate = food.getExpirationDate();
        TextView expDateText = (TextView) findViewById(R.id.ExpDate);
        expDateText.setText(expDate);
        
        //add in nutritional information Iteration 2
        
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
        }
	
	}	
}
