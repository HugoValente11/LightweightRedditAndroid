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

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Create another one
    private static final String CREATE_POST_COMMENT = "CREATE TABLE " +
            DatabaseContract.Posts_Comments.TABLE_NAME + " (" +
            DatabaseContract.Posts_Comments.POSTID + TEXT_TYPE + COMMA +
            DatabaseContract.Posts_Comments.COMMENTID + TEXT_TYPE + COMMA +
            "FOREIGN KEY (" + DatabaseContract.Posts_Comments.POSTID + ") REFERENCES " + DatabaseContract.PostsTable.TABLE_NAME +
            "(" + DatabaseContract.PostsTable.POSTID + ") " + COMMA +
            "FOREIGN KEY (" + DatabaseContract.Posts_Comments.COMMENTID + ") REFERENCES " + DatabaseContract.CommentsTable.TABLE_NAME +
            "(" + DatabaseContract.CommentsTable.COMMENTID + ") )";


    // Create POST TABLE SENTENCE
    private static final String CREATE_POST_TABLE = "CREATE TABLE " +
            DatabaseContract.PostsTable.TABLE_NAME + " (" +
            DatabaseContract.PostsTable.SUBREDDIT + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.POSTID + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.TITLE + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.IMAGELINK + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.AUTHOR + TEXT_TYPE + COMMA +
            DatabaseContract.PostsTable.SELFTEXT + TEXT_TYPE + COMMA +
            "PRIMARY KEY (" + DatabaseContract.PostsTable.POSTID + "))";


    // Create COMMENT TABLE SENTENCE
    private static final String CREATE_COMMENT_TABLE = "CREATE TABLE " +
            DatabaseContract.CommentsTable.TABLE_NAME + " (" +
            DatabaseContract.CommentsTable.COMMENTID + TEXT_TYPE + COMMA +
            DatabaseContract.CommentsTable.AUTHOR + TEXT_TYPE + COMMA +
            DatabaseContract.CommentsTable.COMMENT + TEXT_TYPE + COMMA +
            "PRIMARY KEY (" + DatabaseContract.CommentsTable.COMMENTID + "))";




    // Delete TABLES SENTENCE
    public static final String DROP_POST_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.PostsTable.TABLE_NAME;
    public static final String DROP_COMMENT_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.CommentsTable.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_POST_TABLE);
        sqLiteDatabase.execSQL(CREATE_COMMENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_POST_COMMENT);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_POST_TABLE);
        sqLiteDatabase.execSQL(DROP_COMMENT_TABLE);

        onCreate(sqLiteDatabase);
    }
}
