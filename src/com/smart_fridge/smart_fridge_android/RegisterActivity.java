package com.smart_fridge.smart_fridge_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity {

    private static final String STARTING_TAB = "startingTab";

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputConfirmPassword;

    // url to create new user
    private static String url_create_user = "https://fridgepantry.herokuapp.com/api/access/new";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        session = new SessionManager(getApplicationContext());

        // Edit Text
        inputName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.registerConfirmPassword);
    }

    public void onButtonClick(View v){

        switch (v.getId()){

            case R.id.btnRegister:

                if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    // Implement when external db is set up
                    new CreateNewUser().execute();


                }
                break;
        }
    }

    /**
     * Background Async Task to Create new user
     * */
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Creating Account...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating user
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            // getting JSON Object
            JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat for response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created user
                    session.createLoginSession(email);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(STARTING_TAB, "food");
                    startActivity(intent);
                }
            } catch (JSONException e) {
                // failed to create user
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Registration unsuccessful",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
