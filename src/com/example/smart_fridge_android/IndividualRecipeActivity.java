package com.example.smart_fridge_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;

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
        
        TextView nutriInfo = (TextView) findViewById(R.id.IndRecipeNutrInfoField);
        nutriInfo.setText(""); // To be added in Iteration 2
	}
	
	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
		navBar.onButtonClick(v,  getApplicationContext());
		
		switch (v.getId()) {
		
		case R.id.IndRecipeDeleteButton:
            //delete image from phone only if device has camera
            if (Camera.getNumberOfCameras() > 0) {
                File recipe_image = new File(recipe.getImagePath());
                Boolean deleted = recipe_image.delete(); //Delete the photo from your phone
                if (!deleted)
                    Log.w("Delete Recipe", "Recipe Image wasn't deleted");
            }
			db.deleteRecipe(recipe);
			
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(STARTING_TAB, "recipe");
			this.startActivity(i);
        	break; 
		}
	}
}
