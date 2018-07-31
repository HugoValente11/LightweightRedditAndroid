package com.example.android.redditapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.redditapp.Connection.ConnectionManager;
import com.example.android.redditapp.Connection.Request;
import com.example.android.redditapp.DB.DatabaseContract;
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
    private ImageView thumbnail;
    private String title;
    private String bodyText;
    private String authorText;
    private String imageURL;






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenuItem:
                launchSettings();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.titleTextView);
        bodyTextView = findViewById(R.id.bodyTextView);
        thumbnail = findViewById(R.id.thumbnail);
        authorTextView = findViewById(R.id.authorTextView);


        // https://stackoverflow.com/questions/7965290/put-and-get-string-array-from-shared-preferences
        // Get the shared preferences file and add manually then put it again
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit().putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Put this")
                .putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Also this")

                .apply();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, Context.MODE_PRIVATE);

        loadPost(this, Constants.someRedditPost);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

                title =  posts.get(0).getData().getChildren().get(0).getData().getTitle();
                titleTextView.setText(title);

                authorText =  posts.get(0).getData().getChildren().get(0).getData().getAuthor();
                authorTextView.setText(authorText);

                bodyText =  posts.get(0).getData().getChildren().get(0).getData().getSelftext();
                bodyTextView.setText(bodyText);

                List<String> comments = new ArrayList<>();
                for (int i = 0; i< posts.get(1).getData().getChildren().size(); i++) {
                   String comment = posts.get(1).getData().getChildren().get(i).getData().getBody();
                    comments.add(comment);
                }

                imageURL =  posts.get(0).getData().getChildren().get(0).getData().getThumbnail();
                if( ! TextUtils.isEmpty(imageURL)) {
                    Picasso.get().load(imageURL).into(thumbnail);
                }

                addMovieToDb(posts);


            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        ConnectionManager.getInstance(mContext).add(request);


    }

    private void addMovieToDb(List<Post> postList) {

        Cursor mCursor = getAllPosts();

        for (Post post : postList) {

            boolean isInDB = false;

            // If the cursor is empty add all posts
            if(((mCursor != null) && (mCursor.getCount() > 0))) {


                mCursor.moveToFirst();

                Log.d("First1", "Post Id: " + mCursor.getColumnIndex(DatabaseContract.PostsTable.POSTID));

                Log.d("First2", "Post Id: " + postList.get(0).getData().getChildren().get(0).getData().getId());

                while (mCursor.moveToNext()) {
                    Log.d("Main1", "Post id: " + mCursor.getColumnIndex(DatabaseContract.PostsTable.POSTID));
                    Log.d("Main2", "Post position: " + post.getData().getChildren().get(0).getData().getId());

                    if (post.getData().getChildren().get(0).getData().getId().equals((mCursor.getString(mCursor.getColumnIndex(DatabaseContract.PostsTable.POSTID))))) {
                        isInDB = true;
                    }
                }

            }

            if (!isInDB) {

                ContentValues cv = new ContentValues();

                cv.put(DatabaseContract.PostsTable.SUBREDDIT, post.getData().getChildren().get(0).getData().getSubreddit());
                cv.put(DatabaseContract.PostsTable.POSTID, post.getData().getChildren().get(0).getData().getId());
                cv.put(DatabaseContract.PostsTable.TITLE, post.getData().getChildren().get(0).getData().getTitle());
                cv.put(DatabaseContract.PostsTable.AUTHOR, post.getData().getChildren().get(0).getData().getAuthor());
                cv.put(DatabaseContract.PostsTable.SELFTEXT, post.getData().getChildren().get(0).getData().getSelftext());
                cv.put(DatabaseContract.PostsTable.IMAGELINK, post.getData().getChildren().get(0).getData().getThumbnail());

                Uri uri = getContentResolver().insert(DatabaseContract.CONTENT_URI, cv);
                Log.d("Uri", "Uri: " + uri);
            }
        }


    }

    private Cursor getAllPosts() {
        // fazer query de todos os filmes de uma categoria
        Cursor mCursor;
        mCursor = getContentResolver().query(DatabaseContract.CONTENT_URI, null, null, null, null);

        String info = DatabaseUtils.dumpCursorToString(mCursor);
        Log.d("DEBUGCURSOR", info);

        return mCursor;
    }

}
