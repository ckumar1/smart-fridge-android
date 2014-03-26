package com.example.smart_fridge_android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class IndividualRecipeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_recipe);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.individual_recipe, menu);
		return true;
	}

}
