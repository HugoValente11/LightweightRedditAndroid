package com.example.android.redditapp;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;

import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.redditapp.Connection.ConnectionManager;
import com.example.android.redditapp.Connection.Request;
import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.RecyclerView.RecyclerViewAdapter;
import com.example.android.redditapp.models.Post.Child;
import com.example.android.redditapp.models.Post.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView bodyTextView;
    private TextView authorTextView;
    private TextView subredditTextView;
    private ImageView thumbnail;
    private String subredditText;
    private String title;
    private String bodyText;
    private String authorText;
    private String imageURL;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private Cursor mPostsCursor;






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenuItem:
                launchSettings();
            case R.id.search:
                super.onSearchRequested();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);

        ConstraintLayout constraintLayout = findViewById(R.id.mainActivityLayout);
        constraintLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "This is working", Toast.LENGTH_SHORT).show();
                swipeCursor();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "This is working", Toast.LENGTH_SHORT).show();

            }
        });

        titleTextView = findViewById(R.id.titleTextView);
        bodyTextView = findViewById(R.id.bodyTextView);
        thumbnail = findViewById(R.id.thumbnail);
        authorTextView = findViewById(R.id.authorTextView);
        subredditTextView = findViewById(R.id.subredditTextView);



        // https://stackoverflow.com/questions/7965290/put-and-get-string-array-from-shared-preferences
        // Get the shared preferences file and add manually then put it again
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit().putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Put this")
                .putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Also this")

                .apply();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, Context.MODE_PRIVATE);

        setupAdapter();

        loadPost(this, Constants.someAskRedditPost);
        loadPost(this, Constants.someRedditPost);
        loadPost(this, Constants.anotherRedditPost);
        loadPost(this, Constants.anotheranotherRedditPost);

    }

    private void swipeCursor() {
        int maximum_position = mPostsCursor.getCount();
        if (mPostsCursor.getPosition() + 1 < maximum_position) {
            int position = mPostsCursor.getPosition() + 1;
            mPostsCursor.moveToPosition(position);
        } else {
            mPostsCursor.moveToFirst();
        }
        populateUI();
        mAdapter.notifyDataSetChanged();

    }

    private void setupAdapter() {
        mRecyclerView = findViewById(R.id.commentsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class );
        startActivity(intent);
    }


    public void loadPost(final Context mContext, String url) {


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));

                List<String> comments = new ArrayList<>();
                Log.d("TAG", "comments size: " + posts.get(1).getData().getChildren().size());
                for (int i = 0; i< posts.get(1).getData().getChildren().size(); i++) {
                   String comment = posts.get(1).getData().getChildren().get(i).getData().getBody();
                   if (! TextUtils.isEmpty(comment)) {
                       comments.add(comment);
                       Log.d("TAG",  "Comments after added to array list: " + comments.get(i));

                   }
                }
                Log.d("TAG", "comments array list size: " + comments.size());



                addPostsToDb(posts);
                populateUI();

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Ups, something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });


        ConnectionManager.getInstance(mContext).add(request);


    }

    private void populateUI() {
        if (mPostsCursor == null) {
            mPostsCursor = getAllPosts();
        }
        if (mPostsCursor.getPosition() == -1) {
            mPostsCursor.moveToFirst();
        }



        subredditText = mPostsCursor.getString(mPostsCursor.getColumnIndex(DatabaseContract.PostsTable.SUBREDDIT));
        subredditTextView.setText(subredditText);

        authorText = mPostsCursor.getString(mPostsCursor.getColumnIndex(DatabaseContract.PostsTable.AUTHOR));
        authorTextView.setText(authorText);

        title = mPostsCursor.getString(mPostsCursor.getColumnIndex(DatabaseContract.PostsTable.TITLE));
        titleTextView.setText(title);

        bodyText = mPostsCursor.getString(mPostsCursor.getColumnIndex(DatabaseContract.PostsTable.SELFTEXT));
        bodyTextView.setText(bodyText);

        imageURL = mPostsCursor.getString(mPostsCursor.getColumnIndex(DatabaseContract.PostsTable.IMAGELINK));
        if(!TextUtils.isEmpty(imageURL)) {
            Picasso.get().load(imageURL).into(thumbnail);
        } else {
            thumbnail.setImageResource(R.drawable.image_error);
        }

        String postID = mPostsCursor.getString(mPostsCursor.getColumnIndex(DatabaseContract.PostsTable.POSTID));
        Cursor mCommentsCursor = getAllComments(postID);

        mAdapter = new RecyclerViewAdapter(this, mCommentsCursor);
        mRecyclerView.setAdapter(mAdapter);

    }



    private void addPostsToDb(List<Post> postList) {
        Cursor mCursor = getAllPosts();

        postList.get(0).getData().getChildren().get(0).getData().getId();

        {

            boolean isInDB = false;

            // If the cursor is empty add all posts
            if(((mCursor != null) && (mCursor.getCount() > 0))) {


                mCursor.moveToFirst();

                String postIDFromCursor = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.PostsTable.POSTID));
                String postIDFromPostList = postList.get(0).getData().getChildren().get(0).getData().getId();

                do{
                    postIDFromCursor = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.PostsTable.POSTID));
                    Log.d("THIS", "thing" + postIDFromCursor);
                    if (postIDFromPostList.equals(postIDFromCursor)) {
                        Log.d("I'm alive!", "... sort of. beep boop.");
                        isInDB = true;
                    }
                }while ( mCursor.moveToNext() );

            }

            if (!isInDB) {

                ContentValues cv = new ContentValues();

                cv.put(DatabaseContract.PostsTable.SUBREDDIT, postList.get(0).getData().getChildren().get(0).getData().getSubreddit());
                cv.put(DatabaseContract.PostsTable.POSTID,    postList.get(0).getData().getChildren().get(0).getData().getId());
                cv.put(DatabaseContract.PostsTable.TITLE,     postList.get(0).getData().getChildren().get(0).getData().getTitle());
                cv.put(DatabaseContract.PostsTable.AUTHOR,    postList.get(0).getData().getChildren().get(0).getData().getAuthor());
                cv.put(DatabaseContract.PostsTable.SELFTEXT,  postList.get(0).getData().getChildren().get(0).getData().getSelftext());
                cv.put(DatabaseContract.PostsTable.IMAGELINK, postList.get(0).getData().getChildren().get(0).getData().getThumbnail());

                Uri uri = getContentResolver().insert(DatabaseContract.CONTENT_URI_POSTS, cv);

                for (int i = 0; i < postList.get(1).getData().getChildren().size() - 1; i++) {


                ContentValues cv2 = new ContentValues();

                cv2.put(DatabaseContract.CommentsTable.POSTID, postList.get(0).getData().getChildren().get(0).getData().getId());
                cv2.put(DatabaseContract.CommentsTable.COMMENTID, postList.get(1).getData().getChildren().get(i).getData().getId());
                cv2.put(DatabaseContract.CommentsTable.COMMENT,   postList.get(1).getData().getChildren().get(i).getData().getBody());
                cv2.put(DatabaseContract.CommentsTable.AUTHOR,    postList.get(1).getData().getChildren().get(i).getData().getAuthor());

                Uri uri2 = getContentResolver().insert(DatabaseContract.CONTENT_URI_COMMENTS, cv2);
                Log.d("Uri", "\nUri: " + uri + "\nUri2: " + uri2);



                }
            }
        }


    }

    private Cursor getAllPosts() {
        // fazer query de todos os filmes de uma categoria
        Cursor mCursor;
        mCursor = getContentResolver().query(DatabaseContract.CONTENT_URI_POSTS, null, null, null, null);

        String info = DatabaseUtils.dumpCursorToString(mCursor);
        Log.d("DEBUGCURSOR", info);

        return mCursor;
    }

    private Cursor getAllComments(String postID) {
        // fazer query de todos os filmes de uma categoria
        Cursor mCursor;
        String[] selectionArgs = {postID};
        String selection = DatabaseContract.CommentsTable.POSTID + " = ?";
        mCursor = getContentResolver().query(DatabaseContract.CONTENT_URI_COMMENTS, null, selection, selectionArgs, null);

        String info = DatabaseUtils.dumpCursorToString(mCursor);
        Log.d("DEBUGCURSOR", info);

        return mCursor;
    }

}
