package com.example.smart_fridge_android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new Login().execute();






    }

     class Login extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute(){

        }

        protected String doInBackground(String... args){
            JSONParser jsonParser = new JSONParser();

            String loginURL = "http://immense-hollows-2781.herokuapp.com/";
            String registerURL = "http://immense-hollows-2781.herokuapp.com/";

            String login_tag = "login";
            String register_tag = "register";

            String name = "Carl";
            String email = "test@test.com";
            String password = "password";


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", register_tag));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            // getting JSON Object
            JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
            Log.e("JSON_test", json.toString());
            return json.toString();
        }

        protected void onPostExecute(){

        }

    }
}
