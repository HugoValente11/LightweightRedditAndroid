package com.example.android.redditapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://stackoverflow.com/questions/7965290/put-and-get-string-array-from-shared-preferences
        // Get the shared preferences file and add manually then put it again
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit().putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Put this")
                .putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Also this")

                .apply();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, Context.MODE_PRIVATE);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
