package com.example.smart_fridge_android;

import android.app.Activity;
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

public class AdvancedSearchRecipeActivity extends ListActivity {

    private static final String STARTING_TAB = "startingTab";
    private static final String TAG_RECIPE_ID = "recipe_id";
    private static final String TAG_RECIPE_NAME = "name";

    // Progress Dialog
    private ProgressDialog pDialog;

    ListAdapter adapter;

    JSONObject json;

    JSONParser jsonParser = new JSONParser();

    Button calorieCountButton;

    EditText recipeNameEditText;

    // url to yummly api
    // Using Carl's api id and api key for now.
    private static String url_yummly = "http://api.yummly.com/v1/api/recipes?_app_id=5780cd3c&_app_key=c2d2c46e25e5051ee5f51ebedcdd16fd";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advanced_recipe_search);
    }

    public void onButtonClick(View view) {

        NavigationBar.onTabsClicked(view, this);

        switch (view.getId()) {

            case R.id.btnSearchRecipes:
                prepareSearch();
                new GetRecipes().execute();

                setContentView(R.layout.recipe_search_results);
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

    private void prepareSearch(){

        recipeNameEditText = (EditText) findViewById(R.id.editTextRecipeName);
    }

    private void parseJson(){
        try {
            JSONArray recipesArray = json.getJSONArray("matches");
            JSONObject recipe = recipesArray.getJSONObject(0);
            String name = recipe.get("recipeName").toString();
            List<String> names = new ArrayList<String>();
            names.add(name);

            setResultsAdapter(names);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the recipe results list
     **/
    private void setResultsAdapter(List<String> recipeNames){

        List<HashMap<String, String>> recipesList = new ArrayList<HashMap<String, String>>();

        for (String recipe : recipeNames){

            HashMap<String, String> map = new HashMap<String, String>();

            map.put(TAG_RECIPE_NAME, recipe);
            recipesList.add(map);
        }

        adapter = new SimpleAdapter(AdvancedSearchRecipeActivity.this, recipesList, R.layout.recipe_list_item,
                new String[] {TAG_RECIPE_NAME}, new int[] {R.id.recipe_name});

        setListAdapter(adapter);
        setListView();
    }

    /**
     * Sets the proper ListView for the results being viewed.
     */
    private void setListView(){
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

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

            params.add(new BasicNameValuePair("q", recipeName));

            // getting JSON Object
            json = jsonParser.makeHttpRequest(url_yummly,
                    "GET", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            return null;
        }

        /**
         * After completing background task dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
