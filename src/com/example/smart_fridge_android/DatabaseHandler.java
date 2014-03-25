package com.example.smart_fridge_android;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "smartFridge";

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FOOD = "food";
    private static final String TABLE_RECIPES = "recipes";

    // Users table column names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD_HASH = "password_hash";
    private static final String KEY_USER_LOGGED_IN = "logged_in";

    // Food table column names
    private static final String KEY_FOOD_ID = "food_id";
    private static final String KEY_FOOD_NAME = "name";
    private static final String KEY_FOOD_DESCRIPTION = "description";
    private static final String KEY_FOOD_CATEGORY = "category";
    private static final String KEY_FOOD_EXPIRATION_DATE = "expiration_date";
    private static final String KEY_FOOD_CALORIES = "calories";
    private static final String KEY_FOOD_QUANTITY = "quantity";
    private static final String KEY_FOOD_PHOTO = "photo";

    // Recipe table column names
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_RECIPE_DIRECTIONS = "directions";
    private static final String KEY_RECIPE_NOTES = "notes";
    private static final String KEY_RECIPE_PHOTO = "photo";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE "
                + TABLE_USERS
                + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD_HASH + " CHAR(64),"
                + KEY_USER_LOGGED_IN + " BOOLEAN"
                + ")";

        String CREATE_FOOD_TABLE = "CREATE TABLE "
                + TABLE_FOOD
                + "("
                + KEY_FOOD_ID + " INTEGER PRIMARY KEY,"
                + KEY_FOOD_NAME + " TEXT,"
                + KEY_FOOD_DESCRIPTION + " TEXT,"
                + KEY_FOOD_EXPIRATION_DATE + " TEXT,"
                + KEY_FOOD_CATEGORY + " TEXT,"
                + KEY_FOOD_CALORIES + " INT(16),"
                + KEY_FOOD_QUANTITY + " INT(16),"
                + KEY_FOOD_PHOTO + " BLOB" // Not sure if this is the correct type -carl
                + ")";

        String CREATE_RECIPES_TABLE = "CREATE TABLE "
                + TABLE_RECIPES
                + "("
                + KEY_RECIPE_ID + " INTEGER PRIMARY KEY,"
                + KEY_RECIPE_NAME + " TEXT,"
                + KEY_RECIPE_DIRECTIONS + " TEXT,"
                + KEY_RECIPE_NOTES + " TEXT,"
                + KEY_RECIPE_PHOTO + " BLOB"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for Food
     */

    // Adding new food
    public void addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOOD_NAME, food.getName()); // Food Name
        values.put(KEY_FOOD_DESCRIPTION, food.getDescription()); // Food Description
        values.put(KEY_FOOD_EXPIRATION_DATE, food.getExpirationDate()); // Food Expiration Date
        values.put(KEY_FOOD_CATEGORY, food.getCategory()); // Food Category
        values.put(KEY_FOOD_CALORIES, food.getCalories()); // Food Calories
        values.put(KEY_FOOD_QUANTITY, food.getQuantity()); // Food Quantity

        // Inserting Row
        db.insert(TABLE_FOOD, null, values);
        db.close(); // Closing database connection
    }

    // Getting Food by Id
    public Food getFoodById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FOOD, new String[] { KEY_FOOD_ID,
                KEY_FOOD_NAME, KEY_FOOD_DESCRIPTION, KEY_FOOD_EXPIRATION_DATE,
                KEY_FOOD_CATEGORY, KEY_FOOD_CALORIES, KEY_FOOD_QUANTITY },
                KEY_FOOD_ID + "= ?", // '?' is replaced by selection args
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Food food = new Food(cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4),
                Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));
        food.setId(Integer.parseInt(cursor.getString(0))); // Set the id
        // return food
        return food;
    }

    public Food getFoodByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FOOD, new String[] { KEY_FOOD_ID,
                KEY_FOOD_NAME, KEY_FOOD_DESCRIPTION, KEY_FOOD_EXPIRATION_DATE,
                KEY_FOOD_CATEGORY, KEY_FOOD_CALORIES, KEY_FOOD_QUANTITY},
                KEY_FOOD_NAME + "= ?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Food food = new Food(cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4),
                Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)));
        food.setId(Integer.parseInt(cursor.getString(0))); // Set the id
        // return food
        return food;
    }

    // Getting All Food
    public List<Food> getAllFood() {
        List<Food> foodList = new ArrayList<Food>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOOD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setId(Integer.parseInt(cursor.getString(0)));
                food.setName(cursor.getString(1));
                food.setDescription(cursor.getString(2));
                food.setExpirationDate(cursor.getString(3));
                food.setCategory(cursor.getString(4));
                food.setCalories(cursor.getInt(5));
                food.setQuantity(cursor.getInt(6));

                // Adding food to list
                foodList.add(food);
            } while (cursor.moveToNext());
        }

        // return food list
        return foodList;
    }

    // Updating Food
    public int updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FOOD_NAME, food.getName());
        values.put(KEY_FOOD_DESCRIPTION, food.getDescription());
        values.put(KEY_FOOD_EXPIRATION_DATE, food.getExpirationDate());
        values.put(KEY_FOOD_CATEGORY, food.getCategory());
        values.put(KEY_FOOD_CALORIES, food.getCalories());
        values.put(KEY_FOOD_QUANTITY, food.getQuantity());

        // updating row
        return db.update(TABLE_FOOD, values, KEY_FOOD_ID + " = ?",
                new String[] { String.valueOf(food.getId()) });
    }

    // Counting rows
    public int countFoodRows(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select count(*) from " + TABLE_FOOD, null);
        return cursor.getCount();
    }

    // Deleting food
    public void deleteFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD, KEY_FOOD_ID + " = ?",
                new String[] { String.valueOf(food.getId()) });
        db.close();
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations for Recipes
     */

    // Adding new Recipe
    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE_NAME, recipe.getName()); // Recipe Name
        values.put(KEY_RECIPE_DIRECTIONS, recipe.getDirections()); // Recipe Directions
        values.put(KEY_RECIPE_NOTES, recipe.getNotes()); // Recipe Notes

        // Inserting Row
        db.insert(TABLE_RECIPES, null, values);
        db.close(); // Closing database connection
    }

    // Getting Recipe
    public Recipe getRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECIPES, new String[] { KEY_RECIPE_ID,
                KEY_RECIPE_NAME, KEY_RECIPE_DIRECTIONS, KEY_RECIPE_NOTES },
                KEY_RECIPE_ID + "= ?", // '?' is replaced by selection args
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Recipe recipe = new Recipe(cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
        recipe.setId(Integer.parseInt(cursor.getString(0)));
        // return recipe
        return recipe;
    }

    // Getting all Recipe
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipesList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(Integer.parseInt(cursor.getString(0)));
                recipe.setName(cursor.getString(1));
                recipe.setDirections(cursor.getString(2));
                recipe.setNotes(cursor.getString(3));

                // Adding recipe to list
                recipesList.add(recipe);
            } while (cursor.moveToNext());
        }

        // return recipes list
        return recipesList;
    }

    // Updating Recipe
    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE_NAME, recipe.getName());
        values.put(KEY_RECIPE_DIRECTIONS, recipe.getDirections());
        values.put(KEY_RECIPE_NOTES, recipe.getNotes());

        // updating row
        return db.update(TABLE_RECIPES, values, KEY_RECIPE_ID + " = ?",
                new String[] { String.valueOf(recipe.getId()) });
    }

    // Deleting Recipe
    public void deleteRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, KEY_RECIPE_ID + " = ?",
                new String[] { String.valueOf(recipe.getId()) });
        db.close();
    }
}
