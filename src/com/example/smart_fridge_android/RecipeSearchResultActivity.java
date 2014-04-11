package com.example.smart_fridge_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeSearchResultActivity extends Activity {

    private static final String TAG_APP_ID = "_app_id";
    private static final String TAG_APP_KEY = "_app_key";
    private static final String ACTUAL_APP_ID = "5780cd3c";
    private static final String ACTUAL_APP_KEY = "c2d2c46e25e5051ee5f51ebedcdd16fd";
    private static final String TAG_YUMMLY_ID = "yummlyId";

    // Url to get specific details about a recipe
    private static String url_yummly_get_recipe_details = "http://api.yummly.com/v1/api/recipe/";

    JSONObject json;
    private JSONParser jsonParser = new JSONParser();
    String caloriesFromYummly;
    private ProgressDialog pDialog;
    private String calories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_result);

        Intent intent = getIntent();

        String yummlyId = intent.getStringExtra(TAG_YUMMLY_ID);
        String recipeName = intent.getStringExtra("recipeName");
        String recipeIngredients = intent.getStringExtra("recipeIngredients");


        try {
            caloriesFromYummly = new GetRecipeDetails(yummlyId).execute().get();

        } catch (InterruptedException e){

        } catch (ExecutionException e){

        }

        TextView name = (TextView) findViewById(R.id.textViewRecipeResultName);
        TextView ingredients = (TextView) findViewById(R.id.textViewIngredientsResultList);
        TextView calories = (TextView) findViewById(R.id.textViewRecipeSearchCarloriesResultList);

        name.setText(recipeName);
        ingredients.setText(recipeIngredients);
        calories.setText(caloriesFromYummly);
    }

    /**
     * Background Async Task to execute the yummly query for getting specific recipe details
     * */
    class GetRecipeDetails extends AsyncTask<String, String, String> {

        String yummlyId;

        public GetRecipeDetails(String yummlyId){

            this.yummlyId = yummlyId;
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RecipeSearchResultActivity.this);
            pDialog.setMessage("Gathering your recipe information...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Performing yummly api query
         * */
        protected String doInBackground(String... args) {

            url_yummly_get_recipe_details += yummlyId;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(TAG_APP_ID, ACTUAL_APP_ID));
            params.add(new BasicNameValuePair(TAG_APP_KEY, ACTUAL_APP_KEY));

            // getting JSON Object
            json = jsonParser.makeHttpRequest(url_yummly_get_recipe_details,
                    "GET", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // Go through all the new data on the recipe from yummly
            try {
                JSONArray nutritionEstimates = json.getJSONArray("nutritionEstimates");

                for (int i = 0; i < nutritionEstimates.length(); i++) {
                    JSONObject individualEstimate = nutritionEstimates.getJSONObject(i);

                    // Looking for calories.
                    if (individualEstimate.get("attribute").equals("ENERC_KCAL")){
                        calories = individualEstimate.get("value").toString();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return calories;
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
