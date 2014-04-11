package com.example.smart_fridge_android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class IMadeThisActivity  extends ListActivity {
	
	  private static final String TAG_RECIPE_ID = "recipe_id";
	  private static final String TAG_RECIPE_NAME = "name";
	  private static final String TAG_RECIPE_INGREDIENT = "ingredients";
  private ListView listViewIng;
  private Context ctx;
  ArrayList<HashMap<String, String>> ingredientList;
  List<Recipe> recipesFromDB;

  ListAdapter adapter;
 
  DatabaseHandler db;
  Recipe recipe;
  private int count;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.i_made_this);
     
      Intent intentRecipe = getIntent();
      String id = intentRecipe.getStringExtra("rid");
      
      db = new DatabaseHandler(this);
      db.getDatabaseName();
      
      recipe = db.getRecipeById(Integer.parseInt(id));
      TextView nameText = (TextView) findViewById(R.id.IMadethisRecipeName);
      nameText.setText(recipe.getName());
      setRecipeAdapter(); 
 
  }
  
  public void onButtonClick(View view) {
		
		NavigationBar navBar = new NavigationBar();
      navBar.onButtonClick(view, getApplicationContext());
      NavigationBar.onTabsClicked(view, this);
      switch (view.getId()){

          case R.id.IMadeThisDoneButton:
        	  Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        	  startActivity(intent);       	  
        	 
        	  break;
        	  
        	  
      } 
      }
  private void setRecipeAdapter(){

      ingredientList = new ArrayList<HashMap<String, String>>();
      
      List<String> ingredList = new ArrayList<String>();
      ingredList = recipe.getIngredientList();
      count = 0;
     for (String ingredient : ingredList){
    	 count++;
          HashMap<String, String> map = new HashMap<String, String>();

          Integer id = recipe.getId();
          String  id_as_string = id.toString();

          map.put(TAG_RECIPE_ID, id_as_string);
          map.put(TAG_RECIPE_INGREDIENT, ingredient);
          ingredientList.add(map);
      }
     
      adapter = new SimpleAdapter(IMadeThisActivity.this, ingredientList, R.layout.i_made_this_list_item,
              new String[] {TAG_RECIPE_ID, TAG_RECIPE_INGREDIENT}, new int[] {R.id.recipe_id2, R.id.ingredientName});

      setListAdapter(adapter);
  }  
   }
