package com.example.smart_fridge_android;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.MoreAsserts;
import android.view.View;
import android.view.ViewGroup;
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

	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), AddRecipeActivity.class);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }
    
    public void testStartOnAnyTab() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	startActivity(launchIntent, null, null);
    	ListActivity activity = getActivity();
    	assertNotNull(activity);
    	
    	Button add = (Button)activity.findViewById(R.id.addBtn);
    	assertNotNull(add);
    	assertEquals(View.VISIBLE, add.getVisibility());
    	
    	TextView foodView = (TextView) activity.findViewById(R.id.tabFood);
    	TextView recipesView = (TextView) activity.findViewById(R.id.tabRecipes);
    	TextView settingsView = (TextView) activity.findViewById(R.id.tabSettings);
    	assertNotNull(foodView);
    	assertNotNull(recipesView);
    	assertNotNull(settingsView);
    }
    
    public void testStartOnFoodTab() {
    	launchIntent.putExtra(STARTING_TAB, "food");
    	startActivity(launchIntent, null, null);
    	ListActivity activity = getActivity();
    	assertNotNull(activity);
    	
    	assertTrue("foodTab should be true", MainActivity.foodTab);
    	
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
    
    public void testStartOnRecipeTab() {
    	launchIntent.putExtra(STARTING_TAB, "recipes");
    	startActivity(launchIntent, null, null);
    	ListActivity activity = getActivity();
    	assertNotNull(activity);
    	
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
    
    public void testStartOnSettingsTabWithMetric() {
    	launchIntent.putExtra(STARTING_TAB, "settings");
    	startActivity(launchIntent, null, null);
    	ListActivity activity = getActivity();
    	assertNotNull(activity);
    	
    	assertFalse("foodTab should be false", MainActivity.foodTab);
    	
    	TextView foodView = (TextView) activity.findViewById(R.id.tabFood);
    	TextView recipesView = (TextView) activity.findViewById(R.id.tabRecipes);
    	TextView settingsView = (TextView) activity.findViewById(R.id.tabSettings);
    	assertNotNull(foodView);
    	assertNotNull(recipesView);
    	assertNotNull(settingsView);
    	
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
    
    // TODO Check buttons
}
