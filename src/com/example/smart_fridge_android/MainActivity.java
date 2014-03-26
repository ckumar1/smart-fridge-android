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
    private static final String TAG_RECIPE_ID = "recipe_id";
    private static final String TAG_RECIPE_NAME = "name";
    public static boolean foodTab = true; 

    DatabaseHandler db;
    SessionManager session;

    ListAdapter adapter;
    ArrayList<HashMap<String, String>> foodList; // Passed to the list adapter to be displayed
    ArrayList<HashMap<String, String>> recipesList;
    List<Food> foodsFromDB; // Grabs all food from the internal database
    List<Recipe> recipesFromDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        session = new SessionManager(getApplicationContext());

        db = new DatabaseHandler(this);
        db.getDatabaseName();

        setFoodAdapter(); // Always start on the food page

        ListView listView = getListView();
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
        navBar.onButtonClick(v, getApplicationContext());

        if (v.getId() == R.id.tabFood){
        	foodTab = true;
            setFoodAdapter();
        }

        if (v.getId() == R.id.tabRecipes){
        	foodTab = false;
            setRecipeAdapter();
        }

        if (v.getId() == R.id.tabSettings){
            setContentView(R.layout.settings);
        }
    }

    /** Shows the food list on the main activity */
    public void setFoodAdapter(){

        foodList = new ArrayList<HashMap<String, String>>();

        foodsFromDB = db.getAllFood();

        for (Food food : foodsFromDB){

            HashMap<String, String> map = new HashMap<String, String>();

            Integer id = food.getId();            // Dumb android syntax forces this on me
            String  id_as_string = id.toString(); // Will investigate alternative methods - carl

            map.put(TAG_FOOD_ID, id_as_string);
            map.put(TAG_FOOD_NAME, food.getName());
            foodList.add(map);
        }

        adapter = new SimpleAdapter(MainActivity.this, foodList, R.layout.food_list_item,
                new String[] {TAG_FOOD_ID, TAG_FOOD_NAME}, new int[] {R.id.food_id, R.id.food_name});

        setListAdapter(adapter);
    }

    /** Shows the recipe list on the main activity */
    public void setRecipeAdapter(){

        recipesList = new ArrayList<HashMap<String, String>>();

        recipesFromDB = db.getAllRecipes();

        for (Recipe recipe : recipesFromDB){

            HashMap<String, String> map = new HashMap<String, String>();

            Integer id = recipe.getId();
            String  id_as_string = id.toString();

            map.put(TAG_RECIPE_ID, id_as_string);
            map.put(TAG_RECIPE_NAME, recipe.getName());
            recipesList.add(map);
        }

        adapter = new SimpleAdapter(MainActivity.this, recipesList, R.layout.recipe_list_item,
                new String[] {TAG_RECIPE_ID, TAG_RECIPE_NAME}, new int[] {R.id.recipe_id, R.id.recipe_name});

        setListAdapter(adapter);
    }
}
