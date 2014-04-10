package com.example.smart_fridge_android;

import android.test.AndroidTestCase;
import android.test.MoreAsserts;

public class FoodTest extends AndroidTestCase{
	
	Food food;
	
	protected void setUp() throws Exception{
        super.setUp();
        food = new Food("Apple", "Red", "12/21/14", "Fruit", "", 100, 1);
        food.setId(0);
	}

    protected void tearDown() throws Exception{
        super.tearDown();
    }

    public void testSetGetId() {
    	assertEquals(0, food.getId());
    	food.setId(1);
    	assertEquals(1, food.getId());
    }
    
    public void testSetGetName() {
    	assertEquals("Apple", food.getName());
    	food.setName("Banana");
    	assertEquals("Banana", food.getName());
    }
    
    public void testSetGetDescription() {
    	assertEquals("Red", food.getDescription());
    	food.setDescription("Yellow");
    	assertEquals("Yellow", food.getDescription());
    }
    
    public void testSetGetExpirationDate() {
    	assertEquals("12/21/14", food.getExpirationDate());
    	food.setExpirationDate("11/15/15");
    	assertEquals("11/15/15", food.getExpirationDate());
    }
    
    public void testSetGetCategory() {
    	assertEquals("Fruit", food.getCategory());
    	food.setCategory("Melon");
    	assertEquals("Melon", food.getCategory());
    }
    
    public void testSetGetCalories() {
    	assertEquals(100, food.getCalories());
    	food.setCalories(9000); // Not quite over 9000
    	assertEquals(9000, food.getCalories());
    }
    
    public void testSetGetQuantity() {
    	assertEquals(1, food.getQuantity());
    	food.setQuantity(10);
    	assertEquals(10, food.getQuantity());
    }
    
    public void testToString() {
    	//TODO
    	/*
    	String foodStr = "Food{id=0, name='Apple', description='Red', " +
    			"category='Fruit', expirationDate='12/21/14', " +
    			"calories=100, quantity=1}";
    	assertEquals(foodStr, food.toString());
    	*/
    }
    
    public void testEquals() {
    	// Equals to self
    	assertEquals(food,food);
    	
    	// Check null and non-food object params
    	MoreAsserts.assertNotEqual(null,food);
    	MoreAsserts.assertNotEqual("String test", food);
    	
    	Food other = new Food("Apple", "Red", "12/21/14", "Fruit", "", 100, 1);
    	other.setId(0);
    	
    	// Check different Id
    	assertEquals(food, other);
    	other.setId(1);
    	MoreAsserts.assertNotEqual("food id should be different", 
    			other, food);
    	food.setId(1);
    	
    	// Check different Calories
    	assertEquals(food, other);
    	other.setCalories(200);
    	MoreAsserts.assertNotEqual("food calories should be different", 
    			other, food);
    	food.setCalories(200);
    	
    	// Check different Quantity
    	assertEquals(food, other);
    	other.setQuantity(2);
    	MoreAsserts.assertNotEqual("food quantity should be different", 
    			other, food);
    	food.setQuantity(2);
    	
    	//Check different Category, including null checks
    	assertEquals(food, other);
    	other.setCategory(null);
    	food.setCategory(null);
    	assertEquals(food, other);
    	other.setCategory("Fruit");
    	MoreAsserts.assertNotEqual("other Category should be Fruit", 
    			other, food);
    	food.setCategory("Fruit");
    	assertEquals(food, other);
    	other.setCategory("Melon");
    	MoreAsserts.assertNotEqual("other Category should be Melon", 
    			other, food);
    	food.setCategory("Melon");
    	
    	//Check different Description, including null checks
    	assertEquals(food, other);
    	other.setDescription(null);
    	food.setDescription(null);
    	assertEquals(food, other);
    	other.setDescription("Blue");
    	MoreAsserts.assertNotEqual("other Description should be Blue", 
    			other, food);
    	food.setDescription("Blue");
    	assertEquals(food, other);
    	other.setDescription("Red");
    	MoreAsserts.assertNotEqual("other Description should be Red", 
    			other, food);
    	food.setDescription("Red");
    	
    	//Check different expiration date, including null checks
    	assertEquals(food, other);
    	other.setExpirationDate(null);
    	food.setExpirationDate(null);
    	assertEquals(food, other);
    	other.setExpirationDate("1/1/00");
    	MoreAsserts.assertNotEqual("other ExpirationDate should be 1/1/00", 
    			other, food);
    	food.setExpirationDate("1/1/00");
    	assertEquals(food, other);
    	other.setExpirationDate("1/1/99");
    	MoreAsserts.assertNotEqual("other ExpirationDate should be 1/1/99", 
    			other, food);
    	food.setExpirationDate("1/1/99");
    	
    	//Check different name, including null checks
    	assertEquals(food, other);
    	other.setName(null);
    	food.setName(null);
    	assertEquals(food, other);
    	other.setName("Apple");
    	MoreAsserts.assertNotEqual("other Name should be Apple", 
    			other, food);
    	food.setName("Apple");
    	assertEquals(food, other);
    	other.setName("Banana");
    	MoreAsserts.assertNotEqual("other Name should be Banana", 
    			other, food);
    	food.setName("Banana");
    	assertEquals(food, other);
    }
}
