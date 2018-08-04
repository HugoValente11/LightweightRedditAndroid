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
import com.example.android.redditapp.models.PostsID.PostsID;
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
    private SearchView searchView;
    private String querySearchView;

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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String query;
        if (searchView.getQuery().toString() != null) {
            query = searchView.getQuery().toString();
            Log.d("QUERY", "This log: " + query);
            outState.putString("svkey", query);

        }


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            querySearchView = savedInstanceState.getString("svkey");
        }

        ConstraintLayout constraintLayout = findViewById(R.id.mainActivityLayout);
        constraintLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                swipeCursor();
            }

            @Override
            public void onSwipeRight() {

            }
        });

        titleTextView = findViewById(R.id.titleTextView);
        bodyTextView = findViewById(R.id.bodyTextView);
        thumbnail = findViewById(R.id.thumbnail);
        authorTextView = findViewById(R.id.authorTextView);
        subredditTextView = findViewById(R.id.subredditTextView);

        setupAdapter();

        String url = Constants.searchPostsBase + "/soccer" + Constants.jsonExtension + Constants.limitTo5;
        loadPostsID(this, url);

//        loadPost(this, Constants.someAskRedditPost);
//        loadPost(this, Constants.someRedditPost);
//        loadPost(this, Constants.anotherRedditPost);
//        loadPost(this, Constants.anotheranotherRedditPost);



    }

    private void swipeCursor() {
        // Get info from cursor
        if (mPostsCursor != null) {
        String postID = mPostsCursor.getString(mPostsCursor.getColumnIndex(DatabaseContract.PostsTable.POSTID));

        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.PostsTable.POSTSEEN, 1);
        cv.put(DatabaseContract.PostsTable.POSTID, postID);
        String selection = DatabaseContract.PostsTable.POSTID + " = ?";
        String[] selectionArgs = new String[]{postID};

        // Update value to know post has been seen
        getContentResolver().update(DatabaseContract.CONTENT_URI_POSTS, cv, selection, selectionArgs);


        int maximum_position = mPostsCursor.getCount();
        if (mPostsCursor.getPosition() + 1 < maximum_position) {
           int position = mPostsCursor.getPosition() + 1;
            mPostsCursor.moveToPosition(position);
            populateUI();

        } else {
            // Load more posts
            Toast.makeText(this, "No more posts to load, maybe you should consider getting some Sun.", Toast.LENGTH_SHORT).show();

        }

        }

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

        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    querySearchView = s;
                    return true;
                }
            });
        }
        if (querySearchView != null) {
            searchView.setIconified(false);
            searchView.setQuery(querySearchView, false);
        }

        return true;
    }

    private void launchSettings() {
        Intent intent = new Intent(this, SettingsSubredditsActivity.class );
        startActivity(intent);
    }


    public void loadPost(final Context mContext, String url) {


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                List<Post> posts = Arrays.asList(gson.fromJson(response, Post[].class));

//                List<String> comments = new ArrayList<>();
//                Log.d("TAG", "comments size: " + posts.get(1).getData().getChildren().size());
//                for (int i = 0; i< posts.get(1).getData().getChildren().size(); i++) {
//                   String comment = posts.get(1).getData().getChildren().get(i).getData().getBody();
//                   if (! TextUtils.isEmpty(comment)) {
//                       comments.add(comment);
//                       Log.d("TAG",  "Comments after added to array list: " + comments.get(i));
//
//                   }
//                }
//                Log.d("TAG", "comments array list size: " + comments.size());



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

                // CV is post info
                ContentValues cv = new ContentValues();

                cv.put(DatabaseContract.PostsTable.SUBREDDIT, postList.get(0).getData().getChildren().get(0).getData().getSubreddit());
                cv.put(DatabaseContract.PostsTable.POSTID,    postList.get(0).getData().getChildren().get(0).getData().getId());
                cv.put(DatabaseContract.PostsTable.TITLE,     postList.get(0).getData().getChildren().get(0).getData().getTitle());
                cv.put(DatabaseContract.PostsTable.AUTHOR,    postList.get(0).getData().getChildren().get(0).getData().getAuthor());
                cv.put(DatabaseContract.PostsTable.SELFTEXT,  postList.get(0).getData().getChildren().get(0).getData().getSelftext());
                cv.put(DatabaseContract.PostsTable.IMAGELINK, postList.get(0).getData().getChildren().get(0).getData().getThumbnail());

                getContentResolver().insert(DatabaseContract.CONTENT_URI_POSTS, cv);

                for (int i = 0; i < postList.get(1).getData().getChildren().size() - 1; i++) {

                // CV2 is comments post info
                ContentValues cv2 = new ContentValues();

                cv2.put(DatabaseContract.CommentsTable.POSTID, postList.get(0).getData().getChildren().get(0).getData().getId());
                cv2.put(DatabaseContract.CommentsTable.COMMENTID, postList.get(1).getData().getChildren().get(i).getData().getId());
                cv2.put(DatabaseContract.CommentsTable.COMMENT,   postList.get(1).getData().getChildren().get(i).getData().getBody());
                cv2.put(DatabaseContract.CommentsTable.AUTHOR,    postList.get(1).getData().getChildren().get(i).getData().getAuthor());

                getContentResolver().insert(DatabaseContract.CONTENT_URI_COMMENTS, cv2);

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

    public void loadPostsID(final Context mContext, String url) {


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                PostsID posts = gson.fromJson(response, PostsID.class);


                for (int i=0; i < posts.getData().getChildren().size(); i++) {
                   String postID = posts.getData().getChildren().get(i).getData().getId();
                   String postURL = Constants.searchPostsBase + "/soccer" +  Constants.commentsExtension + postID + Constants.jsonExtension;
                   Log.d("POSTURL", postURL);
                    loadPost(MainActivity.this, postURL );

                }

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

}
