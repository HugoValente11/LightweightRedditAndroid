package com.example.android.redditapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
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
import com.example.android.redditapp.models.Post.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

        loadPost(this, Constants.anotherRedditPost);

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

                imageURL =  posts.get(0).getData().getChildren().get(0).getData().getThumbnail();
                if( ! TextUtils.isEmpty(imageURL)) {
                    Picasso.get().load(imageURL).into(thumbnail);
                }



            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        ConnectionManager.getInstance(mContext).add(request);


    }
}
