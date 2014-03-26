package com.example.smart_fridge_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.app.Activity;


public class AddRecipeActivity extends Activity {


	private Button add;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_recipe);
		add = (Button) findViewById(R.id.addBtn);
        if (add != null)
        	add.setVisibility(View.INVISIBLE);
	}

	
}
