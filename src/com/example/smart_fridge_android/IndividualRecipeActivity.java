package com.example.smart_fridge_android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class IndividualRecipeActivity extends Activity {

	private static final String STARTING_TAB = "startingTab";
	private static final String TAG_INGREDIENT_ID = "recipe_id";
	private static final String TAG_INGREDIENT_NAME = "name";
	// maybe quantity
	
	DatabaseHandler db;
	Recipe recipe;
	ArrayList<HashMap<String, String>> ingredList;


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

            i.putExtra(STARTING_TAB, "recipes");

			this.startActivity(i);
			break; 

		case R.id.IMadeThisButton:
			setContentView(R.layout.i_made_this);
			//TextView nameText = (TextView) findViewById(R.id.IMadethisRecipeName);
			//nameText.setText(recipe.getName());
			/*
	    	List<String> list = new ArrayList<String>();
	        list = recipe.getIngredientList(); 
	        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.i_made_this, list);

	       ListView listViewItems = new ListView(this);
	       listViewItems.setAdapter(adp);
			 */		    
			TextView nameText = (TextView) findViewById(R.id.IMadethisRecipeName);
			nameText.setText(recipe.getName());

			break;	
		
		case R.id.UpdateButton:
		      	String recipeName = ((TextView)findViewById(R.id.IndRecipeNameField)).getText().toString();
	        	if(recipeName.isEmpty()) {
	                Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_LONG).show();
	                break;
	            }
	            if(recipeName.contains("<") && recipeName.contains(">")){
	                Toast.makeText(getApplicationContext(), "No tags (<>) allowed", Toast.LENGTH_LONG).show();
	                break;
	            }
	        	recipe.setName(recipeName);
	            
	        	String directionsNew = ((TextView)findViewById(R.id.IndRecipeInstructionsField)).getText().toString();
	        	recipe.setDirections(directionsNew);
	        	
	        	String ingredientsNew = ((TextView)findViewById(R.id.IndRecipeIngredientsField)).getText().toString();
	        	recipe.setIngredients(ingredientsNew);
	        	
	        	db.updateRecipe(recipe);
	        	break;
	            


		}
	}

	


}
