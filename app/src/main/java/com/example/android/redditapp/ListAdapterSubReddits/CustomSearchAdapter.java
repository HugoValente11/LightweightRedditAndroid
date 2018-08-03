package com.example.android.redditapp.ListAdapterSubReddits;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.redditapp.DB.DatabaseContract;
import com.example.android.redditapp.R;
import com.example.android.redditapp.RecyclerView.SubscribedSubredditsRecyclerViewAdapter;
import com.example.android.redditapp.SearchClickHandler;

import java.util.List;

public class CustomSearchAdapter extends RecyclerView.Adapter<CustomSearchAdapter.ViewHolder> {
    // Keeps track of the context and list of images to display
    private Context mContext;
    private List<String> resultsList;
    private SearchClickHandler mClickHandler;




    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomSearchAdapter(Context context,SearchClickHandler clickHandler, List<String> resultsList) {
        mContext = context;
        this.mClickHandler = clickHandler;
        this.resultsList = resultsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row_searchable, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(resultsList.get(position));
    }


    @Override
    public int getItemCount() {
       return resultsList.size();
    }



    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.resultSubReddit);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            mClickHandler.onClick(adapterPosition);
        }
    }


}
