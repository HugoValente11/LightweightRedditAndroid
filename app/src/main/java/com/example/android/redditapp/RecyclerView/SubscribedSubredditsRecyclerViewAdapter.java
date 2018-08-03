package com.example.android.redditapp.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.R;


public class SubscribedSubredditsRecyclerViewAdapter extends RecyclerView.Adapter<SubscribedSubredditsRecyclerViewAdapter.ViewHolder> {

    // Trying to add Click Listener
// Keeps track of the context and list of images to display
    private Context mContext;
    private Cursor mCursor;
    private CursorAdapterOnClickHandler mClickHandler;


    /**
     * The interface that receives onClick messages.
     */
    public interface CursorAdapterOnClickHandler {
        void onClick(long id);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SubscribedSubredditsRecyclerViewAdapter(Context context, Cursor cursor, CursorAdapterOnClickHandler clickHandler) {
        mContext = context;
        mCursor = cursor;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
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
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.subredditTextView);
            mImageView = itemView.findViewById(R.id.unsubscribeSubredditimageView);

            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//          COMPLETED (37) Instead of passing the String for the clicked item, pass the date from the cursor
            mCursor.moveToPosition(adapterPosition);
            long id = mCursor.getLong(0);
                mClickHandler.onClick(id);
            }
        }

    }

