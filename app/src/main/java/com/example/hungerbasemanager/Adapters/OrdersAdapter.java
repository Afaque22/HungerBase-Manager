package com.example.hungerbasemanager.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hungerbasemanager.Models.OrdersModel;
import com.example.hungerbasemanager.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrdersAdapter extends FirebaseRecyclerAdapter<OrdersModel,OrdersAdapter.orderViewHolder> {
    public OrdersAdapter(@NonNull FirebaseRecyclerOptions<OrdersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrdersAdapter.orderViewHolder holder, int position, @NonNull OrdersModel model) {
        holder.itemName.setText(model.getItemName());
        holder.price.setText("Rs. "+model.getOrderPrice());
        holder.quant.setText(model.getOrderQuanity());
        holder.userName.setText("Order By: " +model.getUserName());
        holder.userAddress.setText("Address: "+model.getOrderAddress());
    }

    @NonNull
    @Override
    public OrdersAdapter.orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_row,parent,false);
        return new orderViewHolder(view);
    }

    public class orderViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, price, quant, userName, userAddress;
        public orderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
            quant = itemView.findViewById(R.id.txtQ);
            userName = itemView.findViewById(R.id.txtUserName);
            userAddress = itemView.findViewById(R.id.txtUserAddress);

        }
    }
}
