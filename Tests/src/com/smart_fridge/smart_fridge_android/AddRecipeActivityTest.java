package com.smart_fridge.smart_fridge_android;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddRecipeActivityTest 
	extends ActivityUnitTestCase<AddRecipeActivity>{
	
	private static final String STARTING_TAB = "startingTab";
	Intent launchIntent;
	Activity activity;
	
	public AddRecipeActivityTest() {
		super(AddRecipeActivity.class);
	}

	protected void setUp() throws Exception{
        super.setUp();
        launchIntent = new Intent(getInstrumentation()
                .getTargetContext(), AddRecipeActivity.class);
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
    
    public void testNavButtons() {
    	NavigationBarTest.tabFoodTest(activity, this);
    	NavigationBarTest.tabRecipesTest(activity, this);
    	NavigationBarTest.tabSettingsTest(activity, this);
    	NavigationBarTest.logoutBtnTest(activity, this);
    }

    public void testViewIngredientList() {
    	Button addIngred = (Button)activity.findViewById(R.id.addingred);
    	assertNotNull(addIngred);
    	
    	EditText ingredientField = (EditText)activity.findViewById(R.id.IngredientField);
    	assertNotNull(ingredientField);
    	assertTrue("Ingredient should start blank", 
    			ingredientField.getText().toString().isEmpty());
    	ingredientField.setText("macaroni");
    	assertEquals("Ingredient should have macaroni", "macaroni",
    			ingredientField.getText().toString());
    	
    	// Add the ingredient and check that the field is blank
    	addIngred.performClick();
    	assertTrue("Ingredient should be blank again", 
    			ingredientField.getText().toString().isEmpty());
    	
    	Button showList = (Button)activity.findViewById(R.id.showlist);
    	assertNotNull(showList);
    	try {
    		showList.performClick();
    	} catch (IllegalStateException e) { 
    		// Trying to show the AlertDialog throws an exception.
    		// This is a well-known bug 
    		// https://code.google.com/p/android/issues/detail?id=14616
    	}
    	// Incomplete
    	// I cannot access the elements of AlertDialog.Builder, nor see if it shows
    }
    
    public void testAddInvalidRecipeName() {
    	Button addRecipe = (Button)activity.findViewById(R.id.addrec);
    	assertNotNull(addRecipe);
    	
    	// Test failure with blank name field
    	EditText nameField = (EditText)activity.findViewById(R.id.RecipeNameField);
    	assertNotNull(nameField);
    	assertTrue("Name should start blank", 
    			nameField.getText().toString().isEmpty());
    	addRecipe.performClick();
    	
    	// Make sure the recipe was not created.
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	List<Recipe> list = db.getAllRecipes();
    	Recipe addedItem = findRecipeInList(list, "");
    	assertNull(addedItem);
    	
    	// Make sure intent was not sent
    	Intent addIntent = getStartedActivityIntent();
    	assertNull(addIntent);
    	
    	// Test failure with a name with tags
    	nameField.setText("Food<br>");
    	assertEquals("Name should have Food<br>", "Food<br>",
    			nameField.getText().toString());
    	addRecipe.performClick();
    	
    	// Make sure the recipe was not created.
    	db = new DatabaseHandler(activity.getApplicationContext());
    	list = db.getAllRecipes();
    	addedItem = findRecipeInList(list, "Food<br>");
    	assertNull(addedItem);
    	
    	// Make sure intent was not sent
    	addIntent = getStartedActivityIntent();
    	assertNull(addIntent);
    }
    
    public void testAddInvalidRecipeDirections() {
    	Button addRecipe = (Button)activity.findViewById(R.id.addrec);
    	assertNotNull(addRecipe);
    	
    	EditText nameField = (EditText)activity.findViewById(R.id.RecipeNameField);
    	assertNotNull(nameField);
    	nameField.setText("TESTNAMETEST");
    	assertEquals("Name should have TESTNAMETEST", 
    			"TESTNAMETEST", nameField.getText().toString());
    	
    	// Test failure with blank name field
    	EditText descriptionField = (EditText)activity.findViewById(R.id.InstructionsField);
    	assertNotNull(descriptionField);
    	assertTrue("Description should start blank", 
    			descriptionField.getText().toString().isEmpty());
    	addRecipe.performClick();
    	
    	// Make sure the recipe was not created.
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	List<Recipe> list = db.getAllRecipes();
    	Recipe addedItem = findRecipeInList(list, "TESTNAMETEST");
    	assertNull(addedItem);
    	
    	// Make sure intent was not sent
    	Intent addIntent = getStartedActivityIntent();
    	assertNull(addIntent);
    }
    
    public void testAddInvalidRecipeIngredients() {
    	Button addRecipe = (Button)activity.findViewById(R.id.addrec);
    	assertNotNull(addRecipe);
    	
    	EditText nameField = (EditText)activity.findViewById(R.id.RecipeNameField);
    	assertNotNull(nameField);
    	nameField.setText("TESTNAMETEST");
    	assertEquals("Name should have TESTNAMETEST", 
    			"TESTNAMETEST", nameField.getText().toString());
    	
    	// Test failure with blank name field
    	EditText descriptionField = (EditText)activity.findViewById(R.id.InstructionsField);
    	assertNotNull(descriptionField);
    	descriptionField.setText("Boil");
    	assertEquals("Description should have Boil", 
    			"Boil", descriptionField.getText().toString());
    	
    	// Should not add the ingredient
    	Button addIngrBtn = (Button)activity.findViewById(R.id.addingred);
    	assertNotNull(addIngrBtn);
    	
    	EditText ingredientField = (EditText)activity.findViewById(R.id.IngredientField);
    	assertNotNull(ingredientField);
    	assertTrue("Ingredient should start blank", 
    			ingredientField.getText().toString().isEmpty());
    	
    	// Add the blank ingredient and add the recipe
    	// Should not add the ingredient, and thus will not add the recipe
    	addIngrBtn.performClick();
    	addRecipe.performClick();
    	
    	// Make sure the recipe was not created.
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	List<Recipe> list = db.getAllRecipes();
    	Recipe addedItem = findRecipeInList(list, "TESTNAMETEST");
    	assertNull(addedItem);
    	
    	// Make sure intent was not sent
    	Intent addIntent = getStartedActivityIntent();
    	assertNull(addIntent);
    }
    
    public void testAddValidRecipe() {
    	//Check name field
    	EditText nameField = (EditText)activity.findViewById(R.id.RecipeNameField);
    	assertNotNull(nameField);
    	assertTrue("Name should start blank", 
    			nameField.getText().toString().isEmpty());
    	nameField.setText("TESTMACNCHEESETEST");
    	assertEquals("Name should have TESTMACNCHEESETEST", "TESTMACNCHEESETEST",
    			nameField.getText().toString());
    	
    	//Check Directions field
    	EditText directionsField = (EditText)activity.findViewById(R.id.InstructionsField);
    	assertNotNull(directionsField);
    	assertTrue("Directions should start blank", 
    			directionsField.getText().toString().isEmpty());
    	directionsField.setText("Boil noodles and add cheese");
    	assertEquals("Name should have Boil noodles and add cheese", 
    			"Boil noodles and add cheese", directionsField.getText().toString());
    	
    	//Check ingredient button
    	Button addIngrBtn = (Button)activity.findViewById(R.id.addingred);
    	assertNotNull(addIngrBtn);
    	
    	// Check Ingredient field
    	EditText ingredientField = (EditText)activity.findViewById(R.id.IngredientField);
    	assertNotNull(ingredientField);
    	assertTrue("Ingredient should start blank", 
    			ingredientField.getText().toString().isEmpty());
    	ingredientField.setText("macaroni");
    	assertEquals("Ingredient should have macaroni", "macaroni",
    			ingredientField.getText().toString());
    	
    	// Add the ingredient and check that the field is blank
    	addIngrBtn.performClick();
    	assertTrue("Ingredient should be blank again", 
    			ingredientField.getText().toString().isEmpty());
    	
    	// Add another ingredient
    	ingredientField.setText("cheese");
    	assertEquals("Ingredient should have cheese", "cheese",
    			ingredientField.getText().toString());
    	addIngrBtn.performClick();
    	assertTrue("Ingredient should be blank again", 
    			ingredientField.getText().toString().isEmpty());
    	
    	Button addRecipe = (Button)activity.findViewById(R.id.addrec);
    	assertNotNull(addRecipe);
    	
    	// Note that this actually creates the recipe item in your application
    	addRecipe.performClick();
    	
    	// Check if the item was created correctly
    	DatabaseHandler db = new DatabaseHandler(activity.getApplicationContext());
    	List<Recipe> list = db.getAllRecipes();
    	Recipe addedItem = findRecipeInList(list, "TESTMACNCHEESETEST");
    	
    	assertNotNull(addedItem);
    	assertEquals("Boil noodles and add cheese", addedItem.getDirections());
    	assertEquals("macaroni\ncheese", addedItem.getIngredients());
    	
    	// Need to delete the food again and check that it is gone
    	db.deleteRecipe(addedItem);
    	assertFalse(db.getAllRecipes().contains(addedItem));
    	
    	// Check that the correct Intent was created
    	Intent addIntent = getStartedActivityIntent();
    	assertNotNull(addIntent);
    	assertEquals(".MainActivity class should be set in Intent",
    			".MainActivity",addIntent.getComponent().getShortClassName());
    	assertTrue("Starting tab extra should be set", 
    			addIntent.hasExtra(STARTING_TAB));
    	assertEquals("Starting tab should be set to recipe", 
    			"recipes", addIntent.getStringExtra(STARTING_TAB));
    	assertEquals("Clear top flag should be set", 
    			Intent.FLAG_ACTIVITY_CLEAR_TOP, addIntent.getFlags());
    }
    
    private Recipe findRecipeInList(List<Recipe> list, String name) {
    	Recipe addedItem = null;
    	for(int i = 0; i < list.size() && addedItem == null; i++) {
    		if(list.get(i).getName().equals(name)) {
    			addedItem = list.get(i);
    		}
    	}
    	return addedItem;
    }
}
