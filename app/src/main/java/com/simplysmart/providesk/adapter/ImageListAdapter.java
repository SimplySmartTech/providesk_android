package com.simplysmart.providesk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simplysmart.providesk.R;
import com.simplysmart.lib.common.DebugLog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> urlList;

    public ImageListAdapter(Context context, ArrayList<String> values) {
        this.mContext = context;
        this.urlList = values;
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_data_view, parent, false);

            holder = new Holder();
            holder.deleteImage = (TextView) convertView.findViewById(R.id.deleteImage);
            holder.thumbImage = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        DebugLog.d("image url : " + urlList.get(position));

        if (!urlList.get(position).isEmpty())
            Picasso.with(mContext).load(urlList.get(position))
                    .placeholder(R.drawable.progress_animation).error(R.drawable.loading_border).into(holder.thumbImage);

        return convertView;
    }

    private static class Holder {
        private TextView deleteImage;
        private ImageView thumbImage;
    }
}
