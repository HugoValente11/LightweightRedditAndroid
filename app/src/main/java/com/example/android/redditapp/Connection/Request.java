package com.example.android.redditapp.Connection;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.android.redditapp.Constants;
import com.example.android.redditapp.models.Post.Post;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.android.redditapp.models.Subreddit.SubReddit;
import com.google.gson.Gson;


import java.util.Arrays;
import java.util.List;


public class Request {


    private static String thumbnail;
    private static String title;
    private static String description;
    private static String listOfSubreddits;



    public static String loadSubreddit(Context mContext, String url) {


    StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Gson gson = new Gson();

            SubReddit subreddit = gson.fromJson(response, SubReddit.class);
            StringBuilder stringBuilder = new StringBuilder();


            for (int i = 0; i < subreddit.getData().getChildren().size(); i++) {
                description = subreddit.getData().getChildren().get(i).getData().getDisplayName();
                stringBuilder.append(description + "\n");

                Log.d("SUBREDDITDESCRIPTION", description);
        }
           listOfSubreddits = stringBuilder.toString();
            Constants.setListOfSubreddits(listOfSubreddits);
        }
    }
            , new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    });


        ConnectionManager.getInstance(mContext).add(request);
            return listOfSubreddits;





    }

    public static String loadPost(final Context mContext, String url) {


        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();

                PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("JSON", response).apply();


            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        ConnectionManager.getInstance(mContext).add(request);
        return listOfSubreddits;





    }
}
