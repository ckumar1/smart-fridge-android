package com.example.smart_fridge_android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ListActivity  {

    private static final String TAG_FOOD_ID = "food_id";
    private static final String TAG_FOOD_NAME = "name";

    DatabaseHandler db;
    
    ArrayList<HashMap<String, String>> foodList; // Passed to the list adapter to be displayed
    List<Food> foodsFromDB; // Grabs all food from the internal database

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        db = new DatabaseHandler(this);
        db.getDatabaseName();

        foodList = new ArrayList<HashMap<String, String>>();
        ListView listView = getListView();

        foodsFromDB = db.getAllFood();

        for (Food food : foodsFromDB){

            HashMap<String, String> map = new HashMap<String, String>();

            Integer id = food.getId();            // Dumb android syntax forces this on me
            String  id_as_string = id.toString(); // Will investigate alternative methods - carl

            map.put(TAG_FOOD_ID, id_as_string);
            map.put(TAG_FOOD_NAME, food.getName());
            foodList.add(map);
        }

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, foodList, R.layout.food_list_item,
                new String[] {TAG_FOOD_ID, TAG_FOOD_NAME}, new int[] {R.id.food_id, R.id.food_name});

        setListAdapter(adapter);

        // on selecting single food item
        // launching Edit Food Screen
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Need to implement the food / recipe views for this one
                // It might be worth it to split them up into separate activities -carl
            }
        });  
    }
    
    public void onButtonClick(View v) {
    	NavigationBar navBar = new NavigationBar();
    	
		switch (v.getId()){
		
		case R.id.addBtn:
		case R.id.logoutBtn:
    		navBar.onButtonClick(v, this);
    		break;
		}
    }
   
    
    
}
