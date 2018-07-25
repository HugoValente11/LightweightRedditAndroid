package com.example.android.redditapp.Connection;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ConnectionManager {   private static RequestQueue sQueue;

    private static ImageLoader sImageLoader;

    public static RequestQueue getInstance (Context context) {
        if (sQueue  == null) {
            sQueue = Volley.newRequestQueue(context);
        }

        return sQueue;
    }
