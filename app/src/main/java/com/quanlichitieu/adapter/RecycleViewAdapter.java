package com.quanlichitieu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quanlichitieu.R;
import com.quanlichitieu.model.Item;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.HomeViewHolder> {
    private List<Item> list;
    private ItemListener itemListener;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }


    public RecycleViewAdapter(List<Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Item item=list.get(position);
        if(item==null){
            return;
        }
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.category.setText(item.getCategory());
        holder.price.setText(item.getPrice());


    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();

        }
        return 0;

    }

    public void setList(List<Item> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public Item getItem(int position){
        return list.get(position);

    }


    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title,category,price,date;

        public HomeViewHolder(@NonNull View View) {
            super(View);
            title=View.findViewById(R.id.tvTitle);
            category=View.findViewById(R.id.tvCategory);
            price=View.findViewById(R.id.tvPrice);
            date= itemView.findViewById(R.id.tvDate);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if(itemListener!=null){
                itemListener.onItemClick(view,getAdapterPosition());
            }

        }
    }
    public interface ItemListener{
        void onItemClick(View view,int position);
    }
}
