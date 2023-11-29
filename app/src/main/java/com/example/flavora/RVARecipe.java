package com.example.flavora;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVARecipe extends RecyclerView.Adapter<RVARecipe.ViewHolder> {

    private ArrayList<String> localDataSet;


    public RVARecipe(ArrayList<String> data) {
        this.localDataSet = data;
//        localDataSet.add("hello");
//        localDataSet.add("bye");
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private  TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Define click listener for the ViewHolder's View
            this.textView =  (TextView) itemView.findViewById(R.id.item_name);

        }

        //GETTERS
        public TextView getTextView() {
            return textView;
        }
        //SETTERS
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

//    public void CustomAdapter(String[] dataSet) {
//        localDataSet = dataSet;
//    }

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
        viewHolder.getTextView().setText(localDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}


