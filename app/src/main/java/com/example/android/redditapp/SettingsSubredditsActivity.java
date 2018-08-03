package com.example.android.redditapp;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.RecyclerView.RecyclerViewAdapter;
import com.example.android.redditapp.RecyclerView.SubscribedSubredditsRecyclerViewAdapter;

public class SettingsSubredditsActivity extends AppCompatActivity implements SubscribedSubredditsRecyclerViewAdapter.CursorAdapterOnClickHandler{

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

        String cursorInfo = DatabaseUtils.dumpCursorToString(mCursor);
        Log.d("CURSORINFO", cursorInfo);


        mAdapter = new SubscribedSubredditsRecyclerViewAdapter(this, mCursor, this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onClick(long id) {
        Uri subredditUri = DatabaseContract.CONTENT_URI_SUBREDDITS.buildUpon()
                .appendPath(Long.toString(id)).build();
        Cursor cursor = getContentResolver().query(subredditUri, null, null, null, null);
        cursor.moveToFirst();
        String subredditTitle = cursor.getString(cursor.getColumnIndex(DatabaseContract.SubRedditsTable.SUBREDDIT));
        Toast.makeText(this, "Subreddit clicked: " + subredditTitle, Toast.LENGTH_SHORT).show();
        getContentResolver().delete(subredditUri, null, null);
        mAdapter.notifyDataSetChanged();
    }
}
