package com.example.smart_fridge_android;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

public class RecipeTest extends AndroidTestCase{
	
	Recipe recipe;
	
	protected void setUp() throws Exception{
        super.setUp();
        recipe = new Recipe("Mac and Cheese", "Boil macaroni and add cheese",
        		"Don't add too much cheese", "Macaroni<b>Cheese");
        recipe.setId(0);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }

    public void testCreateUsingList() {
    	List<String> ingredientList = new ArrayList<String>();
    	ingredientList.add("Macaroni");
    	ingredientList.add("Cheese");
    	Recipe other = new Recipe("Mac and Cheese", "Boil macaroni and add cheese",
    			"Don't add too much cheese", ingredientList);
    	
    	// Should be equal to recipe
    	assertEquals(recipe, other);
    }
    
    public void testSetGetId() {
    	assertEquals(0, recipe.getId());
    	recipe.setId(1);
    	assertEquals(1, recipe.getId());
    }
    
    public void testSetGetName() {
    	assertEquals("Mac and Cheese", recipe.getName());
    	recipe.setName("Mac 'n Cheese");
    	assertEquals("Mac 'n Cheese", recipe.getName());
    }
    
    public void testSetGetDirections() {
    	assertEquals("Boil macaroni and add cheese", recipe.getDirections());
    	recipe.setDirections("Throw it in a pot");
    	assertEquals("Throw it in a pot", recipe.getDirections());
    }
    
    public void testSetGetNotes() {
    	assertEquals("Don't add too much cheese", recipe.getNotes());
    	recipe.setNotes("Overload on the cheese");
    	assertEquals("Overload on the cheese", recipe.getNotes());
    }

    public void testSetGetIngredientsString() {
    	assertEquals("Macaroni<b>Cheese", recipe.getIngredients());
    	recipe.setIngredients("Macaroni<b>Cheese<b>Water");
    	assertEquals("Macaroni<b>Cheese<b>Water", recipe.getIngredients());
    }
    
    public void testSetGetIngredientsList() {
    	List<String> ingredientList = new ArrayList<String>();
    	ingredientList.add("Macaroni");
    	ingredientList.add("Cheese");
    	assertEquals(ingredientList, recipe.getIngredientList());
    	
    	recipe.setIngredientsList(null);
    	assertEquals(null, recipe.getIngredientList());
    	
    	ingredientList.add("Water");
    	ingredientList.remove(0);
    	recipe.setIngredientsList(ingredientList);
    	assertEquals(ingredientList, recipe.getIngredientList());
    	assertEquals("Cheese<b>Water", recipe.getIngredients());
    }
    
    public void testToString() {
    	String recipeStr = "Recipe{id=0, name='Mac and Cheese', " +
    			"directions='Boil macaroni and add cheese', " +
    			"notes='Don't add too much cheese', " +
    			"ingredients='Macaroni<b>Cheese'}";
    	assertEquals(recipeStr, recipe.toString());
    }
    
    public void testEquals() {
    	// Equals to self
    	assertEquals(recipe,recipe);
    	
    	// Check null and non-food object params
    	assertFalse(recipe.equals(null));
    	assertFalse(recipe.equals("String test"));
    	
    	Recipe other = new Recipe("Mac and Cheese", 
    			"Boil macaroni and add cheese", "Don't add too much cheese", 
    			"Macaroni<b>Cheese");
        other.setId(0);
    	
    	// Check different Id
    	assertEquals(recipe, other);
    	other.setId(1);
    	assertFalse("recipe id should be different", recipe.equals(other));
    	recipe.setId(1);
    	
    	//Check different Directions, including null checks
    	assertEquals(recipe, other);
    	other.setDirections(null);
    	recipe.setDirections(null);
    	assertEquals(recipe, other);
    	other.setDirections("Throw in pot");
    	assertFalse("other Directions should be 'Throw in pot'", 
    			recipe.equals(other));
    	recipe.setDirections("Throw in pot");
    	assertEquals(recipe, other);
    	other.setDirections("Use laser eyes");
    	assertFalse("other Directions should be 'Use laser eyes'", 
    			recipe.equals(other));
    	recipe.setDirections("Use laser eyes");
    	
    	//Check different Ingredients, including null checks
    	assertEquals(recipe, other);
    	other.setIngredients(null);
    	recipe.setIngredients(null);
    	assertEquals(recipe, other);
    	other.setIngredients("Water");
    	assertFalse("other Ingredients should be 'Water'", 
    			recipe.equals(other));
    	recipe.setIngredients("Water");
    	assertEquals(recipe, other);
    	other.setIngredients("Water<b>Cheese");
    	assertFalse("other ingredients should be 'Water<b>Cheese'", 
    			recipe.equals(other));
    	recipe.setIngredients("Water<b>Cheese");
    	
    	//Check different name, including null checks
    	assertEquals(recipe, other);
    	other.setName(null);
    	recipe.setName(null);
    	assertEquals(recipe, other);
    	other.setName("Yellow noodles");
    	assertFalse("other Name should be 'Yellow noodles'", 
    			recipe.equals(other));
    	recipe.setName("Yellow noodles");
    	assertEquals(recipe, other);
    	other.setName("Mac 'n Cheese");
    	assertFalse("other Name should be 'Mac 'n Cheese'", 
    			recipe.equals(other));
    	recipe.setName("Mac 'n Cheese");
    	
    	//Check different Notes, including null checks
    	assertEquals(recipe, other);
    	other.setNotes(null);
    	recipe.setNotes(null);
    	assertEquals(recipe, other);
    	other.setNotes("Use water");
    	assertFalse("other Notes should be 'Use water'", 
    			recipe.equals(other));
    	recipe.setNotes("Use water");
    	assertEquals(recipe, other);
    	other.setNotes("Use laser eyes");
    	assertFalse("other Notes should be 'Use laser eyes'", 
    			recipe.equals(other));
    	recipe.setNotes("Use laser eyes");
    	assertEquals(recipe,other);
    }
}
