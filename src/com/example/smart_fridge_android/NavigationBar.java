package com.example.smart_fridge_android;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class NavigationBar {

    SessionManager session;

	public NavigationBar() {

	}

	public void onButtonClick(View v, Context c) {
		
		if (c.getClass() == AddFoodActivity.class)
			return; //Already in the desired Activity, possibly display Toast message?
		
		switch (v.getId()){

            case R.id.addBtn:
                Intent add_food_intent = new Intent(c, AddFoodActivity.class);
                add_food_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(add_food_intent);
                break;

            case R.id.logoutBtn:
                session = new SessionManager(c);
                session.logoutUser();
                Intent logout_intent = new Intent(c, LoginActivity.class);
                logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(logout_intent);
                break;
		}
	}
}
