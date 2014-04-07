package com.example.smart_fridge_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.apache.http.NameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchRecipeActivity extends Activity {

    private static final String STARTING_TAB = "startingTab";

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    Button calorieCountButton;

    // url to yummly api
    // Using Carl's api id and api key for now.
    private static String url_yummly = "http://api.yummly.com/v1/api/recipes?_app_id=5780cd3c&_app_key=c2d2c46e25e5051ee5f51ebedcdd16fd&q=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advanced_recipe_search);
    }

    public void onButtonClick(View v) {

        switch (v.getId()) {

            case R.id.btnSearchRecipes:
                Intent resultsIntent = new Intent(getApplicationContext(), RecipeSearchResultsActivity.class);
                startActivity(resultsIntent);
                break;

            case R.id.btnCalorieCount:
                calorieCountButton = (Button) findViewById(R.id.btnCalorieCount);

                if (calorieCountButton.getText().equals("Less Than")){
                    calorieCountButton.setText("Greater Than");
                } else {
                    calorieCountButton.setText("Less Than");
                }
                break;


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

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                // getting JSON Object
                JSONObject json = jsonParser.makeHttpRequest(url_yummly,
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
}
