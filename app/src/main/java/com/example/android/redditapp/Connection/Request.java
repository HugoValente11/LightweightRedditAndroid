package com.example.android.redditapp.Connection;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.redditapp.models.Post.Post;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class Request {

    private Context mContext;
    private String jsonURL;

    public Request(Context context, String url) {
        mContext = context;
        jsonURL = url;
    }

    private void loadPost() {


    StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, jsonURL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Gson gson = new Gson();

            List<Post> post = Arrays.asList(gson.fromJson(response, Post.class));

            post.get(0).getData().getChildren().get(0).getData().getThumbnail();

//            if (RecipeCardsName.getRecipeCards().size() < recipeCards.size()) {
//                RecipeCardsName.getRecipeCards().clear();
//                // https://kylewbanks.com/blog/tutorial-parsing-json-on-android-using-gson-and-volley
//                // Getting names from Gson
//                for (GsonHandler recipeCard : recipeCards) {
//                    Log.v("RecipeCard", recipeCard.getName() + ": " + recipeCard.getId());
//                    Log.v("RECIPECARD", "Before adding: " + RecipeCardsName.getRecipeCards().size());
//                    RecipeCardsName.addRecipeCards(recipeCard);
//                    RecipeCardsName.addRecipeName(recipeCard.getName());
//                    Log.v("RECIPECARD", "After adding: " + RecipeCardsName.getRecipeCards().size());
//                    Log.v("RECIPECARD", "Line Breaker");
//                }
//            }





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
