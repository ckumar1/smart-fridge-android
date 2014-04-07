package com.example.smart_fridge_android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.TextView;

public class AddFoodActivityTest 
	extends ActivityUnitTestCase<AddFoodActivity>{
	
    private static final String STARTING_TAB = "startingTab";
	Intent launchIntent;
	
	public AddFoodActivityTest() {
		super(AddFoodActivity.class);
	}

	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), AddFoodActivity.class);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }

    public void testPreconditions() {
    	startActivity(launchIntent, null, null);
    	Activity activity = getActivity();
    	assertNotNull(activity);
    }
    
    public void testNavButtons() {
    	startActivity(launchIntent, null, null);
    	Activity activity = getActivity();
    	TextView tabFood = (TextView)activity.findViewById(R.id.tabFood);
    	TextView tabRecipes = (TextView)activity.findViewById(R.id.tabRecipes);
    	TextView tabSettings = (TextView)activity.findViewById(R.id.tabSettings);
    	Button logoutBtn = (Button)activity.findViewById(R.id.logoutBtn);
    	
    	tabFood.performClick();
    	
    	// Check for correct tabfood Intent params
    	Intent tabFoodIntent = getStartedActivityIntent();
    	assertNotNull(tabFoodIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",tabFoodIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			tabFoodIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to food", 
    			"food", tabFoodIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Clear top flag should be set", 
    			Intent.FLAG_ACTIVITY_CLEAR_TOP, tabFoodIntent.getFlags());

    	tabRecipes.performClick();
    	
    	// Check for correct tabRecipe Intent params
    	Intent tabRecipeIntent = getStartedActivityIntent();
    	assertNotNull(tabRecipeIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",tabRecipeIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			tabRecipeIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to recipes", 
    			"recipes", tabRecipeIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Clear top flag should be set", 
    			Intent.FLAG_ACTIVITY_CLEAR_TOP, tabRecipeIntent.getFlags());
    	
    	tabSettings.performClick();
    	
    	// Check for correct tabSettings Intent params
    	Intent tabSettingsIntent = getStartedActivityIntent();
    	assertNotNull(tabSettingsIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",tabSettingsIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			tabSettingsIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to settings", 
    			"settings", tabSettingsIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Intent.FLAG_ACTIVITY_CLEAR_TOP should be set", 
    			Intent.FLAG_ACTIVITY_CLEAR_TOP, tabSettingsIntent.getFlags());

    	/*
    	 * TODO Figure out how to test add and logout buttons
    	logoutBtn.performClick();
    	
    	//Check for correct logout function
    	//TODO check session manager?
    	Intent logoutIntent = getApplicationContext().getStartedActivityIntent();
    	assertFalse(logoutIntent.equals(tabSettingsIntent));
    	assertNotNull(logoutIntent);
    	assertEquals(".LoginActivity class should be set in Intent",
    			".LoginActivity", logoutIntent.getComponent().getShortClassName());
    	assertEquals("Intent.FLAG_ACTIVIT_NEW_TASK should be set", 
    			Intent.FLAG_ACTIVITY_NEW_TASK, logoutIntent.getFlags());
    */
    }
}
