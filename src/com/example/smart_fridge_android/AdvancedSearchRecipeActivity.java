package com.example.smart_fridge_android;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdvancedSearchRecipeActivity extends ListActivity {

    private static final String TAG_RECIPE_NAME = "name";
    private static final String TAG_RECIPE_INGREDIENTS = "ingredients";
    private static final String TAG_YUMMLY_ID = "yummlyId";
    private static final String TAG_APP_ID = "_app_id";
    private static final String TAG_APP_KEY = "_app_key";
    private static final String ACTUAL_APP_ID = "5780cd3c";
    private static final String ACTUAL_APP_KEY = "c2d2c46e25e5051ee5f51ebedcdd16fd";

    // Holds the recipes from the recipe search
    List<Recipe> recipes = new ArrayList<Recipe>();

    // Holds the recipe ids from the search to use in getting specific details
    List<String> yummlyRecipeIds = new ArrayList<String>();

    // Progress Dialog
    private ProgressDialog pDialog;

    ListAdapter adapter;

    JSONObject json;

    JSONParser jsonParser = new JSONParser();

    Button calorieCountButton;
    EditText recipeNameEditText;

    String contentView; // Keeps track of current view to be used in onBackPressed method

    // Using Carl's api id and api key for now.
    // Url to perform a search of recipes on yummly's site
    private static String url_yummly_search_recipes = "http://api.yummly.com/v1/api/recipes";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advanced_recipe_search);
        contentView = "advanced_recipe_search";

        // Don't need the add button from the navbar
        Button add = (Button) findViewById(R.id.addBtn);
        if (add != null){
            add.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

        if (contentView.equals("advanced_recipe_search")){
            finish();
        } else if (contentView.equals("recipe_search_results")){
            setContentView(R.layout.advanced_recipe_search);
            contentView = "advanced_recipe_search";
        } else if (contentView.equals("recipe_result")){
            setContentView(R.layout.recipe_search_results);
            contentView = "recipe_search_results";

            setResultsAdapter(recipes);
        } else {
            finish();
        }
    }

    public void onButtonClick(View view) {

        NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(view, getApplicationContext());
        NavigationBar.onTabsClicked(view, this);

        switch (view.getId()) {

            case R.id.btnSearchRecipes:
                prepareSearch();
                new GetRecipes().execute();

                setContentView(R.layout.recipe_search_results);
                contentView = "recipe_search_results";
                break;

            case R.id.btnCalorieCount:
                calorieCountButton = (Button) findViewById(R.id.btnCalorieCount);

                if (calorieCountButton.getText().equals("Less Than")){
                    calorieCountButton.setText("Greater Than");
                } else {
                    calorieCountButton.setText("Less Than");
                }
                break;
        }
    }

    /**
     * Grab any input that the user has entered and prepare for query
     */
    private void prepareSearch(){

        recipeNameEditText = (EditText) findViewById(R.id.editTextRecipeName);
    }

    /**
     * Shows the recipe results list
     **/
    private void setResultsAdapter(List<Recipe> recipes){

        List<HashMap<String, String>> recipesList = new ArrayList<HashMap<String, String>>();

        for (Recipe recipe : recipes){

            HashMap<String, String> map = new HashMap<String, String>();

            map.put(TAG_RECIPE_NAME, recipe.getName());
            map.put(TAG_RECIPE_INGREDIENTS, recipe.getIngredients());
            recipesList.add(map);
        }

        adapter = new SimpleAdapter(AdvancedSearchRecipeActivity.this, recipesList, R.layout.recipe_list_item,
                new String[] {TAG_RECIPE_NAME}, new int[] {R.id.recipe_name});

        setListAdapter(adapter);
        setListView(recipes);
    }

    /**
     * Displays the proper information for the results being viewed.
     */
    private void setListView(final List<Recipe> recipes){
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

                String yummlyId = yummlyRecipeIds.get(position);

                Intent recipeResultIntent = new Intent(getApplicationContext(), RecipeSearchResultActivity.class);
                recipeResultIntent.putExtra(TAG_YUMMLY_ID, yummlyId);
                recipeResultIntent.putExtra(TAG_RECIPE_NAME, recipes.get(position).getName());
                recipeResultIntent.putExtra(TAG_RECIPE_INGREDIENTS, recipes.get(position).getIngredients());

                startActivity(recipeResultIntent);
            }
        });
    }

    /**
     * Background Async Task to execute the yummly query
     * */
    class GetRecipes extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            recipes.clear(); // Start with a fresh list for recipes

            pDialog = new ProgressDialog(AdvancedSearchRecipeActivity.this);
            pDialog.setMessage("Gathering your recipe results...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Performing yummly api query
         * */
        protected String doInBackground(String... args) {

            String recipeName = recipeNameEditText.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(TAG_APP_ID, ACTUAL_APP_ID));
            params.add(new BasicNameValuePair(TAG_APP_KEY, ACTUAL_APP_KEY));
            params.add(new BasicNameValuePair("q", recipeName));
            params.add(new BasicNameValuePair("maxResult", Integer.toString(20)));
            params.add(new BasicNameValuePair("start", Integer.toString(20)));

            // getting JSON Object
            json = jsonParser.makeHttpRequest(url_yummly_search_recipes,
                    "GET", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // Gather initial information (name, ingredients) on each recipe returned
            try {
                JSONArray recipesArray = json.getJSONArray("matches");

                for (int i = 0; i < recipesArray.length(); i++) {
                    JSONObject recipe = recipesArray.getJSONObject(i);

                    String name = recipe.get("recipeName").toString();

                    JSONArray ingredientsArray = recipe.getJSONArray("ingredients");

                    String ingredients = "";

                    for (int k = 0; k < ingredientsArray.length(); k++){
                        ingredients += ingredientsArray.get(k) + "\n";
                    }

                    Recipe r = new Recipe();
                    r.setName(name);
                    r.setIngredients(ingredients);
                    recipes.add(r);

                    // Put in the id to use in the GetRecipeDetails class
                    String yummlyId = recipe.get("id").toString();
                    yummlyRecipeIds.add(yummlyId);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            setResultsAdapter(recipes);

            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
}
