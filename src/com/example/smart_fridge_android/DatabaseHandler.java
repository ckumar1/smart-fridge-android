package com.example.smart_fridge_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

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
                + KEY_FOOD_EXPIRATION_DATE + " DATE(),"
                + KEY_FOOD_CATEGORY + " TEXT,"
                + KEY_FOOD_CALORIES + " INT(16),"
                + KEY_FOOD_PHOTO + " BLOB"
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
}
