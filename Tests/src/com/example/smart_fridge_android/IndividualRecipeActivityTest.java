package com.example.smart_fridge_android;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.TextView;

public class IndividualRecipeActivityTest 
	extends ActivityUnitTestCase<IndividualRecipeActivity>{
	
	private static final String STARTING_TAB = "startingTab";
	Intent launchIntent;
	Activity activity;
	Recipe testRecipe;
	
	public IndividualRecipeActivityTest() {
		super(IndividualRecipeActivity.class);
	}

	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), IndividualFoodActivity.class);
        
        // Add a test recipe
        DatabaseHandler db = new DatabaseHandler(getInstrumentation()
        		.getTargetContext());
        testRecipe = db.addRecipe(new Recipe("TESTMACNCHEESETEST", 
        		"Boil noodles. Add cheese", "Add lots of cheese", 
        		"macaroni<b>cheese", ""));
        assertNotNull(testRecipe);
        
        launchIntent.putExtra("rid", Integer.toString(testRecipe.getId()));
        startActivity(launchIntent, null, null);
    	activity = getActivity();
    	assertNotNull(activity);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
        DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	if(db.getRecipeById(testRecipe.getId()) != null) {
    		db.deleteRecipe(testRecipe);
    	}
    	assertNull(db.getRecipeById(testRecipe.getId()));
    }

    public void testViewRecipe() {
    	TextView nameText = (TextView) activity.findViewById(R.id.IndRecipeNameField);
        TextView instructionsText = (TextView) activity.findViewById(R.id.IndRecipeInstructionsField);
        TextView ingredientsText = (TextView) activity.findViewById(R.id.IndRecipeIngredientsField);
        assertNotNull(nameText);
        assertNotNull(instructionsText);
        assertNotNull(ingredientsText);
       

        // Check that the text fields are all showing the correct things
        assertEquals("nameText should be TESTMACNCHEESETEST", 
        		"TESTMACNCHEESETEST", nameText.getText().toString());
        assertEquals("instructionsText should be Boil noodles. Add cheese", 
        		"Boil noodles. Add cheese", instructionsText.getText().toString());
        assertEquals("ingredientText should be macaroni\ncheese", 
        		"macaroni\ncheese", ingredientsText.getText().toString());
    
        
        deleteTestRecipe();
    }
    
    public void testDeleteRecipe() {
    	Button deleteButton = (Button) activity.findViewById(R.id.IndRecipeDeleteButton);
    	assertNotNull(deleteButton);

    	deleteButton.performClick();

    	// Check that the recipe was actually deleted
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	assertNull(db.getRecipeById(testRecipe.getId()));

    	// Check that the correct Intent was created
    	Intent addIntent = getStartedActivityIntent();
    	assertNotNull(addIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",addIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			addIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to recipes", 
    			"recipes", addIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Clear top flag should be set", 
    			Intent.FLAG_ACTIVITY_CLEAR_TOP, addIntent.getFlags());
    }
    
    public void testOpenIMadeThis() {
    	Button iMadeThisButton = (Button) activity.findViewById(R.id.IMadeThisButton);
    	assertNotNull(iMadeThisButton);
    	iMadeThisButton.performClick();
    	
    	// TODO add more when IMadeThis is finished
    	deleteTestRecipe();
    }
    
    private void checkOldRecipeValues() {
    	// Check that the old values are in testRecipe
    	assertEquals("TESTMACNCHEESETEST", testRecipe.getName());
    	assertEquals("Boil noodles. Add cheese", testRecipe.getDirections());
    	assertEquals("Add lots of cheese", testRecipe.getNotes());
    	assertEquals("macaroni<b>cheese", testRecipe.getIngredients());
    }

    public void testNavButtons() {
    	NavigationBarTest.tabFoodTest(activity, this);
    	NavigationBarTest.tabRecipesTest(activity, this);
    	NavigationBarTest.tabSettingsTest(activity, this);
    	NavigationBarTest.logoutBtnTest(activity, this);
    	NavigationBarTest.addBtnTest(activity, this);
    	deleteTestRecipe();
    }

    /*  Delete the added recipe. This is done outside tearDown so that
        we can test the delete button as well. Delete also done inside teardown
        so that the recipe is always deleted */
    private void deleteTestRecipe() {
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());

    	// Check to make sure the recipe is still in the database
    	assertNotNull(db.getRecipeById(testRecipe.getId()));

    	// Delete the recipe and check to make sure the recipe is gone
    	db.deleteRecipe(testRecipe);
    	assertNull(db.getRecipeById(testRecipe.getId()));
    }
}
