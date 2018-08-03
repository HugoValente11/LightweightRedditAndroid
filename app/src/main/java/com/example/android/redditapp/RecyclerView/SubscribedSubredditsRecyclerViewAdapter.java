package com.example.android.redditapp.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubscribedSubredditsRecyclerViewAdapter extends RecyclerView.Adapter<SubscribedSubredditsRecyclerViewAdapter.ViewHolder>  {

// Trying to add Click Listener
// Keeps track of the context and list of images to display
private Context mContext;
    private Cursor mCursor;


public interface RecyclerViewClickHandler {
    void onClick(int position);
}

    // Provide a suitable constructor (depends on the kind of dataset)
    public SubscribedSubredditsRecyclerViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_subscribedsubreddits_row, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String subreddit = mCursor.getString(mCursor.getColumnIndex(DatabaseContract.SubRedditsTable.SUBREDDIT));

        holder.mTextView.setText(subreddit);
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
    public TextView mTextView;
    public ImageView mImageView;

    public ViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.subredditTextView);
        mImageView = itemView.findViewById(R.id.unsubscribeSubredditimageView);

        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();

        Log.d("POSITIONTAG", "Position clicked: " + position );
    }
}

}
