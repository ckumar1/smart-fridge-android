package com.example.smart_fridge_android;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import junit.framework.Assert;

import java.util.List;

public class DatabaseHandlerTest extends AndroidTestCase {

    private DatabaseHandler db;

    public void setup(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHandler(context);
    }

    public void addFoodTest(){
        Food food = createFood();
        db.addFood(food);

        Assert.assertEquals(food, db.getFoodByName(food.getName()));
    }

    public void getFoodByIdTest(){
        Food originalFood = createFood();
        db.addFood(originalFood);

        originalFood = db.getFoodByName("Apple");
        Food foodFromDb = db.getFoodById(originalFood.getId());

        Assert.assertEquals(originalFood, foodFromDb);
    }

    public void getAllFoodTest(){
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

    public void updateFoodTest(){
        Food food = createFood();
        db.addFood(food);

        food.setName("Banana");
        db.updateFood(food);

        Assert.assertEquals(food, db.getFoodByName(food.getName()));
    }

    public void countFoodRowsTest(){
        Food food1 = createFood();
        Food food2 = createFood();

        db.addFood(food1);
        db.addFood(food2);

        Assert.assertEquals(2, db.countFoodRows());
    }

    public void deleteFoodTest(){
        Food food = createFood();
        db.addFood(food);
        db.deleteFood(food);

        Assert.assertEquals(0, db.countFoodRows());
    }

    // Food creation for test
    private Food createFood(){
        Food food = new Food();
        food.setName("Apple");
        food.setDescription("Red");
        food.setExpirationDate("12/21/14");
        food.setCategory("Fruit");
        food.setCalories(100);
        return food;
    }

}
