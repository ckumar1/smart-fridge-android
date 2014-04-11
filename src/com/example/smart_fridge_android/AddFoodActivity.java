package com.example.smart_fridge_android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.List;

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
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class AddFoodActivity extends Activity implements OnDateSetListener{

    private static final String STARTING_TAB = "startingTab";
    static final int CAMERA_RESULT = 1;
    private Uri imageUri; //This is the uri we get from taking a photo and storing it
    private String imagePath;
    private int SCAN_REQUEST_CODE = 0;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String upc_name = "";
	private String upcResult = "";

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_food);
        removeAddButton();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
		TextView mDateTextView = (TextView)findViewById(R.id.dateSelector);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		mDateTextView.setText(dateFormat.format(cal.getTime()));
	}

	public void onButtonClick(View view) {
		NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(view, getApplicationContext());
        NavigationBar.onTabsClicked(view, this);

		switch (view.getId()){

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
            case R.id.imageBtn:
                //only make intent if phone has a camera
                if (Camera.getNumberOfCameras() > 0) {
                    dispatchTakePictureIntent();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No camera found", Toast.LENGTH_LONG).show();
                }
    			break;
            case R.id.addMan:
                Food food = new Food();
                String name = ((TextView)findViewById(R.id.nameField)).getText().toString();
                if(name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Name is required", Toast.LENGTH_LONG).show();
                    break;
                }
                if(name.contains("<") && name.contains(">")){
                    Toast.makeText(getApplicationContext(), "No tags (<>) allowed", Toast.LENGTH_LONG).show();
                    break;
                }
                food.setName(name);
                food.setExpirationDate(((TextView)findViewById(R.id.dateSelector)).getText().toString());
                
                if (imagePath != null && !imagePath.isEmpty()) {
                    food.setImagePath(imagePath);
    			}
                else {
    				food.setImagePath("");
    			}
                
                try {
                    int quantity = Integer.parseInt(((TextView)findViewById(R.id.quantityField)).getText().toString());
                    food.setQuantity(quantity);
                } catch(NumberFormatException e) {	}
                food.setCategory(((TextView)findViewById(R.id.foodgroupField)).getText().toString());
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.addFood(food);

                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(STARTING_TAB, "food");
                this.startActivity(i);
                break;
            case R.id.scanBtn:
    			scanBarcode();
    			break;
		}

        removeAddButton();
	}
	
	private void dispatchTakePictureIntent() {
		//camera stuff
		Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

		//folder stuff
		File imagesFolder = new File(Environment.getExternalStorageDirectory(), "fridge_images");
		imagesFolder.mkdirs();

		//filePath = "/fridge_images/food_image" + timeStamp + ".png" ;
		File image = new File(imagesFolder, "food_image" + timeStamp + ".png");
		Uri uriSavedImage = Uri.fromFile(image);

		imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
		imagePath = image.toString();
		startActivityForResult(imageIntent, CAMERA_RESULT);
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

    private void removeAddButton(){
        Button addButton = (Button) findViewById(R.id.addBtn);
        addButton.setVisibility(View.INVISIBLE);
    }
    
    public void scanBarcode() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, SCAN_REQUEST_CODE); 
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
