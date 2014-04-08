package com.example.smart_fridge_android;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.Button;

public class AddRecipeActivityTest 
	extends ActivityUnitTestCase<AddRecipeActivity>{
	
	private static final String STARTING_TAB = "startingTab";
	Intent launchIntent;
	
	public AddRecipeActivityTest() {
		super(AddRecipeActivity.class);
	}

	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), AddRecipeActivity.class);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }
    
    public void testAddButtonGone() {
    	startActivity(launchIntent, null, null);
    	Activity activity = getActivity();
    	assertNotNull(activity);
    	
    	Button addButton = (Button) activity.findViewById(R.id.addBtn);
    	assertNotNull(addButton);
    	assertEquals("addButton should be not visible", 
    			View.INVISIBLE, addButton.getVisibility());
    }
    
    public void testNavButtons() {
    	startActivity(launchIntent, null, null);
    	Activity activity = getActivity();
    	assertNotNull(activity);
    	
    	NavigationBarTest.tabFoodTest(activity, this);
    	NavigationBarTest.tabRecipesTest(activity, this);
    	NavigationBarTest.tabSettingsTest(activity, this);
    	NavigationBarTest.logoutBtnTest(activity, this);
    }

    //TODO Write Tests
}
