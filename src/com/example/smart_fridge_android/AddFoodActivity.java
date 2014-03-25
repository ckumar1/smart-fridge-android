package com.example.smart_fridge_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddFoodActivity extends Activity {
	
	private Button add;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food);
        add = (Button) findViewById(R.id.addBtn);
        if (add != null)
        	add.setVisibility(View.INVISIBLE);
    }
	
	protected void onPause() {
		super.onPause();
		add.setVisibility(View.VISIBLE); //Make button visible again when you leave this view.
	}
	
	public void onButtonClick(View v) {
		NavigationBar navBar = new NavigationBar();
    	
		switch (v.getId()){
		
		case R.id.addBtn:
		case R.id.logoutBtn:
    		navBar.onButtonClick(v, this);
    		break;
		}
	}
}
