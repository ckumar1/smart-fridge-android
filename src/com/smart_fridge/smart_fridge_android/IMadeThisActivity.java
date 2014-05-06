package com.smart_fridge.smart_fridge_android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class IMadeThisActivity  extends ListActivity {

	private static final String TAG_FOOD_ID = "food_id";
	private static final String TAG_FOOD_NAME = "name";

	ArrayList<HashMap<String, String>> foodList;
	List<Recipe> recipesFromDB;

	List<Food> foodFromDB;
	ArrayList<Integer> quantityList = new ArrayList<Integer>();
	AlertDialog.Builder builder;
	int idquantity;
	ListAdapter adapter;

	DatabaseHandler db;
	Recipe recipe;
	Food food;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.i_made_this);

		Intent intentRecipe = getIntent();
		String id = intentRecipe.getStringExtra("rid");

		db = new DatabaseHandler(this);
		db.getDatabaseName();
		
		if(id.contains("*"))
		{
			String recName = id.substring(0, id.indexOf('*'));
			String ingredListYum = id.substring(id.indexOf('*')+1);
			
			TextView nameText = (TextView) findViewById(R.id.IMadethisRecipeName);
			nameText.setText(recName);
			setRecipeAdapter(); 
			TextView ingredients = (TextView) findViewById(R.id.IngredientList);
	        ingredients.setText(ingredListYum.replace("<b>", "\n"));
		}
		else
		{
		recipe = db.getRecipeById(Integer.parseInt(id));
		TextView nameText = (TextView) findViewById(R.id.IMadethisRecipeName);
		nameText.setText(recipe.getName());
		setRecipeAdapter(); 
		
		TextView ingredients = (TextView) findViewById(R.id.IngredientList);
        ingredients.setText(recipe.getIngredients().replace("<b>", "\n"));
		}

	}


	public void onButtonClick(View view) {

		NavigationBar navBar = new NavigationBar();
		navBar.onButtonClick(view, getApplicationContext());
		NavigationBar.onTabsClicked(view, this);


		switch (view.getId()){

		case R.id.IMadeThisDoneButton:
			for(int i=0; i<quantityList.size(); i+=2)
			{
				//Toast.makeText(getApplicationContext(), "HERE", Toast.LENGTH_LONG).show();
				foodFromDB = db.getAllFood();
				Food food = foodFromDB.get(quantityList.get(i));
				int quantity = food.getQuantity();
				int amountsub = quantityList.get(i+1);

				if(quantity > 0)
				{
					if(quantity-amountsub <= 0)
					{
						db.deleteFood(food);
					}
					else
					{
						food.setQuantity(quantity-amountsub);
						db.updateFood(food);
					}
				}

			}
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);       	  
			break;
		} 
	}
	private void setRecipeAdapter(){

		foodList = new ArrayList<HashMap<String, String>>();

		foodFromDB = db.getAllFood();

		String value;
		for (Food food : foodFromDB){
			HashMap<String, String> map = new HashMap<String, String>();
			value = "";

			Integer id = food.getId();
			String  id_as_string = id.toString();
			String foodName = food.getName();
			map.put(TAG_FOOD_ID, id_as_string);
			map.put(TAG_FOOD_NAME, foodName);
			foodList.add(map);
		}
		adapter = new SimpleAdapter(IMadeThisActivity.this, foodList, R.layout.i_made_this_list_item,
				new String[] {TAG_FOOD_ID, TAG_FOOD_NAME}, new int[] {R.id.recipe_id2, R.id.foodName});

		setListAdapter(adapter);
		setListView();
	}

	private void setListView() {
		ListView listView = getListView();

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				//String quantity1 = null;
				String quant = Long.toString(id);
				idquantity = (int) id;
				builder = new AlertDialog.Builder(IMadeThisActivity.this);
				builder.setMessage("Quantity Used in Recipe");
				builder.setTitle("Edit food");
				final EditText input = new EditText(IMadeThisActivity.this);
				builder.setView(input);
				builder.setPositiveButton("Done", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						String quantity1 = input.getText().toString();
						int quantity = Integer.parseInt(quantity1);

						quantityList.add(idquantity);
						quantityList.add(quantity);

					}
				}
						);
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id){

						dialog.cancel();
					}
				}
						);
				builder.create();
				builder.show();

			}
		});

	}
}

