package com.example.smart_fridge_android;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

public class MainActivity extends ListActivity  {

    private static final String TAG_RECIPE_ID = "recipe_id";
    private static final String TAG_RECIPE_NAME = "name";
    public static boolean foodTab;
    private static final String STARTING_TAB = "startingTab";

    DatabaseHandler db;
    SessionManager session;

    ListAdapter adapter;
    ArrayList<HashMap<String, String>> recipesList;
    List<Food> foodsFromDB; // Grabs all food from the internal database
    List<Recipe> recipesFromDB;

    String startingTab = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        session = new SessionManager(getApplicationContext());

        db = new DatabaseHandler(this);
        db.getDatabaseName();

        Button add = (Button) findViewById(R.id.addBtn);
        add.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        startingTab = intent.getStringExtra(STARTING_TAB);

        if (startingTab.equals("food")){
            foodTab = true;
            TextView foodView = (TextView) findViewById(R.id.tabFood);
            foodView.setBackgroundColor(Color.BLUE);
            setFoodAdapter();
            setListView();
        } else if (startingTab.equals("recipe")){
            foodTab = false;
            TextView recipesView = (TextView) findViewById(R.id.tabRecipes);
            recipesView.setBackgroundColor(Color.BLUE);
            setRecipeAdapter();
            setListView();
        }
    }
    
    
    public void onButtonClick(View v) {
    	NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());

        TextView foodView;
        TextView recipesView;
        TextView settingsView;

        CheckBox notifications;
        RadioButton metric;
        RadioButton imperial;

        switch (v.getId()){

            case R.id.tabFood:

                foodTab = true;
                setContentView(R.layout.main);
                setFoodAdapter();

                foodView = (TextView) findViewById(R.id.tabFood);
                recipesView = (TextView) findViewById(R.id.tabRecipes);
                settingsView = (TextView) findViewById(R.id.tabSettings);

                navBar.setTabColors(foodView, recipesView, settingsView);
                break;

            case R.id.tabRecipes:

                foodTab = false;
                setContentView(R.layout.main);
                setRecipeAdapter();

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

                metric = (RadioButton) findViewById(R.id.radioButtonMetric);
                imperial = (RadioButton) findViewById(R.id.radioButtonImperial);

                if (session.getUnits().equals("Metric")){
                    metric.setChecked(true);
                    imperial.setChecked(false);
                } else {
                    metric.setChecked(false);
                    imperial.setChecked(true);
                }

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
            	setContentView(R.layout.change_password);
                break;

            case R.id.radioButtonMetric:
                session.createUnitsSession("Metric");
                metric = (RadioButton) findViewById(R.id.radioButtonMetric);
                imperial = (RadioButton) findViewById(R.id.radioButtonImperial);
                metric.setChecked(true);
                imperial.setChecked(false);
                break;

            case R.id.radioButtonImperial:
                session.createUnitsSession("Imperial");
                metric = (RadioButton) findViewById(R.id.radioButtonMetric);
                imperial = (RadioButton) findViewById(R.id.radioButtonImperial);
                metric.setChecked(false);
                imperial.setChecked(true);
                break;
                
            //case R.id.ChangePassword:
            	
            	
        }
    }

    /** Shows the food list on the main activity */
    private void setFoodAdapter(){
        ArrayList<String> list = new ArrayList<String>();
        String value;

        foodsFromDB = db.getAllFood();

        for (Food food : foodsFromDB){

            value = "";

            Integer id = food.getId();            // Dumb android syntax forces this on me
            String  id_as_string = id.toString(); // Will investigate alternative methods - carl

            value += id_as_string + "<b>";

            String foodName;
            String foodExpiration;
            foodName = food.getName();

            value += foodName + "<b>";

            if (!food.getExpirationDate().isEmpty()){
                //foodExpiration += " - Expires: ";
                foodExpiration = food.getExpirationDate();
            } else {
                foodExpiration = "No Expiration Date Set";
            }

            value += foodExpiration;

            list.add(value);
        }

        FoodCustomAdapter customAdapter = new FoodCustomAdapter(getApplicationContext(), list);

        setListAdapter(customAdapter);
        setListView();
    }

    /** Shows the recipe list on the main activity */
    private void setRecipeAdapter(){

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
        setListView();
    }

    private void setListView(){
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if (foodTab) {
                    String fid = ((TextView) view.findViewById(R.id.food_id)).getText().toString();
                    //make intent and attach id to intent
                    Intent foodInt = new Intent(getApplicationContext(), IndividualFoodActivity.class);

                    foodInt.putExtra("fid", fid);

                    startActivity(foodInt);
                } else if (!foodTab) {
                    String rid = ((TextView) view.findViewById(R.id.recipe_id)).getText().toString();

                    Intent recipeInt = new Intent(getApplicationContext(), IndividualRecipeActivity.class);

                    recipeInt.putExtra("rid", rid);
                    startActivity(recipeInt);
                }
            }
        });
    }
}
