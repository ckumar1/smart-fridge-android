package com.example.smart_fridge_android;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.AndroidTestCase;
import android.widget.Button;
import android.widget.TextView;

public class NavigationBarTest extends AndroidTestCase{
	
    private static final String STARTING_TAB = "startingTab";

	protected void setUp() throws Exception{
        super.setUp();
        //TODO Write Set up code
    }

    protected void tearDown() throws Exception{
        super.tearDown();
        //TODO Write tearDown code
    }

    //TODO Write Tests
    
    /* Test whether food tab works */
    static public void tabFoodTest(Activity activity, ActivityUnitTestCase<?> current) {
    	TextView tabFood = (TextView)activity.findViewById(R.id.tabFood);
    	assertNotNull(tabFood);
    	
		tabFood.performClick();
    	// Check for correct tabfood Intent params
		Intent tabFoodIntent = current.getStartedActivityIntent();
    	assertNotNull(tabFoodIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",tabFoodIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			tabFoodIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to food", 
    			"food", tabFoodIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Clear top flag should be set",
    			Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_NEW_TASK, tabFoodIntent.getFlags());
    }

    /* Test whether recipes tab works */
	public static void tabRecipesTest(Activity activity, ActivityUnitTestCase<?> current) {
    	TextView tabRecipes = (TextView)activity.findViewById(R.id.tabRecipes);
    	assertNotNull(tabRecipes);

    	tabRecipes.performClick();
    	
    	// Check for correct tabRecipe Intent params
    	Intent tabRecipeIntent =  current.getStartedActivityIntent();
    	assertNotNull(tabRecipeIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",tabRecipeIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			tabRecipeIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to recipes", 
    			"recipes", tabRecipeIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Clear top flag should be set",
                Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_NEW_TASK, tabRecipeIntent.getFlags());
	}

	/* Test whether settings tab works */
	public static void tabSettingsTest(Activity activity, ActivityUnitTestCase<?> current) {
    	TextView tabSettings = (TextView)activity.findViewById(R.id.tabSettings);
    	assertNotNull(tabSettings);

    	tabSettings.performClick();
    	
    	// Check for correct tabSettings Intent params
    	Intent tabSettingsIntent = current.getStartedActivityIntent();
    	assertNotNull(tabSettingsIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",tabSettingsIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			tabSettingsIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to settings", 
    			"settings", tabSettingsIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Intent.FLAG_ACTIVITY_CLEAR_TOP should be set",
                Intent.FLAG_ACTIVITY_CLEAR_TOP + Intent.FLAG_ACTIVITY_NEW_TASK, tabSettingsIntent.getFlags());
	}

	/* Test whether logout button works */
	public static void logoutBtnTest(Activity activity, ActivityUnitTestCase<?> current) {
		Button logoutBtn = (Button)activity.findViewById(R.id.logoutBtn);

    	assertNotNull(logoutBtn);
    	
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
	
	/* Test whether add button works */
	public static void addBtnTest(Activity activity, ActivityUnitTestCase<?> current) {
		// TODO
	}
}
