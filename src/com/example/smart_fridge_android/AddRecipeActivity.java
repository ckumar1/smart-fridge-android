package com.example.smart_fridge_android;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.TextView.OnEditorActionListener;


public class AddRecipeActivity extends Activity {


	private Button add;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_recipe);
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
        navBar.onButtonClick(v, getApplicationContext());
        switch (v.getId()){
        
        case R.id.addingred:
        	List <String> ingredlist = new ArrayList<String>();
        	EditText mEdit; 
        	
        	mEdit = (EditText)findViewById(R.id.editText2);
        	ingredlist.add(mEdit.getText().toString());
        	mEdit.setText("");    	
        	break;
        	
        case R.id.showlist:
        	break;
        	
        case R.id.addrec:
        	break;
        	
        
        
        }
	
	
	
	}
	
}
