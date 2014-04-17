package com.smart_fridge.smart_fridge_android;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import java.util.List;

public class DatabaseHandlerTest extends AndroidTestCase {

    private DatabaseHandler db;

    protected void setUp() throws Exception{
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHandler(context);
    }

    protected void tearDown() throws Exception{
        super.tearDown();
    }

    public void testAddFood() throws Exception{
        Food food = createFood();

        food = db.addFood(food);

        assertEquals(food, db.getAllFood().get(0));
    }

    public void testGetFoodById() throws Exception{
        Food originalFood = createFood();
        db.addFood(originalFood);

        originalFood = db.getAllFood().get(0);
        Food foodFromDb = db.getFoodById(originalFood.getId());

        assertEquals(foodFromDb, originalFood);
    }

    public void testGetAllFood() throws Exception{
        Food food1 = createFood();
        Food food2 = createFood();
        food2.setName("Banana");
        db.addFood(food1);
        db.addFood(food2);

        List<Food> foodList = db.getAllFood();

        assertEquals(2, foodList.size());
        assertEquals("Apple", foodList.get(0).getName());
        assertEquals("Banana", foodList.get(1).getName());
    }

    public void testUpdateFood() throws Exception{
        Food food = createFood();
        db.addFood(food);

        food.setName("Banana");
        db.updateFood(food);

        food = db.getAllFood().get(0);

        assertEquals(food, db.getFoodById(1));
    }

    public void testDeleteFood() throws Exception{
        Food food = createFood();
        db.addFood(food);
        food = db.getAllFood().get(0);
        db.deleteFood(food);

        assertEquals(0, db.getAllFood().size());
    }


    public void testAddRecipe() throws Exception{
        Recipe recipe = createRecipe();
        recipe = db.addRecipe(recipe);

        assertEquals(recipe, db.getAllRecipes().get(0));
    }

    public void testGetRecipeById() throws Exception {
        Recipe recipe = createRecipe();

        recipe = db.addRecipe(recipe);

        assertEquals(recipe, db.getRecipeById(recipe.getId()));
    }

    public void testGetAllRecipes() throws Exception {
        Recipe recipe1 = createRecipe();
        Recipe recipe2 = createRecipe();
        recipe2.setName("Pie");

        db.addRecipe(recipe1);
        db.addRecipe(recipe2);

        assertEquals(2, db.getAllRecipes().size());
    }

    public void testUpdateRecipe() throws Exception {
        Recipe recipe = createRecipe();

        recipe = db.addRecipe(recipe);

        recipe.setName("Pie");
        db.updateRecipe(recipe);

        assertEquals("Pie", db.getRecipeById(recipe.getId()).getName());
    }

    public void testDeleteRecipe() throws Exception {
        Recipe recipe = createRecipe();

        recipe = db.addRecipe(recipe);
        db.deleteRecipe(recipe);

        assertEquals(0, db.getAllRecipes().size());
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
