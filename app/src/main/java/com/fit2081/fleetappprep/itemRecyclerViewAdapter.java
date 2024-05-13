package com.fit2081.fleetappprep;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.fleetappprep.provider.Item;

import java.util.ArrayList;

public class itemRecyclerViewAdapter extends RecyclerView.Adapter<itemRecyclerViewAdapter.ViewHolder> {
    ArrayList<Item> itemList = new ArrayList<Item>();

    public void setData(ArrayList<Item> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_row, parent, false); //CardView inflated as RecyclerView list item
        Log.d("week6App","onCreateViewHolder");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Assigning values to the recycler view views
        String itemName = itemList.get(position).getItemName();
        String quantity = String.valueOf(itemList.get(position).getQuantity());
        String cost = String.valueOf(itemList.get(position).getCost());
        holder.itemNameTv.setText(itemName);
        holder.quantityTv.setText(quantity);
        holder.costTv.setText(cost);
        Log.d("week6App","onBindViewHolder");

    }

    @Override
    public int getItemCount() {
        //number of items to display
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //assigning the views in the recycler view to variables

        public TextView itemNameTv, quantityTv, costTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTv = itemView.findViewById(R.id.name_id);
            quantityTv = itemView.findViewById(R.id.quantity_id);
            costTv = itemView.findViewById(R.id.cost_id);
        }
    }
}
