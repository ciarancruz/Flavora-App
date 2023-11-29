package com.example.flavora;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RVARecipe extends RecyclerView.Adapter<RVARecipe.ViewHolder> {

    private ArrayList<RecipeModel> localDataSet;

    // Getting Data from
    public RVARecipe(ArrayList<RecipeModel> data) {
        this.localDataSet = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView recipeName, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Define click listener for the ViewHolder's View
            recipeName =  (TextView) itemView.findViewById(R.id.item_name);
            description = (TextView) itemView.findViewById(R.id.item_description);
        }

//        public TextView getTextView() {
//            return textView;
//        }
//        public void setTextView(TextView textView) {
//            this.textView = textView;
//        }
    }

    // Create new views (invoked by the layout manager)
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
        viewHolder.recipeName.setText(localDataSet.get(position).recipeName);
        viewHolder.description.setText(localDataSet.get(position).description);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}


