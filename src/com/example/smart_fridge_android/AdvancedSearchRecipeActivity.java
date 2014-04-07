package com.example.smart_fridge_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.apache.http.NameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSearchRecipeActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url to yummly api
    // Using Carl's api id and api key for now.
    private static String url_yummly = "http://api.yummly.com/v1/api/recipes?_app_id=5780cd3c&_app_key=c2d2c46e25e5051ee5f51ebedcdd16fd&q=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.advanced_search_recipes);
    }

    public void onButtonClick(View v) {

        switch (v.getId()) {

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