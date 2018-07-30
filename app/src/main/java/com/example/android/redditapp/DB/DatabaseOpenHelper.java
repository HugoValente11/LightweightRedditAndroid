package com.example.android.redditapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DEFAULT_0 = " DEFAULT 0";

    private static final String COMMA = ", ";



    // Constructor
    DatabaseOpenHelper(Context context) {
        super(context, DatabaseContract.DB_NAME, null, DATABASE_VERSION);
    }

    // Create TABLE SENTENCE
    private static final String CREATE_MOVIE_TABLE = "CREATE TABLE " +
            DatabaseContract.PostsTable.TABLE_NAME + " (" +
            DatabaseContract.PostsTable.SUBREDDIT + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.POSTID + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.TITLE + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.IMAGELINK + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.AUTHOR + TEXT_TYPE + COMMA +
//            DatabaseContract.PostsTable.SELFTEXT + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.SELFTEXT + TEXT_TYPE + " )";

//            Integer to boolean 1 is true is favorite 0 is false
//            https://stackoverflow.com/questions/843780/store-boolean-value-in-sqlite

    // Delete TABLE SENTENCE
    public static final String DROP_MOVIE_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.PostsTable.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_MOVIE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
