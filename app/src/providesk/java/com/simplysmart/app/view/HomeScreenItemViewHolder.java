package com.simplysmart.app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simplysmart.app.R;

/**
 * Created by shekhar on 7/9/16.
 */
public class HomeScreenItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public View line;
    public TextView text;
    public LinearLayout home_item_layout;

    public HomeScreenItemViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.icon);
        line = (View) itemView.findViewById(R.id.line);
        text = (TextView) itemView.findViewById(R.id.title);
        home_item_layout = (LinearLayout) itemView.findViewById(R.id.home_item_layout);
    }
}
