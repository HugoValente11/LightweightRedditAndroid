package com.example.android.redditapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.redditapp.Connection.ConnectionManager;
import com.example.android.redditapp.ListAdapterSubReddits.CustomSearchAdapter;
import com.example.android.redditapp.models.Subreddit.SubReddit;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
        subRedditsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SearchableActivity.this,
                        "clicked search result item is: "+ ((TextView)view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        handleSearch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch();
    }


    private void handleSearch() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);

            doQuery(searchQuery);

//            CustomSearchAdapter adapter = new CustomSearchAdapter(this,
//                    android.R.layout.simple_dropdown_item_1line,
//                    // Add list
//                    StoresData.filterData(searchQuery));
//            subRedditsListView.setAdapter(adapter);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String selectedSuggestionRowId = intent.getDataString();
            //execution comes here when an item is selected from search suggestions
            //you can continue from here with user selected search item
            Toast.makeText(this, "selected search suggestion " + selectedSuggestionRowId,
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void doQuery(String searchQuery) {
        String subredditURL = Constants.subRedditBase + searchQuery + Constants.limitTo10;
        loadSubreddit(subredditURL);

    }

    public void loadSubreddit(String url) {


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();

                SubReddit subreddit = gson.fromJson(response, SubReddit.class);
                List<String> subReddits= new ArrayList<>();
                StringBuilder stringBuilder = new StringBuilder();


                for (int i = 0; i < subreddit.getData().getChildren().size(); i++) {
                    subReddits.add(subreddit.getData().getChildren().get(i).getData().getDisplayName());
                    stringBuilder.append(subReddits.get(i) + "\n");

                }

                String subreddts = stringBuilder.toString();
                Log.d("Subreddits size", "Subreddits size: " + subreddts);

                            CustomSearchAdapter adapter = new CustomSearchAdapter(SearchableActivity.this,
                    android.R.layout.simple_dropdown_item_1line,
                    subReddits);
            subRedditsListView.setAdapter(adapter);

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchableActivity.this, "Please check your Internet connection.", Toast.LENGTH_SHORT).show();
            }
        });


        ConnectionManager.getInstance(this).add(request);

    }
}


