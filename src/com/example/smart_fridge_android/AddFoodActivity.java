package com.example.smart_fridge_android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class AddFoodActivity extends Activity implements OnDateSetListener{

    private static final String STARTING_TAB = "startingTab";

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

	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
        navBar.onButtonClick(v, getApplicationContext());

		switch (v.getId()){

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
                if(name.contains("<") && name.contains(">")){
                    Toast.makeText(getApplicationContext(), "No tags (<>) allowed", Toast.LENGTH_LONG).show();
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
                i.putExtra(STARTING_TAB, "food");
                this.startActivity(i);
                break;
		}

        removeAddButton();
	}

    private void removeAddButton(){
        Button addButton = (Button) findViewById(R.id.addBtn);
        addButton.setVisibility(View.INVISIBLE);
    }
}
