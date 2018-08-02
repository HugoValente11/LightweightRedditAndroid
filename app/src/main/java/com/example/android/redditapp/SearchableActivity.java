package com.example.android.redditapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;

import android.os.Bundle;
import android.widget.Toast;

public class SearchableActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            doMySearch(query);
        }
    }


    private void doMySearch(String query) {
        Toast.makeText(this, "Working: " + query, Toast.LENGTH_SHORT).show();
    }



}


