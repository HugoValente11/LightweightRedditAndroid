package com.example.android.redditapp.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.redditapp.Constants;
import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.R;
import com.example.android.redditapp.models.Post.Child;
import com.squareup.picasso.Picasso;


import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

 // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_comments_row, parent, false);

        return new ViewHolder(view);

    }

    public Cursor swapCursor(Cursor c) {
//        // check if this cursor is the same as the previous cursor (mCursor)
//        if (mCursor == c) {
//            return null; // bc nothing has changed
//        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        String comment = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.CommentsTable.COMMENT));
        String author = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.CommentsTable.AUTHOR));


        holder.mCommentTextView.setText(comment);
        holder.mAuthorTextView.setText(author);


    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mAuthorTextView;
        public TextView mCommentTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.authorCommentTextView);
            mCommentTextView = itemView.findViewById(R.id.commentTextView);

//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

//            Intent myIntent = new Intent(mContext, Ingredients.class);
//
//            mContext.startActivity(myIntent);


        }
    }
}
