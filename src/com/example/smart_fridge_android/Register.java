package com.example.smart_fridge_android;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * All Registration network operations live here.
 */

public class Register {

    final String REGISTER_URL = "http://smart-fridge.herokuapp.com/";

    JSONParser jsonParser = new JSONParser();
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    public Register(List<NameValuePair> params){
        this.params = params;
    }

    public void execute(){
        new createRegistration().execute();
    }


    /**
     * NetworkOperations class for performing network operations on the external database.
     * Currently, only POST is implemented.
     * Only Login and Register operations are implemented.
     * We will need to point this to the real external database once it is implemented,
     * but for right now, this dummy DB should work just fine.
     *
     * Will also need to implement password security later too.
     */
    class createRegistration extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){

        }

        protected String doInBackground(String... args){

            // getting JSON Object
            JSONObject json = jsonParser.getJSONFromUrl(REGISTER_URL, params);
            Log.e("JSON_test", json.toString());
            return json.toString();
        }

        protected void onPostExecute(){

        }
    }
}
