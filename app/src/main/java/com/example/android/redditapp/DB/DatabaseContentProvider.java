package com.example.android.redditapp.DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.redditapp.models.Post.Data;

public class DatabaseContentProvider extends ContentProvider {

    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int POSTS = 100;
    public static final int COMMENTS = 200;
    public static final int POSTS_COMMENTS = 300;
    public static final int SUBREDDITS = 400;



    public static final int POSTS_WITH_ID = 101;
    public static final int COMMENTS_WITH_ID = 201;
    public static final int POSTS_COMMENTS_WITH_ID = 301;
    public static final int SUBREDDITS_WITH_ID = 401;




    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_POSTS, POSTS);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_POSTS + "/#", POSTS_WITH_ID);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_COMMENTS, COMMENTS);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_COMMENTS + "/#", COMMENTS_WITH_ID);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_POSTS_COMMENTS, POSTS_COMMENTS);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_POSTS_COMMENTS + "/#", POSTS_COMMENTS_WITH_ID);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_SUBREDDITS, SUBREDDITS);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_SUBREDDITS + "/#", SUBREDDITS_WITH_ID);

        return uriMatcher;
    }


    private DatabaseOpenHelper mDatabaseOpenHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case POSTS:
                retCursor =  db.query(DatabaseContract.PostsTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case COMMENTS:
                retCursor =  db.query(DatabaseContract.CommentsTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SUBREDDITS:
                retCursor =  db.query(DatabaseContract.SubRedditsTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case SUBREDDITS_WITH_ID:
                String id = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{id};
                retCursor =  db.query(DatabaseContract.SubRedditsTable.TABLE_NAME,
                        projection,
                        DatabaseContract.SubRedditsTable._ID +  " = ?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();

        long _id=0;
        Uri returnUri;


        switch(sUriMatcher.match(uri)) {
            case POSTS:
                _id = db.insert(DatabaseContract.PostsTable.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI_POSTS, _id);
                } else {
                    throw new SQLException("Couldn't insert data into " + uri);
                }
                break;
            case COMMENTS:
                _id = db.insert(DatabaseContract.CommentsTable.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI_COMMENTS, _id);
                } else {
                    throw new SQLException("Couldn't insert data into " + uri);
                }
                break;
//            case POSTS_COMMENTS:
//                _id = db.insert(DatabaseContract.PostsCommentsTable.TABLE_NAME, null, contentValues);
//                if (_id > 0) {
//                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI_POSTS_COMMENTS, _id);
//                } else {
//                    throw new SQLException("Couldn't insert data into " + uri);
//                }
//                break;
            case SUBREDDITS:
                _id = db.insert(DatabaseContract.SubRedditsTable.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI_SUBREDDITS, _id);
                } else {
                    throw new SQLException("Couldn't insert data into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }



        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArguments) {
        // Get writable access
        SQLiteDatabase db = new DatabaseOpenHelper(getContext()).getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        String id;
        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case POSTS_WITH_ID:
                // Get the task ID from the URI path
               id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DatabaseContract.PostsTable.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case SUBREDDITS_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(DatabaseContract.SubRedditsTable.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArguments) {
        return 0;
    }
}
