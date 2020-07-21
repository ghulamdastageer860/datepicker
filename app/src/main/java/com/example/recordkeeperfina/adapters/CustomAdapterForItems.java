package com.example.recordkeeperfina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recordkeeperfina.R;
import com.example.recordkeeperfina.database.MyDbHandler;
import com.example.recordkeeperfina.model.ItemsBean;

import java.util.List;

public class CustomAdapterForItems extends RecyclerView.Adapter<CustomAdapterForItems.ViewHolder>
{

    List<ItemsBean>itemsBeanList;
    OnItemClicked activity;
    public interface OnItemClicked
    {
             void onItemDeleteClicked(int index);
            void  onItemEditClicked(int index);
    }
    public CustomAdapterForItems(List<ItemsBean> itemsBeanList,Context context)
    {
        this.itemsBeanList = itemsBeanList;
        activity =(OnItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewItemName , textViewItemPrice,textViewDate;
        private ImageView imageViewEdit,imageViewDelete;
        public ViewHolder(@NonNull View itemView,Context context)
        {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textViewcustomRecyclerViewItemsName);
            textViewItemPrice = itemView.findViewById(R.id.textViewCustomRecyclerViewItemsPrice);
            textViewDate = itemView.findViewById(R.id.textViewCustomRecyclerViewItemsDate);
            imageViewEdit = itemView.findViewById(R.id.imageViewCustomRecyclerViewItemsEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewCustomRecyclerViewItemsDelete);


            imageViewDelete.setOnClickListener(this);
            imageViewEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.imageViewCustomRecyclerViewItemsDelete:
                    activity.onItemDeleteClicked(getAdapterPosition());
                    break;
                case R.id.imageViewCustomRecyclerViewItemsEdit:
                    activity.onItemEditClicked(getAdapterPosition());
                    break;
            }

        }
    } // View Holder closed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_view_items,parent,false);
        return new ViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        ItemsBean bean = itemsBeanList.get(position);
        holder.textViewItemName.setText("Item :"+bean.getItemName());
        holder.textViewItemPrice.setText("Price :"+bean.getItemPrice());
        holder.textViewDate.setText("Date :"+bean.getDate());

    }

    @Override
    public int getItemCount()
    {
        return itemsBeanList.size();
    }



}
