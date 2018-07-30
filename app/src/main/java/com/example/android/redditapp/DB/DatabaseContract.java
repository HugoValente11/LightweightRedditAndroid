package com.example.android.redditapp.DB;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    // The name of the database
    public static final String DB_NAME = "reddit_app_db";

    public static final String CONTENT_AUTHORITY = "com.example.android.redditapp";
    public static final String PATH_POSTS = "post";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS).build();




    public abstract class PostsTable implements BaseColumns{

        public static final String TABLE_NAME = "posts_table";

        public static final String SUBREDDIT = "subreddit";
        public static final String POSTID = "postid";
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String IMAGELINK = "imagelink";
        public static final String SELFTEXT = "selftext";


    }
}
