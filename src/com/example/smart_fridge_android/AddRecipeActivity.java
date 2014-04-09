package com.example.smart_fridge_android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    static final int CAMERA_RESULT = 1;
    private Uri imageUri;
    private String imagePath;
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

        case R.id.addimgR:
            if (Camera.getNumberOfCameras() > 0) {
                dispatchTakePictureIntent();
            }
            else {
                Toast.makeText(getApplicationContext(), "No camera found", Toast.LENGTH_LONG).show();
            }
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

            if (imagePath != null && !imagePath.isEmpty()) {
                recipe.setImagePath(imagePath);
            }
            else {
                recipe.setImagePath("");
            }
        	
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

    private void dispatchTakePictureIntent() {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "fridge_images");
        imagesFolder.mkdirs();
        //filePath = "/fridge_images/recipe_image" + timeStamp + ".png"

        File image = new File(imagesFolder, "recipe_image" + timeStamp + ".png");
        Uri uriSavedImage = Uri.fromFile(image);

        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        imagePath = image.toString();
        startActivityForResult(imageIntent, CAMERA_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
        }
    }

    private void removeAddButton(){
        Button addButton = (Button) findViewById(R.id.addBtn);
        addButton.setVisibility(View.INVISIBLE);
    }
}
