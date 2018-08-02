package com.example.android.redditapp;

import android.app.SearchManager;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

// http://www.zoftino.com/android-search-dialog-with-search-suggestions-example
public class SearchableActivity extends AppCompatActivity {
    private ListView subRedditsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        subRedditsListView = findViewById(R.id.subredditsListView);

        Log.d("TAG", "I'm alive... Sort of.");
        Toast.makeText(this, "I'm alive... Sort of.", Toast.LENGTH_SHORT).show();

        // Add click listener add adapter
//        subRedditsListView.setOnClickListener(new AdapterView);

//        handleSearch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
//        handleSearch();
    }


//    private void handleSearch() {
//        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
//
//            CustomSearchAdapter adapter = new CustomSearchAdapter(this,
//                    android.R.layout.simple_dropdown_item_1line,
//                    StoresData.filterData(searchQuery));
//            listView.setAdapter(adapter);
//
//        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
//            String selectedSuggestionRowId = intent.getDataString();
//            //execution comes here when an item is selected from search suggestions
//            //you can continue from here with user selected search item
//            Toast.makeText(this, "selected search suggestion " + selectedSuggestionRowId,
//                    Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
}


