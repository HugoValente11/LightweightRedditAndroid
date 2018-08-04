package com.example.android.redditapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.redditapp.MainActivity;
import com.example.android.redditapp.R;

public class PostWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String title) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.post_widget);

        Log.d("VIEWS", "PACKAGE: " + views.getPackage());


        // Create the intent
        Intent intent = new Intent(context, MainActivity.class);

        // Create the pending intent that will wrap our intent
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context,0,intent,0);

        if (title.equals("")) {
            title = "Nothing to show";
        }

        Log.d("TAG10", "Recipe ingredients: " + title );

        // Change to adequate text
        views.setTextViewText(R.id.appwidget_text, title);

        // OnClick intent for textview
        views.setOnClickPendingIntent(R.id.relativeLayoutWidget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PostWidgetService.startActionOpenRecipe(context);
    }

    public static void updateWidgetRecipe(Context context, String json, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, json);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

