package com.example.smart_fridge_android;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

import com.semantics3.api.Products;


public class AddFoodActivity extends Activity implements OnDateSetListener{

	private Button add;
	private int SCAN_REQUEST_CODE = 0;
	private String upcResult = "";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    Products products = new Products(
    	    "SEM31C11F4C612B042B3F274453FB309DB59", //API KEY
    	    "OTkwNjg1NTQxZTg4OGY3YmQ4YjY1OGE2N2E1MzZhMDg" //API SECRET
    	);
    
    private String APP_ID = "5538930e";
    private String APP_KEY = "e5e7dd3f04da8c55a349ac6ed45c7b47"; //nutritionix
    private static String url_api = "https://api.nutritionix.com/v1_1/item";

	
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
       /* Uri uriUrl = Uri.parse("http://zxing.appspot.com/scan");
        Intent scanInBrowser= new Intent(Intent.ACTION_VIEW, uriUrl);
        scanInBrowser.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(scanInBrowser, 0); */
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, SCAN_REQUEST_CODE); 
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
            params.add(new BasicNameValuePair("appId", APP_ID));
            params.add(new BasicNameValuePair("appKey", APP_KEY));
            JSONObject json = jsonParser.makeHttpRequest(url_api,
                    "GET", params);

            // check log cat for response
            Log.d("Create Response", json.toString());
            //semanticsQuery();
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            
            // dismiss the dialog once done
            pDialog.dismiss();
        }
        
        protected void semanticsQuery() {
        	String upc = upcResult;
            
            // Building Parameters
            products.productsField("upc",upc); //add parameters to the query

            JSONObject results = null;
            try {
				results = products.getProducts();

			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            Log.d("UPC Results", results.toString());
        }
        
      

    }
	
}
