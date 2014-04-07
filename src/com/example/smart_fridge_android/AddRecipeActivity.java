package com.example.smart_fridge_android;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

public class AddRecipeActivity extends Activity {

    private static final String STARTING_TAB = "startingTab";

    private List <String> ingredlist = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_recipe);
        removeAddButton();
	}

	public void onButtonClick(View v) {
		
		NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());
        switch (v.getId()){

            case R.id.tabFood:

                Intent tabFoodIntent = new Intent(getApplicationContext(), MainActivity.class);
                tabFoodIntent.putExtra(STARTING_TAB, "food");

                tabFoodIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tabFoodIntent);

                break;

            case R.id.tabRecipes:

                Intent tabRecipesIntent = new Intent(getApplicationContext(), MainActivity.class);
                tabRecipesIntent.putExtra(STARTING_TAB, "recipes");

                tabRecipesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tabRecipesIntent);

                break;

            case R.id.tabSettings:

                Intent tabSettingsIntent = new Intent(getApplicationContext(), MainActivity.class);
                tabSettingsIntent.putExtra(STARTING_TAB, "settings");

                tabSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tabSettingsIntent);

                break;
        
            case R.id.addingred:

                EditText mEdit;
                mEdit = (EditText)findViewById(R.id.IngredientField);
                if (mEdit.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter an ingredient name", Toast.LENGTH_SHORT).show();
                    return;
                }
                ingredlist.add(mEdit.getText().toString());
                mEdit.setText("");
                break;

            case R.id.showlist:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                EditText recName;
                recName = (EditText)findViewById(R.id.RecipeNameField);

                String recipeN = recName.getText().toString();

                String message = "";

                for(String ingredient : ingredlist)
                {
                    if(message.equals(""))
                    {
                        message = ingredient;
                    }
                    else {
                    message = message + "\n" + ingredient;
                    }
                }

                builder.setMessage(message);
                builder.setTitle("Current ingredients for "+recipeN);
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
                break;

            case R.id.addrec:
                Recipe recipe = new Recipe();

                String name = ((EditText)findViewById(R.id.RecipeNameField)).getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_LONG).show();
                    break;
                }
                recipe.setName(name);

                String directions = ((EditText)findViewById(R.id.InstructionsField)).getText().toString();
                if(directions.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Directions are required", Toast.LENGTH_LONG).show();
                    break;
                }
                recipe.setDirections(directions);


                if(ingredlist.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingredients are required", Toast.LENGTH_LONG).show();
                    break;
                }
                recipe.setIngredientsList(ingredlist);

                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.addRecipe(recipe);

                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(STARTING_TAB, "recipe");
                this.startActivity(i);
                break;
        }
        removeAddButton();
	}

    private void removeAddButton(){
        Button addButton = (Button) findViewById(R.id.addBtn);
        addButton.setVisibility(View.INVISIBLE);
    }
}
