package com.example.smart_fridge_android;

import java.util.List;

import com.example.smart_fridge_android.AddFoodActivity.PerformUpcLookup;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddFoodActivityTest 
	extends ActivityUnitTestCase<AddFoodActivity>{
	
    private static final String STARTING_TAB = "startingTab";
	Intent launchIntent;
	AddFoodActivity activity;
    private int SCAN_REQUEST_CODE = 0;

	
	public AddFoodActivityTest() {
		super(AddFoodActivity.class);
	}

	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), AddFoodActivity.class);
        startActivity(launchIntent, null, null);
    	activity = getActivity();
    	assertNotNull(activity);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }
    
    public void testAddButtonGone() {
    	Button addButton = (Button) activity.findViewById(R.id.addBtn);
    	assertNotNull(addButton);
    	assertEquals("addButton should be not visible", 
    			View.INVISIBLE, addButton.getVisibility());
    }
    
    public void testDatePickerSelectionExecute() {
    	Button manualBtn = (Button)activity.findViewById(R.id.manualBtn);
    	assertNotNull(manualBtn);
    	
    	manualBtn.performClick();
    	
    	TextView dateSelector = (TextView)activity.findViewById(R.id.dateSelector);
    	assertNotNull(dateSelector);
    	assertTrue("dateSelector onClickListener needs to be set", 
    			dateSelector.hasOnClickListeners());
    	
    	// Replace the onClickListener to test onDateSet
    	dateSelector.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.onDateSet(null, 2014, 2, 3);
            }
        });
    	
    	assertTrue("dateSelector onClickListener needs to be set", 
    			dateSelector.hasOnClickListeners());
    	
    	// Click the selector and see if the date was set correctly.
    	dateSelector.performClick();
    	
    	// Months are offset by 1
    	assertEquals("dateSelector should be set to 03/03/2014", 
    			"03/03/2014", dateSelector.getText().toString());
    }
    
    public void testDatePickerSelectorPopup() {
    	Button manualBtn = (Button)activity.findViewById(R.id.manualBtn);
    	assertNotNull(manualBtn);
    	
    	manualBtn.performClick();
    	
    	TextView dateSelector = (TextView)activity.findViewById(R.id.dateSelector);
    	assertNotNull(dateSelector);
    	assertTrue("dateSelector onClickListener needs to be set", 
    			dateSelector.hasOnClickListeners());
    	
    	dateSelector.performClick();
    	// TODO Finish tests for dialog popup.
    }
    
    public void testManualAddValidFood() {
    	Button manualBtn = (Button)activity.findViewById(R.id.manualBtn);
    	assertNotNull(manualBtn);
    	
    	manualBtn.performClick();
    	
    	//Check name field
    	EditText nameField = (EditText)activity.findViewById(R.id.nameField);
    	assertNotNull(nameField);
    	assertTrue("Name should start blank", 
    			nameField.getText().toString().isEmpty());
    	nameField.setText("TESTAPPLETEST");
    	assertEquals("Name should have TESTAPPLETEST", "TESTAPPLETEST",
    			nameField.getText().toString());
    	
    	// Check Quantity field
    	EditText quantityField = (EditText)activity.findViewById(R.id.quantityField);
    	assertNotNull(quantityField);
    	assertTrue("Quantity should start blank", 
    			quantityField.getText().toString().isEmpty());
    	quantityField.setText("1");
    	assertEquals("Quantity should have 1", "1",
    			quantityField.getText().toString());
    	
    	// Check if dateSelector was set up.
    	TextView dateSelector = (TextView)activity.findViewById(R.id.dateSelector);
    	assertNotNull(dateSelector);
    	assertTrue("dateSelector onClickListener needs to be set", 
    			dateSelector.hasOnClickListeners());
    	assertTrue("Date should start blank",
    			dateSelector.getText().toString().isEmpty());
    	dateSelector.setText("1/1/15");
    	assertEquals("Date should have 1/1/15", "1/1/15",
    			dateSelector.getText().toString());
    	
    	EditText foodGroup = (EditText)activity.findViewById(R.id.foodgroupField);
    	assertNotNull(foodGroup);
    	assertTrue("food group should start blank",
    			foodGroup.getText().toString().isEmpty());
    	foodGroup.setText("Fruit");
    	assertEquals("food group should have Fruit", "Fruit",
    			foodGroup.getText().toString());
    	
    	Button addMan = (Button)activity.findViewById(R.id.addMan);
    	assertNotNull(addMan);
    	
    	// Note that this actually creates the food item in your application.
    	addMan.performClick();
    	
    	// Check if the item was created correctly.
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	List<Food> list = db.getAllFood();
    	Food addedItem = findFoodInList(list, "TESTAPPLETEST");
    	
    	assertNotNull(addedItem);
    	assertEquals(1, addedItem.getQuantity());
    	assertEquals("1/1/15", addedItem.getExpirationDate());
    	assertEquals("Fruit", addedItem.getCategory());
    	
    	// Need to delete the food again and check that it is gone
    	db.deleteFood(addedItem);
    	assertFalse(db.getAllFood().contains(addedItem));
    	
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
    
    public void testManualAddInvalidFood() {
    	Button manualBtn = (Button)activity.findViewById(R.id.manualBtn);
    	assertNotNull(manualBtn);
    	
    	manualBtn.performClick();
    	
    	Button addMan = (Button)activity.findViewById(R.id.addMan);
    	assertNotNull(addMan);
    	
    	// Test failure with blank name field
    	EditText nameField = (EditText)activity.findViewById(R.id.nameField);
    	assertNotNull(nameField);
    	assertTrue("Name should start blank", 
    			nameField.getText().toString().isEmpty());
    	addMan.performClick();
    	
    	// Make sure the food was not created.
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	List<Food> list = db.getAllFood();
    	Food addedItem = findFoodInList(list, "");
    	assertNull(addedItem);
    	
    	// Make sure intent was not sent
    	Intent addIntent = getStartedActivityIntent();
    	assertNull(addIntent);
    	
    	// Test failure with a name with tags
    	nameField.setText("Apple<br>");
    	assertEquals("Name should have Apple<br>", "Apple<br>", 
    			nameField.getText().toString());
    	addMan.performClick();
    	
    	// Make sure the food was not created
    	db = new DatabaseHandler(activity.getApplicationContext());
    	list = db.getAllFood();
    	addedItem = findFoodInList(list, "Apple<br>");
    	assertNull(addedItem);
    	
    	// Make sure intent was not sent
    	addIntent = getStartedActivityIntent();
    	assertNull(addIntent);
    }
    
    public void testUPCRequest() {
    	Intent intent = new Intent();
    	intent.putExtra("SCAN_RESULT", "077975025871");
    	try{
    		activity.onActivityResult(SCAN_REQUEST_CODE, 
    			Activity.RESULT_OK, intent);
    	} catch (BadTokenException e) {
    		// Trying to show the Dialog throws an exception.
    		// This is a well-known bug in tests
    		// https://code.google.com/p/android/issues/detail?id=14616
    	}
    	
    	// make a get call and check if the correct value returns
    	// We're using a Pretzels, Nibbler, Sourdough bag to test
    	PerformUpcLookup lookup = activity.new PerformUpcLookup();
    	activity.upcResult = "077975025871";
    	//TODO fix test
    	//assertEquals("Pretzels, Nibblers, Sourdough", lookup.doInBackground());
    }
    
    private Food findFoodInList(List<Food> list, String name) {
    	Food addedItem = null;
    	for(int i = 0; i < list.size() && addedItem == null; i++) {
    		if(list.get(i).getName().equals(name)) {
    			addedItem = list.get(i);
    		}
    	}
    	return addedItem;
    }
    
    public void testNavButtons() {
    	NavigationBarTest.tabFoodTest(activity, this);
    	NavigationBarTest.tabRecipesTest(activity, this);
    	NavigationBarTest.tabSettingsTest(activity, this);
    	NavigationBarTest.logoutBtnTest(activity, this);
    }
}
