package com.example.android.redditapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.RecyclerView.RecyclerViewAdapter;
import com.example.android.redditapp.RecyclerView.SubscribedSubredditsRecyclerViewAdapter;

public class SettingsSubredditsActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    SubscribedSubredditsRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_subreddits);

        mRecyclerView = findViewById(R.id.subscribedSubredditsListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        queryCursor();

    }

    private void queryCursor() {
        Cursor mCursor = getContentResolver().query(DatabaseContract.CONTENT_URI_SUBREDDITS, null, null, null, null);


        mAdapter = new SubscribedSubredditsRecyclerViewAdapter(this, mCursor);
        mRecyclerView.setAdapter(mAdapter);
    }
}
