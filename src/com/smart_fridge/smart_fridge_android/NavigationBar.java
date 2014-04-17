package com.smart_fridge.smart_fridge_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class NavigationBar {

    private static final String STARTING_TAB = "startingTab";

    SessionManager session;

	public NavigationBar() {

	}

    /**
     * Handles button input for the add and logout buttons
     * located on the main navigation bar.
     *
     * The add button has a different function depending on
     * if the user is on the food or recipes tab.
     *
     * @param view
     * @param context
     */
	public void onButtonClick(View view, Context context) {

		switch (view.getId()){

            case R.id.addBtn:
            	if(MainActivity.foodTab)
            	{
                    Intent add_food_intent = new Intent(context, AddFoodActivity.class);
                    add_food_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(add_food_intent);
            	}
            	else
            	{
            		Intent add_recipe_intent = new Intent(context, AddRecipeActivity.class);
                    add_recipe_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(add_recipe_intent);
            	}
                break;

            case R.id.logoutBtn:
                session = new SessionManager(context);
                session.logoutUser();
                Intent logout_intent = new Intent(context, LoginActivity.class);
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(logout_intent);
                break;
		}
	}

    /**
     * Used to set current tab's color to blue. All other tabs
     * get set to black. Order of parameters passed in matters.
     * The first parameter passed in will be highlighted.
     *
     * @param highlightedView
     * @param blackView1
     * @param blackView2
     */
    public void setTabColors(TextView highlightedView, TextView blackView1, TextView blackView2){

            highlightedView.setBackgroundColor(Color.BLUE);
            blackView1.setBackgroundColor(Color.BLACK);
            blackView2.setBackgroundColor(Color.BLACK);
    }

    /**
     * Allows other activities to access the three main tabs
     * and be taken back to the main activity with the correct
     * starting tab.
     *
     * Do not call this method from main activity's onButtonClicked
     * method as it will result in the activity being started
     * and our smooth transitions between tabs will disappear.
     *
     * @param view
     * @param activity
     */
    public static void onTabsClicked(View view, Activity activity){

        switch (view.getId()){

            case R.id.tabFood:
                Intent tabFoodIntent = new Intent(activity.getApplicationContext(), MainActivity.class);
                tabFoodIntent.putExtra(STARTING_TAB, "food");

                tabFoodIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                tabFoodIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(tabFoodIntent);
                break;

            case R.id.tabRecipes:
                Intent tabRecipesIntent = new Intent(activity, MainActivity.class);
                tabRecipesIntent.putExtra(STARTING_TAB, "recipes");

                tabRecipesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                tabRecipesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(tabRecipesIntent);
                break;

            case R.id.tabSettings:
                Intent tabSettingsIntent = new Intent(activity, MainActivity.class);
                tabSettingsIntent.putExtra(STARTING_TAB, "settings");

                tabSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                tabSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(tabSettingsIntent);
                break;
        }
    }
}
