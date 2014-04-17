package com.smart_fridge.smart_fridge_android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IndividualRecipeActivity extends Activity {

	private static final String STARTING_TAB = "startingTab";
	private static final String TAG_INGREDIENT_ID = "recipe_id";
	private static final String TAG_INGREDIENT_NAME = "name";
	// maybe quantity
	
	DatabaseHandler db;
	Recipe recipe;
	ArrayList<HashMap<String, String>> ingredList;
	
	
    private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_recipe_item);

		Intent intent = getIntent();
	    id = intent.getStringExtra("rid");
        
        db = new DatabaseHandler(this);
        db.getDatabaseName();
        
        recipe = db.getRecipeById(Integer.parseInt(id));
        
        TextView nameText = (TextView) findViewById(R.id.IndRecipeNameField);
        nameText.setText(recipe.getName());
        
        TextView instructions = (TextView) findViewById(R.id.IndRecipeInstructionsField);
        instructions.setText(recipe.getDirections());
        
        TextView ingredients = (TextView) findViewById(R.id.IndRecipeIngredientsField);
        ingredients.setText(recipe.getIngredients().replace("<b>", "\n"));

       String path = recipe.getImagePath();
        if (path != null && !path.isEmpty()) {
             File imgFile = new  File(path);
            if(imgFile.exists()) {
                Log.w("Image", "Image Exists!");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.imgViewRecipe); //fix this

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(),
                        myBitmap.getHeight(), matrix, true);
                myImage.setImageBitmap(rotatedBitmap);
            }

        }
	}



	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
		navBar.onButtonClick(v,  getApplicationContext());
		NavigationBar.onTabsClicked(v, this);

		switch (v.getId()) {

		case R.id.IndRecipeDeleteButton:
            String recipeImgPath = recipe.getImagePath();
            if (recipeImgPath != null && !recipeImgPath.isEmpty()) {
                File recipe_image = new File(recipeImgPath);
                Boolean deleted = recipe_image.delete(); //Delete the photo from your phone
                if (!deleted)
                    Log.w("Delete Recipe", "Recipe Image wasn't deleted");
            }
			db.deleteRecipe(recipe);

			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.putExtra(STARTING_TAB, "recipes");

			this.startActivity(i);
			break; 

		case R.id.IMadeThisButton:

			Intent iMadeThisIntent = new Intent(getApplicationContext(), IMadeThisActivity.class);
			iMadeThisIntent.putExtra("rid", id);
			startActivity(iMadeThisIntent);

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
            recipe.setIngredientsList(Arrays.asList(ingredientsNew.split("<b>")));

            db.updateRecipe(recipe);

            Intent updateRecipeIntent = new Intent(getApplicationContext(), MainActivity.class);
            updateRecipeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            updateRecipeIntent.putExtra(STARTING_TAB, "recipes");
            startActivity(updateRecipeIntent);

            break;
		}
	}

	


}
