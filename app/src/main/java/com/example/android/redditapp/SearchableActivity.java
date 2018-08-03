package com.example.android.redditapp;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.ListAdapterSubReddits.CustomSearchAdapter;
import com.example.android.redditapp.RecyclerView.SubscribedSubredditsRecyclerViewAdapter;
import com.example.android.redditapp.models.Subreddit.SubReddit;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

// http://www.zoftino.com/android-search-dialog-with-search-suggestions-example
public class SearchableActivity extends AppCompatActivity implements SearchClickHandler{
    private RecyclerView mRecyclerView;
    private CustomSearchAdapter mAdapter;
    private static SearchClickHandler mClickHandler;

    @Override
    public void onClick(int position) {
        Toast.makeText(this, "Position clicked: " + position, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        mRecyclerView = findViewById(R.id.subredditsListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        Log.d("TAG", "I'm alive... Sort of.");

//        // Add click listener add adapter
//        subRedditsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            // Add here the subreddit
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String subreddit = ((TextView)view).getText().toString();
//
//                checkIfInDBIfNotAdd(subreddit);
//
//            }
//        });

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

                for (int i=0; i < subreddit.getData().getChildren().size(); i++) {
                    subReddits.add(subreddit.getData().getChildren().get(i).getData().getDisplayName());
                }
                mAdapter = new CustomSearchAdapter(SearchableActivity.this, SearchableActivity.this::onClick, subReddits);
                mRecyclerView.setAdapter(mAdapter);

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

    private void checkIfInDBIfNotAdd (String subreddit) {

        String subredditFromCursor;

        Cursor mCursor =
                getContentResolver().query(DatabaseContract.CONTENT_URI_SUBREDDITS, null, null, null, null);

        boolean isInDB = false;

            // If the cursor is empty add all posts
            if(((mCursor != null) && (mCursor.getCount() > 0))) {


                mCursor.moveToFirst();



                do{
                    subredditFromCursor = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.SubRedditsTable.SUBREDDIT));
                    Log.d("THIS", "thing" + subreddit);
                    if (subredditFromCursor.equals(subreddit)) {
                        Toast.makeText(this, "Subreddit already added.", Toast.LENGTH_SHORT).show();
                        isInDB = true;
                    }
                }while ( mCursor.moveToNext() );

            }

            if (!isInDB) {

                ContentValues cv = new ContentValues();

                cv.put(DatabaseContract.SubRedditsTable.SUBREDDIT, subreddit);


                Uri uri = getContentResolver().insert(DatabaseContract.CONTENT_URI_SUBREDDITS, cv);
                Toast.makeText(this, "Subreddit successfully added.", Toast.LENGTH_SHORT).show();
            }
    }

}


