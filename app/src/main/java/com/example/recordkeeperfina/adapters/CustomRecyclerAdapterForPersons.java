package com.example.recordkeeperfina.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recordkeeperfina.R;
import com.example.recordkeeperfina.model.PersonsBean;

import java.util.List;

public class CustomRecyclerAdapterForPersons extends RecyclerView.Adapter<CustomRecyclerAdapterForPersons.ViewHolder>
{
    private List<PersonsBean>personsBeanList;
    private OnItemSelected activity;
    public CustomRecyclerAdapterForPersons(Context context, List<PersonsBean> personsBeanList)
    {
        //this.context = context;
        this.personsBeanList = personsBeanList;
        activity = (OnItemSelected) context;
    }
    public interface OnItemSelected
    {
         void onListItemClicked(int index);
         void onDeleteClicked(int index);
         void onEditClicked(int index);
        void onCallClicked(int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewName , textViewNumber;
        private ImageView imageViewEdit, imageViewDelete,imageViewCall;
        private CardView cardView;

        public ViewHolder(@NonNull final View itemView,Context context)
        {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewCustomRecyclerPersonsName);
            textViewNumber = itemView.findViewById(R.id.textViewCustomRecyclerPersonsNumber);
            cardView = itemView.findViewById(R.id.cardViewCustomRecyclerViewPersons);
            imageViewCall = itemView.findViewById(R.id.imageViewCustomRecyclerViewPersonCall);
            imageViewDelete = itemView.findViewById(R.id.imageViewCustomRecyclerPersonsDelete);
            imageViewEdit = itemView.findViewById(R.id.imageViewCustomRecyclerPersonsEdit);

            cardView.setOnClickListener(this);
            imageViewEdit.setOnClickListener(this);
            imageViewDelete.setOnClickListener(this);
            imageViewCall.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.cardViewCustomRecyclerViewPersons:
                    activity.onListItemClicked(getAdapterPosition());

                    break;
                case R.id.imageViewCustomRecyclerPersonsDelete:
                    activity.onDeleteClicked(getAdapterPosition());

                    break;
                case R.id.imageViewCustomRecyclerPersonsEdit:
                    activity.onEditClicked(getAdapterPosition());

                    break;
                case R.id.imageViewCustomRecyclerViewPersonCall:
                    activity.onCallClicked(getAdapterPosition());

                    break;


            }// switch closed
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_view_persons,parent,false);

        return new ViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        PersonsBean bean = personsBeanList.get(position);
        holder.textViewName.setText(bean.getPersonName());
        holder.textViewNumber.setText(bean.getPersonNumber());
    }

    @Override
    public int getItemCount()
    {
        return personsBeanList.size();
    }
}
