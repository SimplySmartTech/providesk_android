package com.simplysmart.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplysmart.app.R;
import com.simplysmart.app.model.ItemData;
import com.simplysmart.app.view.HomeScreenItemViewHolder;

import java.util.ArrayList;


/**
 * Created by shekhar on 7/9/16.
 */
public class ShoppingScreenAdapter extends RecyclerView.Adapter<HomeScreenItemViewHolder> {

    private ArrayList<ItemData> itemDatas;
    private Context context;

    public ShoppingScreenAdapter(ArrayList<ItemData> itemDatas, Context context) {
        this.itemDatas = itemDatas;
        this.context = context;
    }

    @Override
    public HomeScreenItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_screen_item, null);
        HomeScreenItemViewHolder viewHolder = new HomeScreenItemViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeScreenItemViewHolder holder, final int position) {

        final ItemData itemData = itemDatas.get(position);

        holder.imageView.setImageResource(itemData.getImage());
        holder.text.setText(itemData.getTitle());

        if ((position == itemDatas.size() - 1) || (position == itemDatas.size() - 2)) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemDatas.size();
    }
}
