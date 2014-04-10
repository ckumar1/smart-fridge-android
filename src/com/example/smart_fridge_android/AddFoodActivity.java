package com.example.smart_fridge_android;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


public class AddFoodActivity extends Activity implements OnDateSetListener{

	private Button add;
	private int SCAN_REQUEST_CODE = 0;
	private String upcResult = "";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String upc_name = "";

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_food);
		add = (Button) findViewById(R.id.addBtn);
        if (add != null)
        	add.setVisibility(View.INVISIBLE);
	}
	

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		TextView mDateTextView = (TextView)findViewById(R.id.dateSelector);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		mDateTextView.setText(dateFormat.format(cal.getTime()));
	}

	protected void onPause() {
		super.onPause();
		add.setVisibility(View.VISIBLE); //Make button visible again when you leave this view.
	}
	
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCAN_REQUEST_CODE) {
           if (resultCode == RESULT_OK) {
               
              String contents = intent.getStringExtra("SCAN_RESULT");
              upcResult = contents; //save away the upc you get from scanning barcode
            //  String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
           
              //display results
              Log.i("result",contents);
              Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_LONG).show();
              
              PerformUpcLookup lookup = new PerformUpcLookup();
              lookup.execute();
              
              
           } else if (resultCode == RESULT_CANCELED) {
              // Handle cancel
              Log.i("App","Scan unsuccessful");
           }
      }
   }
    
    public void scanBarcode() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, SCAN_REQUEST_CODE); 
    }
    
    public void addBarcodeItem() {
    	Food food = new Food();
        food.setName(upc_name);
        food.setExpirationDate("");
		try {
			int quantity = 0;
			food.setQuantity(quantity);
		} catch(NumberFormatException e) {	}
		food.setCategory("");
        
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.addFood(food); 
		
		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(i);
    }
	
	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());
    	
		switch (v.getId()){

		case R.id.manualBtn:
			setContentView(R.layout.add_food_manual);
			TextView mDateTextView = (TextView)findViewById(R.id.dateSelector);
			mDateTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					DialogFragment newFragment = new DatePickerDialogFragment(AddFoodActivity.this);
					newFragment.show(ft, "date_dialog");
				}
			});
			break;
		case R.id.addMan:
			Food food = new Food();
			String name = ((TextView)findViewById(R.id.nameField)).getText().toString();
			if(name.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_LONG).show();
				break;
			}
			food.setName(name);
			food.setExpirationDate(((TextView)findViewById(R.id.dateSelector)).getText().toString());
			try {
				int quantity = Integer.parseInt(((TextView)findViewById(R.id.quantityField)).getText().toString());
				food.setQuantity(quantity);
			} catch(NumberFormatException e) {	}
			food.setCategory(((TextView)findViewById(R.id.foodgroupField)).getText().toString());
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			db.addFood(food);
			
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(i);
			break;
		case R.id.scanBtn:
			scanBarcode();
			break;
		}
	}
	
	
	class PerformUpcLookup extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddFoodActivity.this);
            pDialog.setMessage("Getting food from barcode...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Performing login
         * */
        protected String doInBackground(String... args) {
        	String upc = upcResult;
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
        	params.add(new BasicNameValuePair("upc", upc));
            params.add(new BasicNameValuePair("appId", UpcApis.getNutritionixAppId()));
            params.add(new BasicNameValuePair("appKey", UpcApis.getNutritionixAppKey()));
            JSONObject json = jsonParser.makeHttpRequest(UpcApis.getNutritionixUrl(),
                    "GET", params);

            String item_description = "";
            String item_name = "";
            String brand_name = "";
            try {
				item_description = json.getString("item_description");
	            item_name = json.getString("item_name");
	            brand_name = json.getString("brand_name");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            upc_name = item_name;
            // check log cat for response
            Log.d("Create Response", json.toString());
            return item_name;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            addBarcodeItem();
        	
        }
        
      

    }
	

	
}
