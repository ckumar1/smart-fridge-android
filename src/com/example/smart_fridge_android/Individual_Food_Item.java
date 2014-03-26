package com.example.smart_fridge_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Individual_Food_Item extends Activity {
	DatabaseHandler db; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_food_item);
        Intent intent = getIntent();
        String id = intent.getStringExtra("fid");
        
        db = new DatabaseHandler(this);
        db.getDatabaseName();
        Food food = new Food();
        food = db.getFoodById(Integer.parseInt(id));
        
    }
//get id, intent to start activity
	public void onButtonClick(View v) {
		
		NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());
        
        switch (v.getId()){
        
        case R.id.DeleteButton:
        	
        	
        	break;
        
       //String foodname = food.getName(name);
        
        }
	
	}	
}
