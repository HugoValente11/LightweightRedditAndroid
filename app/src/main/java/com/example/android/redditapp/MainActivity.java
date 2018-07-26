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
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnFragmentInteractionListener{


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

        // https://stackoverflow.com/questions/7965290/put-and-get-string-array-from-shared-preferences
        // Get the shared preferences file and add manually then put it again
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit().putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Put this")
                .putString(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, "Also this")

                .apply();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENES_SUBREDDITS_KEY, Context.MODE_PRIVATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void launchSettings() {
        Intent intent = new Intent(this, SettingsActivity.class );
        startActivity(intent);
    }

}
