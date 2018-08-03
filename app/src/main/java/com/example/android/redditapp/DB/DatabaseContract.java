package com.example.android.redditapp.DB;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    // The name of the database
    public static final String DB_NAME = "reddit_app_db";

    public static final String CONTENT_AUTHORITY = "com.example.android.redditapp";
    public static final String PATH_POSTS = "post";
    public static final String PATH_COMMENTS = "comments";
    public static final String PATH_POSTS_COMMENTS = "postscomments";
    public static final String PATH_SUBREDDITS = "subreddits";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI_POSTS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS).build();
    public static final Uri CONTENT_URI_COMMENTS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENTS).build();
    public static final Uri CONTENT_URI_POSTS_COMMENTS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POSTS_COMMENTS).build();
    public static final Uri CONTENT_URI_SUBREDDITS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBREDDITS).build();


    public abstract class PostsCommentsTable implements BaseColumns {

        public static final String TABLE_NAME = "posts_comments_table";

        public static final String POSTID = "postid";
        public static final String COMMENTID = "commentid";

    }



    public abstract class PostsTable implements BaseColumns{

        public static final String TABLE_NAME = "posts_table";

        public static final String SUBREDDIT = "subreddit";
        public static final String POSTID = "postid";
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String IMAGELINK = "imagelink";
        public static final String SELFTEXT = "selftext";

    }

    public abstract class CommentsTable implements BaseColumns{

        public static final String TABLE_NAME = "comments_table";

        public static final String POSTID = "postid";
        public static final String COMMENTID = "commentid";
        public static final String COMMENT = "comment";
        public static final String AUTHOR = "author";

    }

    public abstract class SubRedditsTable implements BaseColumns {

        public static final String TABLE_NAME = "subreddits";

        public static final String SUBREDDIT = "subreddit";


    }
}
