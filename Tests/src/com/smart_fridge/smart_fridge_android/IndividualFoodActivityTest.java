package com.smart_fridge.smart_fridge_android;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.TextView;

public class IndividualFoodActivityTest 
	extends ActivityUnitTestCase<IndividualFoodActivity>{
	
	private static final String STARTING_TAB = "startingTab";
	Intent launchIntent;
	Activity activity;
	Food testFood;
	
	public IndividualFoodActivityTest() {
		super(IndividualFoodActivity.class);
	}

	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), IndividualFoodActivity.class);
        
        // Add a test food
        DatabaseHandler db = new DatabaseHandler(getInstrumentation()
        		.getTargetContext());
        testFood = db.addFood(new Food("TESTAPPLETEST", "Red", "1/1/15", 
        		"Fruit", "", 100, 1));
        assertNotNull(testFood);
        
        launchIntent.putExtra("fid", Integer.toString(testFood.getId()));
        startActivity(launchIntent, null, null);
    	activity = getActivity();
    	assertNotNull(activity);
	}

    protected void tearDown() throws Exception{
        super.tearDown();
        DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	if(db.getFoodById(testFood.getId()) != null) {
    		db.deleteFood(testFood);
    	}
    	assertNull(db.getFoodById(testFood.getId()));
    }
    
    public void testViewFood() {
        TextView nameText = (TextView) activity.findViewById(R.id.individualFoodItemNameEditText);
        TextView quantityText = (TextView) activity.findViewById(R.id.individualFoodItemQuantityEditText);
        TextView expDateText = (TextView) activity.findViewById(R.id.individualFoodItemExpirationDateSpinner);
        TextView nutriInfoText = (TextView) activity.findViewById(R.id.individualFoodItemNutritionalInfoEditText);
        assertNotNull(nameText);
        assertNotNull(quantityText);
        assertNotNull(expDateText);
        assertNotNull(nutriInfoText);

        // Check that the text fields are all showing the correct things
        assertEquals("nameText should be TESTAPPLETEST", 
        		"TESTAPPLETEST", nameText.getText().toString());
        assertEquals("quantityText should be 1", "1", 
        		quantityText.getText().toString());
        assertEquals("expDateText should be 1/1/15", "1/1/15",
        		expDateText.getText().toString());
        // TODO will likely change as the code is revised
        assertEquals("nutriInfoText should be Red", "Red",
        		nutriInfoText.getText().toString());
        
        assertTrue(expDateText.hasOnClickListeners());
        
        deleteTestFood();
    }

    public void testDeleteFood() {
    	Button deleteButton = (Button) activity.findViewById(R.id.btnDeleteIndividualFoodItem);
    	assertNotNull(deleteButton);

    	deleteButton.performClick();

    	// Check that the food was actually deleted
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	assertNull(db.getFoodById(testFood.getId()));

    	// Check that the correct Intent was created
    	Intent addIntent = getStartedActivityIntent();
    	assertNotNull(addIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",addIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			addIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to food", 
    			"food", addIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Clear top flag should be set", 
    			Intent.FLAG_ACTIVITY_CLEAR_TOP, addIntent.getFlags());
    }
    
    public void testValidUpdateFood() {
    	TextView nameText = (TextView) activity.findViewById(R.id.individualFoodItemNameEditText);
        assertNotNull(nameText);
        nameText.setText("TESTBANANATEST");
        assertEquals("nameText should be the new value TESTBANANATEST",
        		"TESTBANANATEST", nameText.getText().toString());
        
    	TextView quantityText = (TextView) activity.findViewById(R.id.individualFoodItemQuantityEditText);
        assertNotNull(quantityText);
    	quantityText.setText("2");
    	assertEquals("quantityText should be the new value 2", "2",
    			quantityText.getText().toString());
        
        TextView expDateText = (TextView) activity.findViewById(R.id.individualFoodItemExpirationDateSpinner);
        assertNotNull(expDateText);
        expDateText.setText("2/2/22");
        assertEquals("expDateText should be the new value 2/2/22", "2/2/22", 
        		expDateText.getText().toString());
        
        TextView nutriInfoText = (TextView) activity.findViewById(R.id.individualFoodItemNutritionalInfoEditText);
        assertNotNull(nutriInfoText);
        nutriInfoText.setText("Yellow");
        assertEquals("nutriInfoText should be the new value Yellow", 
        		"Yellow", nutriInfoText.getText().toString());
        
        // Perform the update
        Button updateButton = (Button) activity.findViewById(R.id.btnUpdateIndividualFoodItem);
        assertNotNull(updateButton);
        updateButton.performClick();
        
        DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
        testFood = db.getFoodById(testFood.getId());
        assertNotNull(testFood);
        
        // Check that the correct values are in testFood
        assertEquals("TESTBANANATEST", testFood.getName());
        assertEquals(2, testFood.getQuantity());
        assertEquals("2/2/22", testFood.getExpirationDate());
        assertEquals("Yellow", testFood.getDescription());
        
        deleteTestFood();
    }
    
    public void testInvalidUpdateFood() {
    	Button updateButton = (Button) activity.findViewById(R.id.btnUpdateIndividualFoodItem);
        assertNotNull(updateButton);
        
        // Test failure with blank name field
    	TextView nameText = (TextView) activity.findViewById(R.id.individualFoodItemNameEditText);
        assertNotNull(nameText);
        nameText.setText("");
        assertEquals("nameText should be empty",
        		"", nameText.getText().toString());
        
        updateButton.performClick();
        
        DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
        testFood = db.getFoodById(testFood.getId());
        assertNotNull(testFood);
        
        // Check that the values of testFood did not change
        checkOldFoodValues();
        
        // Test failure with a name with tags
    	nameText.setText("Apple<br>");
    	assertEquals("Name should have Apple<br>", "Apple<br>", 
    			nameText.getText().toString());
    	
    	updateButton.performClick();
    	
    	db = new DatabaseHandler(activity.getApplicationContext());
        testFood = db.getFoodById(testFood.getId());
        assertNotNull(testFood);
        
        // Check that the values of testFood did not change
        checkOldFoodValues();
        
        deleteTestFood();
    }
    
    private void checkOldFoodValues() {
    	// Check that the old values are in testFood
        assertEquals("TESTAPPLETEST", testFood.getName());
        assertEquals(1, testFood.getQuantity());
        assertEquals("1/1/15", testFood.getExpirationDate());
        assertEquals("Red", testFood.getDescription());
    }
    
    public void testNavButtons() {
    	NavigationBarTest.tabFoodTest(activity, this);
    	NavigationBarTest.tabRecipesTest(activity, this);
    	NavigationBarTest.tabSettingsTest(activity, this);
    	NavigationBarTest.logoutBtnTest(activity, this);
    	NavigationBarTest.addBtnTest(activity, this);
    	deleteTestFood();
    }
    
    /*  Delete the added food. This is done outside tearDown so that
        we can test the delete button. We also delete in teardown if we
        need to */
    private void deleteTestFood() {
        DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
        
        // Check to make sure the food is still in the database
        assertNotNull(db.getFoodById(testFood.getId()));
        
        // Delete the food and check to make sure the food is gone
        db.deleteFood(testFood);
        assertNull(db.getFoodById(testFood.getId()));
    }
}
