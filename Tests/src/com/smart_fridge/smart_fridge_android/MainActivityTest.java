package com.smart_fridge.smart_fridge_android;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.MoreAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity>{
	
    private static final String STARTING_TAB = "startingTab";
	Intent launchIntent;	
	ListActivity activity;

	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), MainActivity.class);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }
    
    private void activityStart() {
    	startActivity(launchIntent, null, null);
    	activity = getActivity();
    	assertNotNull(activity);
    }
    
    public void testStartOnAnyTab() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	activityStart();
    	
    	Button add = (Button)activity.findViewById(R.id.addBtn);
    	assertNotNull(add);
    	assertEquals(View.VISIBLE, add.getVisibility());
    	
    	checkTabs(activity);
    }
    
    public void testStartOnFoodTab() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	activityStart();
    	
    	checkFoodTabState(activity);     
    }
    
    public void testStartOnRecipeTab() {
    	launchIntent.putExtra(STARTING_TAB, "recipes");
    	activityStart();
    	
    	checkRecipeTabState(activity);
    }
    
    public void testStartOnSettingsTab() {
    	launchIntent.putExtra(STARTING_TAB, "settings");
    	activityStart();
    	
    	checkSettingsTabState(activity);
    }
    
    public void testClickLogout() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	activityStart();
    	
    	// Get the logout button and click it
    	Button logoutBtn = (Button) activity.findViewById(R.id.logoutBtn);
    	assertNotNull(logoutBtn);
    	logoutBtn.performClick();
    	
    	//Check we are logged out
    	SessionManager session = new SessionManager(activity.getApplicationContext());
    	assertFalse("Should be logged out", session.isLoggedIn());
    	
    	//TODO fix logout tests
    	// Check that the correct Intent was created
    	/*
    	Intent addIntent = getStartedActivityIntent();
    	assertNotNull(addIntent);
    	assertEquals(".LoginActivity class should be set in Intent",
    			".LoginActivity", addIntent.getComponent().getShortClassName());
    	assertEquals("FLAG_ACTIVITY_NEW_TASK should be set", 
    			Intent.FLAG_ACTIVITY_NEW_TASK, addIntent.getFlags());
    */
    }
    
    public void testClickAddBtnFromFoodTab() {
    	// TODO 
    }
    
    public void testClickAddBtnFromRecipeTab() {
    	//TODO 
    }
    
    public void testClickFoodTab() {
    	launchIntent.putExtra(STARTING_TAB, "recipes");
    	activityStart();
    	
    	assertFalse("foodTab should be false", MainActivity.foodTab);
    	
    	TextView foodView = (TextView) activity.findViewById(R.id.tabFood);
    	assertNotNull(foodView);
    	
    	foodView.performClick();
    	   
    	checkFoodTabState(activity);
    }
    
    public void testClickRecipeTab() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	activityStart();
    	
    	assertTrue("foodTab should be true", MainActivity.foodTab);
    	
    	TextView recipeView = (TextView) activity.findViewById(R.id.tabRecipes);
    	assertNotNull(recipeView);
    	
    	recipeView.performClick();
    	
    	checkRecipeTabState(activity);
    }
    
    public void testClickSettingsTabs() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	activityStart();
    	
    	assertTrue("foodTab should be true", MainActivity.foodTab);
    	
    	TextView settingsView = (TextView) activity.findViewById(R.id.tabSettings);
    	assertNotNull(settingsView);
    	
    	settingsView.performClick();
    	
    	checkSettingsTabState(activity);
    	
    	// Swap radioButton State
    	RadioButton metric = (RadioButton)activity.findViewById(R.id.radioButtonMetric);
        RadioButton imperial = (RadioButton)activity.findViewById(R.id.radioButtonImperial);
    	assertNotNull(metric);
    	assertNotNull(imperial);
    	if(metric.isChecked()) {
    		imperial.performClick();
    	}
    	else {
    		metric.performClick();
    	}
    	
    	// Swap the notifications
    	CheckBox notifications = (CheckBox)activity.findViewById(R.id.checkBoxNotifications);
    	assertNotNull(notifications);
    	notifications.performClick();
    	
    	// Change to a different tab
    	TextView recipeView = (TextView) activity.findViewById(R.id.tabRecipes);
    	assertNotNull(recipeView);
    	recipeView.performClick();
    	
    	// Change back to test changes
    	settingsView.performClick();
    	checkSettingsTabState(activity);
    }
    
    public void checkSettingsTabState(Activity activity) {
    	assertFalse("foodTab should be false", MainActivity.foodTab);
    	
    	checkTabs(activity);
    	
    	// Check the radio buttons are there and set differently
    	RadioButton metric = (RadioButton) activity.findViewById(R.id.radioButtonMetric);
        RadioButton imperial = (RadioButton) activity.findViewById(R.id.radioButtonImperial);
        assertNotNull(metric);
        assertNotNull(imperial);
        // Check that not both metric and imperial is checked
        MoreAsserts.assertNotEqual(metric.isChecked(), imperial.isChecked());
    
        // Check the add button is invisible
        Button add = (Button)activity.findViewById(R.id.addBtn);
    	assertNotNull(add);
    	assertEquals(View.INVISIBLE, add.getVisibility());
    	
    	// Check the notifications checkbox is there
    	CheckBox notifications = (CheckBox) activity.findViewById(R.id.checkBoxNotifications);
    	assertNotNull(notifications);
    }
    
    public void checkRecipeTabState(ListActivity activity) {
    	assertFalse("foodTab should be false", MainActivity.foodTab);
    	
    	// Check the listAdapter has been set
    	ListAdapter adapter = activity.getListAdapter();
    	assertNotNull(adapter);
    	assertTrue("adapter is the wrong type", adapter instanceof SimpleAdapter);
    	
    	// Check the listView exists.
    	ListView listView = activity.getListView();
    	assertNotNull(listView);
    	assertNotNull(listView.getOnItemClickListener());
    	
    	// Check that the advancedRecipeSearch button is not gone
    	Button advancedRecipeSearch = (Button) activity.findViewById(R.id.btnAdvancedRecipeSearch);
        assertNotNull(advancedRecipeSearch);  
    }
    
    public void checkFoodTabState(ListActivity activity) {
    	assertTrue("foodTab should be true", MainActivity.foodTab);
    	
    	checkTabs(activity);
    	
    	// Check the listAdapter has been set
    	ListAdapter adapter = activity.getListAdapter();
    	assertNotNull(adapter);
    	assertTrue("adapter is the wrong type", adapter instanceof FoodCustomAdapter);
    	
    	// Check the listView exists.
    	ListView listView = activity.getListView();
    	assertNotNull(listView);
    	assertNotNull(listView.getOnItemClickListener());
    	
    	// Check that the advancedRecipeSearch button is gone
    	Button advancedRecipeSearch = (Button) activity.findViewById(R.id.btnAdvancedRecipeSearch);
        assertNull(advancedRecipeSearch);  
    }
    
    public void testClickAdvancedRecipeSearchButton() {
    	launchIntent.putExtra(STARTING_TAB, "recipes");
    	activityStart();
    	
    	Button advRecipeSearchBtn = (Button) activity.findViewById(R.id.btnAdvancedRecipeSearch);
    	assertNotNull(advRecipeSearchBtn);
    	
    	advRecipeSearchBtn.performClick();
    	
    	// Check the correct Intent was created
    	Intent advIntent = getStartedActivityIntent();
    	assertNotNull(advIntent);
    	assertEquals(".AdvancedSearchRecipeActivity class should be set in Intent",
    			".AdvancedSearchRecipeActivity", advIntent.getComponent().getShortClassName());
    }
    
    public void testClickCheckBoxNotifications() {
    	launchIntent.putExtra(STARTING_TAB, "settings");
    	activityStart();
    	
    	CheckBox notifications = (CheckBox)activity.findViewById(R.id.checkBoxNotifications);
    	assertNotNull(notifications);
    	if(notifications.isChecked()) {
    		notifications.performClick();
    	}
    	
    	// Create and check the session values
    	SessionManager session = new SessionManager(activity.getApplicationContext());
    	assertNotNull(session);
    	assertFalse("Should not want notifications", session.wantsNotifications());
    	assertFalse("Should not want notifications", notifications.isChecked());
    	
    	notifications.performClick();
    	
    	// Create and check the session values
    	session = new SessionManager(activity.getApplicationContext());
    	assertNotNull(session);
    	assertTrue("Should want notifications", session.wantsNotifications());
    	assertTrue("Should want notifications", notifications.isChecked());
    }
    
    public void testClickChangePassword() {
    	launchIntent.putExtra(STARTING_TAB, "settings");
    	activityStart();
    	
    	Button changePass = (Button)activity.findViewById(R.id.btnChangePassword);
    	assertNotNull(changePass);
    	changePass.performClick();
    	//TODO write when the ChangePassword code is implemented
    }
    
    public void testClickMetricRadioButton() {
    	launchIntent.putExtra(STARTING_TAB, "settings");
    	activityStart();
    	
    	// Check that the radio buttons are there
    	RadioButton metric = (RadioButton)activity.findViewById(R.id.radioButtonMetric);
    	RadioButton imperial = (RadioButton)activity.findViewById(R.id.radioButtonImperial);
    	assertNotNull(metric);
    	assertNotNull(imperial);
    	
    	metric.performClick();
    	
    	// Check the session manager is set correctly.
    	SessionManager session = new SessionManager(activity.getApplicationContext());
    	assertNotNull(session);
    	assertEquals("Metric", session.getUnits());
    	
    	assertTrue(metric.isChecked());
    	assertFalse(imperial.isChecked());
    }
    
    public void testClickImperialRadioButton() {
    	launchIntent.putExtra(STARTING_TAB, "settings");
    	activityStart();
    	
    	// Check that the radio buttons are there
    	RadioButton metric = (RadioButton)activity.findViewById(R.id.radioButtonMetric);
    	RadioButton imperial = (RadioButton)activity.findViewById(R.id.radioButtonImperial);
    	assertNotNull(metric);
    	assertNotNull(imperial);
    	
    	imperial.performClick();
    	
    	// Check the session manager is set correctly.
    	SessionManager session = new SessionManager(activity.getApplicationContext());
    	assertNotNull(session);
    	assertEquals("Imperial", session.getUnits());
    	
    	assertFalse(metric.isChecked());
    	assertTrue(imperial.isChecked());
    }
    
    public void testClickIndividualFood() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	// Add a test food so that there is at least 1 item in the list
        DatabaseHandler db = new DatabaseHandler(getInstrumentation()
        		.getTargetContext());
        Food testFood = db.addFood(new Food("TESTAPPLETEST", "Red", "1/1/15", 
        		"Fruit", "", 100, 1));
        assertNotNull(testFood);
    	activityStart();
    	
    	assertTrue("foodTab should be true", MainActivity.foodTab);

    	// Check the listView exists.
    	ListView listView = activity.getListView();
    	assertNotNull(listView);
    	assertNotNull(listView.getOnItemClickListener());

    	// Get the first food item if it exists
    	listView.performItemClick( listView.getAdapter().getView(0, null, null),
    			0, listView.getAdapter().getItemId(0));

    	// Check the correct Intent was created
    	Intent indIntent = getStartedActivityIntent();
    	assertNotNull(indIntent);
    	assertEquals(".IndividualFoodActivity class should be set in Intent",
    			".IndividualFoodActivity", indIntent.getComponent().getShortClassName());
    	assertTrue("fid extra should be set", 
    			indIntent.hasExtra("fid"));

    	db = new DatabaseHandler(activity.getApplicationContext());

    	// Check to make sure the food is still in the database
    	assertNotNull(db.getFoodById(testFood.getId()));

    	// Delete the food and check to make sure the food is gone
    	db.deleteFood(testFood);
    	assertNull(db.getFoodById(testFood.getId()));
    }
    
    public void testClickIndividualRecipe() {
    	launchIntent.putExtra(STARTING_TAB, "recipes");
    	// Add a test recipe so that the list isn't empty
        DatabaseHandler db = new DatabaseHandler(getInstrumentation()
        		.getTargetContext());
        Recipe testRecipe = db.addRecipe(new Recipe("TESTMACNCHEESETEST", 
        		"Boil noodles. Add cheese", "Add lots of cheese", 
        		"macaroni<b>cheese", ""));
        assertNotNull(testRecipe);
        
    	activityStart();
    	
    	assertFalse("foodTab should be false", MainActivity.foodTab);
    	
    	// Check the listView exists.
    	ListView listView = activity.getListView();
    	assertNotNull(listView);
    	assertNotNull(listView.getOnItemClickListener());

    	// Get the first food item 
    	listView.performItemClick( listView.getAdapter().getView(0, null, null),
    			0, listView.getAdapter().getItemId(0));

    	// Check the correct Intent was created
    	Intent indIntent = getStartedActivityIntent();
    	assertNotNull(indIntent);
    	assertEquals(".IndividualRecipeActivity class should be set in Intent",
    			".IndividualRecipeActivity", indIntent.getComponent().getShortClassName());
    	assertTrue("rid extra should be set", 
    			indIntent.hasExtra("rid"));

    	db = new DatabaseHandler(activity.getApplicationContext());

    	// Check to make sure the recipe is still in the database
    	assertNotNull(db.getRecipeById(testRecipe.getId()));

    	// Delete the recipe and check to make sure the recipe is gone
    	db.deleteRecipe(testRecipe);
    	assertNull(db.getRecipeById(testRecipe.getId()));
    }
    
    public void checkTabs(Activity activity) {
    	TextView foodView = (TextView) activity.findViewById(R.id.tabFood);
    	TextView recipesView = (TextView) activity.findViewById(R.id.tabRecipes);
    	TextView settingsView = (TextView) activity.findViewById(R.id.tabSettings);
    	assertNotNull(foodView);
    	assertNotNull(recipesView);
    	assertNotNull(settingsView);
    }
}
