package com.example.smart_fridge_android;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import junit.framework.Assert;
import java.util.List;

public class DatabaseHandlerTest extends AndroidTestCase {

    private DatabaseHandler db;

    protected void setup() throws Exception{
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHandler(context);
        super.setUp();
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }

    public void testAddFood() throws Exception{
        setup();
        Food food = createFood();
        db.addFood(food);
        food = db.getAllFood().get(0);

        Assert.assertEquals(food, db.getFoodById(1));

        tearDown();
    }

    public void testGetFoodById() throws Exception{
        setup();
        Food originalFood = createFood();
        db.addFood(originalFood);

        originalFood = db.getAllFood().get(0);
        Food foodFromDb = db.getFoodById(originalFood.getId());

        Assert.assertEquals(foodFromDb, originalFood);
    }

    public void testGetAllFood() throws Exception{
        setup();
        Food food1 = createFood();
        Food food2 = createFood();
        food2.setName("Banana");
        db.addFood(food1);
        db.addFood(food2);

        List<Food> foodList = db.getAllFood();

        Assert.assertEquals(2, foodList.size());
        Assert.assertEquals("Apple", foodList.get(0).getName());
        Assert.assertEquals("Banana", foodList.get(1).getName());
    }

    public void testUpdateFood() throws Exception{
        setup();
        Food food = createFood();
        db.addFood(food);

        food.setName("Banana");
        db.updateFood(food);

        food = db.getAllFood().get(0);

        Assert.assertEquals(food, db.getFoodById(1));
    }

    public void testDeleteFood() throws Exception{
        setup();
        Food food = createFood();
        db.addFood(food);
        food = db.getAllFood().get(0);
        db.deleteFood(food);

        Assert.assertEquals(0, db.getAllFood().size());
    }


    public void testAddRecipe() throws Exception{
        setup();
        Recipe recipe = createRecipe();
        db.addRecipe(recipe);

        //Assert.assertEquals(recipe,);
    }

    // Food creation for test
    private Food createFood(){
        Food food = new Food();
        food.setName("Apple");
        food.setDescription("Red");
        food.setExpirationDate("12/21/14");
        food.setCategory("Fruit");
        food.setCalories(100);
        food.setQuantity(1);
        return food;
    }

    // Recipe creation for test
    private Recipe createRecipe(){
        Recipe recipe = new Recipe();
        recipe.setName("Macaroni and Cheese");
        recipe.setDirections("Boil macaroni and add cheese");
        recipe.setNotes("Don't add too much cheese");
        return recipe;
    }
}
