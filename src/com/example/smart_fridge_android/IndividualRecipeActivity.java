package com.example.smart_fridge_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class IndividualRecipeActivity extends Activity {

    private static final String STARTING_TAB = "startingTab";

    DatabaseHandler db;
	Recipe recipe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_recipe_item);
		
		Intent intent = getIntent();
        String id = intent.getStringExtra("rid");
        
        db = new DatabaseHandler(this);
        db.getDatabaseName();
        
        recipe = db.getRecipeById(Integer.parseInt(id));
        
        TextView nameText = (TextView) findViewById(R.id.IndRecipeNameField);
        nameText.setText(recipe.getName());
        
        TextView instructions = (TextView) findViewById(R.id.IndRecipeInstructionsField);
        instructions.setText(recipe.getDirections());
        
        TextView ingredients = (TextView) findViewById(R.id.IndRecipeIngredientsField);
        ingredients.setText(recipe.getIngredients().replace("<b>", "\n"));
        
        TextView nutriInfo = (TextView) findViewById(R.id.IndRecipeNutrInfoField);
        nutriInfo.setText(""); // To be added in Iteration 2
	}
	
	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
		navBar.onButtonClick(v,  getApplicationContext());
        NavigationBar.onTabsClicked(v, this);

        switch (v.getId()) {
		
		case R.id.IndRecipeDeleteButton:
			db.deleteRecipe(recipe);
			
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(STARTING_TAB, "recipe");
			this.startActivity(i);
        	break; 
        
		case R.id.IMadeThisButton:
			setContentView(R.layout.i_made_this);
			TextView nameText = (TextView) findViewById(R.id.IMadethisRecipeName);
	        nameText.setText(recipe.getName());
	        break;	       
			
			
		}
	}
}
