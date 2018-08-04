package com.example.android.redditapp.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class PostWidgetService extends IntentService {

    public static final String ACTION_OPEN_RECIPE = "com.example.android.redditapp.Widget.widget_service";

    public PostWidgetService() {
        super("RecipeWidgetService");
    }

    public PostWidgetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_RECIPE.equals(action)) {
                handleActionOpenRecipe();
            }
        }
    }

    private void handleActionOpenRecipe() {
        // Get data from shared preferences
        SharedPreferences prefs = getSharedPreferences("Title", MODE_PRIVATE);
        String title = prefs.getString("title", null);
        Log.d("TAG10", "last post clicked: " + title);


        // Build string to show in widget
//        StringBuilder stringBuilder = new StringBuilder();
//        Gson gson = new Gson();
//        List<GsonHandler> recipes = Arrays.asList(gson.fromJson(recipe, GsonHandler[].class));
//        List<Ingredient> ingredients = recipes.get(lastRecipeClicked).getIngredients();
//
//        String recipeName = recipes.get(lastRecipeClicked).getName();
//
//        stringBuilder.append(recipeName + ":\n");
//
//        for (Ingredient ingredient: ingredients) {
//            String ingredientName = ingredient.getIngredient();
//            stringBuilder.append(ingredientName + "\n");
//        }


        //TODO change ingredientsString to post title and selftext
//        String ingredientsString = stringBuilder.toString();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appIWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PostWidget.class));
        PostWidget.updateWidgetRecipe(this, title, appWidgetManager, appIWidgetIds);
    }


    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, PostWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }
}
