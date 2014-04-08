package com.example.smart_fridge_android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeSearchResultsActivity extends ListActivity {

    private static final String TAG_RECIPE_ID = "recipe_id";
    private static final String TAG_RECIPE_NAME = "name";

    ListAdapter adapter;
    List recipesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_search_results);
    }

    /**
     * Shows the recipe results list
     **/
    private void setResultsAdapter(List<String> recipeNames){

        List<HashMap<String, String>> recipesList = new ArrayList<HashMap<String, String>>();

        for (String recipe : recipeNames){

            HashMap<String, String> map = new HashMap<String, String>();

            map.put(TAG_RECIPE_NAME, recipe);
            recipesList.add(map);
        }

        adapter = new SimpleAdapter(RecipeSearchResultsActivity.this, recipesList, R.layout.recipe_list_item,
                new String[] {TAG_RECIPE_ID, TAG_RECIPE_NAME}, new int[] {R.id.recipe_id, R.id.recipe_name});

        setListAdapter(adapter);
        setListView();
    }

    /**
     * Sets the proper ListView for the results being viewed.
     */
    private void setListView(){
        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

            }
        });
    }
}
