package com.example.flavora;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

// ROOM Code from GeeksForGeeks (MSD Lab 6)

public class RVARecipe extends ListAdapter<RecipeModel, RVARecipe.ViewHolder> {

    private static OnItemClickListener listener;

    public RVARecipe() {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<RecipeModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<RecipeModel>() {
        @Override
        public boolean areItemsTheSame(RecipeModel oldItem, RecipeModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(RecipeModel oldItem, RecipeModel newItem) {
            // below line is to check the course name, description and course duration.
            return oldItem.getRecipeName().equals(newItem.getRecipeName()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create a new view, which defines the Ui of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row,parent,false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        RecipeModel model = getRecipeAt(position);
        viewHolder.recipeName.setText(model.getRecipeName());
        viewHolder.description.setText(model.getDescription());
    }

    public RecipeModel getRecipeAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView recipeName, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName =  itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.item_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // inside on click listener we are passing
                    // position to our item of recycler view.
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecipeModel model);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return localDataSet.size();
//    }
}


