package com.example.smart_fridge_android;

import android.app.ListActivity;
import android.os.Bundle;

public class RecipeSearchResultsActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_search_results);
    }
}
