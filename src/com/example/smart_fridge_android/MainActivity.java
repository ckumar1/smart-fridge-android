package com.example.smart_fridge_android;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ListActivity  {

    private static final String TAG_FOOD_ID = "food_id";
    private static final String TAG_FOOD_DATA = "data";
    private static final String TAG_RECIPE_ID = "recipe_id";
    private static final String TAG_RECIPE_NAME = "name";
    public static boolean foodTab;

    DatabaseHandler db;
    SessionManager session;

    Spinner spinner;
    ListAdapter adapter;
    ArrayList<HashMap<String, String>> foodList; // Passed to the list adapter to be displayed
    ArrayList<HashMap<String, String>> recipesList;
    List<Food> foodsFromDB; // Grabs all food from the internal database
    List<Recipe> recipesFromDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        foodTab = true;

        session = new SessionManager(getApplicationContext());

        db = new DatabaseHandler(this);
        db.getDatabaseName();

        spinner = (Spinner) findViewById(R.id.spinnerUnits);

        TextView foodView = (TextView) findViewById(R.id.tabFood);
        foodView.setBackgroundColor(Color.BLUE);
        setFoodAdapter(); // Always start on the food page

        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {


            }
        });
    }
    
    public void onButtonClick(View v) {
    	NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());

        TextView foodView;
        TextView recipesView;
        TextView settingsView;

        CheckBox notifications;
        Spinner units;


        switch (v.getId()){


            case R.id.tabFood:
                foodTab = true;

                setFoodAdapter();
                setContentView(R.layout.main);

                foodView = (TextView) findViewById(R.id.tabFood);
                recipesView = (TextView) findViewById(R.id.tabRecipes);
                settingsView = (TextView) findViewById(R.id.tabSettings);

                navBar.setTabColors(foodView, recipesView, settingsView);
                break;

            case R.id.tabRecipes:
                foodTab = false;

                setRecipeAdapter();
                setContentView(R.layout.main);

                foodView = (TextView) findViewById(R.id.tabFood);
                recipesView = (TextView) findViewById(R.id.tabRecipes);
                settingsView = (TextView) findViewById(R.id.tabSettings);

                navBar.setTabColors(recipesView, foodView, settingsView);
                break;

            case R.id.tabSettings:

                setContentView(R.layout.settings);

                foodView = (TextView) findViewById(R.id.tabFood);
                recipesView = (TextView) findViewById(R.id.tabRecipes);
                settingsView = (TextView) findViewById(R.id.tabSettings);

                navBar.setTabColors(settingsView, recipesView, foodView);

                Button add = (Button) findViewById(R.id.addBtn);
                if (add != null){
                    add.setVisibility(View.INVISIBLE);
                }

                if (session.wantsNotifications()){
                    notifications = (CheckBox) findViewById(R.id.checkBoxNotifications);
                    notifications.setChecked(true);
                }

                break;

            case R.id.checkBoxNotifications:
                notifications = (CheckBox) findViewById(R.id.checkBoxNotifications);
                if (notifications.isChecked()){
                    session.createNotificationsSession(true);
                }
                else{
                    session.createNotificationsSession(false);
                }

                break;

            case R.id.btnChangePassword:
                break;

            case R.id.spinnerUnits:
                break;
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

            String foodData = "";
            foodData += food.getName();
            foodData += " - Expires: ";
            foodData += food.getExpirationDate();


            map.put(TAG_FOOD_ID, id_as_string);
            map.put(TAG_FOOD_DATA, foodData);
            foodList.add(map);
        }

        adapter = new SimpleAdapter(MainActivity.this, foodList, R.layout.food_list_item,
                new String[] {TAG_FOOD_ID, TAG_FOOD_DATA}, new int[] {R.id.food_id, R.id.food_data});

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
