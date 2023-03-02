package com.example.hungerbasemanager.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungerbasemanager.Models.MenuModel;
import com.example.hungerbasemanager.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MenuAdapter extends FirebaseRecyclerAdapter<MenuModel,MenuAdapter.ViewHolder> {

    public MenuAdapter(@NonNull FirebaseRecyclerOptions<MenuModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuAdapter.ViewHolder holder, int position, @NonNull MenuModel model) {
        holder.itemName.setText(model.getItemName());
        holder.itemPrice.setText(model.getPrice());

    }

    @NonNull
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txtName);
            itemPrice = itemView.findViewById(R.id.txtPrice);
        }
    }

}
